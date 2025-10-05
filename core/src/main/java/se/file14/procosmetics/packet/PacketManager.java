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

    private final String CUSTOM_CHANNEL_NAME;

    private final ProCosmeticsPlugin plugin;
    private final List<PacketHandler> packetHandlers = new ArrayList<>();

    public PacketManager(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;

        CUSTOM_CHANNEL_NAME = plugin.getName() + "_channel";

        addPacketHandler(new EntityInUse(plugin));
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
    }

    private void addPacketHandler(PacketHandler... packetHandlers) {
        Collections.addAll(this.packetHandlers, packetHandlers);
    }

    public void injectPlayer(Player player) {
        try {
            ChannelPipeline channelPipeline = plugin.getNMSManager().getNMSUtil().getChannel(player).pipeline();

            if (channelPipeline.get(CUSTOM_CHANNEL_NAME) == null) {
                channelPipeline.addBefore("packet_handler", CUSTOM_CHANNEL_NAME, new ChannelDuplexHandler() {
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

            if (channelPipeline.context(CUSTOM_CHANNEL_NAME) != null && channelPipeline.get(CUSTOM_CHANNEL_NAME) != null) {
                channel.eventLoop().submit(() -> {
                    channelPipeline.remove(CUSTOM_CHANNEL_NAME);
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