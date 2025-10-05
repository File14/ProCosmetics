package se.file14.procosmetics.menu.menus;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.menu.menus.purchase.TreasurePurchaseMenu;
import se.file14.procosmetics.treasure.animation.type.Common;
import se.file14.procosmetics.treasure.animation.type.Legendary;
import se.file14.procosmetics.treasure.animation.type.Mythical;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class TreasureChestMenu extends MenuImpl {

    public TreasureChestMenu(ProCosmetics plugin, User user) {
        super(plugin, user, user.translate("menu.treasure_chests.title"),
                plugin.getConfigManager().getConfig("treasure_chests").getInt("menu.rows")
        );
    }

    @Override
    protected void addItems() {
        for (TreasureChest treasureChest : plugin.getTreasureChestManager().getTreasureChests()) {
            if (!treasureChest.isEnabled()) {
                continue;
            }
            int keys = user.getTreasureChests(treasureChest);
            String name = treasureChest.getName(user);
            ItemBuilder itemBuilder = treasureChest.getItemBuilder();
            TagResolver tagResolver = treasureChest.getResolvers(user);
            String path;

            if (!treasureChest.isPurchasable()) {
                path = "purchase_disabled";
            } else if (treasureChest.hasPurchasePermission(player)) {
                path = "purchasable";
            } else {
                path = "purchase_no_permission";
            }

            itemBuilder.setDisplayName(user.translate(
                    "menu.treasure_chests." + treasureChest.getKey() + "." + path + ".name",
                    Placeholder.unparsed("name", name),
                    tagResolver
            ));
            itemBuilder.setLoreComponent(user.translateList(
                    "menu.treasure_chests." + treasureChest.getKey() + "." + path + ".desc",
                    Placeholder.unparsed("name", name),
                    tagResolver
            ));

            setItem(itemBuilder.getSlot(), itemBuilder.getItemStack(), event -> {
                        if (keys < 1 || event.isRightClick()) {
                            if (treasureChest.isPurchasable() && treasureChest.hasPurchasePermission(player)) {
                                new TreasurePurchaseMenu(plugin, user, treasureChest).open();
                            } else {
                                playDenySound();
                            }
                        } else {
                            player.closeInventory();
                            TreasureChestPlatform platform = user.getCurrentPlatform();

                            if (platform == null) {
                                player.closeInventory();
                                return;
                            }

                            if (platform.isInUse()) {
                                user.sendMessage(user.translate("treasure_chest.already_in_use"));
                                return;
                            }
                            platform.setUser(user);

                            plugin.getDatabase().removeTreasureKeysAsync(user, treasureChest, 1).thenAcceptAsync(result -> {
                                if (result.leftBoolean()) {
                                    switch (treasureChest.getChestAnimationType()) {
                                        case COMMON: {
                                            new Common(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                        case MYTHICAL: {
                                            new Mythical(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                        case LEGENDARY: {
                                            new Legendary(plugin, platform, treasureChest, user);
                                            break;
                                        }
                                    }
                                } else {
                                    user.sendMessage(user.translate("generic.error.database"));
                                    platform.setUser(null);
                                }
                            }, plugin.getSyncExecutor());
                        }
                    }
            );
        }
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        Config config = plugin.getConfigManager().getConfig("treasure_chests");

        if (!config.getBoolean("menu.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.items.fill_empty_slots").getItemStack();
    }
}