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
