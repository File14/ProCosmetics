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
package se.file14.procosmetics.v1_21_11;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.nms.AbstractNMSEquipment;

import java.util.Collections;

public class NMSEquipment extends AbstractNMSEquipment<Packet> {

    public NMSEquipment(Player player, boolean tracker) {
        super(player, tracker);
    }

    @Override
    public void sendUpdateToPlayer(Player player) {
        if (player != null) {
            ServerGamePacketListenerImpl playerConnection = ((CraftPlayer) player).getHandle().connection;

            if (player.getUniqueId() != uuid) {
                playerConnection.send(helmetPacket);
            } else {
                playerConnection.send(slotSetPacket);
            }
        }
    }

    @Override
    public void sendRemoveUpdate(Player player, ItemStack itemStack) {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEquipmentPacket(
                id, Collections.singletonList(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)))
        ));
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack craftItemStack = CraftItemStack.asNMSCopy(itemStack);

        helmetPacket = new ClientboundSetEquipmentPacket(id, Collections.singletonList(new Pair<>(EquipmentSlot.HEAD, craftItemStack)));
        slotSetPacket = new ClientboundContainerSetSlotPacket(0, 0, 5, craftItemStack);
    }
}
