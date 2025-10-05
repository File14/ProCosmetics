package se.file14.procosmetics.api.cosmetic.registry;

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

import javax.annotation.Nullable;
import java.util.Collection;

public interface CategoryRegistries {

    <T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, U extends CosmeticType.Builder<T, B, U>>
    void register(CosmeticCategory<T, B, U> registry);

    @Nullable
    @SuppressWarnings("unchecked")
    default <T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, U extends CosmeticType.Builder<T, B, U>> CosmeticCategory<T, B, U> getCategory(String key) {
        return (CosmeticCategory<T, B, U>) getCategoryRaw(key);
    }

    default boolean hasRegistry(String key) {
        return getCategoryRaw(key) != null;
    }

    @Nullable
    CosmeticCategory<?, ?, ?> getCategoryRaw(String key);

    Collection<CosmeticCategory<?, ?, ?>> getCategories();

    default CosmeticCategory<ArrowEffectType, ArrowEffectBehavior, ArrowEffectType.Builder> arrowEffects() {
        return getCategory(BuiltIn.ARROW_EFFECTS);
    }

    default CosmeticCategory<BalloonType, BalloonBehavior, BalloonType.Builder> balloons() {
        return getCategory(BuiltIn.BALLOONS);
    }

    default CosmeticCategory<BannerType, BannerBehavior, BannerType.Builder> banners() {
        return getCategory(BuiltIn.BANNERS);
    }

    default CosmeticCategory<DeathEffectType, DeathEffectBehavior, DeathEffectType.Builder> deathEffects() {
        return getCategory(BuiltIn.DEATH_EFFECTS);
    }

    default CosmeticCategory<EmoteType, EmoteBehavior, EmoteType.Builder> emotes() {
        return getCategory(BuiltIn.EMOTES);
    }

    default CosmeticCategory<GadgetType, GadgetBehavior, GadgetType.Builder> gadgets() {
        return getCategory(BuiltIn.GADGETS);
    }

    default CosmeticCategory<MiniatureType, MiniatureBehavior, MiniatureType.Builder> miniatures() {
        return getCategory(BuiltIn.MINIATURES);
    }

    default CosmeticCategory<MorphType, MorphBehavior, MorphType.Builder> morphs() {
        return getCategory(BuiltIn.MORPHS);
    }

    default CosmeticCategory<MountType, MountBehavior, MountType.Builder> mounts() {
        return getCategory(BuiltIn.MOUNTS);
    }

    default CosmeticCategory<MusicType, MusicBehavior, MusicType.Builder> music() {
        return getCategory(BuiltIn.MUSIC);
    }

    default CosmeticCategory<ParticleEffectType, ParticleEffectBehavior, ParticleEffectType.Builder> particleEffects() {
        return getCategory(BuiltIn.PARTICLE_EFFECTS);
    }

    default CosmeticCategory<PetType, PetBehavior, PetType.Builder> pets() {
        return getCategory(BuiltIn.PETS);
    }

    default CosmeticCategory<StatusType, StatusBehavior, StatusType.Builder> statuses() {
        return getCategory(BuiltIn.STATUSES);
    }

    interface BuiltIn {

        String ARROW_EFFECTS = "arrow_effects";
        String BALLOONS = "balloons";
        String BANNERS = "banners";
        String DEATH_EFFECTS = "death_effects";
        String EMOTES = "emotes";
        String GADGETS = "gadgets";
        String MINIATURES = "miniatures";
        String MORPHS = "morphs";
        String MOUNTS = "mounts";
        String MUSIC = "music";
        String PARTICLE_EFFECTS = "particle_effects";
        String PETS = "pets";
        String STATUSES = "statuses";
    }
}
