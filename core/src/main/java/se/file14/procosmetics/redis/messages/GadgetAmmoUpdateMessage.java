/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.redis.messages;

import com.google.gson.JsonObject;
import se.file14.procosmetics.redis.Message;
import se.file14.procosmetics.redis.MessageSerializer;

public class GadgetAmmoUpdateMessage extends Message {

    public static final MessageSerializer<GadgetAmmoUpdateMessage> SERIALIZER = new MessageSerializer<>() {
        @Override
        public GadgetAmmoUpdateMessage decode(JsonObject in) {
            return new GadgetAmmoUpdateMessage(
                    in.get("user_id").getAsInt(),
                    in.get("gadget_key").getAsString(),
                    in.get("amount").getAsInt()
            );
        }

        @Override
        public void encode(GadgetAmmoUpdateMessage message, JsonObject out) {
            out.addProperty("user_id", message.userId);
            out.addProperty("gadget_key", message.gadgetKey);
            out.addProperty("amount", message.amount);
        }
    };

    private final int userId;
    private final String gadgetKey;
    private final int amount;

    public GadgetAmmoUpdateMessage(int userId, String gadgetKey, int amount) {
        this.userId = userId;
        this.gadgetKey = gadgetKey;
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public String getGadgetKey() {
        return gadgetKey;
    }

    public int getAmount() {
        return amount;
    }
}
