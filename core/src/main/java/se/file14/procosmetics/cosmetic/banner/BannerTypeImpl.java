package se.file14.procosmetics.cosmetic.banner;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.banner.Banner;
import se.file14.procosmetics.api.cosmetic.banner.BannerBehavior;
import se.file14.procosmetics.api.cosmetic.banner.BannerType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.AnimationFrame;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BannerTypeImpl extends CosmeticTypeImpl<BannerType, BannerBehavior> implements BannerType {

    private final long tickInterval;
    private final List<AnimationFrame> frames;

    public BannerTypeImpl(String key,
                          CosmeticCategory<BannerType, BannerBehavior, ?> category,
                          Supplier<BannerBehavior> behaviorFactory,
                          boolean enabled,
                          boolean findable,
                          boolean purchasable,
                          int cost,
                          CosmeticRarity rarity,
                          ItemStack itemStack,
                          long tickInterval,
                          List<AnimationFrame> frames) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.tickInterval = tickInterval;
        this.frames = frames;
    }

    @Override
    protected Banner createInstance(ProCosmeticsPlugin plugin, User user, BannerBehavior behavior) {
        return new BannerImpl(plugin, user, this, behavior);
    }

    @Override
    public long getTickInterval() {
        return tickInterval;
    }

    @Override
    public List<AnimationFrame> getFrames() {
        return frames;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<BannerType, BannerBehavior, BannerType.Builder> implements BannerType.Builder {

        private long tickInterval = -1L;
        private List<AnimationFrame> frames = new ArrayList<>();

        public BuilderImpl(String key, CosmeticCategory<BannerType, BannerBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected BannerType.Builder self() {
            return this;
        }

        @Override
        public BannerType.Builder readFromConfig() {
            super.readFromConfig();

            frames.clear();

            Config config = category.getConfig();
            String path = getPath();

            tickInterval = config.getInt(path + "tick_interval", false);

            if (config.hasKey(path + "frames")) {
                List<?> animationList = config.getGenericList(path + "frames");

                if (animationList != null) {
                    for (Object animation : animationList) {
                        if (animation instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> frameMap = (Map<String, Object>) animation;
                            String item = (String) frameMap.get("item");
                            int duration = (int) frameMap.get("duration");

                            if (item != null && duration > 0) {
                                addFrame(AnimationFrame.of(new ItemBuilderImpl(item).getItemStack(), duration));
                            }
                        }
                    }
                }
            }
            return this;
        }

        @Override
        public BannerType.Builder tickInterval(long tickInterval) {
            this.tickInterval = tickInterval;
            return this;
        }

        @Override
        public BannerType.Builder frames(List<AnimationFrame> frames) {
            this.frames = new ArrayList<>(frames);
            return this;
        }

        @Override
        public BannerType.Builder addFrame(AnimationFrame frame) {
            frames.add(frame);
            return this;
        }

        @Override
        public BannerType build() {
            return new BannerTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    tickInterval,
                    frames
            );
        }
    }
}