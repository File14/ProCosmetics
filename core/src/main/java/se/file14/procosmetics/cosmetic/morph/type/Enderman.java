package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MetadataUtil;

public class Enderman implements MorphBehavior {

    private static final FireworkEffect FIREWORK_EFFECT = FireworkEffect.builder()
            .flicker(false)
            .withColor(Color.BLACK)
            .with(Type.BALL_LARGE)
            .trail(false)
            .build();

    private boolean defaultFlight = false;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        Player player = context.getPlayer();

        if (player.getAllowFlight()) {
            defaultFlight = true;
        } else if (context.getType().hasAbility()) {
            player.setAllowFlight(true);
        }
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        return InteractionResult.NO_ACTION;
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        Player player = context.getPlayer();

        if (!player.isFlying()) {
            return performTeleport(context);
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        Player player = context.getPlayer();
        player.setAllowFlight(defaultFlight);
    }

    @Override
    public boolean hasAttackAnimation() {
        return true;
    }

    @Override
    public boolean hasItemHoldAnimation() {
        return true;
    }

    private InteractionResult performTeleport(CosmeticContext<MorphType> context) {
        Player player = context.getPlayer();
        Location currentLocation = player.getLocation();

        // Play smoke effect at current location
        player.getWorld().playEffect(currentLocation, Effect.SMOKE, 4);

        // Get target block for teleportation
        Location teleportLocation = player.getTargetBlock(null, 17).getLocation();
        teleportLocation.setPitch(currentLocation.getPitch());
        teleportLocation.setYaw(currentLocation.getYaw());

        // Adjust Y if target block is not air
        if (!teleportLocation.getBlock().getType().isAir()) {
            teleportLocation.setY(teleportLocation.getY() + 1.0d);
        }

        // Perform teleportation
        player.teleport(teleportLocation);
        player.getWorld().playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.0f);

        // Spawn firework effect at teleport location
        spawnTeleportFirework(teleportLocation);

        return InteractionResult.SUCCESS;
    }

    private void spawnTeleportFirework(Location location) {
        location.getWorld().spawn(location.add(0.0d, 1.0d, 0.0d), Firework.class,
                entity -> {
                    FireworkMeta meta = entity.getFireworkMeta();
                    meta.addEffect(FIREWORK_EFFECT);
                    entity.setFireworkMeta(meta);
                    MetadataUtil.setCustomEntity(entity);
                }
        ).detonate();
    }
}
