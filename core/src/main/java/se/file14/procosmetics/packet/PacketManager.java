package se.file14.procosmetics.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.packet.listener.EntityInUse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class PacketManager {

    private final String channelName;

    private final ProCosmeticsPlugin plugin;
    private final List<PacketHandler> packetHandlers = new ArrayList<>();

    public PacketManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
        channelName = plugin.getName() + "_channel";
        addPacketHandler(new EntityInUse(plugin));
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
    }

    private void addPacketHandler(PacketHandler... packetHandlers) {
        Collections.addAll(this.packetHandlers, packetHandlers);
    }

    public void injectPlayer(Player player) {
        try {
            ChannelPipeline channelPipeline = plugin.getNMSManager().getNMSUtil().getChannel(player).pipeline();

            if (channelPipeline.get(channelName) == null) {
                channelPipeline.addBefore("packet_handler", channelName, new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
                        for (PacketHandler packetHandler : packetHandlers) {
                            if (packetHandler.getClazz().isInstance(packet)) {
                                packetHandler.onPacket(player, packet);
                            }
                        }
                        super.channelRead(context, packet);
                    }
                });
            }
        } catch (NoSuchElementException ignored) {
            // KEEP THIS
        }
    }

    public void uninjectPlayer(Player player) {
        if (!player.isOnline()) {
            return;
        }

        try {
            Channel channel = plugin.getNMSManager().getNMSUtil().getChannel(player);
            ChannelPipeline channelPipeline = channel.pipeline();

            if (channelPipeline.context(channelName) != null && channelPipeline.get(channelName) != null) {
                channel.eventLoop().submit(() -> {
                    channelPipeline.remove(channelName);
                });
            }
        } catch (NoSuchElementException ignored) {
            // KEEP THIS
        }
    }

    private class Listeners implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            injectPlayer(event.getPlayer());
        }

        @EventHandler
        public void onJoin(PlayerQuitEvent event) {
            uninjectPlayer(event.getPlayer());
        }
    }
}