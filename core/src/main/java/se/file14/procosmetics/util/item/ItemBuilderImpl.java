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
package se.file14.procosmetics.util.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerProfile;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.config.Config;
import se.file14.procosmetics.api.util.item.ItemBuilder;
import se.file14.procosmetics.util.LogUtil;
import se.file14.procosmetics.util.mapping.Mapping;
import se.file14.procosmetics.util.mapping.MappingType;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ItemBuilderImpl implements ItemBuilder {

    private static final String TEXTURE_URL = "https://textures.minecraft.net/texture/%s";

    public static final ItemFlag[] ITEM_FLAGS = Arrays.stream(ItemFlag.values())
            .filter(flag -> Mapping.MAPPING_TYPE == MappingType.SPIGOT && flag != ItemFlag.HIDE_LORE)
            .toArray(ItemFlag[]::new);

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final ProCosmeticsPlugin PLUGIN = ProCosmeticsPlugin.getPlugin();

    private final ItemStack itemStack;
    private int slot;

    public ItemBuilderImpl(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilderImpl(ItemStack itemStack, int slot) {
        this.itemStack = itemStack.clone();
        this.slot = slot;
    }

    public ItemBuilderImpl(Material material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemBuilderImpl(Material material, int amount) {
        this.itemStack = new ItemStack(material, Math.max(1, amount));
    }

    public ItemBuilderImpl(Config config, String path) {
        String item = config.getString(path + ".item");
        ItemStack itemStack = new ItemStack(Material.BARRIER);

        try {
            itemStack = PLUGIN.getServer().getItemFactory().createItemStack(item);
        } catch (IllegalArgumentException e) {
            LogUtil.log("Failed to parse item " + item + " [Path: " + path + " Config: " + config.getFile().getName() + "]. This appears to be an issue with YOUR configuration. Delete the " + config.getFile().getName() + " file and RESTART your server before requesting support.");
        }
        this.itemStack = itemStack;
        this.slot = config.getInt(path + ".slot", false);
        int count = config.getInt(path + ".count", false);

        if (count > 0) {
            setAmount(count);
        }
    }

    public ItemBuilderImpl(String itemData) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);

        try {
            itemStack = PLUGIN.getServer().getItemFactory().createItemStack(itemData);
        } catch (IllegalArgumentException e) {
            LogUtil.log("Failed to parse item " + itemData + " This appears to be an issue with the provided item data. Check the item string format.");
        }
        this.itemStack = itemStack;
        this.slot = -1;
    }

    public static ItemBuilderImpl of(ItemStack itemStack) {
        return new ItemBuilderImpl(itemStack);
    }

    public static ItemBuilderImpl of(Material material) {
        return new ItemBuilderImpl(material);
    }

    public static ItemBuilderImpl of(Material material, int amount) {
        return new ItemBuilderImpl(material, amount);
    }

    @Override
    public ItemBuilderImpl setType(Material material) {
        itemStack.setType(material);
        return this;
    }

    @Override
    public ItemBuilderImpl setAmount(int amount) {
        itemStack.setAmount(Math.max(1, amount));
        return this;
    }

    @Override
    public <T, Z> ItemBuilderImpl setCustomData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        return modifyItemMeta(meta -> {
            meta.getPersistentDataContainer().set(key, type, value);
        });
    }

    @Override
    public ItemBuilderImpl setDisplayName(@Nullable Component displayName) {
        return modifyItemMeta(meta -> {
            if (displayName != null) {
                meta.setItemName(SERIALIZER.serialize(displayName));
            }
        });
    }

    @Override
    public Component getDisplayName() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            return SERIALIZER.deserialize(itemMeta.getDisplayName());
        }
        return Component.empty();
    }

    @Override
    public ItemBuilderImpl setLore(@Nullable List<Component> lines) {
        return modifyItemMeta(meta -> {
            if (lines != null && !lines.isEmpty()) {
                List<String> processedLore = new ArrayList<>();

                for (Component component : lines) {
                    processedLore.add(SERIALIZER.serialize(component));
                }
                meta.setLore(processedLore);
            } else {
                meta.setLore(null);
            }
        });
    }

    @Override
    public ItemBuilderImpl addLore(Component line) {
        List<Component> currentLore = new ArrayList<>(getLore());
        currentLore.add(line);
        return setLore(currentLore);
    }

    @Override
    public ItemBuilderImpl addLore(List<Component> lines) {
        List<Component> currentLore = new ArrayList<>(getLore());
        currentLore.addAll(lines);
        return setLore(currentLore);
    }

    private List<String> getLoreList() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            return lore != null ? new ArrayList<>(lore) : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Component> getLore() {
        List<String> lore = getLoreList();

        if (!lore.isEmpty()) {
            List<Component> components = new ArrayList<>();
            for (String line : lore) {
                components.add(SERIALIZER.deserialize(line));
            }
            return components;
        }
        return Collections.emptyList();
    }

    @Override
    public ItemBuilderImpl setUnbreakable() {
        return setUnbreakable(true);
    }

    @Override
    public ItemBuilderImpl setUnbreakable(boolean unbreakable) {
        return modifyItemMeta(meta -> {
            meta.setUnbreakable(unbreakable);

            if (unbreakable) {
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            } else {
                meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
        });
    }

    @Override
    public ItemBuilderImpl setMaxSize(int amount) {
        return modifyItemMeta(meta -> {
            meta.setMaxStackSize(amount);
        });
    }

    @Override
    public ItemBuilderImpl setGlintOverride(boolean override) {
        return modifyItemMeta(meta -> meta.setEnchantmentGlintOverride(override));
    }

    @Override
    public ItemBuilderImpl addEnchant(Enchantment enchantment, int level) {
        return addEnchant(enchantment, level, true);
    }

    @Override
    public ItemBuilderImpl addEnchant(Enchantment enchantment, int level, boolean unsafe) {
        return modifyItemMeta(meta -> meta.addEnchant(enchantment, level, unsafe));
    }

    @Override
    public ItemBuilderImpl removeEnchant(Enchantment enchantment) {
        return modifyItemMeta(meta -> meta.removeEnchant(enchantment));
    }

    @Override
    public ItemBuilderImpl setLeatherArmorColor(Color color) {
        return modifyItemMeta(meta -> {
            if (meta instanceof LeatherArmorMeta leatherMeta) {
                leatherMeta.setColor(color);
            }
        });
    }

    @Override
    public ItemBuilderImpl addPotionEffect(PotionEffect effect, boolean overwrite) {
        return modifyItemMeta(meta -> {
            if (meta instanceof PotionMeta potionMeta) {
                potionMeta.addCustomEffect(effect, overwrite);
            }
        });
    }

    @Override
    public ItemBuilderImpl setSkullOwner(Player player) {
        return modifyItemMeta(meta -> {
            if (meta instanceof SkullMeta skullMeta) {
                skullMeta.setOwnerProfile(player.getPlayerProfile());
            }
        });
    }

    @Override
    public ItemBuilderImpl setSkullTexture(String texture) {
        if (texture == null || texture.isBlank()) {
            return this;
        }
        return modifyItemMeta(meta -> {
            if (meta instanceof SkullMeta skullMeta) {
                final String finalTexture = String.format(TEXTURE_URL, texture);

                PlayerProfile profile = PLUGIN.getJavaPlugin().getServer().createPlayerProfile(
                        UUID.nameUUIDFromBytes(finalTexture.getBytes()), ""
                );

                try {
                    profile.getTextures().setSkin(URI.create(finalTexture).toURL());
                } catch (MalformedURLException e) {
                    PLUGIN.getLogger().log(Level.WARNING, "Invalid skin URL.", e);
                }
                skullMeta.setOwnerProfile(profile);
            }
        });
    }

    @Override
    public ItemBuilderImpl addFlags(ItemFlag... flags) {
        return modifyItemMeta(meta -> meta.addItemFlags(flags));
    }

    @Override
    public ItemBuilderImpl removeFlags(ItemFlag... flags) {
        return modifyItemMeta(meta -> meta.removeItemFlags(flags));
    }

    @Override
    public ItemBuilderImpl hide() {
        return addFlags(ITEM_FLAGS);
    }

    @Override
    public ItemBuilderImpl modifyItemMeta(Consumer<ItemMeta> modifier) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            modifier.accept(itemMeta);
            itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilderImpl modifyItemStack(Consumer<ItemStack> modifier) {
        modifier.accept(itemStack);
        return this;
    }

    @Override
    public boolean hasEnchant(Enchantment enchantment) {
        return itemStack.containsEnchantment(enchantment);
    }

    @Override
    public int getEnchantLevel(Enchantment enchantment) {
        return itemStack.getEnchantmentLevel(enchantment);
    }

    @Override
    public boolean hasEnchants() {
        return !itemStack.getEnchantments().isEmpty();
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return new HashMap<>(itemStack.getEnchantments());
    }

    @Override
    public Material getType() {
        return itemStack.getType();
    }

    @Override
    public int getAmount() {
        return itemStack.getAmount();
    }

    @Override
    public boolean hasDisplayName() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null && meta.hasDisplayName();
    }

    @Override
    public boolean hasLore() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null && meta.hasLore();
    }

    @Override
    public boolean isUnbreakable() {
        ItemMeta meta = itemStack.getItemMeta();
        return meta != null && meta.isUnbreakable();
    }

    @Override
    public String toString() {
        return "ItemBuilder{" +
                "type=" + itemStack.getType() +
                ", amount=" + itemStack.getAmount() +
                ", displayName='" + getDisplayName() + '\'' +
                '}';
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }
}
