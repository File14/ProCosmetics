package se.file14.procosmetics.redis;

import com.google.gson.JsonObject;

public interface MessageSerializer<T> {

    T decode(JsonObject in);

    void encode(T message, JsonObject out);
}
