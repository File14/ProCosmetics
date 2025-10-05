package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.MathUtil;
import se.file14.procosmetics.util.RGBFade;

public class RainbowWings implements ParticleEffectBehavior {

    private static final boolean x = true;
    private static final boolean o = false;
    private static final boolean[][] SHAPE = new boolean[][]{
            {o, x, x, x, x, o, o, o, o, o, o, o, x, x, x, x, o},
            {o, o, x, x, x, x, x, o, o, o, x, x, x, x, x, o, o},
            {o, o, o, x, x, x, x, x, x, x, x, x, x, x, o, o, o},
            {o, o, o, o, x, x, x, x, x, x, x, x, x, o, o, o, o},
            {o, o, o, o, x, x, x, x, o, x, x, x, x, o, o, o, o},
            {o, o, o, o, o, x, x, x, o, x, x, x, o, o, o, o, o},
            {o, o, o, o, o, x, x, o, o, o, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, o, o, o, o, o, x, x, o, o, o, o}
    };

    private final RGBFade rgbFade = new RGBFade();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            rgbFade.nextRGB();
            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location.add(0.0d, 0.6d, 0.0d),
                    1,
                    new Particle.DustOptions(
                            Color.fromRGB(rgbFade.getR(), rgbFade.getG(), rgbFade.getB()),
                            1
                    )
            );
        } else {
            final double defaultX = -1.6d;
            double x = defaultX;
            double y = 2.0d;
            final double z = -0.3d;
            final double angle = -Math.toRadians(location.getYaw());
            Vector offsetVector = new Vector();

            for (boolean[] line : SHAPE) {
                rgbFade.nextRGB();

                for (boolean dot : line) {
                    if (dot) {
                        offsetVector.setX(x).setY(y).setZ(z);
                        Vector rotated = MathUtil.rotateAroundAxisY(offsetVector, angle);
                        location.add(rotated);

                        location.getWorld().spawnParticle(
                                Particle.DUST,
                                location,
                                1,
                                new Particle.DustOptions(
                                        Color.fromRGB(rgbFade.getR(), rgbFade.getG(), rgbFade.getB()),
                                        1
                                )
                        );
                        location.subtract(rotated);
                    }
                    x += 0.2f;
                }
                y -= 0.2f;
                x = defaultX;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}