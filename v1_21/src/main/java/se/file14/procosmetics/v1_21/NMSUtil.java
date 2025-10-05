package se.file14.procosmetics.v1_21;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
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
    public GameProfile getProfile(Player player) {
        return ((CraftPlayer) player).getHandle().getGameProfile();
    }

    @Override
    public String getPropertiesValue(Property property) {
        return property.value();
    }

    @Override
    public void playChestAnimation(Block block, boolean open) {
        Location location = block.getLocation();
        BlockPos blockPos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        ((CraftWorld) location.getWorld()).getHandle().blockEvent(blockPos,
                block.getType() == Material.CHEST ? Blocks.CHEST : Blocks.ENDER_CHEST,
                1,
                open ? 1 : 0
        );
    }
}