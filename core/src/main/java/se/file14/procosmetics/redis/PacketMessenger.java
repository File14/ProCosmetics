package se.file14.procosmetics.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import se.file14.procosmetics.ProCosmeticsPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class PacketMessenger {

    private static final Gson GSON = new Gson();

    private final ProCosmeticsPlugin plugin;
    private final Map<String, Channel<?>> channels = new HashMap<>();
    private final List<JedisPubSub> subscriptions = new CopyOnWriteArrayList<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public PacketMessenger(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    public <T extends Message> Channel<T> registerChannel(String channel, MessageSerializer<T> serializer) {
        return registerChannel(new Channel<>(plugin, channel, serializer));
    }

    @SuppressWarnings("unchecked")
    public <T extends Message, R extends Channel<T>> R registerChannel(R channel) {
        return (R) channels.computeIfAbsent(channel.getName(), (name) -> {
            threadPool.submit(() -> {
                try (Jedis jedis = plugin.getRedisManager().getRedis().getJedisPool().getResource()) {
                    JedisPubSub pubSub = new JedisPubSub() {

                        @Override
                        public void onMessage(String channelName, String encodedMessage) {
                            try {
                                channel.handle(GSON.fromJson(encodedMessage, JsonObject.class));
                            } catch (Exception e) {
                                plugin.getLogger().log(Level.SEVERE, "[REDIS] Error handling message for channel " + channelName + ".", e);
                            }
                        }

                        @Override
                        public void onSubscribe(String channelName, int subscribedChannels) {
                            plugin.getLogger().log(Level.INFO, "[REDIS] Subscribed to channel " + channelName + ".");
                        }

                        @Override
                        public void onUnsubscribe(String channelName, int subscribedChannels) {
                            plugin.getLogger().log(Level.INFO, "[REDIS] Unsubscribed from channel " + channelName + ".");
                        }
                    };
                    subscriptions.add(pubSub);
                    jedis.subscribe(pubSub, name);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "[REDIS] Failed to initialize channel " + name + ".", e);
                }
            });
            return channel;
        });
    }

    public void unregisterChannels() {
        for (JedisPubSub pubSub : subscriptions) {
            if (pubSub.isSubscribed()) {
                pubSub.unsubscribe();
            }
        }
        plugin.getLogger().log(Level.INFO, "[REDIS] Successfully unregistered " + subscriptions.size() + " channels.");
    }
}
