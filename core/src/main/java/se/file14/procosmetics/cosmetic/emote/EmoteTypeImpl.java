/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.cosmetic.emote;

import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.emote.Emote;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.AnimationFrame;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;
import se.file14.procosmetics.util.item.ItemBuilderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EmoteTypeImpl extends CosmeticTypeImpl<EmoteType, EmoteBehavior> implements EmoteType {

    private final long tickInterval;
    private final List<AnimationFrame> frames;

    public EmoteTypeImpl(String key,
                         CosmeticCategory<EmoteType, EmoteBehavior, ?> category,
                         Supplier<EmoteBehavior> behaviorFactory,
                         boolean enabled,
                         boolean purchasable,
                         int cost,
                         CosmeticRarity rarity,
                         ItemStack itemStack,
                         List<String> treasureChests,
                         long tickInterval,
                         List<AnimationFrame> frames) {
        super(key, category, behaviorFactory, enabled, purchasable, cost, rarity, itemStack, treasureChests);
        this.tickInterval = tickInterval;
        this.frames = frames != null ? frames : new ArrayList<>();
    }

    @Override
    protected Emote createInstance(ProCosmeticsPlugin plugin, User user, EmoteBehavior behavior) {
        return new EmoteImpl(plugin, user, this, behavior);
    }

    @Override
    public long getTickInterval() {
        return tickInterval;
    }

    @Override
    public List<AnimationFrame> getFrames() {
        return frames;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<EmoteType, EmoteBehavior, EmoteType.Builder> implements EmoteType.Builder {

        private long tickInterval = 1L;
        private List<AnimationFrame> frames = new ArrayList<>();

        public BuilderImpl(String key, CosmeticCategory<EmoteType, EmoteBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected EmoteType.Builder self() {
            return this;
        }

        @Override
        public EmoteType.Builder readFromConfig() {
            super.readFromConfig();

            frames.clear();

            Config config = category.getConfig();
            String path = getPath();

            tickInterval = config.getInt(path + "tick_interval");

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
        public EmoteType.Builder tickInterval(long tickInterval) {
            this.tickInterval = tickInterval;
            return this;
        }

        @Override
        public EmoteType.Builder frames(List<AnimationFrame> frames) {
            this.frames = new ArrayList<>(frames);
            return this;
        }

        @Override
        public EmoteType.Builder addFrame(AnimationFrame frame) {
            frames.add(frame);
            return this;
        }

        @Override
        public EmoteType build() {
            return new EmoteTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    treasureChests,
                    tickInterval,
                    frames
            );
        }
    }
}
