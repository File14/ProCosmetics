package se.file14.procosmetics.util;

public final class FastMathUtil {

    // Riven's sine/cosine (http://www.java-gaming.org/topics/fast-math-sin-cos-lookup-tables/24191/view.html)
    // Found at: https://jvm-gaming.org/t/extremely-fast-sine-cosine/55153

    private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
    private static final float radFull, radToIndex;
    private static final float degFull, degToIndex;
    private static final float[] sin, cos;
    private static final float DEGREES_TO_RADIANS;
    public static final float PI;

    static {
        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;

        radFull = (float) (Math.PI * 2.0d);
        degFull = 360.0f;
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sin = new float[SIN_COUNT];
        cos = new float[SIN_COUNT];

        for (int i = 0; i < SIN_COUNT; i++) {
            sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
        }
        double rad = Math.PI / 180.0d;

        for (int i = 0; i < 360; i += 90) {
            sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * rad);
            cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * rad);
        }
        DEGREES_TO_RADIANS = 0.017453292f;
        PI = 3.1415926535897f;
    }

    public static float sin(float rad) {
        return sin[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static float cos(float rad) {
        return cos[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static float toRadians(float angle) {
        return DEGREES_TO_RADIANS * angle;
    }

    public static float toRadians(double angle) {
        return (float) (DEGREES_TO_RADIANS * angle);
    }
}