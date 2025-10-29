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
package se.file14.procosmetics.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.util.AbstractRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractNMSEquipment<T> extends AbstractRunnable {

    protected static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();
    private static final double RANGE = 24.0d * 24.0d;

    protected UUID uuid;
    protected int id;
    protected Set<Player> viewers = new HashSet<>();
    protected T helmetPacket;
    protected T slotSetPacket;

    private final Location ownerLocation = new Location(null, 0.0d, 0.0d, 0.0d);
    private final Location otherLocation = new Location(null, 0.0d, 0.0d, 0.0d);

    public AbstractNMSEquipment(Player player, boolean tracker) {
        uuid = player.getUniqueId();
        id = player.getEntityId();
        if (tracker) {
            runTaskTimer(PLUGIN, 2L, 60L);
        }
    }

    @Override
    public void run() {
        viewers.removeIf(player -> !player.isOnline());
        Player holder = PLUGIN.getServer().getPlayer(uuid);

        if (holder != null) {
            World world = holder.getWorld();
            ItemStack helmet = holder.getInventory().getHelmet();

            for (Player player : world.getPlayers()) {
                if (player.isValid() && player.getLocation(otherLocation).distanceSquared(holder.getLocation(ownerLocation)) <= RANGE) {
                    if (!viewers.contains(player)) {
                        viewers.add(player);
                        sendUpdateToPlayer(player);
                    }
                } else {
                    sendRemoveUpdate(player, helmet);
                    viewers.remove(player);
                }
            }
        }
    }

    public void sendUpdateToViewers() {
        for (Player player : viewers) {
            sendUpdateToPlayer(player);
        }
    }

    public abstract void sendUpdateToPlayer(Player player);

    public abstract void sendRemoveUpdate(Player player, ItemStack itemStack);

    public abstract void setItemStack(ItemStack itemStack);
}
