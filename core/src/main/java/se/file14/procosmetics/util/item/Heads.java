package se.file14.procosmetics.util.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Heads {

    SANTA("2d61ccbfdcdf8941adaf76c6c0e0182d2c8bbb5dc18f374895652bc661b6ed"),
    ELF("957be47be6f4b26b079c7758b66ca888b9c34eaed72e6e58b13d87ffda0b3"),
    AQUA_PRESENT("512e9451cdb196b78195a8f0a4b9c1c0a04f5827887927b6a82aad39cab2f430"),
    GOLD_PRESENT("6b4cde16a4014de0a7651f6067f12695bb5fed6feaec1e9413ca4271e7c819"),
    GREEN_PRESENT("9715f537fe7af6f5aa6eb98ad6902c13d05fb36c16b311ed832b09b598828"),
    RED_PRESENT("6cef9aa14e884773eac134a4ee8972063f466de678363cf7b1a21a85b7"),
    DARK_PINK_PRESENT("47e55fcc809a2ac1861da2a67f7f31bd7237887d162eca1eda526a7512a64910"),
    TURQUOISE_PRESENT("7fcd1c82e2fb3fa368cfa9a506ab6c98647595d215d6471ad47cce29685af"),
    PINK_PRESENT("10c75a05b344ea043863974c180ba817aea68678cbea5e4ba395f74d4803d1d"),
    EGG_BASKET("83c25a7a188196b18717264ffe837ca348cf719e827179edc4b78cbcb8c7dd8"),
    DISCO_BALL("91c581a8b597692b5b94d3b8beb9c52f56999d62f7395668fac57ac952fe4dc4");

    private final ItemStack itemStack;

    Heads(String texture) {
        itemStack = new ItemBuilderImpl(Material.PLAYER_HEAD).setSkullTexture(texture).getItemStack();
    }

    public ItemStack getSkull() {
        return itemStack;
    }
}