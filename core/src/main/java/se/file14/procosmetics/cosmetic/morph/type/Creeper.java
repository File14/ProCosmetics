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
package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphBehavior;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.util.MathUtil;

public class Creeper implements MorphBehavior {

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private int charge;
    private boolean activated;

    public void onEquip(CosmeticContext<MorphType> context) {
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        return InteractionResult.noAction();
    }

    @Override
    public InteractionResult onToggleSneak(CosmeticContext<MorphType> context, PlayerToggleSneakEvent event, NMSEntity nmsEntity) {
        if (activated) {
            return InteractionResult.noAction();
        }
        Player player = context.getPlayer();

        setCreeperEffect(nmsEntity, true);
        player.getWorld().playSound(player, Sound.ENTITY_CREEPER_PRIMED, 0.5f, charge <= 10 ? charge / 5.0f : 1.0f);
        activated = true;

        return InteractionResult.success();
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        if (activated && charge++ >= 30) {
            Player player = context.getPlayer();
            player.getLocation(location);

            location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 1);
            player.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f);

            for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(location, 5.0d)) {
                if (hitPlayer == player) {
                    continue;
                }
                MathUtil.pushEntity(hitPlayer, location, 1.2d, 1.5d);
                User otherUser = context.getPlugin().getUserManager().getConnected(hitPlayer);

                if (otherUser != null) {
                    otherUser.setFallDamageProtection(6);
                }
            }
            setCreeperEffect(nmsEntity, false);
            charge = 0;
            activated = false;
        }
    }

    private void setCreeperEffect(NMSEntity nmsEntity, boolean effect) {
        nmsEntity.setCreeperPowered(effect);
        nmsEntity.setCreeperIgnited(effect);
        nmsEntity.sendEntityMetadataPacket();
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        charge = 0;
    }
}
