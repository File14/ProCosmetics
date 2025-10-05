package se.file14.procosmetics.command.commands.get;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class GetAmmoCommand extends SubCommand<CommandSender> {

    public GetAmmoCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.get.ammo", true);
        addFlats("get", "ammo");
        addArgument(String.class, "target", sender -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        addArgument(String.class, "ammo", sender -> plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getTypes().stream().map(GadgetType::getKey).collect(Collectors.toList()));
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
        plugin.getUserManager().getAsync(name).thenAccept(user -> {
            if (user == null) {
                audience(sender).sendMessage(translator.translate("generic.error.player_data.target"));
                return;
            }
            audience(sender).sendMessage(translator.translate(
                    "command.get.ammo",
                    Placeholder.unparsed("player", user.getName()),
                    Placeholder.unparsed("amount", String.valueOf(user.getAmmo(gadgetType))),
                    Placeholder.unparsed("gadget", gadgetType.getName(translator))
            ));
        });
    }
}