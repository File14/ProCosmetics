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
package se.file14.procosmetics.redis;

import redis.clients.jedis.*;
import se.file14.procosmetics.ProCosmeticsPlugin;

import java.util.UUID;
import java.util.logging.Level;

public class RedisProvider {

    private final JedisPool jedisPool;
    private final String serverId;

    public RedisProvider(ProCosmeticsPlugin plugin, String host, int port, String username, String password, boolean ssl) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        HostAndPort hostAndPort = new HostAndPort(host, port);
        DefaultJedisClientConfig.Builder configBuilder = DefaultJedisClientConfig.builder()
                .ssl(ssl)
                .timeoutMillis(Protocol.DEFAULT_TIMEOUT);

        // Only set username if it's not empty
        if (username != null && !username.isEmpty()) {
            configBuilder.user(username);
        }
        // Only set password if it's not empty
        if (password != null && !password.isEmpty()) {
            configBuilder.password(password);
        }
        JedisClientConfig clientConfig = configBuilder.build();

        this.jedisPool = new JedisPool(poolConfig, hostAndPort, clientConfig);
        this.serverId = UUID.randomUUID().toString().substring(0, 8);

        testConnection(plugin);
    }

    public void testConnection(ProCosmeticsPlugin plugin) {
        try (Jedis jedis = jedisPool.getResource()) {
            String response = jedis.ping();

            if ("PONG".equals(response)) {
                plugin.getLogger().log(Level.INFO, "[REDIS] Successfully connected to Redis server.");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "[REDIS] Failed to connect to Redis server.", e);
            e.printStackTrace();
        }
    }

    public void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public String getServerId() {
        return serverId;
    }
}
