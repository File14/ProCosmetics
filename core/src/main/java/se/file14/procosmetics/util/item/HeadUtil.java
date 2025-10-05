package se.file14.procosmetics.util.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.nms.NMSUtilImpl;
import se.file14.procosmetics.util.ReflectionUtil;
import se.file14.procosmetics.util.version.BukkitVersion;
import se.file14.procosmetics.util.version.VersionUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

public class HeadUtil {

    private static final String TEXTURE_URL = "https://textures.minecraft.net/texture/";
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private static final Field PROFILE_FIELD;
    private static Constructor<?> RESOLVABLE_PROFILE_CONSTRUCTOR;

    static {
        Class<?> skullMeta = ReflectionUtil.getBukkitClass("inventory.CraftMetaSkull");
        PROFILE_FIELD = ReflectionUtil.getDeclaredField(skullMeta, "profile");

        // 1.21.1+
        if (VersionUtil.isHigherThanOrEqualTo(BukkitVersion.v1_21)) {
            Class<?> resolvableProfile = ReflectionUtil.getClass("net.minecraft.world.item.component.ResolvableProfile");

            if (PROFILE_FIELD.getType().equals(resolvableProfile)) {
                RESOLVABLE_PROFILE_CONSTRUCTOR = ReflectionUtil.getConstructor(resolvableProfile, GameProfile.class);
            }
        }
    }

    public static ItemStack getPlayerSkull(Player player) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        NMSUtilImpl nmsUtil = ProCosmeticsPlugin.getPlugin().getNMSManager().getNMSUtil();
        Optional<Property> property = nmsUtil.getProfile(player).getProperties().get("textures").stream().findFirst();

        if (property.isPresent()) {
            GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());
            profile.getProperties().put("textures", new Property("textures", nmsUtil.getPropertiesValue(property.get())));
            setSkullMeta(itemStack, profile);
        }
        return itemStack;
    }

    public static ItemStack getSkull(@Nullable String texture) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        if (texture == null || texture.isEmpty()) {
            return itemStack;
        }
        texture = TEXTURE_URL + texture;

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        byte[] encodedData = ENCODER.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        setSkullMeta(itemStack, profile);

        return itemStack;
    }

    private static void setSkullMeta(ItemStack itemStack, GameProfile profile) {
        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
            try {
                Object profileObject = profile;

                if (RESOLVABLE_PROFILE_CONSTRUCTOR != null) {
                    profileObject = RESOLVABLE_PROFILE_CONSTRUCTOR.newInstance(profile);
                }
                PROFILE_FIELD.set(skullMeta, profileObject);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            itemStack.setItemMeta(skullMeta);
        }
    }
}