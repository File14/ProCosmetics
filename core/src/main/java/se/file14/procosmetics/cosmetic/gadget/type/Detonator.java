package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class Detonator implements GadgetBehavior {

    private static final ItemStack TNT_ITEM = new ItemStack(Material.TNT);
    private static final int BOMB_AMOUNT = 3;

    private final List<NMSEntity> entities = new ArrayList<>();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    private boolean shouldExplode() {
        return entities.size() >= BOMB_AMOUNT;
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        if (shouldExplode()) {
            // Explode all
            for (NMSEntity nmsEntity : entities) {
                Location location = nmsEntity.getPreviousLocation().clone();

                for (Player nearbyPlayer : MathUtil.getClosestPlayersFromLocation(location, 4.0d)) {
                    MathUtil.pushEntity(nearbyPlayer, location, 2.0d, 0.5d);
                }
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f);
                location.getWorld().spawnParticle(Particle.EXPLOSION, location.add(0.0d, 1.0d, 0.0d), 0);
                nmsEntity.getTracker().destroy();
            }
            entities.clear();
            return InteractionResult.SUCCESS;
        } else {
            // Should never happen in this case
            if (clickedBlock == null || clickedPosition == null) {
                return InteractionResult.FAILED;
            }
            // Place new tnt
            Location location = clickedBlock.getLocation().add(clickedPosition.getX(), clickedPosition.getY() - 0.7d, clickedPosition.getZ());
            location.getWorld().playSound(location, Sound.ENTITY_HORSE_ARMOR, 0.5f, 1.5f);

            Player player = context.getPlayer();

            NMSEntity nmsEntity = context.getPlugin().getNMSManager().createEntity(player.getWorld(), EntityType.ARMOR_STAND);

            if (nmsEntity.getBukkitEntity() instanceof ArmorStand armorStand) {
                armorStand.setInvisible(true);
                armorStand.setSmall(true);
                armorStand.setArms(false);
                armorStand.setMarker(true);
            }
            nmsEntity.setHelmet(TNT_ITEM);
            nmsEntity.setPositionRotation(location);
            nmsEntity.setHeadPose(0.0f, player.getLocation().getYaw(), 0.0f);
            nmsEntity.getTracker().startTracking();
            entities.add(nmsEntity);

            return InteractionResult.FAILED;
        }
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        for (NMSEntity nmsEntity : entities) {
            Location location = nmsEntity.getPreviousLocation();
            location.getWorld().spawnParticle(Particle.CLOUD, location, 0);
            nmsEntity.getTracker().destroy();
        }
        entities.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
