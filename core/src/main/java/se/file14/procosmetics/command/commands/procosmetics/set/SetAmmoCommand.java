package se.file14.procosmetics.command.commands.procosmetics.set;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class SetAmmoCommand extends SubCommand<CommandSender> {

    public SetAmmoCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.set.ammo", true);
        addFlats("set", "ammo");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "ammo", sender -> plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getTypes().stream().map(GadgetType::getKey).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        String name = parseArgument(args, 2);
        GadgetType gadgetType = plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getType(parseArgument(args, 3));

        if (gadgetType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }
        int amount = parseArgument(args, 4);

        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            plugin.getDatabase().setGadgetAmmoAsync(user, gadgetType, amount).thenAccept(result -> {
                if (result.leftBoolean()) {
                    audience(sender).sendMessage(translator.translate(
                            "command.set.ammo",
                            Placeholder.unparsed("amount", String.valueOf(amount)),
                            Placeholder.unparsed("gadget", gadgetType.getName(translator)),
                            Placeholder.unparsed("player", user.getName())
                    ));
                } else {
                    audience(sender).sendMessage(translator.translate("generic.error.database"));
                }
            });
        });
    }
}