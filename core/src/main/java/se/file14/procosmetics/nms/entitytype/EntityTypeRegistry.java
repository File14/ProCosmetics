package se.file14.procosmetics.nms.entitytype;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityTypeRegistry {

    private static final Map<EntityType, CachedEntityType> ENTITY_TYPE_CACHE = new HashMap<>();

    public static CachedEntityType getCachedEntityType(EntityType entityType) {
        return ENTITY_TYPE_CACHE.computeIfAbsent(entityType, CachedEntityType::new);
    }

    public static Object getEntityTypeObject(EntityType entityType) {
        CachedEntityType cached = getCachedEntityType(entityType);
        return cached.getEntityTypeObject();
    }

    public static boolean isCached(EntityType entityType) {
        return ENTITY_TYPE_CACHE.containsKey(entityType);
    }

    public static void clearCache() {
        ENTITY_TYPE_CACHE.clear();
    }
}