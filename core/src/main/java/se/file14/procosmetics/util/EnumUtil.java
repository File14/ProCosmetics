package se.file14.procosmetics.util;

public class EnumUtil {

    public static <T extends Enum<T>> T getType(Class<T> clazz, String key) {
        try {
            return Enum.valueOf(clazz, key.toUpperCase());
        } catch (IllegalArgumentException e) {
            LogUtil.log("Failed to parse " + key + " in " + clazz.getName() + ". Fallback to first value.");
            return clazz.getEnumConstants()[0];
        }
    }
}
