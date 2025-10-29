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
package se.file14.procosmetics.api.cosmetic.music;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import se.file14.procosmetics.api.cosmetic.CosmeticType;

/**
 * Represents a type of music cosmetic.
 */
public interface MusicType extends CosmeticType<MusicType, MusicBehavior> {

    /**
     * Gets the song associated with this music type.
     * The song contains the musical data that will be played when this cosmetic is active.
     *
     * @return the song to be played
     */
    Song getSong();

    /**
     * Builder interface for constructing music type instances.
     */
    interface Builder extends CosmeticType.Builder<MusicType, MusicBehavior, Builder> {

        /**
         * Sets the song to be played by this music cosmetic.
         *
         * @param song the song from NoteBlockAPI
         * @return this builder for method chaining
         */
        Builder song(Song song);

        /**
         * Builds and returns the configured music type instance.
         *
         * @return the built music type
         */
        @Override
        MusicType build();
    }
}
