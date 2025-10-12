package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Transformation;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;

public class Block implements MorphBehavior {

    private Material currentMaterial = Material.EMERALD_BLOCK;

    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (nmsEntity.getBukkitEntity() instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(currentMaterial.createBlockData());
            blockDisplay.setInterpolationDelay(0);
            blockDisplay.setInterpolationDuration(1);
            blockDisplay.setTeleportDuration(1);

            Transformation transformation = blockDisplay.getTransformation();
            transformation.getTranslation().set(-0.5f, 0.0f, -0.5f); // Center on X and Z
            blockDisplay.setTransformation(transformation);
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            org.bukkit.block.Block block = event.getClickedBlock();

            if (block.getType().isItem() && block.getType() != currentMaterial) {
                currentMaterial = block.getType();
                Player player = context.getPlayer();

                if (nmsEntity instanceof BlockDisplay blockDisplay) {
                    blockDisplay.setBlock(currentMaterial.createBlockData());
                }
                nmsEntity.sendEntityEquipmentPacket();

                player.getWorld().playSound(
                        player,
                        Sound.UI_BUTTON_CLICK,
                        0.5f,
                        1.0f
                );
                player.getWorld().playEffect(
                        player.getLocation(),
                        Effect.STEP_SOUND,
                        currentMaterial
                );
                return InteractionResult.SUCCESS;
            }
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
