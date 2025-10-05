package se.file14.procosmetics.cosmetic.emote.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;

public class Sad implements EmoteBehavior {

    @Override
    public void onEquip(CosmeticContext<EmoteType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<EmoteType> context, int frame) {
        if (frame > 8 && frame < 20) {
            Location location = context.getPlayer().getEyeLocation().add(0.0d, 0.2d, 0.0d);
            location.getWorld().spawnParticle(Particle.SPLASH, location, 10, 0.15d, 0.1d, 0.15d, 0.0d);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<EmoteType> context) {
    }
}