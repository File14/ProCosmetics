package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import javax.annotation.Nullable;

public class DivingBoard implements GadgetBehavior {

    private BlockStructure structure;
    private Location center;
    private Location location;
    private Location jump;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        center = context.getPlayer().getLocation();
        location = center.clone();

        double angle = structure.spawn(center);
        jump = center.clone().add(MathUtil.rotateAroundAxisY(new Vector(0.0d, 3.0d, 3.0d), angle));

        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (jump != null) {
            for (Player worldPlayer : jump.getWorld().getPlayers()) {
                Block block = worldPlayer.getLocation(location).getBlock();

                if (block.equals(jump.getBlock())) {
                    worldPlayer.setVelocity(worldPlayer.getVelocity().add(new Vector(
                            0.0d,
                            Math.random() * 2.0d,
                            0.0d
                    )));
                    worldPlayer.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5f, 1.4f);

                    User otherUser = context.getPlugin().getUserManager().getConnected(worldPlayer);

                    if (otherUser != null) {
                        otherUser.setFallDamageProtection(10);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        jump = null;
        center = null;
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}