package se.file14.procosmetics.cosmetic.morph.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.morph.MorphType;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.morph.FlyableMorph;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.MetadataUtil;

import javax.annotation.Nullable;

public class ElderGuardian extends FlyableMorph {

    private static final double HIT_RANGE = 7.0d;

    private ArmorStand armorStand;
    private int tick;


    @Override
    public void onEquip(CosmeticContext<MorphType> context) {
        super.onEquip(context);
    }

    @Override
    public void setupEntity(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
    }

    @Override
    public void onUpdate(CosmeticContext<MorphType> context, NMSEntity nmsEntity) {
        super.onUpdate(context, nmsEntity);

        if (armorStand != null) {
            if (tick++ > 60) {
                Player player = context.getPlayer();
                Location location = armorStand.getLocation();

                location.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, location, 5, 2.0d, 1.0d, 2.0d, 0.1d);
                location.getWorld().playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.0f);

                setGuardianTarget(nmsEntity, null);

                for (Player hitPlayer : MathUtil.getClosestPlayersFromLocation(location, HIT_RANGE)) {
                    if (hitPlayer != player) {
                        hitPlayer.setVelocity(hitPlayer.getVelocity().setY(1.5d));

                        User otherUser = context.getPlugin().getUserManager().getConnected(hitPlayer);

                        if (otherUser != null) {
                            otherUser.setFallDamageProtection(6);
                        }
                    }
                }
                armorStand.remove();
                armorStand = null;
                tick = 0;
            }
        }
    }

    @Override
    public InteractionResult onInteract(CosmeticContext<MorphType> context, PlayerInteractEvent event, NMSEntity nmsEntity) {
        if (event.getAction() == Action.LEFT_CLICK_AIR && armorStand == null) {
            Location location = context.getPlayer().getLocation();
            Vector dir = location.getDirection();

            for (int i = 0; i < 15; i++) {
                location.add(dir);

                if (location.getBlock().getType() != Material.AIR) {
                    break;
                }
            }
            armorStand = location.getWorld().spawn(location, ArmorStand.class, entity -> {
                entity.setVisible(false);
                entity.setSmall(true);
                entity.setGravity(false);
                MetadataUtil.setCustomEntity(entity);
            });
            setGuardianTarget(nmsEntity, armorStand);
        }
        return InteractionResult.SUCCESS;
    }

    public void setGuardianTarget(NMSEntity nmsEntity, @Nullable LivingEntity target) {
        nmsEntity.setGuardianTarget(target != null ? target.getEntityId() : 0);
        nmsEntity.sendEntityMetadataPacket();
    }

    @Override
    public void onUnequip(CosmeticContext<MorphType> context) {
        super.onUnequip(context);

        if (armorStand != null) {
            armorStand.remove();
            armorStand = null;
        }
        tick = 0;
    }
}