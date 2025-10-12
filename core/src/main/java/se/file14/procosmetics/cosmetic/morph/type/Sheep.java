package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

public class Sheep implements MorphBehavior {

    private static final DyeColor[] DYE_COLORS = DyeColor.values();

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
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

        if (!player.isSneaking()) {
            if (nmsEntity.getBukkitEntity() instanceof org.bukkit.entity.Sheep sheep) {
                sheep.setColor(DYE_COLORS[MathUtil.randomRangeInt(0, DYE_COLORS.length - 1)]);
                nmsEntity.sendEntityMetadataPacket();
            }
            player.getWorld().playSound(player, Sound.ENTITY_SHEEP_AMBIENT, 0.5f, 1.0f);
            player.getWorld().spawnParticle(Particle.CLOUD,
                    player.getLocation().add(0.0d, 0.5d, 0.0d),
                    20,
                    0.5d,
                    0.5d,
                    0.5d,
                    0.0d
            );
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
    }
}