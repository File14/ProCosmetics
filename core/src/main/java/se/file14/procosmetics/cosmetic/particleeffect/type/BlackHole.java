package se.file14.procosmetics.cosmetic.particleeffect.type;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import se.file14.procosmetics.api.cosmetic.CosmeticContext;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectBehavior;
import se.file14.procosmetics.api.cosmetic.particleeffect.ParticleEffectType;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

public class BlackHole implements ParticleEffectBehavior {

    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);

    private int ticks;
    private final Vector vector = new Vector();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        if (context.getUser().isMoving()) {
            location.getWorld().spawnParticle(Particle.PORTAL, location, 4, 0.1d, 0.1d, 0.1d, 0.0d);
            location.getWorld().spawnParticle(Particle.DUST, location.add(0.0d, 0.3d, 0.0d), 2, 0, 0, 0, 0.0d, DUST_OPTIONS);
        } else {
            location.add(0.0d, 0.1d, 0.0d);

            float range = 1.0f;
            float tickAngle = 0.02f * ticks;
            int totalLines = 6;
            float current = FastMathUtil.PI * 2.0f / totalLines;

            for (int line = 1; line <= totalLines; line++) {
                for (int curve = 0; curve < 6; curve++) {
                    float particle = curve / 4.0f;
                    float angle = particle + current * line;
                    float calc = particle * range;

                    float x = FastMathUtil.cos(angle) * calc;
                    float z = FastMathUtil.sin(angle) * calc;

                    vector.setX(x).setZ(z);
                    MathUtil.rotateAroundAxisY(vector, tickAngle);
                    location.add(vector);
                    location.getWorld().spawnParticle(
                            Particle.DUST,
                            location,
                            0,
                            0,
                            0,
                            0,
                            0.0d,
                            DUST_OPTIONS
                    );
                    location.subtract(vector);
                }
            }

            if (ticks % 4 == 0) {
                context.getPlayer().getLocation(location);

                Location flameLocation = location.clone().add(
                        MathUtil.randomRange(-1.7d, 1.7d),
                        MathUtil.randomRange(0.5d, 0.7d),
                        MathUtil.randomRange(-1.7d, 1.7d)
                );
                Vector towardsPlayerVector = location.toVector().subtract(flameLocation.toVector());

                location.getWorld().spawnParticle(Particle.FLAME,
                        flameLocation,
                        0,
                        towardsPlayerVector.getX(),
                        towardsPlayerVector.getY(),
                        towardsPlayerVector.getZ(),
                        0.08d
                );
                location.getWorld().spawnParticle(Particle.PORTAL, location, 3, 0.6d, 0.0d, 0.6d, 0.0d);
            }

            ticks++;
            if (ticks > 360) {
                ticks = 0;
            }
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
        // No unequip behavior
    }
}