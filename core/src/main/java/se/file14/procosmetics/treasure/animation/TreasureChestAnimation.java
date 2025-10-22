package se.file14.procosmetics.treasure.animation;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.event.PlayerOpenTreasureChestEvent;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.treasure.loot.LootEntry;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.treasure.TreasureChestImpl;
import se.file14.procosmetics.treasure.TreasureChestManagerImpl;
import se.file14.procosmetics.treasure.TreasureChestPlatformImpl;
import se.file14.procosmetics.treasure.loot.LootTable;
import se.file14.procosmetics.util.LocationUtil;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.type.BlockStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class TreasureChestAnimation extends BukkitRunnable implements Listener {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final Title.Times DEFAULT_TIMES = Title.Times.times(Ticks.duration(5), Ticks.duration(50), Ticks.duration(5));
    public static final int MAX_TIME_BEFORE_FORCE_OPEN = 1000;

    protected ProCosmetics plugin;
    protected TreasureChestPlatformImpl platform;
    protected TreasureChestImpl treasureChest;
    protected User user;
    protected Player player;

    protected List<NMSEntity> armorStandChests = new ArrayList<>();
    protected List<NMSEntity> texts = new ArrayList<>();
    protected List<NMSEntity> items = new ArrayList<>();
    protected List<Location> openedChests = new ArrayList<>();

    protected AnimationState animationState = AnimationState.BUILDING;
    private int buildingStates;
    protected int ticks;

    protected Location location;

    public TreasureChestAnimation(ProCosmetics plugin, TreasureChestPlatform platform, TreasureChest treasureChest, User user) {
        this.plugin = plugin;
        this.platform = (TreasureChestPlatformImpl) platform; // TODO: Remove chest when more is exposed to the API
        this.treasureChest = (TreasureChestImpl) treasureChest;
        this.user = user;
        this.player = user.getPlayer();
        this.location = LocationUtil.center(platform.getCenter().clone());

        this.platform.destroyChest();
        this.platform.getHologram().despawn();

        if (treasureChest.isOpeningBroadcast()) {
            ((TreasureChestManagerImpl) plugin.getTreasureChestManager()).getOpeningTreasureBroadcaster().broadcastMessage(
                    player,
                    "treasure_chest.open.broadcast",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("treasure_chest", treasureChest.getName(user))
            );
        }
        user.showTitle(user.translate(
                        "treasure_chest.open.title",
                        Placeholder.unparsed("treasure_chest", treasureChest.getName(user))
                ),
                user.translate(
                        "treasure_chest.open.subtitle",
                        Placeholder.unparsed("amount", String.valueOf(treasureChest.getChestsToOpen()))
                ),
                DEFAULT_TIMES
        );
        Server server = plugin.getJavaPlugin().getServer();
        server.getPluginManager().callEvent(new PlayerOpenTreasureChestEvent(user, player, treasureChest));
        server.getPluginManager().registerEvents(this, plugin.getJavaPlugin());
        server.getLogger().log(Level.INFO, user + " is opening a " + treasureChest.getKey() + " treasure chest.");
        runTaskTimer(plugin.getJavaPlugin(), 0L, 1L);
    }

    @Override
    public void run() {
        if (user == null || player == null || !player.isOnline()) {
            reset();
        } else {
            onUpdate();
        }
    }

    public enum AnimationState {
        BUILDING,
        SPAWNING_CHESTS,
        BUILT,
        DONE,
    }

    public void onUpdate() {
        if (ticks % 20 == 0) {
            if (animationState == AnimationState.BUILDING) {
                if (buildingStates < treasureChest.getStructures().size()) {
                    StructureData structureData = treasureChest.getStructures().get(buildingStates++);
                    BlockStructure blockStructure = new BlockStructure(structureData);
                    blockStructure.spawn(platform.getCenter(), true);
                } else {
                    animationState = AnimationState.SPAWNING_CHESTS;
                }
            }
            onSecondUpdate();
            checkPlayersTooClose();
            checkOpenerLocation();
        }
        onTickUpdate();

        if (ticks == MAX_TIME_BEFORE_FORCE_OPEN) {
            forceOpenChests();
        }
        ticks++;
    }

    public abstract void onSecondUpdate();

    public abstract void onTickUpdate();

    private void registerOpenedChest(Location location) {
        openedChests.add(location);

        if (openedChests.size() >= treasureChest.getChestsToOpen()) {
            plugin.getJavaPlugin().getServer().getScheduler().runTaskLater(plugin.getJavaPlugin(), this::reset, 100L);
        }
    }

    private void openChest(Block block) {
        Location blockLocation = block.getLocation().add(0.5d, 0.0d, 0.5d);

        if (openedChests.size() >= treasureChest.getChestsToOpen() || openedChests.contains(blockLocation)) {
            return;
        }
        registerOpenedChest(blockLocation);
        Location location = blockLocation.clone().add(0.0d, 0.7d, 0.0d);
        LootTable<?> lootTable = treasureChest.getRandomLootTable();

        if (lootTable != null) {
            giveRandomLoot(lootTable, location);
        }
        location.getWorld().playSound(location, Sound.BLOCK_CHEST_OPEN, 0.4f, 0.0f);
        plugin.getNMSManager().getNMSUtil().playChestAnimation(block, true);
    }

    private <T extends LootEntry> T giveRandomLoot(LootTable<T> lootTable, Location location) {
        T lootEntry = lootTable.getRandomLoot();
        lootTable.give(player, user, lootEntry);

        CosmeticRarity rarity = lootEntry.getRarity();
        spawnFirework(location, rarity);

        NMSEntity nmsEntity = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.ITEM);
        nmsEntity.setEntityItemStack(lootEntry.getItemStack());
        nmsEntity.setVelocity(0.0d, 0.2d, 0.0d);
        nmsEntity.setPositionRotation(location);
        nmsEntity.getTracker().startTracking();
        items.add(nmsEntity);

        NMSEntity text = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.TEXT_DISPLAY);

        if (text.getBukkitEntity() instanceof TextDisplay textDisplay) {
            textDisplay.setText(SERIALIZER.serialize(user.translate(
                    "treasure_chest.open.hologram",
                    Placeholder.unparsed("loot", lootEntry.getName(user)),
                    Placeholder.unparsed("category", lootTable.getCategory(user)),
                    Placeholder.parsed("rarity_primary_color", rarity.getPrimaryColor()),
                    Placeholder.parsed("rarity_secondary_color", rarity.getSecondaryColor())
            )));
            textDisplay.setBillboard(TextDisplay.Billboard.CENTER);
            textDisplay.setTeleportDuration(1);
        }
        text.setPositionRotation(location.clone().add(0.0d, 1.0d, 0.0d));
        text.getTracker().startTracking();
        texts.add(text);

        return lootEntry;
    }

    private void spawnFirework(Location location, CosmeticRarity rarity) {
        if (rarity.getDetonations() > 0) {
            new BukkitRunnable() {
                int i;

                @Override
                public void run() {
                    location.getWorld().spawn(location, Firework.class, entity -> {
                        FireworkMeta fireworkMeta = entity.getFireworkMeta();
                        fireworkMeta.addEffect(rarity.getFireworkEffect());
                        entity.setFireworkMeta(fireworkMeta);

                        MetadataUtil.setCustomEntity(entity);
                    }).detonate();

                    if (++i >= rarity.getDetonations()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin.getJavaPlugin(), 0L, rarity.getTickInterval());
        }
    }

    public void forceOpenChests() {
        for (Location location : platform.getChestLocations()) {
            openChest(location.getBlock());
        }
    }

    protected void setChestBlock(Material material, Location location) {
        Block block = location.getBlock();
        block.setType(material);
        MetadataUtil.setCustomBlock(block);

        if (block.getBlockData() instanceof Directional directional) {
            directional.setFacing(MathUtil.yawToFace(platform.getCenter(), location));
            block.setBlockData(directional);
        }
        location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
        location.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.4f, 1.0f);
    }

    protected void despawnChestArmorstands() {
        for (NMSEntity removeArmorStand : armorStandChests) {
            removeArmorStand.getTracker().destroy();
        }
        armorStandChests.clear();
    }

    protected NMSEntity spawnChestArmorstand(Material material, Location location) {
        NMSEntity nmsEntity = spawnNMSArmorstand(location);
        nmsEntity.setHelmet(new ItemStack(material));
        nmsEntity.getTracker().startTracking();
        armorStandChests.add(nmsEntity);
        return nmsEntity;
    }

    private NMSEntity spawnNMSArmorstand(Location location) {
        NMSEntity nmsEntity = plugin.getNMSManager().createEntity(location.getWorld(), EntityType.ARMOR_STAND);

        if (nmsEntity.getNMSEntity() instanceof ArmorStand armorStand) {
            armorStand.setInvisible(true);
            armorStand.setSmall(true);
        }
        nmsEntity.setPositionRotation(location);

        return nmsEntity;
    }

    public void reset() {
        // PREVENT MULTIPLE RESETS
        if (treasureChest == null) {
            return;
        }
        if (player != null && player.getLocation().getBlock().equals(platform.getCenter().getBlock())) {
            player.setVelocity(new Vector(0.0d, 0.5d, 0.0d));
        }
        user.setCurrentPlatform(null);
        platform.setUser(null);
        platform.build();

        for (Location location : platform.getChestLocations()) {
            Block block = location.getBlock();

            MetadataUtil.removeCustomBlock(block);
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
        }
        despawnChestArmorstands();

        items.forEach(entity -> entity.getTracker().destroy());
        items.clear();

        texts.forEach(entity -> entity.getTracker().destroy());
        texts.clear();

        plugin = null;
        platform = null;
        treasureChest = null;
        armorStandChests = null;
        items = null;
        openedChests = null;
        texts = null;

        HandlerList.unregisterAll(this);
        cancel();
    }

    private void checkOpenerLocation() {
        Location center = platform.getCenter();
        player.getLocation(location);

        if (openedChests.size() >= treasureChest.getChestsToOpen()
                || center.getWorld() != player.getWorld()
                || center.distanceSquared(location) < platform.getBlockStructure().getData().getHalfSizeSquared()) {
            return;
        }
        Location teleportLocation = platform.getCenter().clone();
        teleportLocation.setPitch(location.getPitch());
        teleportLocation.setYaw(location.getYaw());

        player.teleport(teleportLocation);
    }

    private void checkPlayersTooClose() {
        for (Player player : MathUtil.getClosestPlayersFromLocationSquared(location, platform.getBlockStructure().getData().getHalfSizeSquared())) {
            if (user.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                vehicle.eject();
            }
            MathUtil.pushEntity(player, location, 1.5d, 1.0d);
            User otherUser = plugin.getUserManager().getConnected(player);

            if (otherUser != null) {
                otherUser.setFallDamageProtection(4);
            }
        }
    }

    @EventHandler
    public void onClickChest(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (user.getUniqueId().equals(event.getPlayer().getUniqueId())
                && platform.getChestLocations().contains(LocationUtil.center(block.getLocation()))) {
            event.setCancelled(true);

            openChest(block);
        }
    }
}