package se.file14.procosmetics.command.commands.procosmetics.addall;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.command.SubCommand;

import java.util.stream.Collectors;

public class AddAllAmmoCommand extends SubCommand<CommandSender> {

    public AddAllAmmoCommand(ProCosmeticsPlugin plugin) {
        super(plugin, "procosmetics.command.addall.ammo", true);
        addFlats("allall", "ammo");
        addArgument(String.class, "ammo", sender -> plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getTypes().stream().map(GadgetType::getKey).collect(Collectors.toList()));
        addArgument(Integer.class, "amount");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        Translator translator = translator(sender);
        GadgetType gadgetType = plugin.getCategoryRegistries().gadgets().getCosmeticRegistry().getType(parseArgument(args, 2));

        if (gadgetType == null) {
            audience(sender).sendMessage(translator.translate("cosmetic.not_found"));
            return;
        }
        int amount = parseArgument(args, 3);
        int players = 0;

        for (User user : plugin.getUserManager().getAllConnected()) {
            plugin.getDatabase().addGadgetAmmoAsync(user, gadgetType, amount);
            players++;
        }

        audience(sender).sendMessage(translator.translate(
                "command.addall.ammo",
                Placeholder.unparsed("amount", String.valueOf(amount)),
                Placeholder.unparsed("gadget", gadgetType.getName(translator)),
                Placeholder.unparsed("player_count", String.valueOf(players))
        ));
    }
}