package se.file14.procosmetics.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.api.user.UserManager;
import se.file14.procosmetics.util.AbstractRunnable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class UserManagerImpl implements UserManager {

    private final ProCosmeticsPlugin plugin;

    private final Cache<@NotNull UUID, @NotNull User> cached = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    private final Cache<@NotNull Integer, @NotNull User> cachedIds = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

    private final Map<UUID, Object> loginLocks = new ConcurrentHashMap<>();
    private final Cache<@NotNull UUID, @NotNull User> connecting = CacheBuilder.newBuilder().expireAfterWrite(40, TimeUnit.SECONDS).build();
    private final Map<UUID, User> connected = new ConcurrentHashMap<>();

    private final Map<UUID, CompletableFuture<User>> uuidRequests = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<User>> nameRequests = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<User>> idRequests = new ConcurrentHashMap<>();

    public UserManagerImpl(ProCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Listeners(), plugin);
        plugin.getServer().getScheduler().runTaskTimer(plugin, new MovementRunnable(), 1L, 1L);
    }

    public void loadOnlinePlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            User user = plugin.getDatabase().loadUser(player.getUniqueId());

            if (user == null) {
                user = plugin.getDatabase().insertUser(player.getUniqueId(), player.getName());
            }

            if (user != null) {
                cached.invalidate(player.getUniqueId());
                cachedIds.invalidate(user.getDatabaseId());
                connected.put(player.getUniqueId(), user);

                if (player.getGameMode() != GameMode.SPECTATOR) {
                    user.equipSavedCosmetics(true);
                }
            }
        }
    }

    private final class Listeners implements Listener {

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        private void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
            if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                UUID uuid = event.getUniqueId();
                String name = event.getName();

                connecting.invalidate(uuid);

                synchronized (loginLocks.computeIfAbsent(uuid, uuid1 -> new Object())) {
                    User user = plugin.getDatabase().loadUser(uuid);

                    if (user == null) {
                        user = plugin.getDatabase().insertUser(uuid, name);
                    } else if (!user.getName().equals(name)) {
                        plugin.getDatabase().updateName(user, name);
                    }

                    if (user != null) {
                        connecting.put(uuid, user);
                    }
                    loginLocks.remove(uuid);
                }
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            connecting.cleanUp();
            User user = connecting.getIfPresent(uuid);

            if (user != null) {
                cached.invalidate(uuid);
                cachedIds.invalidate(user.getDatabaseId());
                connected.put(uuid, user);

                if (player.getGameMode() != GameMode.SPECTATOR) {
                    user.equipSavedCosmetics(true);
                }
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private void onQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            User user = connected.remove(player.getUniqueId());

            if (user != null) {
                user.clearAllCosmetics(true, false);
            }
        }
    }

    @Override
    public Collection<User> getAllConnected() {
        return connected.values();
    }

    @Override
    @Nullable
    public User getConnected(@Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return connected.get(uuid);
    }

    @Override
    @Nullable
    public User getConnectedOrCached(@Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }
        User user = getConnected(uuid);

        if (user == null) {
            cached.cleanUp();
            cachedIds.cleanUp();
            user = cached.getIfPresent(uuid);
        }
        return user;
    }

    @Override
    @Nullable
    public User getConnectedOrCached(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        User user = getConnected(name);

        if (user == null) {
            cached.cleanUp();
            cachedIds.cleanUp();

            for (User cachedUser : cached.asMap().values()) {
                if (cachedUser.getName().equalsIgnoreCase(name)) {
                    user = cachedUser;
                    break;
                }
            }
        }
        return user;
    }

    @Override
    @Nullable
    public User getConnectedOrCached(int id) {
        User user = getConnected(id);

        if (user == null) {
            cached.cleanUp();
            cachedIds.cleanUp();
            user = cachedIds.getIfPresent(id);
        }
        return user;
    }

    @Override
    @Nullable
    public User get(@Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }
        User user = getConnectedOrCached(uuid);

        if (user == null) {
            user = plugin.getDatabase().loadUser(uuid);

            if (user != null) {
                cached.put(uuid, user);
                cachedIds.put(user.getDatabaseId(), user);
            }
        }
        return user;
    }

    @Override
    @Nullable
    public User get(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        User user = getConnectedOrCached(name);

        if (user == null) {
            user = plugin.getDatabase().loadUser(name);

            if (user != null) {
                cached.put(user.getUniqueId(), user);
                cachedIds.put(user.getDatabaseId(), user);
            }
        }
        return user;
    }

    @Override
    @Nullable
    public User get(int id) {
        User user = getConnectedOrCached(id);

        if (user == null) {
            user = plugin.getDatabase().loadUser(id);

            if (user != null) {
                cached.put(user.getUniqueId(), user);
                cachedIds.put(user.getDatabaseId(), user);
            }
        }
        return user;
    }

    @Override
    public CompletableFuture<@Nullable User> getAsync(@Nullable UUID uuid) {
        if (uuid == null) {
            return CompletableFuture.completedFuture(null);
        }
        return uuidRequests.computeIfAbsent(uuid, (uuid1) -> CompletableFuture.supplyAsync(() -> get(uuid), plugin.getAsyncExecutor())
                .whenComplete((corePlayer, throwable) -> {
                    if (throwable != null) {
                        plugin.getLogger().log(Level.WARNING, "Failed to load user asynchronously for UUID: " + uuid + ".", throwable);
                    }
                    uuidRequests.remove(uuid);
                }));
    }

    @Override
    public CompletableFuture<@Nullable User> getAsync(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        String lowerName = name.toLowerCase(Locale.ROOT);

        return nameRequests.computeIfAbsent(lowerName, (name1) -> CompletableFuture.supplyAsync(() -> get(lowerName), plugin.getAsyncExecutor())
                .whenComplete((corePlayer, throwable) -> {
                    if (throwable != null) {
                        plugin.getLogger().log(Level.WARNING, "Failed to load user asynchronously for name: " + name + ".", throwable);
                    }
                    nameRequests.remove(lowerName);
                }));
    }

    @Override
    public CompletableFuture<@Nullable User> getAsync(int id) {
        return idRequests.computeIfAbsent(id, (uuid1) -> CompletableFuture.supplyAsync(() -> get(id), plugin.getAsyncExecutor())
                .whenComplete((corePlayer, throwable) -> {
                    if (throwable != null) {
                        plugin.getLogger().log(Level.WARNING, "Failed to load user asynchronously for id: " + id + ".", throwable);
                    }
                    idRequests.remove(id);
                }));
    }

    private final class MovementRunnable extends AbstractRunnable {

        private final Location reuseableLocation = new Location(null, 0, 0, 0);

        @Override
        public void run() {
            for (User user : connected.values()) {
                Player player = user.getPlayer();

                if (player != null) {
                    Location location = player.getLocation(reuseableLocation);
                    boolean moving = hasDifference(location, user.getLastLocation());
                    user.setMoving(moving);

                    if (moving) {
                        user.setLastLocation(location);
                    }
                }
            }
        }

        private boolean hasDifference(@Nullable Location from, @Nullable Location to) {
            if (from == null || to == null) {
                return true;
            }

            if (from.getWorld() != to.getWorld()) {
                return true;
            }
            return Double.doubleToLongBits(from.getX()) != Double.doubleToLongBits(to.getX())
                    || Double.doubleToLongBits(from.getY()) != Double.doubleToLongBits(to.getY())
                    || Double.doubleToLongBits(from.getZ()) != Double.doubleToLongBits(to.getZ());
        }
    }
}