package se.file14.procosmetics.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.balloon.Balloon;
import se.file14.procosmetics.api.cosmetic.gadget.Gadget;
import se.file14.procosmetics.api.cosmetic.miniature.Miniature;
import se.file14.procosmetics.api.cosmetic.morph.Morph;
import se.file14.procosmetics.api.cosmetic.mount.Mount;
import se.file14.procosmetics.api.cosmetic.pet.Pet;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.status.Status;
import se.file14.procosmetics.api.user.User;

public class CosmeticListener implements Listener {

    private static final int MAX_DISTANCE_SQUARED = 128 * 128;

    private final ProCosmeticsPlugin plugin;

    public CosmeticListener(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        User user = plugin.getUserManager().getConnected(event.getEntity());

        if (user != null) {
            for (Cosmetic<?, ?> cosmetic : user.getCosmetics().values()) {
                CosmeticCategory<?, ?, ?> category = cosmetic.getType().getCategory();

                if (category.equals(plugin.getCategoryRegistries().arrowEffects())
                        || category.equals(plugin.getCategoryRegistries().deathEffects())) {
                    continue;
                }

                if (category.equals(plugin.getCategoryRegistries().music())) {
                    user.removeCosmetic(plugin.getCategoryRegistries().music(), true, false);
                } else {
                    user.unequipCosmetic(category, true, false);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || from.getWorld().equals(to.getWorld()) && from.distanceSquared(to) < MAX_DISTANCE_SQUARED) {
            return;
        }
        to = to.clone(); // Keep this for safety reasons as event.getTo() is not a copy of the loc

        Player player = event.getPlayer();
        User user = plugin.getUserManager().getConnected(player);

        if (user == null) {
            return;
        }
        Balloon balloon = (Balloon) user.getCosmetic(plugin.getCategoryRegistries().balloons());
        Gadget gadget = (Gadget) user.getCosmetic(plugin.getCategoryRegistries().gadgets());
        Miniature miniature = (Miniature) user.getCosmetic(plugin.getCategoryRegistries().miniatures());
        Mount mount = (Mount) user.getCosmetic(plugin.getCategoryRegistries().mounts());
        Morph morph = (Morph) user.getCosmetic(plugin.getCategoryRegistries().morphs());
        Pet pet = (Pet) user.getCosmetic(plugin.getCategoryRegistries().pets());
        Status status = (Status) user.getCosmetic(plugin.getCategoryRegistries().statuses());

        if (balloon != null && balloon.isEquipped()) {
            balloon.getTracker().respawnAt(to);
        }

        if (gadget != null && gadget.isEquipped() && gadget.getBehavior().shouldUnequipOnTeleport()) {
            gadget.unequip(false, true);
        }

        if (miniature != null && miniature.isEquipped()) {
            miniature.getNMSEntity().getTracker().respawnAt(to);
        }

        if (mount != null && mount.isEquipped()) {
            mount.spawn(to);
        }

        if (morph != null && morph.isEquipped() && user.hasSelfViewMorph()) {
            morph.getNMSEntity().getTracker().respawnAt(to);
        }

        if (pet != null && pet.isEquipped()) {
            pet.spawn(to);
        }

        if (status != null && status.isEquipped() && user.hasSelfViewStatus()) {
            status.getNMSEntity().getTracker().respawnAt(to);
        }
    }
}