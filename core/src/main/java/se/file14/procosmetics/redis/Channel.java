package se.file14.procosmetics.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import redis.clients.jedis.Jedis;
import se.file14.procosmetics.ProCosmeticsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Channel<T extends Message> {

    private static final Gson GSON = new Gson();

    private final ProCosmeticsPlugin plugin;
    private final String name;
    private final MessageSerializer<T> serializer;

    private final List<Consumer<T>> listeners = new ArrayList<>();

    public Channel(ProCosmeticsPlugin plugin, String name, MessageSerializer<T> serializer) {
        this.plugin = plugin;
        this.name = name;
        this.serializer = serializer;
    }

    public void listen(Consumer<T> handle) {
        listeners.add(handle);
    }

    public void handle(JsonObject in) {
        String source = in.get("source").getAsString();

        if (plugin.getRedisManager().getRedis().getServerId().equals(source)) {
            return;
        }
        T message = serializer.decode(in);

        if (message != null) {
            message.setSourceServer(source);

            for (Consumer<T> listener : listeners) {
                listener.accept(message);
            }
        }
    }

    public void send(T message, boolean handleLocally) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("source", plugin.getRedisManager().getRedis().getServerId());

        serializer.encode(message, jsonObject);

        try (Jedis jedis = plugin.getRedisManager().getRedis().getJedisPool().getResource()) {
            jedis.publish(name, GSON.toJson(jsonObject));
        }

        if (handleLocally) {
            for (Consumer<T> listener : listeners) {
                listener.accept(message);
            }
        }
    }

    public void send(T message) {
        send(message, false);
    }

    public CompletableFuture<Void> sendAsync(T message, boolean handleLocally) {
        return CompletableFuture.runAsync(() -> send(message, handleLocally));
    }

    public CompletableFuture<Void> sendAsync(T message) {
        return sendAsync(message, false);
    }

    public String getName() {
        return name;
    }
}
