package se.file14.procosmetics.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.nms.EntityTrackerImpl;

public class HologramLine {

    private final Hologram hologram;
    private final Spacing spacing;
    private NMSEntity entity;

    HologramLine(Hologram hologram, Component text, Spacing spacing, EntityTrackerImpl tracker) {
        this.hologram = hologram;
        this.spacing = spacing;

        if (!text.equals(Component.empty())) {
            entity = ProCosmeticsPlugin.getPlugin().getNMSManager().createEntity(hologram.getWorld(), EntityType.ARMOR_STAND, tracker);
            entity.setCustomName(text);

            if (entity.getBukkitEntity() instanceof ArmorStand armorStand) {
                armorStand.setInvisible(true);
                armorStand.setSmall(true);
                armorStand.setArms(false);
            }
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
