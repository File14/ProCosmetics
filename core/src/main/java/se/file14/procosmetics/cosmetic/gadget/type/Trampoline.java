package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.List;

public class Trampoline implements GadgetBehavior {

    private static final List<Material> BOUNCE_MATERIALS = List.of(
            Material.BLACK_WOOL,
            Material.WHITE_WOOL
    );

    private BlockStructure structure;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructure(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getLocation();

        structure.spawn(location);

        Location teleport = location.clone().add(0.d, 3.5d, 0.0d);

        for (Player closePlayer : MathUtil.getClosestPlayersFromLocation(location, 4.0d)) {
            closePlayer.teleport(teleport);
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (location != null) {
            for (Player worldPlayer : location.getWorld().getPlayers()) {
                Block block = worldPlayer.getLocation(location).subtract(0.0d, 1.0d, 0.0d).getBlock();

                if (structure.getPlacedEntries().contains(block) && BOUNCE_MATERIALS.contains(block.getType())) {
                    worldPlayer.setVelocity(worldPlayer.getVelocity().add(new Vector(0.0d, Math.random() >= 0.5d ? 2.5d : 3.0d, 0.0d)));

                    worldPlayer.getWorld().playEffect(location, Effect.STEP_SOUND, Material.BLACK_CONCRETE);
                    worldPlayer.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5f, 1.4f);

                    User otherUser = context.getPlugin().getUserManager().getConnected(worldPlayer);

                    if (otherUser != null) {
                        otherUser.setFallDamageProtection(9);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        location = null;
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