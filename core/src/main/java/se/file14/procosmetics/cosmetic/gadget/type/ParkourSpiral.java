package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.material.Materials;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ParkourSpiral implements GadgetBehavior {

    private static final Vector SPAWN_BLOCK_OFFSET = new Vector(0.0d, 0.0d, 5.0d);
    private static final double BLOCK_OFFSET = 0.3d;
    private static final double[] BLOCK_OFFSETS = {BLOCK_OFFSET, -BLOCK_OFFSET};

    private static final float ANGLE_PER_BLOCK = (float) ((2 * Math.PI) / 8);
    private static final float ANGLE_OFFSET = ANGLE_PER_BLOCK * 2.0f;
    private static final float RADIUS = 4.0f;

    private Location centerLocation;
    private Location playerLocation;
    private Location currentLocation;
    private final List<Block> blocksToRemove = new ArrayList<>(32);

    private double angle;
    private int step;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        placeBlock(context, step++);

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (blocksToRemove.isEmpty()) {
            return;
        }
        Player player = context.getPlayer();

        for (double x : BLOCK_OFFSETS) {
            for (double z : BLOCK_OFFSETS) {
                playerLocation = player.getLocation(playerLocation).add(x, 0.0d, z);

                if (isSameBlockLocation(playerLocation, currentLocation)) {
                    placeBlock(context, step++);
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        step = 0;

        for (Block block : blocksToRemove) {
            block.setType(Material.AIR);
        }
        blocksToRemove.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return true;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        centerLocation = location.clone().add(0.0d, 1.0d, 0.0d);
        playerLocation = location.clone();

        LocationUtil.center(centerLocation);

        float yaw = 90.0f * (Math.round(centerLocation.getYaw() / 90.0f) & 0x3);
        centerLocation.setYaw(yaw);
        centerLocation.setPitch(0.0f);

        angle = -FastMathUtil.toRadians(centerLocation.getYaw());
        centerLocation.add(MathUtil.rotateAroundAxisY(SPAWN_BLOCK_OFFSET, angle));

        if (!getNextLocation(0).getBlock().getType().isAir()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return false;
    }

    private Location getNextLocation(int i) {
        float blockAngle = ANGLE_PER_BLOCK * i - ANGLE_OFFSET;
        float x = RADIUS * FastMathUtil.cos(blockAngle);
        float z = RADIUS * FastMathUtil.sin(blockAngle);

        return centerLocation.clone().add(MathUtil.rotateAroundAxisY(new Vector(x, i - 1, z), angle));
    }

    private void placeBlock(CosmeticContext<GadgetType> context, int i) {
        Location nextLocation = getNextLocation(i);
        Block block = nextLocation.getBlock();

        if (!block.getType().isAir()) {
            onUnequip(context);
            return;
        }
        removePreviousBlock();
        blocksToRemove.add(block);

        block.setType(Materials.CONCRETE.get(i % Materials.CONCRETE.size()));
        currentLocation = nextLocation.add(0.0d, 1.0d, 0.0d);
    }

    private void removePreviousBlock() {
        if (blocksToRemove.size() > 1) {
            Block block = blocksToRemove.removeFirst();

            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
        }
    }

    private boolean isSameBlockLocation(Location location1, Location location2) {
        return location1.getBlockX() == location2.getBlockX()
                && location1.getBlockY() == location2.getBlockY()
                && location1.getBlockZ() == location2.getBlockZ();
    }
}