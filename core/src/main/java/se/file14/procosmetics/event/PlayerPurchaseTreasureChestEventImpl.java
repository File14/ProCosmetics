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
import se.file14.procosmetics.api.event.PlayerPurchaseTreasureChestEvent;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.user.User;

public class PlayerPurchaseTreasureChestEventImpl extends ProCosmeticsEventImpl implements PlayerPurchaseTreasureChestEvent {

    private final User user;
    private final Player player;
    private final TreasureChest treasureChest;
    private final int amount;

    public PlayerPurchaseTreasureChestEventImpl(ProCosmetics plugin, User user, Player player, TreasureChest treasureChest, int amount) {
        super(plugin);
        this.user = user;
        this.player = player;
        this.treasureChest = treasureChest;
        this.amount = amount;
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

    @Override
    public int getAmount() {
        return amount;
    }
}
