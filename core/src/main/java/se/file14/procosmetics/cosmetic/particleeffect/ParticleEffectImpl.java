package se.file14.procosmetics.cosmetic.particleeffect;

import org.bukkit.Location;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffect;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticImpl;

public class ParticleEffectImpl extends CosmeticImpl<ParticleEffectType, ParticleEffectBehavior> implements ParticleEffect {

    protected Location location = new Location(null, 0.0d, 0.0d, 0.0d);
    private final boolean sneakToPause;

    public ParticleEffectImpl(ProCosmeticsPlugin plugin, User user, ParticleEffectTypeImpl type, ParticleEffectBehavior behavior) {
        super(plugin, user, type, behavior);
        this.sneakToPause = type.getCategory().getConfig().getBoolean("sneak_to_pause");
    }

    @Override
    protected void onEquip() {
        runTaskTimer(plugin, 0L, cosmeticType.getRepeatDelay());
    }

    @Override
    protected void onUpdate() {
        if (sneakToPause && player.isSneaking()) {
            return;
        }
        location = player.getLocation(location);
        behavior.onUpdate(this, location);
    }

    @Override
    protected void onUnequip() {
    }
}