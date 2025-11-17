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
package se.file14.procosmetics.api.cosmetic.registry;

import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.api.cosmetic.CosmeticBehavior;
import se.file14.procosmetics.api.cosmetic.CosmeticType;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonBehavior;
import se.file14.procosmetics.api.cosmetic.balloon.BalloonType;
import se.file14.procosmetics.api.cosmetic.banner.BannerBehavior;
import se.file14.procosmetics.api.cosmetic.banner.BannerType;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectBehavior;
import se.file14.procosmetics.api.cosmetic.deatheffect.DeathEffectType;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureBehavior;
import se.file14.procosmetics.api.cosmetic.miniature.MiniatureType;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.cosmetic.mount.MountBehavior;
import se.file14.procosmetics.api.cosmetic.mount.MountType;
import se.file14.procosmetics.api.cosmetic.music.MusicBehavior;
import se.file14.procosmetics.api.cosmetic.music.MusicType;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;
import se.file14.procosmetics.api.cosmetic.status.StatusBehavior;
import se.file14.procosmetics.api.cosmetic.status.StatusType;

import java.util.Collection;

/**
 * Represents the central registry that holds all cosmetic categories.
 *
 * @see CosmeticCategory
 * @see CosmeticRegistry
 * @see CosmeticType
 */
public interface CategoryRegistries {

    /**
     * Registers a new cosmetic category into the registry.
     *
     * @param registry the category to register
     * @param <T>      the cosmetic type contained within the category
     * @param <B>      the behavior type associated with the cosmetic
     * @param <U>      the builder type used to construct the cosmetic type
     */
    <T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, U extends CosmeticType.Builder<T, B, U>>
    void register(CosmeticCategory<T, B, U> registry);

    /**
     * Retrieves a cosmetic category by its unique key.
     *
     * @param key the category key
     * @param <T> the cosmetic type contained within the category
     * @param <B> the behavior type associated with the cosmetic
     * @param <U> the builder type used to construct the cosmetic type
     * @return the matching {@link CosmeticCategory}, or {@code null} if not found
     */
    @Nullable
    @SuppressWarnings("unchecked")
    default <T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, U extends CosmeticType.Builder<T, B, U>> CosmeticCategory<T, B, U> getCategory(String key) {
        return (CosmeticCategory<T, B, U>) getCategoryRaw(key);
    }

    /**
     * Checks whether a category with the given key exists in the registry.
     *
     * @param key the category key
     * @return {@code true} if a category with the key exists, otherwise {@code false}
     */
    default boolean hasRegistry(String key) {
        return getCategoryRaw(key) != null;
    }

    /**
     * Retrieves a cosmetic category without generic type safety.
     *
     * @param key the category key
     * @return the category, or {@code null} if not found
     */
    @Nullable
    CosmeticCategory<?, ?, ?> getCategoryRaw(String key);

    /**
     * Returns all registered cosmetic categories.
     *
     * @return a collection of all registered {@link CosmeticCategory} instances
     */
    Collection<CosmeticCategory<?, ?, ?>> getCategories();

    // --- Built-in category accessors ---

    /**
     * Gets the arrow effects cosmetic category.
     *
     * @return the built-in {@code arrow_effects} category, or {@code null} if not registered
     */
    default CosmeticCategory<ArrowEffectType, ArrowEffectBehavior, ArrowEffectType.Builder> arrowEffects() {
        return getCategory(BuiltIn.ARROW_EFFECTS);
    }

    /**
     * Gets the balloons cosmetic category.
     *
     * @return the built-in {@code balloons} category, or {@code null} if not registered
     */
    default CosmeticCategory<BalloonType, BalloonBehavior, BalloonType.Builder> balloons() {
        return getCategory(BuiltIn.BALLOONS);
    }

    /**
     * Gets the banners cosmetic category.
     *
     * @return the built-in {@code banners} category, or {@code null} if not registered
     */
    default CosmeticCategory<BannerType, BannerBehavior, BannerType.Builder> banners() {
        return getCategory(BuiltIn.BANNERS);
    }

    /**
     * Gets the death effects cosmetic category.
     *
     * @return the built-in {@code death_effects} category, or {@code null} if not registered
     */
    default CosmeticCategory<DeathEffectType, DeathEffectBehavior, DeathEffectType.Builder> deathEffects() {
        return getCategory(BuiltIn.DEATH_EFFECTS);
    }

