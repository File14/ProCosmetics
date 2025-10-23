package se.file14.procosmetics.menu.menus.purchase;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.economy.EconomyProvider;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.event.PlayerPurchaseCosmeticEventImpl;
import se.file14.procosmetics.menu.MenuImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

public class CosmeticPurchaseMenu extends MenuImpl {

    private static final int COOLDOWN = 20;

    private final CosmeticType<?, ?> cosmeticType;
    private final Config config;

    public CosmeticPurchaseMenu(ProCosmetics plugin, User user, CosmeticType<?, ?> cosmeticType) {
        super(plugin, user,
                user.translate("menu.purchase.cosmetic.title"),
                plugin.getConfigManager().getMainConfig().getInt("menu.purchase.cosmetic.rows")
        );
        this.cosmeticType = cosmeticType;
        this.config = plugin.getConfigManager().getMainConfig();
    }

    @Override
    protected void addItems() {
        // Add the cosmetic item to display
        CosmeticCategory<?, ?, ?> category = cosmeticType.getCategory();
        ItemBuilder cosmeticItem = new ItemBuilderImpl(cosmeticType.getItemStack());
        String name = cosmeticType.getName(user);
        TagResolver tagResolver = cosmeticType.getResolvers(user);
        cosmeticItem.setDisplayName(user.translate(
                "menu." + category.getKey() + "." + cosmeticType.getKey() + ".name",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        cosmeticItem.setLoreComponent(user.translateList(
                "menu." + category.getKey() + "." + cosmeticType.getKey() + ".desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(13, cosmeticItem.getItemStack(), event -> {
        });

        ItemBuilderImpl acceptPurchase = new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.accept");
        acceptPurchase.setDisplayName(user.translate("menu.purchase.cosmetic.accept.name"));
        acceptPurchase.setLoreComponent(user.translateList(
                "menu.purchase.cosmetic.accept.desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));
        Server server = plugin.getJavaPlugin().getServer();
        player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

        // Accept purchase button
        setItem(acceptPurchase.getSlot(), acceptPurchase.getItemStack(), event -> {
            if (player.hasCooldown(acceptPurchase.getItemStack())) {
                return;
            }
            close();
            player.setCooldown(acceptPurchase.getItemStack(), COOLDOWN);

            EconomyProvider economy = plugin.getEconomyManager().getEconomyProvider();
            int cost = cosmeticType.getCost();

            if (!economy.hasCoins(user, cost)) {
                economy.sendInsufficientCoinsMessage(user, cost);
                playDenySound();
                return;
            }

            economy.removeCoinsAsync(user, cost).thenAcceptAsync(result -> {
                if (result.booleanValue()) {
                    server.dispatchCommand(server.getConsoleSender(), config.getString("settings.permission_add_command")
                            .replace("<player>", player.getName())
                            .replace("<permission>", cosmeticType.getPermission())
                    );
                    server.getPluginManager().callEvent(new PlayerPurchaseCosmeticEventImpl(plugin, user, player, cosmeticType));
                    cosmeticType.equip(user, false, true);
                } else {
                    economy.sendInsufficientCoinsMessage(user, cost);
                    playDenySound();
                }
            }, plugin.getSyncExecutor());
        });

        // Deny purchase button
        ItemBuilderImpl denyPurchase = new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.deny");
        denyPurchase.setDisplayName(user.translate("menu.purchase.cosmetic.deny.name"));
        denyPurchase.setLoreComponent(user.translateList(
                "menu.purchase.cosmetic.deny.desc",
                Placeholder.unparsed("name", name),
                tagResolver
        ));

        setItem(denyPurchase.getSlot(), denyPurchase.getItemStack(), event -> {
            playClickSound();
            close();
        });
    }

    @Override
    public ItemStack getFillEmptySlotsItem() {
        if (!config.getBoolean("menu.purchase.cosmetic.items.fill_empty_slots.enable")) {
            return null;
        }
        return new ItemBuilderImpl(config, "menu.purchase.cosmetic.items.fill_empty_slots").getItemStack();
    }
}