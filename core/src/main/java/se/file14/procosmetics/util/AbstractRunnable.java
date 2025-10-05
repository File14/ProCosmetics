package se.file14.procosmetics.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractRunnable implements Runnable {

    private BukkitTask task;

    public BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTask(plugin, this));
    }

    public BukkitTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, this));
    }

    public BukkitTask runTaskLater(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTaskLater(plugin, this, delay));
    }

    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
    }

    public BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period));
    }

    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
    }

    private BukkitTask setupTask(BukkitTask task) {
        this.task = task;
        return task;
    }

    public void cancel() {
        if (isRunning()) {
            Bukkit.getScheduler().cancelTask(getTaskId());
            task = null;
        }
    }

    public boolean isRunning() {
        return task != null;
    }

    public int getTaskId() throws IllegalStateException {
        checkScheduled();
        return task.getTaskId();
    }

    private void checkScheduled() {
        if (task == null)
            throw new IllegalStateException("Not scheduled yet");
    }

    private void checkNotYetScheduled() {
        if (task != null)
            throw new IllegalStateException("Already scheduled as " + task.getTaskId());
    }
}
