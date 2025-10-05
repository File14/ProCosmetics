package se.file14.procosmetics.packet.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.cosmetic.gadget.type.MerryGoRound;
import se.file14.procosmetics.packet.PacketHandler;
import se.file14.procosmetics.util.ReflectionUtil;
import se.file14.procosmetics.util.mapping.MappingRegistry;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class EntityInUse extends PacketHandler {

    private final Field getIDField;

    public EntityInUse(ProCosmeticsPlugin plugin) {
        super(plugin, "network.protocol.game", MappingRegistry.getMappedFieldName(MappingRegistry.SERVERBOUND_INTERACT_PACKET));
        getIDField = ReflectionUtil.getDeclaredField(clazz, MappingRegistry.getMappedFieldName(MappingRegistry.SERVERBOUND_INTERACT_PACKET_ID));
    }

    @Override
    public void onPacket(Player player, Object packet) {
        try {
            int id = (int) getIDField.get(packet);

            for (MerryGoRound.CoasterHorse coasterHorse : MerryGoRound.COASTER_HORSES) {
                if (coasterHorse.horse().getId() == id) {
                    Entity entity = coasterHorse.armorStand().getBukkitEntity();

                    if (entity.getPassengers().isEmpty()) {
                        plugin.getServer().getScheduler().runTask(plugin, () -> entity.addPassenger(player));
                    }
                    return;
                }
            }
        } catch (IllegalAccessException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to get the entity id in EntityInUse listener!", e);
        }
    }
}
