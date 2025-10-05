package se.file14.procosmetics.treasure.animation.type;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import se.file14.procosmetics.api.ProCosmetics;
import se.file14.procosmetics.api.nms.NMSEntity;
import se.file14.procosmetics.api.treasure.TreasureChest;
import se.file14.procosmetics.api.treasure.TreasureChestPlatform;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.treasure.animation.TreasureChestAnimation;
import se.file14.procosmetics.util.FastMathUtil;
import se.file14.procosmetics.util.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mythical extends TreasureChestAnimation {

    private static final int ANGLE_OFFSET = 110;
    private static final double SPEED = 6.0d;
    private static final double RADIUS = 3.2d;
    private static final double Y_OFFSET = 6.0d;
    private static final double Y_DECREASE_PER_TICK = 0.1d;

    private static class MovingChest {

        public NMSEntity nmsEntity;
        public int index;
        public boolean reachedGround = false;

        public MovingChest(NMSEntity nmsEntity, int index) {
            this.nmsEntity = nmsEntity;
            this.index = index;
        }
    }

    private final double ANGLE_PER_CHEST = 360.0d / platform.getChestLocations().size();
    private final List<MovingChest> movingChests = new ArrayList<>();

    private int chests;

    public Mythical(ProCosmetics plugin, TreasureChestPlatform platform, TreasureChest treasure, User user) {
        super(plugin, platform, treasure, user);
    }

    @Override
    public void onSecondUpdate() {
        if (animationState == AnimationState.SPAWNING_CHESTS) {
            if (chests < platform.getChestLocations().size()) {
                float angle = FastMathUtil.toRadians(chests * ANGLE_PER_CHEST + (ticks - 60 - 20 * chests) * SPEED - ANGLE_OFFSET);
                Location location = getLocation(angle);
                location.setY(platform.getCenter().getY() + Y_OFFSET);

                NMSEntity nmsEntity = spawnChestArmorstand(Material.CHEST, location);
                movingChests.add(new MovingChest(nmsEntity, chests));

                chests++;
            } else {
                animationState = AnimationState.BUILT;
            }
        }
    }

    @Override
    public void onTickUpdate() {
        if (animationState == AnimationState.SPAWNING_CHESTS || animationState == AnimationState.BUILT) {
            Iterator<MovingChest> iterator = movingChests.iterator();
            while (iterator.hasNext()) {
                MovingChest movingChest = iterator.next();
                NMSEntity nmsEntity = movingChest.nmsEntity;
                int index = movingChest.index;

                float angle = FastMathUtil.toRadians(index * ANGLE_PER_CHEST + (ticks - 60 - 20 * index) * SPEED - ANGLE_OFFSET);

                location = getLocation(angle);
                location.setY(nmsEntity.getPreviousLocation().getY() - Y_DECREASE_PER_TICK);

                nmsEntity.sendPositionRotationPacket(location);

                if (location.getY() < platform.getCenter().getY()) {
                    if (!movingChest.reachedGround) {
                        setChestBlock(Material.CHEST, platform.getChestLocations().get(index));
                        nmsEntity.getTracker().destroy();
                        movingChest.reachedGround = true;
                        iterator.remove();

                        if (!iterator.hasNext()) {
                            animationState = AnimationState.DONE;
                        }
                    }
                }
                location.getWorld().spawnParticle(Particle.CLOUD, location.add(0.0d, 0.6d, 0.0d), 0);
            }
        }
    }

    private Location getLocation(double angle) {
        return MathUtil.getLocationAroundCircle(platform.getCenter(), RADIUS, angle);
    }
}
