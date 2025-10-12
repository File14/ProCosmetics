package se.file14.procosmetics.util.structure.type;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.EntityTracker;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.util.structure.StructureData;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.Structure;

import java.util.Map;

public class FallingBlocksStructure extends Structure<NMSEntity> {

    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private final EntityTracker tracker = new EntityTrackerImpl();

    public FallingBlocksStructure(StructureData data) {
        super(data, block -> block.isPassable() && !block.isLiquid());
    }

    @Override
    public double spawn(Location location) {
        double angle = calculateAngle(location);

        for (Map.Entry<Vector, BlockData> entry : data.getPlacement().entrySet()) {
            Vector vector = MathUtil.rotateAroundAxisY(entry.getKey().clone(), angle);
            BlockData blockData = entry.getValue().clone();

            rotate(blockData, location.getYaw());

            NMSEntity nmsFallingBlock = PLUGIN.getNMSManager().createFallingBlock(location.getWorld(), blockData, tracker);
            if (nmsFallingBlock instanceof FallingBlock fallingBlock) {
                fallingBlock.setGravity(false);
            }
            location.add(vector);
            nmsFallingBlock.setPositionRotation(location);
            location.subtract(vector);

            placedEntries.add(nmsFallingBlock);
        }
        tracker.startTracking();
        return angle;
    }

    @Override
    public void remove() {
        tracker.destroy();
        placedEntries.clear();
    }
}