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
package se.file14.procosmetics.event;

import org.bukkit.entity.Player;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.event.PlayerOpenTreasureChestEvent;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

public class PlayerOpenTreasureChestEventImpl extends ProCosmeticsEventImpl implements PlayerOpenTreasureChestEvent {

    private final User user;
    private final Player player;
    private final TreasureChest treasureChest;

    public PlayerOpenTreasureChestEventImpl(ProCosmetics plugin, User user, Player player, TreasureChest treasureChest) {
        super(plugin);
        this.user = user;
        this.player = player;
        this.treasureChest = treasureChest;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public TreasureChest getTreasureChest() {
        return treasureChest;
    }
}
