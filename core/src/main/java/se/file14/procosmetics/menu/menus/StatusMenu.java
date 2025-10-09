package se.file14.procosmetics.menu.menus;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.status.StatusType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.CosmeticMenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class StatusMenu extends CosmeticMenuImpl<StatusType> {

    private static final int COOLDOWN = 20;

    public StatusMenu(ProCosmetics plugin, User user, CosmeticCategory<StatusType, ?, ?> category) {
        super(plugin, user, category);
    }

    @Override
    protected void addCustomItems() {
        if (category.getConfig().getBoolean("menu.items.toggle_self_view.enable")) {
            Player player = getPlayer();
            ItemBuilder itemBuilder = new ItemBuilderImpl(category.getConfig(), "menu.items.toggle_self_view");
            String path = user.hasSelfViewStatus() ? "disable" : "enable";
            itemBuilder.setDisplayName(user.translate("menu.statuses.toggle_self_view." + path + ".name"));
            itemBuilder.setLoreComponent(user.translateList("menu.statuses.toggle_self_view." + path + ".desc"));

            if (user.hasSelfViewStatus()) {
                itemBuilder.setGlintOverride(true);
            }

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                if (player.hasCooldown(itemBuilder.getItemStack())) {
                    return;
                }
                player.setCooldown(itemBuilder.getItemStack(), COOLDOWN);

                boolean toggle = !user.hasSelfViewStatus();
                user.setSelfViewStatus(toggle, true);
                plugin.getDatabase().setSelfViewStatusAsync(user, toggle);

                player.playSound(player, Sound.ENTITY_CHICKEN_EGG, 0.5f, 1.0f);
                refresh();
            });
        }
    }
}