    /**
     * Gets the emotes cosmetic category.
     *
     * @return the built-in {@code emotes} category, or {@code null} if not registered
     */
    default CosmeticCategory<EmoteType, EmoteBehavior, EmoteType.Builder> emotes() {
        return getCategory(BuiltIn.EMOTES);
    }

    /**
     * Gets the gadgets cosmetic category.
     *
     * @return the built-in {@code gadgets} category, or {@code null} if not registered
     */
    default CosmeticCategory<GadgetType, GadgetBehavior, GadgetType.Builder> gadgets() {
        return getCategory(BuiltIn.GADGETS);
    }

    /**
     * Gets the miniatures cosmetic category.
     *
     * @return the built-in {@code miniatures} category, or {@code null} if not registered
     */
    default CosmeticCategory<MiniatureType, MiniatureBehavior, MiniatureType.Builder> miniatures() {
        return getCategory(BuiltIn.MINIATURES);
    }

    /**
     * Gets the morphs cosmetic category.
     *
     * @return the built-in {@code morphs} category, or {@code null} if not registered
     */
    default CosmeticCategory<MorphType, MorphBehavior, MorphType.Builder> morphs() {
        return getCategory(BuiltIn.MORPHS);
    }

    /**
     * Gets the mounts cosmetic category.
     *
     * @return the built-in {@code mounts} category, or {@code null} if not registered
     */
    default CosmeticCategory<MountType, MountBehavior, MountType.Builder> mounts() {
        return getCategory(BuiltIn.MOUNTS);
    }

    /**
     * Gets the music cosmetic category.
     *
     * @return the built-in {@code music} category, or {@code null} if not registered
     */
    default CosmeticCategory<MusicType, MusicBehavior, MusicType.Builder> music() {
        return getCategory(BuiltIn.MUSIC);
    }

    /**
     * Gets the particle effects cosmetic category.
     *
     * @return the built-in {@code particle_effects} category, or {@code null} if not registered
     */
    default CosmeticCategory<ParticleEffectType, ParticleEffectBehavior, ParticleEffectType.Builder> particleEffects() {
        return getCategory(BuiltIn.PARTICLE_EFFECTS);
    }

    /**
     * Gets the pets cosmetic category.
     *
     * @return the built-in {@code pets} category, or {@code null} if not registered
     */
    default CosmeticCategory<PetType, PetBehavior, PetType.Builder> pets() {
        return getCategory(BuiltIn.PETS);
    }

    /**
     * Gets the statuses cosmetic category.
     *
     * @return the built-in {@code statuses} category, or {@code null} if not registered
     */
    default CosmeticCategory<StatusType, StatusBehavior, StatusType.Builder> statuses() {
        return getCategory(BuiltIn.STATUSES);
    }

    /**
     * Defines constant keys for all built-in cosmetic categories.
     */
    interface BuiltIn {

        /**
         * Built-in key for arrow effects.
         */
        String ARROW_EFFECTS = "arrow_effects";

        /**
         * Built-in key for balloons.
         */
        String BALLOONS = "balloons";

        /**
         * Built-in key for banners.
         */
        String BANNERS = "banners";

        /**
         * Built-in key for death effects.
         */
        String DEATH_EFFECTS = "death_effects";

        /**
         * Built-in key for emotes.
         */
        String EMOTES = "emotes";

        /**
         * Built-in key for gadgets.
         */
        String GADGETS = "gadgets";

        /**
         * Built-in key for miniatures.
         */
        String MINIATURES = "miniatures";

        /**
         * Built-in key for morphs.
         */
        String MORPHS = "morphs";

        /**
         * Built-in key for mounts.
         */
        String MOUNTS = "mounts";

        /**
         * Built-in key for music cosmetics.
         */
        String MUSIC = "music";

        /**
         * Built-in key for particle effects.
         */
        String PARTICLE_EFFECTS = "particle_effects";

        /**
         * Built-in key for pets.
         */
        String PETS = "pets";

        /**
         * Built-in key for statuses.
         */
        String STATUSES = "statuses";
    }
}
