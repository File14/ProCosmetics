package se.file14.procosmetics.cosmetic.registry;

import org.bukkit.entity.EntityType;
import se.file14.procosmetics.api.ProCosmetics;
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
import se.file14.procosmetics.api.cosmetic.registry.CategoryRegistries;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticRegistry;
import se.file14.procosmetics.api.cosmetic.status.StatusBehavior;
import se.file14.procosmetics.api.cosmetic.status.StatusType;
import se.file14.procosmetics.cosmetic.arroweffect.ArrowEffectTypeImpl;
import se.file14.procosmetics.cosmetic.arroweffect.type.*;
import se.file14.procosmetics.cosmetic.balloon.BalloonTypeImpl;
import se.file14.procosmetics.cosmetic.balloon.type.DefaultBalloon;
import se.file14.procosmetics.cosmetic.banner.BannerTypeImpl;
import se.file14.procosmetics.cosmetic.banner.type.DefaultBanner;
import se.file14.procosmetics.cosmetic.deatheffect.DeathEffectTypeImpl;
import se.file14.procosmetics.cosmetic.deatheffect.type.*;
import se.file14.procosmetics.cosmetic.emote.EmoteTypeImpl;
import se.file14.procosmetics.cosmetic.emote.type.DefaultEmote;
import se.file14.procosmetics.cosmetic.emote.type.Rage;
import se.file14.procosmetics.cosmetic.emote.type.Sad;
import se.file14.procosmetics.cosmetic.emote.type.Wink;
import se.file14.procosmetics.cosmetic.gadget.GadgetTypeImpl;
import se.file14.procosmetics.cosmetic.gadget.type.*;
import se.file14.procosmetics.cosmetic.miniature.MiniatureTypeImpl;
import se.file14.procosmetics.cosmetic.miniature.type.*;
import se.file14.procosmetics.cosmetic.miniature.type.Chicken;
import se.file14.procosmetics.cosmetic.miniature.type.Cow;
import se.file14.procosmetics.cosmetic.miniature.type.Enderman;
import se.file14.procosmetics.cosmetic.miniature.type.IronGolem;
import se.file14.procosmetics.cosmetic.miniature.type.Sheep;
import se.file14.procosmetics.cosmetic.miniature.type.Skeleton;
import se.file14.procosmetics.cosmetic.miniature.type.Witch;
import se.file14.procosmetics.cosmetic.miniature.type.Zombie;
import se.file14.procosmetics.cosmetic.morph.MorphTypeImpl;
import se.file14.procosmetics.cosmetic.morph.type.*;
import se.file14.procosmetics.cosmetic.mount.MountTypeImpl;
import se.file14.procosmetics.cosmetic.mount.type.*;
import se.file14.procosmetics.cosmetic.mount.type.CaveSpider;
import se.file14.procosmetics.cosmetic.music.MusicTypeImpl;
import se.file14.procosmetics.cosmetic.music.type.DefaultMusic;
import se.file14.procosmetics.cosmetic.particleeffect.ParticleEffectTypeImpl;
import se.file14.procosmetics.cosmetic.particleeffect.type.*;
import se.file14.procosmetics.cosmetic.particleeffect.type.Bunny;
import se.file14.procosmetics.cosmetic.particleeffect.type.Confetti;
import se.file14.procosmetics.cosmetic.pet.PetTypeImpl;
import se.file14.procosmetics.cosmetic.pet.type.*;
import se.file14.procosmetics.cosmetic.pet.type.SantaClaus;
import se.file14.procosmetics.cosmetic.pet.type.Villager;
import se.file14.procosmetics.cosmetic.status.StatusTypeImpl;
import se.file14.procosmetics.cosmetic.status.type.DefaultStatus;
import se.file14.procosmetics.menu.CosmeticMenuImpl;
import se.file14.procosmetics.menu.menus.MorphMenu;
import se.file14.procosmetics.menu.menus.StatusMenu;
import se.file14.procosmetics.util.version.BukkitVersion;
import se.file14.procosmetics.util.version.VersionUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CategoryRegistriesImpl implements CategoryRegistries {

    private final Map<String, CosmeticCategory<?, ?, ?>> categories = new HashMap<>();

    public CategoryRegistriesImpl(ProCosmetics plugin) {
        register(new CosmeticCategoryImpl<>(BuiltIn.ARROW_EFFECTS, ArrowEffectTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.BALLOONS, BalloonTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.BANNERS, BannerTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.DEATH_EFFECTS, DeathEffectTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.EMOTES, EmoteTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.GADGETS, GadgetTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.MINIATURES, MiniatureTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.MORPHS, MorphTypeImpl.BuilderImpl::new, MorphMenu::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.MOUNTS, MountTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.MUSIC, MusicTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.PARTICLE_EFFECTS, ParticleEffectTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.PETS, PetTypeImpl.BuilderImpl::new, CosmeticMenuImpl::new));
        register(new CosmeticCategoryImpl<>(BuiltIn.STATUSES, StatusTypeImpl.BuilderImpl::new, StatusMenu::new));

        populateCosmetics();
    }

    private void populateCosmetics() {
        CosmeticRegistry<ArrowEffectType, ArrowEffectBehavior, ArrowEffectType.Builder> arrowEffects = arrowEffects().getCosmeticRegistry();
        arrowEffects.register(arrowEffects.builder("bloody_arrows").readFromConfig().factory(BloodyArrows::new).build());
        arrowEffects.register(arrowEffects.builder("candy_cane_arrows").readFromConfig().factory(CandyCaneArrows::new).build());
        arrowEffects.register(arrowEffects.builder("confetti_arrows").readFromConfig().factory(ConfettiArrows::new).build());
        arrowEffects.register(arrowEffects.builder("enchanted_arrows").readFromConfig().factory(EnchantedArrows::new).build());
        arrowEffects.register(arrowEffects.builder("flame_arrows").readFromConfig().factory(FlameArrows::new).build());
        arrowEffects.register(arrowEffects.builder("frost_lord_arrows").readFromConfig().factory(FrostLordArrows::new).build());
        arrowEffects.register(arrowEffects.builder("green_arrows").readFromConfig().factory(GreenArrows::new).build());
        arrowEffects.register(arrowEffects.builder("lava_arrows").readFromConfig().factory(LavaArrows::new).build());
        arrowEffects.register(arrowEffects.builder("love_arrows").readFromConfig().factory(LoveArrows::new).build());
        arrowEffects.register(arrowEffects.builder("magical_arrows").readFromConfig().factory(MagicalArrows::new).build());
        arrowEffects.register(arrowEffects.builder("music_arrows").readFromConfig().factory(MusicArrows::new).build());
        arrowEffects.register(arrowEffects.builder("rain_arrows").readFromConfig().factory(RainArrows::new).build());
        arrowEffects.register(arrowEffects.builder("shadow_arrows").readFromConfig().factory(ShadowArrows::new).build());
        arrowEffects.register(arrowEffects.builder("sparkly_arrows").readFromConfig().factory(SparklyArrows::new).build());

        CosmeticRegistry<BalloonType, BalloonBehavior, BalloonType.Builder> balloons = balloons().getCosmeticRegistry();
        for (String key : balloons().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!balloons.isRegistered(key)) {
                balloons.register(balloons.builder(key).readFromConfig().factory(DefaultBalloon::new).build());
            }
        }

        CosmeticRegistry<BannerType, BannerBehavior, BannerType.Builder> banners = banners().getCosmeticRegistry();
        for (String key : banners().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!banners.isRegistered(key)) {
                banners.register(banners.builder(key).readFromConfig().factory(DefaultBanner::new).build());
            }
        }

        CosmeticRegistry<DeathEffectType, DeathEffectBehavior, DeathEffectType.Builder> deathEffects = deathEffects().getCosmeticRegistry();
        deathEffects.register(deathEffects.builder("bloody_death").readFromConfig().factory(BloodyDeath::new).build());
        deathEffects.register(deathEffects.builder("candy_cane_remains").readFromConfig().factory(CandyCaneRemains::new).build());
        deathEffects.register(deathEffects.builder("confetti_death").readFromConfig().factory(ConfettiDeath::new).build());
        deathEffects.register(deathEffects.builder("enchanted_death").readFromConfig().factory(EnchantedDeath::new).build());
        deathEffects.register(deathEffects.builder("flame_death").readFromConfig().factory(FlameDeath::new).build());
        deathEffects.register(deathEffects.builder("fall_of_the_frost_lord").readFromConfig().factory(FallOfTheFrostLord::new).build());
        deathEffects.register(deathEffects.builder("lava_death").readFromConfig().factory(LavaDeath::new).build());
        deathEffects.register(deathEffects.builder("lightning_death").readFromConfig().factory(LightningDeath::new).build());
        deathEffects.register(deathEffects.builder("magical_death").readFromConfig().factory(MagicalDeath::new).build());
        deathEffects.register(deathEffects.builder("last_love").readFromConfig().factory(LastLove::new).build());
        deathEffects.register(deathEffects.builder("musical_death").readFromConfig().factory(MusicalDeath::new).build());
        deathEffects.register(deathEffects.builder("wet_death").readFromConfig().factory(WetDeath::new).build());
        deathEffects.register(deathEffects.builder("sparkly_death").readFromConfig().factory(SparklyDeath::new).build());
        deathEffects.register(deathEffects.builder("shadow_death").readFromConfig().factory(ShadowDeath::new).build());

        CosmeticRegistry<EmoteType, EmoteBehavior, EmoteType.Builder> emotes = emotes().getCosmeticRegistry();
        emotes.register(emotes.builder("in_love").readFromConfig().factory(se.file14.procosmetics.cosmetic.emote.type.InLove::new).build());
        emotes.register(emotes.builder("rage").readFromConfig().factory(Rage::new).build());
        emotes.register(emotes.builder("sad").readFromConfig().factory(Sad::new).build());
        emotes.register(emotes.builder("wink").readFromConfig().factory(Wink::new).build());
        for (String key : emotes().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!emotes.isRegistered(key)) {
                emotes.register(emotes.builder(key).readFromConfig().factory(DefaultEmote::new).build());
            }
        }

        CosmeticRegistry<GadgetType, GadgetBehavior, GadgetType.Builder> gadgets = gadgets().getCosmeticRegistry();
        gadgets.register(gadgets.builder("bat_blaster").readFromConfig().factory(BatBlaster::new).build());
        gadgets.register(gadgets.builder("detonator").readFromConfig().factory(Detonator::new).build());
        gadgets.register(gadgets.builder("cannon").readFromConfig().factory(Cannon::new).build());
        gadgets.register(gadgets.builder("chicken_parachute").readFromConfig().factory(ChickenParachute::new).build());
        gadgets.register(gadgets.builder("coin_party_bomb").readFromConfig().factory(CoinPartyBomb::new).build());
        gadgets.register(gadgets.builder("confetti").readFromConfig().factory(se.file14.procosmetics.cosmetic.gadget.type.Confetti::new).build());
        gadgets.register(gadgets.builder("cowboy").readFromConfig().factory(Cowboy::new).build());
        gadgets.register(gadgets.builder("disco_ball").readFromConfig().factory(DiscoBall::new).build());
        gadgets.register(gadgets.builder("diving_board").readFromConfig().factory(DivingBoard::new).build());
        gadgets.register(gadgets.builder("ethereal_pearl").readFromConfig().factory(EtherealPearl::new).build());
        gadgets.register(gadgets.builder("explosive_sheep").readFromConfig().factory(ExplosiveSheep::new).build());
        gadgets.register(gadgets.builder("fireworks").readFromConfig().factory(Fireworks::new).build());
        gadgets.register(gadgets.builder("flame_thrower").readFromConfig().factory(FlameThrower::new).build());
        gadgets.register(gadgets.builder("flesh_hook").readFromConfig().factory(FleshHook::new).build());
        gadgets.register(gadgets.builder("grappling_hook").readFromConfig().factory(GrapplingHook::new).build());
        gadgets.register(gadgets.builder("hot_tub").readFromConfig().factory(HotTub::new).build());
        gadgets.register(gadgets.builder("hot_air_balloon").readFromConfig().factory(HotAirBalloon::new).build());
        gadgets.register(gadgets.builder("melon_launcher").readFromConfig().readFromConfig().readFromConfig().factory(MelonLauncher::new).build());
        gadgets.register(gadgets.builder("merry_go_round").readFromConfig().factory(MerryGoRound::new).build());
        gadgets.register(gadgets.builder("paintball").readFromConfig().factory(Paintball::new).build());
        gadgets.register(gadgets.builder("parkour_spiral").readFromConfig().factory(ParkourSpiral::new).build());
        gadgets.register(gadgets.builder("party_popper").readFromConfig().readFromConfig().factory(PartyPopper::new).build());
        gadgets.register(gadgets.builder("rocket").readFromConfig().factory(Rocket::new).build());
        gadgets.register(gadgets.builder("shower").readFromConfig().factory(Shower::new).build());
        gadgets.register(gadgets.builder("slide").readFromConfig().factory(Slide::new).build());
        gadgets.register(gadgets.builder("soccer_ball").readFromConfig().factory(SoccerBall::new).build());
        gadgets.register(gadgets.builder("swing").readFromConfig().factory(Swing::new).build());
        gadgets.register(gadgets.builder("tnt").readFromConfig().factory(TNT::new).build());
        gadgets.register(gadgets.builder("trampoline").readFromConfig().factory(Trampoline::new).build());
        gadgets.register(gadgets.builder("water_gun").readFromConfig().factory(Watergun::new).build());
        gadgets.register(gadgets.builder("wither_missile").readFromConfig().factory(WitherMissile::new).build());

        CosmeticRegistry<MiniatureType, MiniatureBehavior, MiniatureType.Builder> miniatures = miniatures().getCosmeticRegistry();
        miniatures.register(miniatures.builder("astronaut").readFromConfig().factory(Astronaut::new).build());
        miniatures.register(miniatures.builder("brown_bear").readFromConfig().factory(BrownBear::new).build());
        miniatures.register(miniatures.builder("bunny").readFromConfig().factory(se.file14.procosmetics.cosmetic.miniature.type.Bunny::new).build());
        miniatures.register(miniatures.builder("chicken").readFromConfig().factory(Chicken::new).build());
        miniatures.register(miniatures.builder("cow").readFromConfig().factory(Cow::new).build());
        miniatures.register(miniatures.builder("devil").readFromConfig().factory(Devil::new).build());
        miniatures.register(miniatures.builder("doge").readFromConfig().factory(Doge::new).build());
        miniatures.register(miniatures.builder("dolphin").readFromConfig().factory(Dolphin::new).build());
        miniatures.register(miniatures.builder("dwarf").readFromConfig().factory(Dwarf::new).build());
        miniatures.register(miniatures.builder("elephant").readFromConfig().factory(Elephant::new).build());
        miniatures.register(miniatures.builder("enderman").readFromConfig().factory(Enderman::new).build());
        miniatures.register(miniatures.builder("fox").readFromConfig().factory(Fox::new).build());
        miniatures.register(miniatures.builder("ghost").readFromConfig().factory(Ghost::new).build());
        miniatures.register(miniatures.builder("iron_golem").readFromConfig().factory(IronGolem::new).build());
        miniatures.register(miniatures.builder("koala_bear").readFromConfig().factory(KoalaBear::new).build());
        miniatures.register(miniatures.builder("ladybug").readFromConfig().factory(Ladybug::new).build());
        miniatures.register(miniatures.builder("monkey").readFromConfig().factory(Monkey::new).build());
        miniatures.register(miniatures.builder("panda").readFromConfig().factory(Panda::new).build());
        miniatures.register(miniatures.builder("parrot").readFromConfig().factory(Parrot::new).build());
        miniatures.register(miniatures.builder("santa_claus").readFromConfig().factory(se.file14.procosmetics.cosmetic.miniature.type.SantaClaus::new).build());
        miniatures.register(miniatures.builder("shark").readFromConfig().readFromConfig().factory(Shark::new).build());
        miniatures.register(miniatures.builder("sheep").readFromConfig().factory(Sheep::new).build());
        miniatures.register(miniatures.builder("skeleton").readFromConfig().factory(Skeleton::new).build());
        miniatures.register(miniatures.builder("snail").readFromConfig().factory(Snail::new).build());
        miniatures.register(miniatures.builder("snowman").readFromConfig().factory(Snowman::new).build());
        miniatures.register(miniatures.builder("spider").readFromConfig().factory(se.file14.procosmetics.cosmetic.miniature.type.Spider::new).build());
        miniatures.register(miniatures.builder("tiger").readFromConfig().factory(Tiger::new).build());
        miniatures.register(miniatures.builder("tiki").readFromConfig().readFromConfig().factory(Tiki::new).build());
        miniatures.register(miniatures.builder("turtle").readFromConfig().factory(Turtle::new).build());
        miniatures.register(miniatures.builder("vampire").readFromConfig().factory(Vampire::new).build());
        miniatures.register(miniatures.builder("witch").readFromConfig().factory(Witch::new).build());
        miniatures.register(miniatures.builder("zebra").readFromConfig().factory(Zebra::new).build());
        miniatures.register(miniatures.builder("zombie").readFromConfig().factory(Zombie::new).build());

        CosmeticRegistry<MorphType, MorphBehavior, MorphType.Builder> morphs = morphs().getCosmeticRegistry();
        morphs.register(morphs.builder("bat").entityType(EntityType.BAT).readFromConfig().factory(Bat::new).build());
        morphs.register(morphs.builder("blaze").entityType(EntityType.BLAZE).readFromConfig().factory(Blaze::new).build());
        morphs.register(morphs.builder("block").entityType(EntityType.BLOCK_DISPLAY).readFromConfig().factory(Block::new).build());
        morphs.register(morphs.builder("bunny").entityType(EntityType.RABBIT).readFromConfig().readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Bunny::new).build());
        morphs.register(morphs.builder("cat").entityType(EntityType.CAT).readFromConfig().factory(Cat::new).build());
        morphs.register(morphs.builder("cave_spider").entityType(EntityType.CAVE_SPIDER).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.CaveSpider::new).build());
        morphs.register(morphs.builder("chicken").entityType(EntityType.CHICKEN).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Chicken::new).build());
        morphs.register(morphs.builder("cow").entityType(EntityType.COW).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Cow::new).build());
        morphs.register(morphs.builder("creeper").entityType(EntityType.CREEPER).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Creeper::new).build());
        morphs.register(morphs.builder("dog").entityType(EntityType.WOLF).readFromConfig().factory(Dog::new).build());
        morphs.register(morphs.builder("donkey").entityType(EntityType.DONKEY).readFromConfig().factory(Donkey::new).build());
        morphs.register(morphs.builder("elder_guardian").entityType(EntityType.ELDER_GUARDIAN).readFromConfig().factory(ElderGuardian::new).build());
        morphs.register(morphs.builder("enderman").entityType(EntityType.ENDERMAN).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Enderman::new).build());
        morphs.register(morphs.builder("iron_golem").entityType(EntityType.IRON_GOLEM).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.IronGolem::new).build());
        morphs.register(morphs.builder("magma_cube").entityType(EntityType.MAGMA_CUBE).readFromConfig().factory(MagmaCube::new).build());
        morphs.register(morphs.builder("mushroom_cow").entityType(EntityType.MOOSHROOM).readFromConfig().factory(MushroomCow::new).build());
        morphs.register(morphs.builder("olaf").entityType(EntityType.SNOW_GOLEM).readFromConfig().factory(Olaf::new).build());
        morphs.register(morphs.builder("pig").entityType(EntityType.PIG).readFromConfig().factory(Pig::new).build());
        morphs.register(morphs.builder("piglin").entityType(EntityType.PIGLIN).readFromConfig().factory(Piglin::new).build());
        morphs.register(morphs.builder("sheep").entityType(EntityType.SHEEP).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Sheep::new).build());
        morphs.register(morphs.builder("skeleton").entityType(EntityType.SKELETON).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Skeleton::new).build());
        morphs.register(morphs.builder("slime").entityType(EntityType.SLIME).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Slime::new).build());
        morphs.register(morphs.builder("spider").entityType(EntityType.SPIDER).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Spider::new).build());
        morphs.register(morphs.builder("squid").entityType(EntityType.SQUID).readFromConfig().factory(Squid::new).build());
        morphs.register(morphs.builder("villager").entityType(EntityType.VILLAGER).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Villager::new).build());
        morphs.register(morphs.builder("witch").entityType(EntityType.WITCH).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Witch::new).build());
        morphs.register(morphs.builder("wither").entityType(EntityType.WITHER).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Wither::new).build());
        morphs.register(morphs.builder("zombie").entityType(EntityType.ZOMBIE).readFromConfig().factory(se.file14.procosmetics.cosmetic.morph.type.Zombie::new).build());

        CosmeticRegistry<MountType, MountBehavior, MountType.Builder> mounts = mounts().getCosmeticRegistry();
        mounts.register(mounts.builder("baby_reindeer").entityType(EntityType.HORSE).readFromConfig().factory(BabyReindeer::new).build());
        mounts.register(mounts.builder("cave_spider").entityType(EntityType.CAVE_SPIDER).readFromConfig().factory(CaveSpider::new).build());
        mounts.register(mounts.builder("clumsy_mule").entityType(EntityType.MULE).readFromConfig().readFromConfig().factory(ClumsyMule::new).build());
        mounts.register(mounts.builder("crazy_chicken").entityType(EntityType.CHICKEN).readFromConfig().factory(CrazyChicken::new).build());
        mounts.register(mounts.builder("decrepit_warhorse").entityType(EntityType.ZOMBIE_HORSE).readFromConfig().factory(DecrepitWarhorse::new).build());
        mounts.register(mounts.builder("ethereal_dragon").entityType(EntityType.ENDER_DRAGON).readFromConfig().factory(EtherealDragon::new).build());
        mounts.register(mounts.builder("frosty_snowman").entityType(EntityType.SNOW_GOLEM).readFromConfig().factory(FrostySnowman::new).build());
        mounts.register(mounts.builder("glacial_steed").entityType(EntityType.HORSE).readFromConfig().factory(GlacialSteed::new).build());
        mounts.register(mounts.builder("hype_train").entityType(EntityType.ARMOR_STAND).readFromConfig().factory(HypeTrain::new).build());
        mounts.register(mounts.builder("infernal_horror").entityType(EntityType.SKELETON_HORSE).readFromConfig().factory(InfernalHorror::new).build());
        mounts.register(mounts.builder("lovely_sheep").entityType(EntityType.SHEEP).readFromConfig().factory(LovelySheep::new).build());
        mounts.register(mounts.builder("molten_snake").entityType(EntityType.MAGMA_CUBE).readFromConfig().factory(MoltenSnake::new).build());
        mounts.register(mounts.builder("pirate_ship").entityType(VersionUtil.isHigherThanOrEqualTo(BukkitVersion.v1_21) ? EntityType.OAK_BOAT : EntityType.valueOf("BOAT")).readFromConfig().factory(PirateShip::new).build());
        mounts.register(mounts.builder("rudolf").entityType(EntityType.HORSE).readFromConfig().factory(Rudolf::new).build());
        mounts.register(mounts.builder("slime").entityType(EntityType.SLIME).readFromConfig().factory(se.file14.procosmetics.cosmetic.mount.type.Slime::new).build());
        mounts.register(mounts.builder("unicorn").entityType(EntityType.HORSE).readFromConfig().factory(Unicorn::new).build());

        CosmeticRegistry<MusicType, MusicBehavior, MusicType.Builder> music = music().getCosmeticRegistry();
        for (String key : music().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!music.isRegistered(key)) {
                music.register(music.builder(key).readFromConfig().factory(DefaultMusic::new).build());
            }
        }

        CosmeticRegistry<ParticleEffectType, ParticleEffectBehavior, ParticleEffectType.Builder> particleEffects = particleEffects().getCosmeticRegistry();
        particleEffects.register(particleEffects.builder("angel_wings").readFromConfig().factory(AngelWings::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("aura_of_flame").readFromConfig().factory(AuraOfFlame::new).build());
        particleEffects.register(particleEffects.builder("big_heart").readFromConfig().factory(BigHeart::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("black_hole").readFromConfig().factory(BlackHole::new).build());
        particleEffects.register(particleEffects.builder("blood_helix").readFromConfig().factory(BloodHelix::new).build());
        particleEffects.register(particleEffects.builder("bunny").readFromConfig().factory(Bunny::new).build());
        particleEffects.register(particleEffects.builder("christmas_tree").readFromConfig().factory(ChristmasTree::new).build());
        particleEffects.register(particleEffects.builder("colorful_trail").readFromConfig().factory(ColorfulTrail::new).build());
        particleEffects.register(particleEffects.builder("confetti").readFromConfig().factory(Confetti::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("crushed_candy_cane").readFromConfig().factory(CrushedCandyCane::new).build());
        particleEffects.register(particleEffects.builder("demon_twist").readFromConfig().factory(DemonTwist::new).build());
        particleEffects.register(particleEffects.builder("emerald_twirl").readFromConfig().factory(EmeraldTwirl::new).build());
        particleEffects.register(particleEffects.builder("enchanted").readFromConfig().factory(Enchanted::new).build());
        particleEffects.register(particleEffects.builder("flame_flairy").readFromConfig().factory(FlameFlairy::new).build());
        particleEffects.register(particleEffects.builder("flame_of_magic").readFromConfig().factory(FlameOfMagic::new).build());
        particleEffects.register(particleEffects.builder("flame_of_the_demons").readFromConfig().factory(FlameOfTheDemons::new).build());
        particleEffects.register(particleEffects.builder("flame_rings").readFromConfig().factory(FlameRings::new).build());
        particleEffects.register(particleEffects.builder("frost_lord").readFromConfig().factory(FrostLord::new).build());
        particleEffects.register(particleEffects.builder("in_love").readFromConfig().factory(se.file14.procosmetics.cosmetic.particleeffect.type.InLove::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("jack_o_lantern").readFromConfig().factory(JackOLantern::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("legendary_aura").readFromConfig().factory(LegendaryAura::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("music").readFromConfig().factory(Music::new).repeatDelay(1).build());
        particleEffects.register(particleEffects.builder("party_time").readFromConfig().factory(PartyTime::new).build());
        particleEffects.register(particleEffects.builder("protective_shield").readFromConfig().factory(ProtectiveShield::new).build());
        particleEffects.register(particleEffects.builder("rainbow_wings").readFromConfig().factory(RainbowWings::new).build());
        particleEffects.register(particleEffects.builder("rain_cloud").readFromConfig().factory(RainCloud::new).build());
        particleEffects.register(particleEffects.builder("shadow_walk").readFromConfig().factory(ShadowWalk::new).repeatDelay(5).build());
        particleEffects.register(particleEffects.builder("skull").readFromConfig().factory(Skull::new).repeatDelay(1).build());
        particleEffects.register(particleEffects.builder("snow_cloud").readFromConfig().factory(SnowCloud::new).build());
        particleEffects.register(particleEffects.builder("snowflake").readFromConfig().factory(Snowflake::new).build());
        particleEffects.register(particleEffects.builder("star").readFromConfig().factory(Star::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("tornado").readFromConfig().factory(Tornado::new).build());
        particleEffects.register(particleEffects.builder("umbrella").readFromConfig().factory(Umbrella::new).build());
        particleEffects.register(particleEffects.builder("vampire_wings").readFromConfig().factory(VampireWings::new).repeatDelay(2).build());
        particleEffects.register(particleEffects.builder("yin_and_yang").readFromConfig().factory(YinAndYang::new).build());

        CosmeticRegistry<PetType, PetBehavior, PetType.Builder> pets = pets().getCosmeticRegistry();
        pets.register(pets.builder("body_guard").readFromConfig().factory(BodyGuard::new).build());
        pets.register(pets.builder("colorful_lamb").readFromConfig().factory(ColorfulLamb::new).build());
        pets.register(pets.builder("elf").readFromConfig().factory(Elf::new).build());
        pets.register(pets.builder("santa_claus").readFromConfig().factory(SantaClaus::new).build());
        pets.register(pets.builder("villager").readFromConfig().factory(Villager::new).build());
        for (String key : pets().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!pets.isRegistered(key)) {
                pets.register(pets.builder(key).readFromConfig().factory(DefaultPet::new).build());
            }
        }

        CosmeticRegistry<StatusType, StatusBehavior, StatusType.Builder> statuses = statuses().getCosmeticRegistry();
        for (String key : statuses().getConfig().getConfigurationSection("cosmetics").getKeys(false)) {
            if (!statuses.isRegistered(key)) {
                statuses.register(statuses.builder(key).readFromConfig().factory(DefaultStatus::new).build());
            }
        }
    }

    @Override
    public <T extends CosmeticType<T, B>, B extends CosmeticBehavior<T>, U extends CosmeticType.Builder<T, B, U>>
    void register(CosmeticCategory<T, B, U> category) {
        categories.put(category.getKey().toLowerCase(), category);
    }

    @Override
    public CosmeticCategory<?, ?, ?> getCategoryRaw(String key) {
        return categories.get(key.toLowerCase());
    }

    @Override
    public Collection<CosmeticCategory<?, ?, ?>> getCategories() {
        return categories.values();
    }
}
