package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HotTub implements GadgetBehavior, Listener {

    private BlockStructure structure;
    private Location center;
    private Location shower;
    private Location buttonLocation;
    private boolean bubbles;
    private final List<Location> waterLocations = new ArrayList<>();

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        center = context.getPlayer().getLocation();

        double angle = structure.spawn(center);

        for (Block block : structure.getPlacedEntries()) {
            if (Tag.BUTTONS.isTagged(block.getType())) {
                buttonLocation = block.getLocation();
            } else if (block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged()) {
                waterLocations.add(block.getLocation().add(0.5d, 0.2d, 0.5d));
            }
        }
        shower = center.clone().add(MathUtil.rotateAroundAxisY(new Vector(0.0d, 0.0d, 5.0d), angle));

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (shower == null) {
            return;
        }
        double SPREAD = 0.3d;

        if (bubbles) {
            for (Location dripLocation : waterLocations) {
                center.getWorld().spawnParticle(
                        Particle.BUBBLE,
                        dripLocation,
                        0,
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        0.5d,
                        MathUtil.randomRange(-SPREAD, SPREAD),
                        1.0d
                );
            }
            shower.getWorld().playSound(shower, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.1f, 1.8f);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        bubbles = false;
        center = null;
        shower = null;
        waterLocations.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return true;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }

    @EventHandler
    public void onButtonClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBock = event.getClickedBlock();

            if (clickedBock != null && buttonLocation != null && buttonLocation.getBlock().equals(clickedBock)) {
                bubbles = !bubbles;
                event.setCancelled(true);
            }
        }
    }
}