package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    private Song song;

    @BeforeEach
    public void runBefore() {
        song = new Song("Eden's Song", "Eden", 60, "Classical");
    }

    @Test
    public void testConstructor() {
        assertEquals("Eden's Song", song.getTitle());
        assertEquals("Eden", song.getArtist());
        assertEquals(60, song.getTime());
        assertEquals("Classical", song.getGenre());
    }

    @Test
    public void testToString() {
        assertEquals("Eden's Song", song.toString());
    }

    @Test
    public void testGuiString() {
        assertEquals("<html>Title: Eden's Song<br/>Artist: Eden<br/>Duration: " +
                        "60 seconds<br/>Genre: Classical<html>", song.guiString());
    }

    @Test
    public void testHashCode() {
        Song testSong = new Song("Eden's Song", "Eden", 60, "Classical");
        assertEquals(song.hashCode(), testSong.hashCode());
    }

    @Test
    public void testEquals() {
        Song testSong = new Song("Eden's Song", "Eden", 60, "Classical");
        assertEquals(testSong, song);
        assertEquals(testSong.getTitle(), song.getTitle());
        assertEquals(testSong.getArtist(), song.getArtist());
        assertEquals(testSong.getTime(), song.getTime());
        assertEquals(testSong.getGenre(), song.getGenre());
        assertNotNull(testSong);
        assertNotNull(song);
        assertEquals(testSong.getClass(), song.getClass());
    }

    @Test
    public void testNotEqualsDifferentArtist() {
        Song testSong =  new Song("Eden's Song", "EK", 60, "Classical");
        assertNotEquals(testSong, song);
    }

    @Test
    public void testNotEqualsDifferentTitle() {
        Song testSong =  new Song("A Song", "Eden", 60, "Classical");
        assertNotEquals(testSong, song);
    }

    @Test
    public void testNotEqualsDifferentTime() {
        Song testSong =  new Song("Eden's Song", "Eden", 61, "Classical");
        assertNotEquals(testSong, song);
    }

    @Test
    public void testNotEqualsDifferentGenre() {
        Song testSong =  new Song("Eden's Song", "Eden", 60, "Orchestral");
        assertNotEquals(testSong, song);
    }


    @Test
    public void testNotEqualsNull() {
        Song testSong = null;
        assertNotEquals(testSong, song);
        assertFalse(song.equals(testSong));
    }

    @Test
    public void testNotEqualsNullWrongClass() {
        Playlist testPlaylist = null;
        assertNotEquals(testPlaylist, song);
    }

    @Test
    public void testNotEqualsWrongClass() {
        String testSong = "Title: Eden's Song";
        assertNotEquals(testSong, song);
        assertFalse(song.equals(testSong));
    }

    @Test
    public void testNotEquals() {
        Song testSong = new Song("Song", "Eden", 60, "Classical");
        assertNotEquals(testSong, song);
        assertFalse(song.equals(testSong));
    }

}
