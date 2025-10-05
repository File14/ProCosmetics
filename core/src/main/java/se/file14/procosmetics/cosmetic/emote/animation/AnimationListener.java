package se.file14.procosmetics.cosmetic.emote.animation;

import se.file14.procosmetics.api.util.AnimationFrame;

public interface AnimationListener {

    default void onFrameChanged(int frameIndex, AnimationFrame frame) {
    }

    default void onFrameTick(int frameIndex, int ticksInFrame) {
    }

    default void onAnimationComplete() {
    }
}