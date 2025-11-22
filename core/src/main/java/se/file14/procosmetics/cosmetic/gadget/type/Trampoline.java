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
package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.util.structure.type.BlockStructure;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.structure.type.BlockStructureImpl;

import javax.annotation.Nullable;
import java.util.Set;

public class Trampoline implements GadgetBehavior {

    private static final Set<Material> BOUNCE_MATERIALS = Set.of(
            Material.BLACK_WOOL,
            Material.WHITE_WOOL
    );

    private BlockStructure structure;
    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
        if (structure == null) {
            structure = new BlockStructureImpl(context.getType().getStructure());
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        location = context.getPlayer().getLocation();

        structure.spawn(location);

        Location teleport = location.clone().add(0.d, 3.5d, 0.0d);

        for (Player closePlayer : MathUtil.getClosestPlayersFromLocation(location, 4.0d)) {
            closePlayer.teleport(teleport);
        }
        context.getPlugin().getJavaPlugin().getServer().getScheduler().runTaskLater(context.getPlugin().getJavaPlugin(),
                () -> onUnequip(context),
                context.getType().getDurationInTicks()
        );
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        if (location != null) {
            for (Player worldPlayer : location.getWorld().getPlayers()) {
                Block block = worldPlayer.getLocation(location).subtract(0.0d, 1.0d, 0.0d).getBlock();

                if (structure.getPlacedEntries().contains(block) && BOUNCE_MATERIALS.contains(block.getType())) {
                    worldPlayer.setVelocity(worldPlayer.getVelocity().add(new Vector(0.0d, Math.random() >= 0.5d ? 2.5d : 3.0d, 0.0d)));

                    worldPlayer.getWorld().playEffect(location, Effect.STEP_SOUND, Material.BLACK_CONCRETE);
                    worldPlayer.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5f, 1.4f);

                    User otherUser = context.getPlugin().getUserManager().getConnected(worldPlayer);

                    if (otherUser != null) {
                        otherUser.setFallDamageProtection(9);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        structure.remove();
        location = null;
    }

    @Override
    public boolean requiresGroundOnUse() {
        return true;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return structure.isEnoughSpace(location);
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return true;
    }
}
