package se.file14.procosmetics.util.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.MetadataUtil;

public class FakeBlockManager {

    protected static final double RANGE = 64.0d * 64.0d;

    protected final ProCosmeticsPlugin plugin;

    public FakeBlockManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setFakeBlock(Block block, ItemStack itemStack, boolean checkFakeBlock, long duration) {
        if (checkFakeBlock && MetadataUtil.isCustomBlock(block)) {
            return false;
        }
        if (!canSetFakeBlock(block)) {
            return false;
        }
        setFakeBlock(block, itemStack);

        if (duration > 0) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> resetBlock(block), duration * 20L);
        }
        return true;
    }

    public boolean setFakeBlock(Block block, ItemStack itemStack, boolean checkFakeBlock) {
        return setFakeBlock(block, itemStack, checkFakeBlock, 0);
    }

    private void setFakeBlock(Block block, ItemStack itemStack) {
        Location location = block.getLocation();
        Location tmp = location.clone();
        MetadataUtil.setCustomBlock(block);
        BlockData blockData = itemStack.getType().createBlockData();

        for (Player player : block.getWorld().getPlayers()) {
            if (player.getLocation(tmp).distanceSquared(location) < RANGE) {
                player.sendBlockChange(location, blockData);
            }
        }
    }

    public void resetBlock(Block block) {
        Location location = block.getLocation();
        BlockData blockData = block.getBlockData();

        for (Player player : block.getWorld().getPlayers()) {
            player.sendBlockChange(location, blockData);
        }
        MetadataUtil.removeCustomBlock(block);
    }

    private boolean canSetFakeBlock(Block block) {
        Material material = block.getType();
        String materialName = material.toString();

        return material != Material.AIR
                && material.isSolid()
                && material != Material.BARRIER
                && material != Material.CACTUS
                && material != Material.SLIME_BLOCK
                && material != Material.TNT
                && material != Material.GLOWSTONE
                && material != Material.HOPPER
                && material != Material.BEACON
                && material != Material.CAULDRON
                && material != Material.BREWING_STAND
                && material != Material.FURNACE
                && material != Material.ANVIL
                && material != Material.NOTE_BLOCK
                && material != Material.DISPENSER
                && material != Material.DROPPER
                && material != Material.DAYLIGHT_DETECTOR
                && material != Material.JUKEBOX
                && material != Material.ENCHANTING_TABLE
                && material != Material.CRAFTING_TABLE
                && !Tag.SLABS.isTagged(material)
                && !Tag.DOORS.isTagged(material)
                && !Tag.BANNERS.isTagged(material)
                && !Tag.BUTTONS.isTagged(material)
                && !Tag.STAIRS.isTagged(material)
                && !materialName.contains("PATH")
                && !materialName.contains("WALL")
                && !materialName.contains("CARPET")
                && !materialName.contains("CHEST")
                && !materialName.contains("WOOL")
                && !materialName.contains("SIGN")
                && !materialName.contains("STAINED_GLASS")
                && !materialName.contains("FENCE")
                && !materialName.contains("REDSTONE")
                && !materialName.contains("PLATE")
                && !materialName.contains("THIN_GLASS")
                && !materialName.contains("CAKE")
                && !materialName.contains("ICE")
                && !materialName.contains("BANNER")
                && !materialName.contains("STEP")
                && !materialName.contains("GLASS_PANE")
                && !materialName.contains("OBSERVER")
                && !materialName.contains("SHULKER")
                && !materialName.contains("BED")
                && !materialName.contains("LANTERN")
                && !materialName.contains("FARMLAND")
                && !materialName.contains("PISTON");
    }
}
