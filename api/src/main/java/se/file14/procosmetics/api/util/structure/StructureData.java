package se.file14.procosmetics.api.util.structure;

import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Map;

/**
 * Represents an API interface for accessing immutable structure data.
 */
public interface StructureData {

    /**
     * Retrieves an immutable map of vectors to block data representing the
     * structure's placement.
     *
     * @return an unmodifiable map of {@link Vector} to {@link BlockData}
     */
    Map<Vector, BlockData> getPlacement();

    /**
     * Gets the width of the structure.
     *
     * @return the width of the structure
     */
    double getWidth();

    /**
     * Gets the height of the structure.
     *
     * @return the height of the structure
     */
    double getHeight();

    /**
     * Gets the length of the structure.
     *
     * @return the length of the structure
     */
    double getLength();

    /**
     * Calculates and returns a squared size metric for the structure.
     *
     * @return the squared size of the structure
     */
    double getSizeSquared();

    /**
     * Calculates and returns half of a squared size metric for the structure.
     *
     * @return half of the squared size of the structure
     */
    double getHalfSizeSquared();
}
