package se.file14.procosmetics.cosmetic.morph;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.morph.Morph;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;

public class MorphImpl extends CosmeticImpl<MorphType, MorphBehavior> implements Morph {

    protected final Location location = new Location(null, 0.0d, 0.0, 0.0d);
    private final boolean showNameTag;
    protected NMSEntity nmsEntity;
    protected boolean cooldown;

    public MorphImpl(ProCosmeticsPlugin plugin, User user, MorphType type, MorphBehavior behavior) {
        super(plugin, user, type, behavior);
        showNameTag = getType().getCategory().getConfig().getBoolean("self_view.show_name_tag");
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().balloons(), false, true);
        user.removeCosmetic(plugin.getCategoryRegistries().banners(), false, true);
        user.removeCosmetic(plugin.getCategoryRegistries().emotes(), false, true);
        user.removeCosmetic(plugin.getCategoryRegistries().mounts(), false, true);
        user.removeCosmetic(plugin.getCategoryRegistries().statuses(), false, true);

        nmsEntity = plugin.getNMSManager().createEntity(player.getWorld(), cosmeticType.getEntityType());

        if (showNameTag) {
            nmsEntity.setCustomName(Component.text(player.getName()));
        }

        if (!user.hasSelfViewMorph()) {
            nmsEntity.getTracker().addAntiViewer(player);
        } else {
            nmsEntity.removeCollision(player);
        }
        nmsEntity.setPositionRotation(player.getLocation(location));
        behavior.setupEntity(this, nmsEntity);
        nmsEntity.getTracker().startTracking();

        player.setInvisible(true);

        runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    protected void onUpdate() {
        updateEntityPosition();

        if (cosmeticType.hasAbility()) {
            behavior.onUpdate(this, nmsEntity);
        }
    }

    @Override
    protected void onUnequip() {
        if (user.hasSelfViewMorph()) {
            nmsEntity.addCollision(player);
        }
        player.setInvisible(false);

        nmsEntity.getTracker().destroy();
        nmsEntity = null;
    }

    private void updateEntityPosition() {
        player.getLocation(location);
        nmsEntity.sendPositionRotationPacket(location);
    }

    private boolean canPerformAbility() {
        return !cooldown && cosmeticType.hasAbility();
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.getPlayer() == player && nmsEntity != null) {
            nmsEntity.setPose(event.isSneaking() ? Pose.SNEAKING : Pose.STANDING);
            nmsEntity.sendEntityMetadataPacket();

            if (canPerformAbility()) {
                MorphBehavior.InteractionResult result = behavior.onToggleSneak(this, event, nmsEntity);

                if (result.shouldApplyCooldown()) {
                    addCooldown(cosmeticType.getCooldown());
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        // Ignore all physical
        if (action == Action.PHYSICAL) {
            return;
        }

        if (event.getPlayer() == player) {
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                if (behavior.hasAttackAnimation()) {
                    nmsEntity.sendAnimatePacket(0);
                }
            }

            if (canPerformAbility()) {
                MorphBehavior.InteractionResult result = behavior.onInteract(this, event, nmsEntity);

                if (result.shouldApplyCooldown()) {
                    addCooldown(cosmeticType.getCooldown());
                }
            }
        }
    }


    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer() == player && behavior.hasItemHoldAnimation()) {
            ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
            ItemStack previousItemStack = player.getInventory().getItem(event.getPreviousSlot());

            if (itemStack != null && previousItemStack == null
                    || itemStack == null && previousItemStack != null
                    || itemStack != null && previousItemStack.getType() != itemStack.getType()) {
                nmsEntity.setMainHand(itemStack);
                nmsEntity.sendEntityEquipmentPacket();
            }
        }
    }

    @Override
    public NMSEntity getNMSEntity() {
        return nmsEntity;
    }

    protected void addCooldown(double time) {
        cooldown = true;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (isEquipped()) {
                user.sendActionBar(user.translate(
                        "cosmetic.morphs.ability.cooldown.ready",
                        Placeholder.unparsed("morph", cosmeticType.getName(user))
                ));
                cooldown = false;
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2.0f);
            }
        }, (long) time * 20L);
    }

    public boolean isCooldown() {
        return cooldown;
    }
}