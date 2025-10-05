package se.file14.procosmetics.util.structure.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.Structure;

import java.util.Map;

public class FakeBlockStructure extends Structure<Block> {

    private static final double RANGE = 64.0d * 64.0d;

    public FakeBlockStructure(StructureData data) {
        super(data, block -> block.getType().isAir());
    }

    @Override
    public double spawn(Location location) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            location.add(vector);
            Block block = location.getBlock();
            location.subtract(vector);

            for (Player player : block.getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(location) < RANGE) {
                    player.sendBlockChange(location, blockData);
                }
            }
            MetadataUtil.setCustomBlock(block);
            placedEntries.add(block);
        }
        return angle;
    }

    @Override
    public void remove() {
        for (Block block : placedEntries) {
            Location location = block.getLocation();
            BlockData blockData = block.getBlockData();

            for (Player player : block.getWorld().getPlayers()) {
                player.sendBlockChange(location, blockData);
            }
            MetadataUtil.removeCustomBlock(block);
        }
        placedEntries.clear();
    }
}
