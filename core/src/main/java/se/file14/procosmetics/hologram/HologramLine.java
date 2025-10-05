package se.file14.procosmetics.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.nms.EntityTrackerImpl;
import se.file14.procosmetics.nms.NMSEntityImpl;

public class HologramLine {

    private final Hologram hologram;
    private final Spacing spacing;
    private NMSEntityImpl entity;

    HologramLine(Hologram hologram, Component text, Spacing spacing, EntityTrackerImpl tracker) {
        this.hologram = hologram;
        this.spacing = spacing;

        if (!text.equals(Component.empty())) {
            entity = ProCosmeticsPlugin.getPlugin().getNMSManager().createEntity(hologram.getWorld(), EntityType.ARMOR_STAND, tracker);
            entity.setInvisible(true);
            entity.setArmorStandSmall(true);
            entity.setArmorStandArms(false);
            entity.setCustomName(text);
        }
    }

    public Spacing getSpacing() {
        return spacing;
    }

    public double getHeight() {
        return 0.25d;
    }

    void setY(double y) {
        if (entity != null) {
            entity.setPositionRotation(new Location(hologram.getWorld(), hologram.getX(), y, hologram.getZ()));
        }
    }
}
