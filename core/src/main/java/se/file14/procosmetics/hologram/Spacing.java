package se.file14.procosmetics.hologram;

public class Spacing {

    public static final Spacing NONE = new Spacing(0.0d);
    public static final Spacing SMALL = new Spacing(0.0625d);
    public static final Spacing FULL = new Spacing(0.25d);

    private final double value;

    private Spacing(double value) {
        this.value = value;
    }

    public static Spacing of(double value) {
        return new Spacing(value);
    }

    public double getValue() {
        return value;
    }
}
