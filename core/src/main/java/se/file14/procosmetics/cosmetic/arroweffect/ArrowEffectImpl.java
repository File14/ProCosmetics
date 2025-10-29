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
package se.file14.procosmetics.cosmetic.arroweffect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffect;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectBehavior;
import se.file14.procosmetics.api.cosmetic.arroweffect.ArrowEffectType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;

public class ArrowEffectImpl extends CosmeticImpl<ArrowEffectType, ArrowEffectBehavior> implements ArrowEffect {

    private Entity projectile;
    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);

    public ArrowEffectImpl(ProCosmeticsPlugin plugin, User user, ArrowEffectType type, ArrowEffectBehavior behavior) {
        super(plugin, user, type, behavior);
    }

    @Override
    protected void onEquip() {
    }

    @Override
    protected void onUpdate() {
        if (projectile != null && projectile.isValid() && !projectile.isOnGround()) {
            behavior.onUpdate(this, projectile.getLocation(location));
        } else {
            projectile = null;
            cancel();
        }
    }

    @Override
    protected void onUnequip() {
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (event.getEntity() == player) {
            projectile = event.getProjectile();

            if (!isRunning()) {
                runTaskTimerAsynchronously(plugin, 2L, 1L);
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() == projectile) {
            behavior.onArrowHit(this, projectile.getLocation(location));
        }
    }

    @Override
    public Entity getProjectile() {
        return projectile;
    }
}
