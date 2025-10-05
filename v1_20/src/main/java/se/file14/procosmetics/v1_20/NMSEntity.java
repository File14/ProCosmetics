package se.file14.procosmetics.v1_20;

import com.mojang.datafixers.util.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.nms.NMSEntityImpl;
import se.file14.procosmetics.nms.entitytype.CachedEntityType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class NMSEntity extends NMSEntityImpl {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    private static Constructor<FallingBlockEntity> fallingBlockConstructor;

    private Entity entity;

    private Entity leashHolder;
    private ClientboundSetEquipmentPacket helmetPacket, chestplatePacket, leggningsPacket, bootsPacket, handPacket, offhandPacket;

    private List<SynchedEntityData.DataValue<?>> metadataList;

    public NMSEntity(World world, CachedEntityType cachedEntityType, EntityTracker tracker) {
        super(world, cachedEntityType, tracker);
        entity = ((net.minecraft.world.entity.EntityType<?>) cachedEntityType.getEntityTypeObject()).create(((CraftWorld) world).getHandle());
    }

    public NMSEntity(World world, BlockData blockData, EntityTracker tracker) {
        super(world, blockData, tracker);

        try {
            if (fallingBlockConstructor == null) {
                fallingBlockConstructor = FallingBlockEntity.class.getDeclaredConstructor(Level.class, double.class, double.class, double.class, BlockState.class);
                fallingBlockConstructor.setAccessible(true);
            }
            entity = fallingBlockConstructor.newInstance(((CraftWorld) world).getHandle(), 0.0d, 0.0d, 0.0d, ((CraftBlockData) blockData).getState());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public NMSEntity(org.bukkit.entity.Entity realEntity) {
        super(realEntity);
        entity = ((CraftEntity) realEntity).getHandle();
    }

    @Override
    public void spawn(Player player) {
        ((CraftPlayer) player).getHandle().connection.send(entity.getAddEntityPacket());

        sendEntityMetadataPacket(player);
        sendUpdateAttributesPacket(player);
        sendEntityEquipmentPacket(player);
        sendLeashHolderPacket(player);
        sendVelocityPacket(player);
        sendMountPacket(player);
    }

    @Override
    public void despawn(Player... players) {
        ClientboundRemoveEntitiesPacket removeEntityPacket = new ClientboundRemoveEntitiesPacket(entity.getId());

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().connection.send(removeEntityPacket);
        }
    }

    @Override
    public void sendPacketsToViewers(Object... packets) {
        for (Player player : entityTracker.getViewers()) {
            ServerGamePacketListenerImpl playerConnection = ((CraftPlayer) player).getHandle().connection;

            for (Object packet : packets) {
                playerConnection.send((Packet<?>) packet);
            }
        }
    }

    @Override
    public void sendEntityMetadataPacket(Player player) {
        metadataList = entity.getEntityData().getNonDefaultValues();

        if (metadataList != null) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(entity.getId(), metadataList));
        }
    }

    @Override
    public void sendUpdateAttributesPacket(Player player) {
        if (entity instanceof LivingEntity) {
            Set<AttributeInstance> set = ((LivingEntity) entity).getAttributes().getDirtyAttributes();

            if (!set.isEmpty()) {
                ((CraftPlayer) player).getHandle().connection.send(new ClientboundUpdateAttributesPacket(entity.getId(), set));
            }
        }
    }

    @Override
    public void sendEntityEquipmentPacket(Player player) {
        ServerGamePacketListenerImpl playerConnection = ((CraftPlayer) player).getHandle().connection;

        if (handPacket != null) {
            playerConnection.send(handPacket);
        }
        if (offhandPacket != null) {
            playerConnection.send(offhandPacket);
        }
        if (helmetPacket != null) {
            playerConnection.send(helmetPacket);
        }
        if (chestplatePacket != null) {
            playerConnection.send(chestplatePacket);
        }
        if (leggningsPacket != null) {
            playerConnection.send(leggningsPacket);
        }
        if (bootsPacket != null) {
            playerConnection.send(bootsPacket);
        }
    }

    @Override
    public void sendPositionRotationPacket(Location location) {
        if (location == null || location.equals(previousLocation)) {
            return;
        }
        setPositionRotation(location);
        sendPacketsToViewers(new ClientboundTeleportEntityPacket(entity));

        if (entity instanceof LivingEntity) {
            sendHeadRotationPacket();
        }
    }

    @Override
    public void sendMetadataPacket() {
        metadataList = entity.getEntityData().packDirty();

        if (metadataList != null) {
            sendPacketsToViewers(new ClientboundSetEntityDataPacket(entity.getId(), metadataList));
        }
    }

    @Override
    public void sendIronGolemAttackAnimationPacket() {
        sendPacketsToViewers(new ClientboundEntityEventPacket(entity, (byte) 4));
    }

    @Override
    public void sendRabbitJumpAnimationPacket() {
        sendPacketsToViewers(new ClientboundEntityEventPacket(entity, (byte) 1));
    }

    @Override
    public void sendEntityAnimationPacket() {
        sendPacketsToViewers(new ClientboundAnimatePacket(entity, 0));
    }

    @Override
    public void sendLeashHolderPacket(Player player) {
        if (leashHolder != null && (!(leashHolder instanceof ServerPlayer) || player.canSee((Player) leashHolder.getBukkitEntity()))) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityLinkPacket(entity, leashHolder));
        }
    }

    @Override
    public void sendVelocityPacket(Player player) {
        Vec3 vec3D = entity.getDeltaMovement();

        if (vec3D.x() != 0.0d || vec3D.y() != 0.0d || vec3D.z() != 0.0d) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityMotionPacket(entity));
        }
    }

    @Override
    public void sendVelocityPacket() {
        Vec3 vec3D = entity.getDeltaMovement();

        if (vec3D.x() != 0.0d || vec3D.y() != 0.0d || vec3D.z() != 0.0d) {
            sendPacketsToViewers(new ClientboundSetEntityMotionPacket(entity));
        }
    }

    @Override
    public void sendMountPacket(Player player) {
        if (!entity.getPassengers().isEmpty()) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetPassengersPacket(entity));
        }
    }

    @Override
    public void setMainHand(ItemStack itemStack) {
        handPacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setOffhandHand(ItemStack itemStack) {
        offhandPacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        helmetPacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        chestplatePacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        leggningsPacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        bootsPacket = new ClientboundSetEquipmentPacket(entity.getId(), Collections.singletonList(new Pair<>(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(itemStack))));
    }

    @Override
    public void setPositionRotation(Location location) {
        if (previousLocation == null) {
            previousLocation = location.clone();
        } else {
            if (previousLocation.equals(location)) {
                return;
            }
            setPreviousLocation(location);
        }
        entity.setPosRaw(location.getX(), location.getY(), location.getZ());
        entity.setXRot(location.getPitch());
        entity.setYRot(location.getYaw());
        entity.setYHeadRot(location.getYaw());
        entity.setYBodyRot(location.getYaw());
    }

    @Override
    public void sendHeadRotationPacket() {
        float yaw = entity.getYRot() * 256.0f / 360.0f;
        sendPacketsToViewers(new ClientboundRotateHeadPacket(entity, (byte) yaw));
    }

    @Override
    public void setInvisible(boolean invisible) {
        entity.setInvisible(invisible);
    }

    @Override
    public void setLeashHolder(org.bukkit.entity.Entity entity2) {
        leashHolder = ((CraftEntity) entity2).getHandle();
    }

    @Override
    public void setBaby(boolean baby) {
        if (entity instanceof AgeableMob ageableMob) {
            ageableMob.setBaby(baby);
        }
    }

    @Override
    public void setZombieBaby(boolean baby) {
        if (entity instanceof Zombie zombie) {
            zombie.setBaby(baby);
        }
    }

    @Override
    public void setArmorStandSmall(boolean small) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setSmall(small);
        }
    }

    @Override
    public void setArmorStandBasePlate(boolean plate) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setNoBasePlate(!plate);
        }
    }

    @Override
    public void setArmorStandArms(boolean arms) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setShowArms(arms);
        }
    }

    @Override
    public void setArmorStandMarker(boolean marker) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setMarker(marker);
        }
    }

    @Override
    public void setGuardianAttackTarget(int id) {
        if (entity instanceof Guardian guardian) {
            guardian.setActiveAttackTarget(id);
        }
    }

    @Override
    public void setSlimeSize(int size) {
        if (entity instanceof Slime slime) {
            slime.setSize(size, true);
        }
    }

    @Override
    public void setSilent(boolean silent) {
        entity.setSilent(silent);
    }

    @Override
    public void setBatSleeping(boolean sleeping) {
        if (entity instanceof Bat bat) {
            bat.setResting(sleeping);
        }
    }

    @Override
    public void setCreeperIgnited(boolean ignited) {
        if (entity instanceof Creeper creeper) {
            creeper.setSwellDir(ignited ? 1 : -1);
        }
    }

    @Override
    public void setCreeperPowered(boolean powered) {
        if (entity instanceof Creeper creeper) {
            creeper.setPowered(powered);
        }
    }

    @Override
    public boolean isCreeperPowered() {
        return entity instanceof Creeper creeper && creeper.isPowered();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        entity.setPose(sneaking ? Pose.CROUCHING : Pose.STANDING);
    }

    @Override
    public void setGravity(boolean gravity) {
        entity.setNoGravity(!gravity);
    }

    @Override
    public void setCustomName(Component component) {
        entity.setCustomNameVisible(true);
        entity.setCustomName(CraftChatMessage.fromStringOrNull(SERIALIZER.serialize(component)));
    }

    @Override
    public void setHeadPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setHeadPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setBodyPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setBodyPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setLeftArmPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setLeftArmPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setRightArmPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setRightArmPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setLeftLegPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setLeftLegPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setRightLegPose(float x, float y, float z) {
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setRightLegPose(new Rotations(x, y, z));
        }
    }

    @Override
    public void setNoClip(boolean noClip) {
        entity.noPhysics = noClip;
    }

    @Override
    public void setNoAI(boolean ai) {
        if (entity instanceof Mob mob) {
            mob.setNoAi(ai);
        }
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        entity.setInvulnerable(invulnerable);
    }

    @Override
    public void setPersistent(boolean persistent) {
        entity.getBukkitEntity().setPersistent(persistent);
    }

    @Override
    public void setDonkeyStanding(boolean standing) {
        if (entity instanceof Donkey donkey) {
            donkey.setStanding(standing);
        }
    }

    @Override
    public void setYaw(float yaw) {
        entity.setYRot(yaw);
    }

    @Override
    public void setPitch(float pitch) {
        entity.setXRot(pitch);
    }

    @Override
    public void move(Vector vector) {
        entity.move(MoverType.SELF, new Vec3(vector.getX(), vector.getY(), vector.getZ()));
    }

    @Override
    public void moveRide(Player player) {
        LivingEntity entityLivingPlayer = ((CraftPlayer) player).getHandle();
        float x = entityLivingPlayer.xxa;
        float z = entityLivingPlayer.zza;
        boolean jumping = false;

        try {
            jumping = (boolean) JUMPING_FIELD.get(entityLivingPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setYaw(entityLivingPlayer.getYRot());
        setPitch(entityLivingPlayer.getXRot());

        LivingEntity entityLiving = (LivingEntity) entity;

        entityLiving.setYHeadRot(entityLivingPlayer.getYRot());

        if (z < 0.0f) {
            z *= 0.50f;
        }
        if (entity.onGround() && jumping) {
            Vec3 vec3d1 = entityLiving.getDeltaMovement();
            double jumpPower = 0.25d;
            entityLiving.setDeltaMovement(vec3d1.x * jumpPower, 0.5d, vec3d1.z * jumpPower);
        }
        float speedModifier = entity.onGround() ? getDefaultRideSpeed() : 0.3f;
        double y = 0.0f;
        float speed;
        float swimSpeed;

        if (entityLiving.isInWater()) {
            double locY = entityLiving.getY();
            speed = 0.8f;
            swimSpeed = 0.02f;

            entityLiving.moveRelative(swimSpeed, new Vec3(x, y, z));
            entityLiving.move(MoverType.SELF, entityLiving.getDeltaMovement());
            double motX = entityLiving.getDeltaMovement().x * (double) speed;
            double motY = entityLiving.getDeltaMovement().y * 0.800000011920929d;
            double motZ = entityLiving.getDeltaMovement().z * (double) speed;
            motY -= 0.02d;

            if (entityLiving.horizontalCollision && entityLiving.isFree(entityLiving.getDeltaMovement().x, entityLiving.getDeltaMovement().y + 0.6000000238418579d - entityLiving.getY() + locY, entityLiving.getDeltaMovement().z)) {
                motY = 0.30000001192092896d;
            }
            entityLiving.setDeltaMovement(motX, motY, motZ);
        } else {
            double minY = entityLiving.getBoundingBox().minY;
            float friction = 0.51f;

            if (entityLiving.onGround()) {
                friction = entityLiving.level().getBlockState(new BlockPos(Mth.floor(entityLiving.getX()), Mth.floor(minY) - 1, Mth.floor(entityLiving.getZ()))).getBlock().getFriction() * 0.91f;
            }
            speed = speedModifier * 0.21600002f / friction * friction * friction;

            a(speed, new Vec3(x, y, z));

            double motX = entityLiving.getDeltaMovement().x;
            double motY = entityLiving.getDeltaMovement().y;
            double motZ = entityLiving.getDeltaMovement().z;

/*            if (entityLiving.isClimbing()) {
                swimSpeed = 0.15f;
                motX = MathHelper.a(motX, -swimSpeed, swimSpeed);
                motZ = MathHelper.a(motZ, -swimSpeed, swimSpeed);
                entityLiving.K = 0.0f;

                if (motY < -0.15d) {
                    motY = -0.15d;
                }
            }*/
            entityLiving.move(MoverType.SELF, new Vec3(motX, motY, motZ));

/*            if (entityLiving.A && entityLiving.isClimbing()) {
                motY = 0.2d;
            }*/
            motY -= 0.08d;
            motY *= 0.9800000190734863d;
            motX *= friction;
            motZ *= friction;

            entityLiving.setDeltaMovement(motX, motY, motZ);
        }
        entityLiving.calculateEntityAnimation(false);
    }

    public void a(float f, Vec3 vec3d) {
        Vec3 vec3d1 = a(vec3d, f, entity.getYRot());
        entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d1));
    }

    private static Vec3 a(Vec3 vec3d, float f, float f1) {
        double d0 = vec3d.length();
        if (d0 < 1.0E-7D) {
            return Vec3.ZERO;
        } else {
            Vec3 vec3d1 = (d0 > 1.0D ? vec3d.normalize() : vec3d).scale(f);
            float f2 = Mth.sin(f1 * 0.017453292F);
            float f3 = Mth.cos(f1 * 0.017453292F);
            return new Vec3(vec3d1.x * (double) f3 - vec3d1.z * (double) f2, vec3d1.y, vec3d1.z * (double) f3 + vec3d1.x * (double) f2);
        }
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        entity.setDeltaMovement(x, y, z);
    }

    @Override
    public void goToLocation(Location location, double speed) {
        if (entity instanceof Mob mob) {
            PathNavigation navigation = mob.getNavigation();

            navigation.moveTo(location.getX(), location.getY(), location.getZ(), speed);
            // TODO Check if it is not needed?
            navigation.setSpeedModifier(speed);
        }
    }

    @Override
    public void setHurtTicks(int ticks) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.hurtTime = ticks;
        }
    }

    @Override
    public void setEntityItemStack(ItemStack itemStack) {
        if (entity instanceof ItemEntity itemEntity) {
            itemEntity.setItem(CraftItemStack.asNMSCopy(itemStack));
        }
    }

    @Override
    public Object getNMSEntity() {
        return entity;
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntity() {
        return entity.getBukkitEntity();
    }

    @Override
    public int getId() {
        return entity.getId();
    }

    @Override
    public void removePathfinder() {
        try {
            Mob entityInsentient = (Mob) entity;

            entityInsentient.goalSelector.getAvailableGoals().clear();
            entityInsentient.targetSelector.getAvailableGoals().clear();

            //entityInsentient.goalSelector.removeAllGoals();
            //entityInsentient.targetSelector.removeAllGoals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}