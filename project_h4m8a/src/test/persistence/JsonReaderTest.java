package persistence;

import model.Playlist;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderInvalidFile() {
        JsonReader reader = new JsonReader("./data/notPlaylist.json");
        try {
            Playlist playlist = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
        }
    }

    @Test
    public void testReaderNormal() {
        JsonReader reader = new JsonReader("./data/testReaderNormal.json");
        try {
            Playlist playlist = reader.read();
            assertEquals("My Playlist", playlist.getPlaylistName());
            assertEquals(3, playlist.getSongs().size());
            checkSong("eden's song", "eden", 100, "classical", playlist.getSong(0));
            checkSong("a song", "e", 1, "blues", playlist.getSong(1));
            checkSong("a title", "artist", 182, "rap", playlist.getSong(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderEmpty() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            Playlist playlist = reader.read();
            assertEquals("Empty Playlist", playlist.getPlaylistName());
            assertEquals(0, playlist.getNumSongs());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
