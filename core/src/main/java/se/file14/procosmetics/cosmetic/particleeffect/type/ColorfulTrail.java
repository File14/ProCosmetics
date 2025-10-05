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

public class ColorfulTrail implements ParticleEffectBehavior {

    private int r = 255;
    private int g = 0;
    private int b = 0;

    private final Vector vector = new Vector();

    @Override
    public void onEquip(CosmeticContext<ParticleEffectType> context) {
    }

    @Override
    public void onUpdate(CosmeticContext<ParticleEffectType> context, Location location) {
        location.add(0.0d, 0.1d, 0.0d);
        float x = -0.85f;
        float angle = -FastMathUtil.toRadians(location.getYaw());

        if (context.getUser().isMoving()) {
            nextRGB();
        }

        for (int i = 0; i < 10; i++) {
            vector.setX(x).setY(0.0d).setZ(0.0d);
            MathUtil.rotateAroundAxisY(vector, angle);
            location.add(vector);

            location.getWorld().spawnParticle(
                    Particle.DUST,
                    location,
                    2,
                    0,
                    0,
                    0,
                    0.0d,
                    new Particle.DustOptions(Color.fromRGB(r, g, b), 1)
            );
            location.subtract(vector);
            x += 0.2f;
        }
    }

    private void nextRGB() {
        if (r == 255 && g < 255 && b == 0) {
            g = Math.min(g + 15, 255);
        } else if (g == 255 && r > 0 && b == 0) {
            r = Math.max(r - 15, 0);
        } else if (g == 255 && b < 255 && r == 0) {
            b = Math.min(b + 15, 255);
        } else if (b == 255 && g > 0 && r == 0) {
            g = Math.max(g - 15, 0);
        } else if (b == 255 && r < 255 && g == 0) {
            r = Math.min(r + 15, 255);
        } else if (r == 255 && b > 0 && g == 0) {
            b = Math.max(b - 15, 0);
        }
    }

    @Override
    public void onUnequip(CosmeticContext<ParticleEffectType> context) {
    }
}