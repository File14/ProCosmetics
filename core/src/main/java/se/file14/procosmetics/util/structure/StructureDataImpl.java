package se.file14.procosmetics.util.structure;

import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.util.structure.StructureData;

import java.util.Map;

public class StructureDataImpl implements StructureData {

    private final Map<Vector, BlockData> placement;
    private final double width;
    private final double height;
    private final double length;
    private final double sizeSquared;
    private final double halfSizeSquared;

    public StructureDataImpl(Map<Vector, BlockData> placement) {
        this.placement = placement;

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        for (Vector vector : placement.keySet()) {
            double x = vector.getX();
            double y = vector.getY();
            double z = vector.getZ();

            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
            if (z < minZ) {
                minZ = z;
            }
            if (z > maxZ) {
                maxZ = z;
            }
        }
        this.width = maxX - minX;
        this.height = maxY - minY;
        this.length = maxZ - minZ;

        sizeSquared = Math.pow(width + length / 2.0d, 2.0d);
        halfSizeSquared = Math.pow((width + length / 2.0d) / 2.0d, 2.0d);
    }

    public Map<Vector, BlockData> getPlacement() {
        return placement;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getSizeSquared() {
        return sizeSquared;
    }

    public double getHalfSizeSquared() {
        return halfSizeSquared;
    }
}
