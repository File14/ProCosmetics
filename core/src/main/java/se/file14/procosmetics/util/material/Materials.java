package se.file14.procosmetics.util.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import se.file14.procosmetics.util.MathUtil;

import java.util.List;

public class Materials {

    public static final List<Material> TERRACOTTA = List.of(
            Material.BLACK_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.RED_TERRACOTTA,
            Material.WHITE_TERRACOTTA
    );

    public static final List<Material> WOOL = List.of(
            Material.BLACK_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL, Material.GREEN_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.LIME_WOOL,
            Material.MAGENTA_WOOL,
            Material.ORANGE_WOOL,
            Material.PINK_WOOL,
            Material.PURPLE_WOOL,
            Material.RED_WOOL,
            Material.WHITE_WOOL
    );

    public static final List<Material> STAINED_GLASS = List.of(
            Material.BLACK_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS
    );

    public static final List<Material> INK = List.of(
            Material.RED_DYE,
            Material.GREEN_DYE,
            Material.COCOA_BEANS,
            Material.LAPIS_LAZULI,
            Material.PURPLE_DYE,
            Material.CYAN_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.GRAY_DYE,
            Material.PINK_DYE,
            Material.YELLOW_DYE,
            Material.LIGHT_BLUE_DYE,
            Material.MAGENTA_DYE,
            Material.ORANGE_DYE,
            Material.BONE_MEAL
    );

    public static final List<Material> CONCRETE = List.of(
            Material.BLACK_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.LIME_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.PINK_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.RED_CONCRETE,
            Material.WHITE_CONCRETE
    );

    public static Material getRandomTerracotta() {
        return TERRACOTTA.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(TERRACOTTA.size()));
    }

    public static ItemStack getRandomTerracottaItem() {
        return new ItemStack(getRandomTerracotta());
    }

    public static Material getRandomWool() {
        return WOOL.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(WOOL.size()));
    }

    public static ItemStack getRandomWoolItem() {
        return new ItemStack(getRandomWool());
    }

    public static Material getRandomStainedGlass() {
        return STAINED_GLASS.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(STAINED_GLASS.size()));
    }

    public static ItemStack getRandomStainedGlassItem() {
        return new ItemStack(getRandomStainedGlass());
    }

    public static Material getRandomInk() {
        return INK.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(INK.size()));
    }

    public static ItemStack getRandomInkItem() {
        return new ItemStack(getRandomInk());
    }

    public static Material getRandomConcrete() {
        return CONCRETE.get(MathUtil.THREAD_LOCAL_RANDOM.nextInt(CONCRETE.size()));
    }

    public static ItemStack getRandomConcreteItem() {
        return new ItemStack(getRandomConcrete());
    }
}