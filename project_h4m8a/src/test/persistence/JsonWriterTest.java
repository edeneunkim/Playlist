package persistence;

import model.Song;
import model.Playlist;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            Playlist playlist = new Playlist("Test Playlist");
            JsonWriter writer = new JsonWriter("./data/illegal\0playlist.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
        }
    }

    @Test
    public void testWriterNormal() {
        try {
           JsonWriter writer = new JsonWriter("./data/testWriterNormal.json");
           Playlist playlist = new Playlist("A Playlist");
           Song song1 = new Song("Song 1", "eden", 302, "Orchestral");
           Song song2 = new Song("Song 2", "Eun", 196, "Classical");
           playlist.addSong(song1);
           playlist.addSong(song2);
           writer.open();
           writer.write(playlist);
           writer.close();

           JsonReader reader = new JsonReader("./data/testWriterNormal.json");
           playlist = reader.read();

           assertEquals("A Playlist", playlist.getPlaylistName());
           assertEquals(2, playlist.getNumSongs());
           checkSong("Song 1", "eden", 302, "Orchestral", playlist.getSong(0));
           checkSong("Song 2", "Eun", 196, "Classical", playlist.getSong(1));
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriterEmpty() {
        try {
            Playlist playlist = new Playlist("Playlist");
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(playlist);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            playlist = reader.read();

            assertEquals("Playlist", playlist.getPlaylistName());
            assertEquals(0, playlist.getNumSongs());
        } catch(IOException e) {
            fail("Exception not expected");
        }
    }
}
