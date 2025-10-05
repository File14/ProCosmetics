package se.file14.procosmetics.cosmetic.pet.type;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.pet.PetBehavior;
import se.file14.procosmetics.api.cosmetic.pet.PetType;

public class BodyGuard implements PetBehavior {

    private final Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private int tick;

    @Override
    public void onEquip(CosmeticContext<PetType> context) {
    }


    @Override
    public void onSetupEntity(CosmeticContext<PetType> context, Entity entity) {
    }

    @Override
    public void onUpdate(CosmeticContext<PetType> context, Entity entity) {
        if (tick % 5 == 0) {
            entity.getLocation(location);
            location.getWorld().spawnParticle(Particle.HEART, location, 2, 0.6f, 1.0f, 0.6f, 0.1f);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 2, 1.0f, 1.0f, 1.0f, 0.3f);

            if (tick % 15 == 0) {
                entity.playEffect(EntityEffect.IRON_GOLEM_ROSE);
            }
        }

        if (tick++ > 360) {
            tick = 0;
        }
    }


    @Override
    public void onUnequip(CosmeticContext<PetType> context) {
    }
}