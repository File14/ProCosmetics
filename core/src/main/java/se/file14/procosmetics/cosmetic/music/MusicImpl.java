/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.cosmetic.music;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.music.Music;
import se.file14.procosmetics.api.cosmetic.music.MusicBehavior;
import se.file14.procosmetics.api.cosmetic.music.MusicType;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.item.Heads;
import se.file14.procosmetics.util.item.ItemBuilderImpl;
import se.file14.procosmetics.util.material.Materials;

public class MusicImpl extends CosmeticImpl<MusicType, MusicBehavior> implements Music, Listener {

    private static final ItemStack DJ_HAND = new ItemStack(Material.MUSIC_DISC_CHIRP);
    private static final ItemStack DISCO_BALL = Heads.DISCO_BALL.getSkull();
    private static final ItemStack DISCO_FLOOR_BOTTOM_BLOCK = new ItemStack(Material.BLACK_CONCRETE);

    private static final float ANGLE_1 = (float) (Math.PI / 180.0f);
    private static final double ANGLE_120 = 2.0d * Math.PI / 3.0d;
    private static final double ANGLE_240 = 4.0d * Math.PI / 3.0d;
    private static final double POWER = 255 * 0.5d;
    private static final double ANGLE_360 = 2.0d * Math.PI;

    private static final int DJ_BLOCK_SPACE = 2;
    private static final double MIN_DJ_DISTANCE = 32.0d * 32.0d;
    private static final double DJ_ARMOR_COLOR_CHANGE_SPEED = ANGLE_1 * 3.0d;
    private static final float DJ_MOVEMENT_SPEED = ANGLE_1 * 35.0f;
    private static final int DISCO_BALL_BEAM_LENGTH = 8;
    private static final float DISCO_BALL_ROTATION_SPEED = 6.0f;
    private static final double DISCO_FLOOR_SIZE = 2.5d;
    private int tick;

    private final ItemBuilderImpl DJ_CHESTPLATE = new ItemBuilderImpl(new ItemStack(Material.LEATHER_CHESTPLATE));
    private final ItemBuilderImpl DJ_LEGGINGS = new ItemBuilderImpl(new ItemStack(Material.LEATHER_LEGGINGS));
    private final ItemBuilderImpl DJ_BOOTS = new ItemBuilderImpl(new ItemStack(Material.LEATHER_BOOTS));
    private final Vector discoBallVector = new Vector();

    private final boolean discoFloor;
    private PositionSongPlayer songPlayer;
    private final EntityTracker tracker = new EntityTrackerImpl();
    private NMSEntity armorStand;
    private NMSEntity jukebox;
    private NMSEntity discoBall;
    private int step;
    private double colorAngle;
    private float angle;
    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private boolean displayingSong;

    public MusicImpl(ProCosmeticsPlugin plugin, User user, MusicType type, MusicBehavior behavior) {
        super(plugin, user, type, behavior);
        discoFloor = type.getCategory().getConfig().getBoolean("disco_floor");
    }

    @Override
    public boolean canEquip() {
        Location location = player.getLocation();
        location.setX(location.getBlockX());
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ());

        if (!isOnGround(location)) {
            user.sendMessage(user.translate("cosmetic.equip.deny.ground"));
            return false;
        }

        if (!isEnoughSpace(location)) {
            user.sendMessage(user.translate("cosmetic.equip.deny.space"));
            return false;
        }

