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
package v1_21_10;

import io.netty.channel.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;
import se.file14.procosmetics.nms.NMSUtilImpl;
import se.file14.procosmetics.util.ReflectionUtil;
import se.file14.procosmetics.util.mapping.MappingRegistry;

import java.lang.reflect.Field;

public class NMSUtil extends NMSUtilImpl {

    private static final Field NETWORK_FIELD = ReflectionUtil.getDeclaredField(
            ServerCommonPacketListenerImpl.class,
            MappingRegistry.getMappedFieldName(MappingRegistry.CONNECTION)
    );

    @Override
    public Channel getChannel(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        try {
            Connection connection = (Connection) (NETWORK_FIELD.get(serverPlayer.connection));
            return connection.channel;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public int getPing(Player player) {
        return player.getPing();
    }

    @Override
    public void playChestAnimation(Block block, boolean open) {
        BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());

        ((CraftWorld) block.getWorld()).getHandle().blockEvent(blockPos,
                block.getType() == Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST,
                1,
                open ? 1 : 0
        );
    }
}
