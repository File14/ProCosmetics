package se.file14.procosmetics.cosmetic.gadget.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetBehavior;
import se.file14.procosmetics.api.cosmetic.gadget.GadgetType;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Watergun implements GadgetBehavior {

    private static final ItemStack ITEM_STACK = new ItemStack(Material.BLUE_DYE);
    private static final Vector OFFSET_VECTOR = new Vector(0.0d, 0.4d, 0.0d);

    private final List<Projectile> balls = new ArrayList<>();

    private Location location;

    @Override
    public void onEquip(CosmeticContext<GadgetType> context) {
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<GadgetType> context, @Nullable Block clickedBlock, @Nullable Vector clickedPosition) {
        Player player = context.getPlayer();
        location = player.getEyeLocation();

        location = player.getLocation(location).add(0.0d, 1.5d, 0.0d);
        Vector vector = location.getDirection();
        vector.add(OFFSET_VECTOR);

        balls.add(location.getWorld().spawn(location, Snowball.class, entity -> {
            entity.setVelocity(location.getDirection().multiply(2.0d));
            entity.setShooter(player);
            entity.setItem(ITEM_STACK);

            MetadataUtil.setCustomEntity(entity);
        }));
        player.getWorld().spawnParticle(Particle.SPLASH, location, 0, vector.getX(), vector.getY(), vector.getZ(), 1.0d);
        location.getWorld().playSound(location, Sound.BLOCK_WATER_AMBIENT, 1.0f, 1.0f);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUpdate(CosmeticContext<GadgetType> context) {
        for (Projectile projectile : balls) {
            if (projectile.isValid()) {
                context.getPlayer().getWorld().spawnParticle(Particle.SPLASH,
                        projectile.getLocation(location),
                        0,
                        0.0d,
                        0.0d,
                        0.0d,
                        1.0d
                );
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<GadgetType> context) {
        for (Entity entity : balls) {
            entity.remove();
        }
        balls.clear();
    }

    @Override
    public boolean requiresGroundOnUse() {
        return false;
    }

    @Override
    public boolean isEnoughSpaceToUse(Location location) {
        return true;
    }

    @Override
    public boolean shouldUnequipOnTeleport() {
        return false;
    }
}