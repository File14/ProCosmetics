package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class Cat implements MorphBehavior {

    private static final List<org.bukkit.entity.Cat.Type> TYPES = List.of(
            org.bukkit.entity.Cat.Type.TABBY,
            org.bukkit.entity.Cat.Type.BLACK,
            org.bukkit.entity.Cat.Type.RED,
            org.bukkit.entity.Cat.Type.SIAMESE,
            org.bukkit.entity.Cat.Type.BRITISH_SHORTHAIR,
            org.bukkit.entity.Cat.Type.CALICO,
            org.bukkit.entity.Cat.Type.PERSIAN,
            org.bukkit.entity.Cat.Type.RAGDOLL,
            org.bukkit.entity.Cat.Type.WHITE,
            org.bukkit.entity.Cat.Type.JELLIE,
            org.bukkit.entity.Cat.Type.ALL_BLACK
    );

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof org.bukkit.entity.Cat cat) {
            cat.setCatType(MathUtil.getRandomElement(TYPES));
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = context.getPlayer();
            player.getWorld().playSound(player, Sound.ENTITY_CAT_AMBIENT, 0.8f, 1.2f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.NO_ACTION;
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        if (event.isSneaking()) {
            Player player = context.getPlayer();
            player.getWorld().playSound(player, Sound.ENTITY_CAT_PURR, 0.8f, 1.0f);

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