package se.file14.procosmetics.api.treasure;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.treasure.animation.AnimationType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.api.util.structure.StructureData;

import java.util.List;

public interface TreasureChest {

    boolean hasPurchasePermission(Player player);

    String getKey();

    String getName(Translator translator);

    TagResolver getResolvers(User user);

    boolean isEnabled();

    boolean isPurchasable();

    int getCost();

    int getChestsToOpen();

    AnimationType getChestAnimationType();

    List<StructureData> getStructures();

    boolean isOpeningBroadcast();

    ItemBuilder getItemBuilder();

    ItemStack getItemStack();
}

