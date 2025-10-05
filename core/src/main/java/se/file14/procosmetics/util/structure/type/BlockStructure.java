package se.file14.procosmetics.util.structure.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;
import se.file14.procosmetics.util.structure.Structure;

import java.util.Map;

public class BlockStructure extends Structure<Block> {

    public BlockStructure(StructureData data) {
        super(data, block -> block.getType().isAir());
    }

    @Override
    public double spawn(Location location) {
        return spawn(location, false);
    }

    public double spawn(Location location, boolean blockBreakEffect) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            location.add(vector);
            Block block = location.getBlock();
            block.setBlockData(blockData, false);

            if (blockBreakEffect) {
                location.getWorld().playEffect(location, Effect.STEP_SOUND, block.getType());
            }
            location.subtract(vector);
            MetadataUtil.setCustomBlock(block);
            placedEntries.add(block);
        }
        return angle;
    }

    @Override
    public void remove() {
        for (Block block : placedEntries) {
            block.setType(Material.AIR, false);
            MetadataUtil.removeCustomBlock(block);
        }
        placedEntries.clear();
    }
}
