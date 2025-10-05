package se.file14.procosmetics.cosmetic;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.Cosmetic;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.locale.Translator;
import se.file14.procosmetics.api.rarity.CosmeticRarity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.rarity.CosmeticRarityRegistry;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.function.Supplier;
import java.util.logging.Level;

public abstract class CosmeticTypeImpl<T extends CosmeticType<T, B>,
        B extends CosmeticBehavior<T>>
        implements CosmeticType<T, B> {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    public static final String PERMISSION_ALL_COSMETICS = "procosmetics.cosmetic.*";
    public static final String PERMISSION_PURCHASE_ALL_COSMETICS = "procosmetics.purchase.*";

    protected final String key;
    protected final CosmeticCategory<T, B, ?> category;
    private final Supplier<B> behaviorFactory;

    private final String permission;
    private final String legacyPermission;
    private final String purchasePermission;
    private final boolean enabled;
    private boolean findable;
    private boolean purchasable;
    private int cost;
    private final CosmeticRarity rarity;
    protected ItemBuilderImpl itemBuilder;

    public CosmeticTypeImpl(String key, CosmeticCategory<T, B, ?> category,
                            Supplier<B> behaviorFactory,
                            boolean enabled, boolean findable, boolean purchasable, int cost,
                            CosmeticRarity rarity, ItemStack itemStack) {
        this.key = key;
        this.category = category;
        this.behaviorFactory = behaviorFactory;
        this.permission = "procosmetics.cosmetic." + category.getKey() + "." + key;
        this.legacyPermission = permission.replace("_", "-");
        this.purchasePermission = "procosmetics.purchase." + category.getKey() + "." + key;
        this.enabled = enabled;
        this.findable = findable;
        this.purchasable = purchasable;
        this.cost = cost;
        this.rarity = rarity;
        this.itemBuilder = new ItemBuilderImpl(itemStack).setUnbreakable();
    }

    protected abstract Cosmetic<T, B> createInstance(ProCosmeticsPlugin plugin, User user, B behavior);

    public Cosmetic<T, B> create(ProCosmeticsPlugin plugin, User user) {
        if (behaviorFactory == null) {
            throw new IllegalStateException("Tried to create cosmetic instance of " + key + " without a behavior factory.");
        }
        return createInstance(plugin, user, behaviorFactory.get());
    }

    @Override
    public void equip(User user, boolean silent, boolean saveToDatabase) {
        Cosmetic<T, B> cosmetic = create(PLUGIN, user);
        cosmetic.equip(silent, saveToDatabase);
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getName(Translator translator) {
        return translator.translateRaw("cosmetic." + category.getKey() + "." + key);
    }

    @Override
    public TagResolver getResolvers(User user) {
        return TagResolver.resolver(
                Placeholder.unparsed("cost", String.valueOf(cost))
        );
    }

    @Override
    public CosmeticCategory<T, B, ?> getCategory() {
        return category;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(PERMISSION_ALL_COSMETICS)
                || player.hasPermission(category.getPermission())
                || player.hasPermission(permission)
                || player.hasPermission(legacyPermission);
    }

    @Override
    public boolean hasPurchasePermission(Player player) {
        return player.hasPermission(PERMISSION_PURCHASE_ALL_COSMETICS)
                || player.hasPermission(category.getPurchasePermission())
                || player.hasPermission(purchasePermission);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setFindable(boolean findable) {
        this.findable = findable;
    }

    public boolean isFindable() {
        return findable;
    }

    @Override
    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    @Override
    public boolean isPurchasable() {
        return purchasable;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public ItemStack getItemStack() {
        return itemBuilder.getItemStack();
    }

    public ItemBuilderImpl getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public CosmeticRarity getRarity() {
        return rarity;
    }

    public static abstract class BuilderImpl<T extends CosmeticType<T, B>,
            B extends CosmeticBehavior<T>,
            S extends Builder<T, B, S>> implements CosmeticType.Builder<T, B, S> {

        protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

        protected final String key;
        protected final CosmeticCategory<T, B, ?> category;
        protected ItemStack itemStack;
        protected CosmeticRarity rarity;
        protected boolean enabled = true;
        protected boolean findable = true;
        protected boolean purchasable = true;
        protected int cost = 0;
        protected Supplier<B> factory;

        public BuilderImpl(String key, CosmeticCategory<T, B, ?> category) {
            this.key = key;
            this.category = category;
        }

        protected abstract S self();

        protected String getPath() {
            return "cosmetics." + key + ".";
        }

        protected EntityType parseEntity(String path) {
            String entityTypeName = category.getConfig().getString(path);
            EntityType entityType = EntityType.PIG;

            if (entityTypeName == null || entityTypeName.isEmpty()) {
                PLUGIN.getLogger().log(Level.WARNING, "Missing entity type for " + key + ". Using: " + entityType.name() + " (default).");
                return entityType;
            }

            try {
                entityType = EntityType.valueOf(entityTypeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                PLUGIN.getLogger().log(Level.WARNING, "Invalid entity type " + entityTypeName + " for " + key + ". Using " + entityType.name() + " (default).");
            }
            return entityType;
        }

        @Nullable
        protected Sound parseSound(String path) {
            String soundName = category.getConfig().getString(path);

            if (soundName == null || soundName.isEmpty()) {
                return null;
            }

            try {
                return Sound.valueOf(soundName.toUpperCase());
            } catch (IllegalArgumentException e) {
                PLUGIN.getLogger().log(Level.WARNING, "Invalid sound " + soundName + " for " + key + ".");
            }
            return null;
        }

        public S readFromConfig() {
            Config config = category.getConfig();
            String path = getPath();

            this.enabled = config.getBoolean(path + "enable");
            this.findable = config.getBoolean(path + "obtainable");
            this.purchasable = config.getBoolean(path + "purchasable");
            this.cost = config.getInt(path + "cost");
            this.rarity = CosmeticRarityRegistry.getSafelyBy(config.getString(path + "rarity"));
            this.itemStack = new ItemBuilderImpl(config, path).getItemStack();

            return self();
        }

        @Override
        public S itemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return self();
        }

        @Override
        public S rarity(CosmeticRarity rarity) {
            this.rarity = rarity;
            return self();
        }

        @Override
        public S enabled(boolean enabled) {
            this.enabled = enabled;
            return self();
        }

        @Override
        public S findable(boolean findable) {
            this.findable = findable;
            return self();
        }

        @Override
        public S purchasable(boolean purchasable) {
            this.purchasable = purchasable;
            return self();
        }

        @Override
        public S cost(int cost) {
            this.cost = cost;
            return self();
        }

        @Override
        public S factory(Supplier<B> factory) {
            this.factory = factory;
            return self();
        }
    }
}