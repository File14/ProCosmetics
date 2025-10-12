package se.file14.procosmetics.cosmetic.status;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.entity.TextDisplay;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.status.Status;
import se.file14.procosmetics.api.cosmetic.status.StatusBehavior;
import se.file14.procosmetics.api.cosmetic.status.StatusType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;

public class StatusImpl extends CosmeticImpl<StatusType, StatusBehavior> implements Status {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();

    private NMSEntity nmsEntity;
    private int ticks;

    private final double heightOffset;
    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    public StatusImpl(ProCosmeticsPlugin plugin, User user, StatusType type, StatusBehavior behavior) {
        super(plugin, user, type, behavior);
        heightOffset = type.getCategory().getConfig().getDouble("height_offset");
    }

    @Override
    protected void onEquip() {
        user.removeCosmetic(plugin.getCategoryRegistries().morphs(), false, true);

        nmsEntity = plugin.getNMSManager().createEntity(player.getWorld(), EntityType.TEXT_DISPLAY);

        if (nmsEntity.getBukkitEntity() instanceof TextDisplay textDisplay) {
            textDisplay.setBillboard(TextDisplay.Billboard.CENTER);
            textDisplay.setTeleportDuration(1);
        }
        refreshText(false);

        // TODO: In the future, consider setting display text as passenger of the player entity (only for the owner)
        // as this, would fix the de-sync issue when the player is moving fast.
        if (!user.hasSelfViewStatus()) {
            nmsEntity.getTracker().addAntiViewer(player);
        }
        nmsEntity.setPositionRotation(getUpdatedLocation());
        nmsEntity.getTracker().startTracking();

        runTaskTimerAsynchronously(plugin, 1L, 0L);
    }

    @Override
    protected void onUpdate() {
        nmsEntity.sendPositionRotationPacket(getUpdatedLocation());

        // Check if refreshing is needed
        if (cosmeticType.getRefreshInterval() > 0) {
            if (ticks > cosmeticType.getRefreshInterval()) {
                refreshText(true);
                ticks = 0;
            }
            ticks++;
        }
    }

    @Override
    protected void onUnequip() {
        if (nmsEntity != null) {
            nmsEntity.getTracker().destroy();
            nmsEntity = null;
        }
    }

    private Location getUpdatedLocation() {
        double sneakOffset = 0.0f;

        if (player.getPose() == Pose.SNEAKING) {
            sneakOffset = -player.getAttribute(Attribute.SCALE).getBaseValue() / 8.0d;
        }

        return player.getLocation(location).add(0.0d,
                player.getBoundingBox().getHeight() + heightOffset + sneakOffset,
                0.0d
        );
    }

    private void refreshText(boolean sendPacket) {
        String updatedTagText = SERIALIZER.serialize(cosmeticType.getTextProvider().apply(cosmeticType, user));
        // TODO: Find a better way for placeholders like this in the future
        updatedTagText = plugin.getPlaceholderManager().setPlaceholders(player, updatedTagText);

        if (nmsEntity.getBukkitEntity() instanceof TextDisplay textDisplay) {
            // Make sure it actually changed
            if (!textDisplay.getText().equals(updatedTagText)) {
                textDisplay.setText(updatedTagText);

                if (sendPacket) {
                    nmsEntity.sendEntityMetadataPacket();
                }
            }
        }
    }

    @Override
    public NMSEntity getNMSEntity() {
        return nmsEntity;
    }
}