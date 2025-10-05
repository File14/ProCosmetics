package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Effect;
import org.bukkit.Location;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;

public class LegendaryAura implements ParticleEffectBehavior {

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        context.getPlayer().getWorld().playEffect(context.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 0);
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}