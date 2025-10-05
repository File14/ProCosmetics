package se.file14.procosmetics.cosmetic.emote.type;

import org.bukkit.Location;
import org.bukkit.Particle;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.emote.EmoteBehavior;
import se.file14.procosmetics.api.cosmetic.emote.EmoteType;

public class Wink implements EmoteBehavior {

    @Override
    public void onEquip(CosmeticContext<EmoteType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<EmoteType> context, int frame) {
        if (frame == 4) {
            Location location = context.getPlayer().getEyeLocation().add(0.0d, 0.4d, 0.0d);
            location.getWorld().spawnParticle(Particle.HEART, location, 1);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<EmoteType> context) {
    }
}