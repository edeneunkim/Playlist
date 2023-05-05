package persistence;

import model.Song;
import model.Playlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkSong(String title, String artist, int duration, String genre, Song song) {
        assertEquals(title, song.getTitle());
        assertEquals(artist, song.getArtist());
        assertEquals(duration, song.getTime());
        assertEquals(genre, song.getGenre());
    }
}
