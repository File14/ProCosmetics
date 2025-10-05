package se.file14.procosmetics.worldguard;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;

public class CosmeticCategoryFlag extends Flag<CosmeticCategory<?, ?, ?>> {

    private final ProCosmetics plugin;

    public CosmeticCategoryFlag(String name, ProCosmetics plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public CosmeticCategory<?, ?, ?> parseInput(FlagContext context) throws InvalidFlagFormat {
        String input = context.getUserInput();
        CosmeticCategory<?, ?, ?> category = plugin.getCategoryRegistries().getCategory(input);

        if (category == null) {
            throw new InvalidFlagFormat("Unknown cosmetic category: " + input);
        }
        return category;
    }

    @Override
    public CosmeticCategory<?, ?, ?> unmarshal(Object o) {
        if (o instanceof String str) {
            return plugin.getCategoryRegistries().getCategory(str);
        }
        return null;
    }

    @Override
    public Object marshal(CosmeticCategory o) {
        return o.getKey();
    }
}