        if (isMusicNearby(location)) {
            user.sendMessage(user.translate("cosmetic.music.equip.deny"));
            return false;
        }
        return true;
    }

    @Override
    public void onEquip() {
        player.getLocation(location);
        float yaw = 90.0f * (Math.round(location.getYaw() / 90.0f) & 0x3);
        location.setX(location.getBlockX() + 0.5d);
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ() + 0.5d);
        location.setYaw(yaw);
        location.setPitch(0.0f);

        createDJ(location);
        location.add(location.getDirection().multiply(1.0d));
        createJukebox(location);
        createDiscoBall(location);
        spawnFirework(location);
        tracker.startTracking();

        songPlayer = new PositionSongPlayer(cosmeticType.getSong(), SoundCategory.RECORDS);
        songPlayer.setTargetLocation(location);
        songPlayer.setPlaying(true);

        runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void onUpdate() {
        if (armorStand == null) {
            return;
        }
        updateDJ();
        updateDiscoBall();

        if (step % 4 == 0) {
            spawnParticles(location);
        }

        if (discoFloor && step % 10 == 0) {
            Location blockLoc = location.clone().subtract(0.0d, 1.0d, 0.0d);
            ItemStack itemStack = Materials.getRandomStainedGlassItem();

            for (Block block : MathUtil.getIn2DRadius(blockLoc, DISCO_FLOOR_SIZE)) {
                plugin.getBlockRestoreManager().setFakeBlock(block, itemStack, false);
            }

            for (Block block : MathUtil.getIn2DRadius(blockLoc.subtract(0.0d, 1.0d, 0.0d), DISCO_FLOOR_SIZE)) {
                plugin.getBlockRestoreManager().setFakeBlock(block, DISCO_FLOOR_BOTTOM_BLOCK, false);
            }
        }
        if (step++ > 100) {
            step = 0;
            updateHologramName();
        }

        if (tick++ >= 360) {
            tick = 0;
        }
    }

    private void updateHologramName() {
        Component component;

        if (displayingSong) {
            component = user.translate("cosmetic.music.hologram.dj",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("song", cosmeticType.getName(user)));
        } else {
            component = user.translate("cosmetic.music.hologram.song",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("song", cosmeticType.getName(user)));
        }
        displayingSong = !displayingSong;

        if (!component.equals(Component.empty())) {
            armorStand.setCustomName(component);
        }
    }

    @Override
    public void onUnequip() {
        if (songPlayer != null) {
            songPlayer.destroy();
            songPlayer = null;
        }
        tracker.destroy();

        if (discoFloor) {
            for (int i = 0; i < 2; i++) {
                for (Block block : MathUtil.getIn2DRadius(location.subtract(0.0d, 1.0d, 0.0d), DISCO_FLOOR_SIZE)) {
                    plugin.getBlockRestoreManager().resetBlock(block);
                }
            }
        }
    }

    @EventHandler
    public void onSongEnd(SongEndEvent event) {
        if (songPlayer == event.getSongPlayer()) {
            user.removeCosmetic(cosmeticType.getCategory(), false, false);
        }
    }

    private boolean isOnGround(Location location) {
        Block block = location.clone().subtract(0.0d, 1.0d, 0.0d).getBlock();
        return block.getType() != Material.AIR;
    }

    private boolean isEnoughSpace(Location location) {
        for (Block block : MathUtil.getIn3DRadius(location, DJ_BLOCK_SPACE, DJ_BLOCK_SPACE)) {
            if (!block.isPassable() || block.isLiquid()) {
                return false;
            }
        }
        return true;
    }

    private boolean isMusicNearby(Location location) {
        for (User otherUser : plugin.getUserManager().getAllConnected()) {
            if (otherUser == user || !otherUser.hasCosmetic(cosmeticType.getCategory())) {
                continue;
            }
            MusicImpl music = (MusicImpl) otherUser.getCosmetic(cosmeticType.getCategory());

            if (music == null || !music.isEquipped()) {
                continue;
            }
            Location otherBoxLocation = music.getLocation();

            if (otherBoxLocation.getWorld() == location.getWorld() && otherBoxLocation.distanceSquared(location) < MIN_DJ_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    private void createDJ(Location location) {
        armorStand = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.ARMOR_STAND, tracker);
        armorStand.setHelmet(new ItemBuilderImpl(Material.PLAYER_HEAD).setSkullOwner(player).getItemStack());
        armorStand.setChestplate(DJ_CHESTPLATE.getItemStack());
        armorStand.setLeggings(DJ_LEGGINGS.getItemStack());
        armorStand.setBoots(DJ_BOOTS.getItemStack());
        armorStand.setMainHand(DJ_HAND);
        armorStand.setPositionRotation(location);

        if (armorStand.getBukkitEntity() instanceof ArmorStand armorStandBukkit) {
            armorStandBukkit.setArms(true);
            armorStandBukkit.setBasePlate(false);
        }
        updateHologramName();
    }

    private void createJukebox(Location location) {
        jukebox = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.BLOCK_DISPLAY, tracker);
        jukebox.setPositionRotation(location);

        if (jukebox.getBukkitEntity() instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(Material.JUKEBOX.createBlockData());

            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.identity()
                    //.scale(scale)
                    //.rotateY(radians)
                    .translate(-0.5f, 0.0f, -0.5f);
            blockDisplay.setTransformationMatrix(transformationMatrix);
            blockDisplay.setTeleportDuration(1);
        }
    }

    private void createDiscoBall(Location location) {
        discoBall = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.ITEM_DISPLAY, tracker);

        if (discoBall.getBukkitEntity() instanceof ItemDisplay itemDisplay) {
            itemDisplay.setItemStack(DISCO_BALL);
            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.identity()
                    //.scale(scale)
                    //.rotateY(radians)
                    .translate(0.0f, 0.25f, 0.0f);
            itemDisplay.setTransformationMatrix(transformationMatrix);
            itemDisplay.setTeleportDuration(1);
        }
        discoBall.setPositionRotation(location.clone().add(0.0d, 4.0d, 0.0d));
    }

    private void spawnFirework(Location location) {
        location.getWorld().spawn(location, Firework.class, entity -> {
            FireworkMeta fireworkMeta = entity.getFireworkMeta();

            FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(true).withColor(
                    Color.fromBGR(
                            MathUtil.randomRangeInt(0, 255),
                            MathUtil.randomRangeInt(0, 255),
                            MathUtil.randomRangeInt(0, 255)
                    )).with(FireworkEffect.Type.BALL_LARGE).build();
            fireworkMeta.addEffect(fireworkEffect);
            fireworkMeta.setPower(2);
            entity.setFireworkMeta(fireworkMeta);
            MetadataUtil.setCustomEntity(entity);
        }).detonate();
    }

    private void updateDJ() {
        Color color = Color.fromRGB(
                (int) (POWER * (Math.sin(colorAngle) + 1)),
                (int) (POWER * (Math.sin(colorAngle + ANGLE_120) + 1)),
                (int) (POWER * (Math.sin(colorAngle + ANGLE_240) + 1))
        );
        colorAngle += DJ_ARMOR_COLOR_CHANGE_SPEED;

        if (colorAngle > ANGLE_360) {
            colorAngle = 0.0d;
        }
        armorStand.setChestplate(DJ_CHESTPLATE.setLeatherArmorColor(color).getItemStack());
        armorStand.setLeggings(DJ_LEGGINGS.setLeatherArmorColor(color).getItemStack());
        armorStand.setBoots(DJ_BOOTS.setLeatherArmorColor(color).getItemStack());
        armorStand.sendEntityEquipmentPacket();

        angle += DJ_MOVEMENT_SPEED;

        if (angle > ANGLE_360) {
            angle = 0.0f;
        }
        float res = FastMathUtil.sin(angle) + 1;
        float move = 5.0f * res;
        float headMove = 10.0f * res - 25.0f;

        armorStand.setHeadPose(headMove, 10.0f, 0.0f);
        armorStand.setRightArmPose(240.0f - headMove, 50.0f, 0.0f);
        armorStand.setLeftArmPose(-60.0f, -headMove, 0.0f);
        armorStand.setBodyPose(0.0f, move, 0.0f);
        armorStand.setLeftLegPose(10.0f, move, -6.0f);
        armorStand.setRightLegPose(-move, move, 12.0f);
        armorStand.sendEntityMetadataPacket();
    }

    private void updateDiscoBall() {
        Location loc = discoBall.getPreviousLocation().clone();
        loc.setYaw(tick * DISCO_BALL_ROTATION_SPEED);
        discoBall.sendPositionRotationPacket(loc);

        discoBallVector.setX(MathUtil.randomRange(-1.0d, 1.0d));
        discoBallVector.setY(MathUtil.randomRange(-1.0d, 1.0d));
        discoBallVector.setZ(MathUtil.randomRange(-1.0d, 1.0d));
        discoBallVector.multiply(0.3d);

        int r = MathUtil.randomRangeInt(0, 255);
        int g = MathUtil.randomRangeInt(0, 255);
        int b = MathUtil.randomRangeInt(0, 255);

        for (int i = 0; i < DISCO_BALL_BEAM_LENGTH; i++) {
            loc.add(discoBallVector);
            location.getWorld().spawnParticle(Particle.DUST, loc, 1, 0.0d, 0.0d, 0.0d, 0.0d,
                    new Particle.DustOptions(Color.fromRGB(r, g, b), 1)
            );
        }
    }

    private void spawnParticles(Location location) {
        double offset = 1.0d + MathUtil.randomRange(-0.3d, 0.3d);
        location.add(0.0d, offset, 0.0d);
        location.getWorld().spawnParticle(Particle.NOTE,
                location,
                0,
                MathUtil.randomRange(0.0d, 24.0d),
                0.0d,
                0.0d
        );
        location.subtract(0.0d, offset, 0.0d);

        double offsetX = MathUtil.randomRange(-3.0d, 3.0d);
        double offsetY = 3.0d + MathUtil.randomRange(-3.0d, 3.0d);
        double offsetZ = MathUtil.randomRange(-3.0d, 3.0d);
        location.add(offsetX, offsetY, offsetZ);
        location.getWorld().spawnParticle(Particle.FIREWORK, location, 1, 0.0d, 0.0d, 0.0d, 0.0d);
        location.subtract(offsetX, offsetY, offsetZ);
    }

    public Location getLocation() {
        return location;
    }
}
