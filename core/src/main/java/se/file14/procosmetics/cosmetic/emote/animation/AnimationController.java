/*
 * This file is part of ProCosmetics - https://github.com/File14/ProCosmetics
 * Copyright (C) 2025 File14 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package se.file14.procosmetics.cosmetic.emote.animation;

import se.file14.procosmetics.api.util.AnimationFrame;

import java.util.ArrayList;
import java.util.List;

public class AnimationController {

    private final List<AnimationFrame> frames;
    private final long tickInterval;
    private int currentFrameIndex = 0;
    private int currentFrameTicks = 0;
    private boolean isPlaying = false;
    private boolean shouldLoop = true;

    private final List<AnimationListener> listeners = new ArrayList<>();

    public AnimationController(List<AnimationFrame> frames, long tickInterval) {
        this.frames = frames;
        this.tickInterval = tickInterval;
    }

    public void start() {
        if (frames.isEmpty()) {
            return;
        }
        currentFrameIndex = 0;
        currentFrameTicks = 0;
        isPlaying = true;
        notifyFrameChanged();
    }

    public void tick() {
        if (!isPlaying || frames.isEmpty()) {
            return;
        }
        AnimationFrame currentFrame = frames.get(currentFrameIndex);
        currentFrameTicks++;

        if (currentFrameTicks >= currentFrame.getTickDuration()) {
            currentFrameTicks = 0;
            currentFrameIndex++;

            if (currentFrameIndex >= frames.size()) {
                if (shouldLoop) {
                    currentFrameIndex = 0;
                    notifyFrameChanged();
                } else {
                    stop();
                    notifyAnimationComplete();
                    return;
                }
            } else {
                notifyFrameChanged();
            }
        }
        notifyFrameTick();
    }

    public void stop() {
        isPlaying = false;
    }

    public void pause() {
        isPlaying = false;
    }

    public void resume() {
        isPlaying = true;
    }

    public AnimationFrame getCurrentFrame() {
        return frames.isEmpty() ? null : frames.get(currentFrameIndex);
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void addListener(AnimationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AnimationListener listener) {
        listeners.remove(listener);
    }

    private void notifyFrameChanged() {
        AnimationFrame frame = getCurrentFrame();
        listeners.forEach(listener -> listener.onFrameChanged(currentFrameIndex, frame));
    }

    private void notifyFrameTick() {
        listeners.forEach(listener -> listener.onFrameTick(currentFrameIndex, currentFrameTicks));
    }

    private void notifyAnimationComplete() {
        listeners.forEach(AnimationListener::onAnimationComplete);
    }

    public void setLooping(boolean shouldLoop) {
        this.shouldLoop = shouldLoop;
    }

    public long getTickInterval() {
        return tickInterval;
    }
}
