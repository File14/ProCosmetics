package se.file14.procosmetics.redis.messages;

import com.google.gson.JsonObject;
import se.file14.procosmetics.redis.Message;
import se.file14.procosmetics.redis.MessageSerializer;

public class CoinUpdateMessage extends Message {

    public static final MessageSerializer<CoinUpdateMessage> SERIALIZER = new MessageSerializer<>() {
        @Override
        public CoinUpdateMessage decode(JsonObject in) {
            return new CoinUpdateMessage(
                    in.get("user_id").getAsInt(),
                    in.get("amount").getAsInt()
            );
        }

        @Override
        public void encode(CoinUpdateMessage message, JsonObject out) {
            out.addProperty("user_id", message.userId);
            out.addProperty("amount", message.amount);
        }
    };

    private final int userId;
    private final int amount;

    public CoinUpdateMessage(int userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }
}