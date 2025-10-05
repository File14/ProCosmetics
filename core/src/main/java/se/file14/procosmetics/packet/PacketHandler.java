package se.file14.procosmetics.packet;

import org.bukkit.entity.Player;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.ReflectionUtil;

public abstract class PacketHandler {

    protected final ProCosmeticsPlugin plugin;
    protected final Class<?> clazz;

    public PacketHandler(ProCosmeticsPlugin plugin, String path, String clazz) {
        this.plugin = plugin;
        this.clazz = ReflectionUtil.getNMSClass(path, clazz);
    }

    public abstract void onPacket(Player player, Object packet);

    public Class<?> getClazz() {
        return clazz;
    }
}