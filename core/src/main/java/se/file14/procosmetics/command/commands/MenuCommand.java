package se.file14.procosmetics.command.commands;


import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;
import se.file14.procosmetics.menu.menus.MainMenu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuCommand extends SubCommand<CommandSender> {

    private static final String MAIN_MENU = "main";

    public MenuCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.menu", true);
        addFlat("menu");
        addArgument(Player.class, "player", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "menu", sender -> {
            List<String> menus = plugin.getCategoryRegistries().getCategories().stream().map(CosmeticCategory::getKey).collect(Collectors.toList());
            menus.add(MAIN_MENU);
            return menus;
        });
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        Player target = parseArgument(args, 1);

        if (target == null) {
            audience(sender).sendMessage(translator.translate("generic.player_offline"));
            return;
        }
        User user = plugin.getUserManager().getConnected(target);

        if (user == null) {
            audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
            return;
        }
        String menuName = parseArgument(args, 2).toString().toLowerCase().replace("-", "_");

        if (menuName.startsWith(MAIN_MENU)) {
            new MainMenu(plugin, user).open();
        } else {
            CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(menuName);

            if (category == null) {
                audience(sender).sendMessage(translator.translate("command.menu.not_found"));
                return;
            }
            category.createMenu(plugin, user).open();
        }
        audience(sender).sendMessage(translator.translate(
                "command.menu.success",
                Placeholder.unparsed("menu", menuName),
                Placeholder.unparsed("player", target.getName())
        ));
    }
}
