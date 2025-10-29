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
package se.file14.procosmetics.cosmetic.music;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import se.file14.procosmetics.ProCosmeticsPlugin;
import se.file14.procosmetics.api.cosmetic.CosmeticRarity;
import se.file14.procosmetics.api.cosmetic.music.Music;
import se.file14.procosmetics.api.cosmetic.music.MusicBehavior;
import se.file14.procosmetics.api.cosmetic.music.MusicType;
import se.file14.procosmetics.api.cosmetic.registry.CosmeticCategory;
import se.file14.procosmetics.api.user.User;
import se.file14.procosmetics.cosmetic.CosmeticTypeImpl;

import java.io.File;
import java.util.function.Supplier;
import java.util.logging.Level;

public class MusicTypeImpl extends CosmeticTypeImpl<MusicType, MusicBehavior> implements MusicType {

    private final Song song;

    public MusicTypeImpl(String key,
                         CosmeticCategory<MusicType, MusicBehavior, ?> category,
                         Supplier<MusicBehavior> behaviorFactory,
                         boolean enabled,
                         boolean findable,
                         boolean purchasable,
                         int cost,
                         CosmeticRarity rarity,
                         ItemStack itemStack,
                         Song song) {
        super(key, category, behaviorFactory, enabled, findable, purchasable, cost, rarity, itemStack);
        this.song = song;
    }

    @Override
    protected Music createInstance(ProCosmeticsPlugin plugin, User user, MusicBehavior behavior) {
        return new MusicImpl(plugin, user, this, behavior);
    }

    @Override
    public @Nullable Song getSong() {
        return song;
    }

    public static class BuilderImpl extends CosmeticTypeImpl.BuilderImpl<MusicType, MusicBehavior, MusicType.Builder> implements MusicType.Builder {

        private Song song;

        public BuilderImpl(String key, CosmeticCategory<MusicType, MusicBehavior, ?> category) {
            super(key, category);
        }

        @Override
        protected MusicType.Builder self() {
            return this;
        }

        @Override
        public MusicType.Builder readFromConfig() {
            super.readFromConfig();
            loadSong();
            return this;
        }

        private void loadSong() {
            File file = PLUGIN.getDataFolder().toPath().resolve("songs").resolve(key + ".nbs").toFile();

            if (!file.exists()) {
                PLUGIN.getLogger().log(Level.WARNING, "Song file not found: " + file.getName()
                        + ". Ensure the file exists in the songs folder (case-sensitive).");
                return;
            }
            song = NBSDecoder.parse(file);
        }

        @Override
        public MusicType.Builder song(Song song) {
            this.song = song;
            return this;
        }

        @Override
        public MusicType build() {
            return new MusicTypeImpl(key,
                    category,
                    factory,
                    enabled,
                    findable,
                    purchasable,
                    cost,
                    rarity,
                    itemStack,
                    song
            );
        }
    }
}
