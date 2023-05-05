package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.spi.CalendarDataProvider;

class PlaylistTest {
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;
    private String message;

    @BeforeEach
    public void runBefore() {
        playlist = new Playlist("Eden's Playlist");
        song1 = new Song("Eden's Song", "Eden", 138, "Classical");
        song2 = new Song("My Song", "EK", 120, "Orchestral");
        song3 = new Song("A Song", "Eun", 60, "Hip-Hop");

        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        Date time = Calendar.getInstance().getTime();
        message = time + "\nEvent log cleared.\n\n";
    }

    @Test
    public void testConstructor() {
        assertEquals("Eden's Playlist", playlist.getPlaylistName());
    }

    @Test
    public void testShuffle() {
        EventLog.getInstance().clear();
        playlist.playNextSong();

        playlist.shuffle();

        Date time = Calendar.getInstance().getTime();
        assertEquals(0, playlist.getCounter());
        assertEquals(message + time + "\nShuffled Eden's Playlist\n\n", printLog(EventLog.getInstance()));
    }

    @Test
    public void testNowPlaying() {
        assertEquals(song1, playlist.nowPlaying());
    }

    @Test
    public void testPlayNextSong() {
        playlist.playNextSong();

        assertEquals(song2, playlist.nowPlaying());
    }

    @Test
    public void testPlayNextSongMultipleTimes() {
        playlist.playNextSong();
        playlist.playNextSong();

        assertEquals(song3, playlist.nowPlaying());
    }

    @Test
    public void testPlayNextSongAtEndOfPlaylist() {
        playlist.playNextSong();
        playlist.playNextSong();
        playlist.playNextSong();

        assertEquals(song1, playlist.nowPlaying());
        assertEquals(0, playlist.getCounter());
    }

    @Test
    public void testPlayNextSongOneSong() {
        Playlist playlist2 = new Playlist("Another Playlist");
        playlist2.addSong(song1);
        playlist2.playNextSong();

        assertEquals(song1, playlist2.nowPlaying());
        assertEquals(0, playlist2.getCounter());
    }

    @Test
    public void testPlayPrevSong() {
        playlist.playNextSong();
        playlist.playNextSong();
        playlist.playPrevSong();

        assertEquals(song2, playlist.nowPlaying());
    }

    @Test
    public void testPlayPrevSongFirstSongOfPlaylist() {
        playlist.playPrevSong();

        assertEquals(song1, playlist.nowPlaying());
        assertEquals(0, playlist.getCounter());
    }

    @Test
    public void testReplay() {
        playlist.playNextSong();
        playlist.replay();

        assertEquals(song1, playlist.nowPlaying());
        assertEquals(0, playlist.getCounter());
    }

    @Test
    public void testReplayFromEnd() {
        playlist.playNextSong();
        playlist.playNextSong();
        playlist.replay();

        assertEquals(song1, playlist.nowPlaying());
        assertEquals(0, playlist.getCounter());
    }

    @Test
    public void testGetSongs() {
        List<Song> songs = Arrays.asList(song1, song2, song3);

        assertEquals(songs, playlist.getSongs());
    }

    @Test
    public void testSongTitlesInPlaylist() {
        List<String> songList = Arrays.asList("Eden's Song", "My Song", "A Song");

        assertEquals(songList, playlist.songTitlesInPlaylist());
    }

    @Test
    public void testSongTitlesInPlaylistEmpty() {
        List<String> songList = new ArrayList<>();
        Playlist playlist2 = new Playlist("A Playlist");

        assertEquals(songList, playlist2.songTitlesInPlaylist());
    }

    @Test
    public void testSongTimesInPlaylist() {
        List<Integer> songTimes = Arrays.asList(138, 120, 60);

        assertEquals(songTimes, playlist.songTimesInPlaylist());
    }

    @Test
    public void testSongTimesInPlaylistEmpty() {
        List<Integer> songTimes = new ArrayList<>();
        Playlist playlist2 = new Playlist("A Playlist");

        assertEquals(songTimes, playlist2.songTimesInPlaylist());
    }

    @Test
    public void testArtistsInPlaylist() {
        List<String> artists = Arrays.asList("Eden", "EK", "Eun");

        assertEquals(artists, playlist.artistsInPlaylist());
    }

    @Test
    public void testArtistsInPlaylistEmpty() {
        List<String> artists = new ArrayList<>();
        Playlist playlist2 = new Playlist("A Playlist");

        assertEquals(artists, playlist2.artistsInPlaylist());
    }

    @Test
    public void testGenresInPlaylist() {
        List<String> genres = Arrays.asList("Classical", "Orchestral", "Hip-Hop");

        assertEquals(genres, playlist.genresInPlaylist());
    }

    @Test
    public void testGenresInPlaylistEmpty() {
        List<String> genres = new ArrayList<>();
        Playlist playlist2 = new Playlist("Empty");

        assertEquals(genres, playlist2.genresInPlaylist());
    }

    @Test
    public void testFindSongIndex() {
        assertEquals(1, playlist.findSongIndex("My Song"));
    }

    @Test
    public void testFindSongIndexNotInPlaylist() {
        assertEquals(-1, playlist.findSongIndex("edennn"));
    }

    @Test
    public void testAddSong() {
        EventLog.getInstance().clear();
        Song song4 = new Song("Title", "Artist", 1000, "Rock");
        playlist.addSong(song4);

        assertEquals(4, playlist.getNumSongs());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nAdded Title to Eden's Playlist\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testAddSongToEmptyPlaylist() {
        Song aSong = new Song("title", "artist", 123, "Rap");
        Playlist playlist2 = new Playlist("Empty Playlist");
        playlist2.addSong(aSong);

        assertEquals(1, playlist2.getNumSongs());
        assertTrue(playlist2.isInPlaylist("title"));
    }

    @Test
    public void testAddSameSong() {
        playlist.addSong(song2);
        List<String> songs = Arrays.asList("Eden's Song", "My Song", "A Song", "A Song");
        List<String> songs2 = Arrays.asList("Eden's Song", "My Song", "A Song");

        assertEquals(3, playlist.getNumSongs());
        assertNotEquals(songs, playlist.songTitlesInPlaylist());
        assertEquals(songs2, playlist.songTitlesInPlaylist());
    }

    @Test
    public void testAddSongJson() {
        Song song4 = new Song("Title", "Artist", 1000, "Rock");
        playlist.addSongJson(song4);

        assertEquals(4, playlist.getNumSongs());
    }

    @Test
    public void testAddSongJsonEmptyPlaylist() {
        Song aSong = new Song("title", "artist", 123, "Rap");
        Playlist playlist2 = new Playlist("Empty Playlist");
        playlist2.addSongJson(aSong);

        assertEquals(1, playlist2.getNumSongs());
        assertTrue(playlist2.isInPlaylist("title"));
    }

    @Test
    public void testAddSongJsonSameSong() {
        playlist.addSongJson(song2);
        List<String> songs = Arrays.asList("Eden's Song", "My Song", "A Song", "A Song");
        List<String> songs2 = Arrays.asList("Eden's Song", "My Song", "A Song");

        assertEquals(3, playlist.getNumSongs());
        assertNotEquals(songs, playlist.songTitlesInPlaylist());
        assertEquals(songs2, playlist.songTitlesInPlaylist());
    }

    @Test
    public void testRemoveSong() {
        EventLog.getInstance().clear();
        playlist.removeSong("My Song");

        assertEquals(2, playlist.getNumSongs());
        assertFalse(playlist.isInPlaylist("My Song"));
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nRemoved My Song from Eden's Playlist\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testGetSong() {
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testIsInPlaylistString() {
        assertTrue(playlist.isInPlaylist("Eden's Song"));
    }

    @Test
    public void testNotIsInPlaylistString() {
        assertFalse(playlist.isInPlaylist("The Best Song"));
    }

    @Test
    public void testIsInPlaylistSong() {
        assertTrue(playlist.isInPlaylist(song1));
    }

    @Test
    public void testNotIsInPlaylistSong() {
        Song song4 = new Song("ABC", "D", 1, "E");
        assertFalse(playlist.isInPlaylist(song4));
    }

    @Test
    public void testTotalTime() {
        assertEquals(318, playlist.totalTime());
    }

    @Test
    public void testTotalTimeEmpty() {
        Playlist playlist2 = new Playlist("empty playlist");

        assertEquals(0, playlist2.totalTime());
    }

    @Test
    public void testMoveSong() {
        List<String> listOfSongs = Arrays.asList("My Song", "Eden's Song", "A Song");
        playlist.moveSong(song1, 2);

        assertEquals(listOfSongs, playlist.songTitlesInPlaylist());
    }

    @Test
    public void testMoveSongPlaylistHasOneSong() {
        List<String> listOfSongs = new ArrayList<>();
        Playlist playlist2 = new Playlist("Playlist");
        Song newSong = new Song("song", "artist", 10, "Hip-Hop");

        listOfSongs.add("song");
        playlist2.addSong(newSong);
        playlist2.moveSong(newSong, 1);

        assertEquals(listOfSongs, playlist2.songTitlesInPlaylist());
    }

    @Test
    public void testReverse() {
        EventLog.getInstance().clear();
        List<String> reversePlaylist = Arrays.asList("A Song", "My Song", "Eden's Song");
        playlist.reverse();

        assertEquals(reversePlaylist, playlist.songTitlesInPlaylist());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nReversed playlist order\n\n", printLog(EventLog.getInstance()));
    }

    @Test
    public void testArrangeByTitle() {
        EventLog.getInstance().clear();
        List<String> titles = Arrays.asList("A Song", "Eden's Song", "My Song");
        playlist.arrangeByTitle();

        assertEquals(titles, playlist.songTitlesInPlaylist());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nArranged Eden's Playlist in alphabetical order of song titles\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testArrangeByTitleSameTitle() {
        List<String> artists = Arrays.asList("kim", "eden");
        List<String> titles = Arrays.asList("eden", "eden");
        Playlist newSongList = new Playlist("playlist 2");
        Song newSong1 = new Song("eden", "eden", 10, "Pop");
        Song newSong2 = new Song("eden", "kim", 10, "Pop");

        newSongList.addSong(newSong2);
        newSongList.addSong(newSong1);
        newSongList.arrangeByTitle();

        assertEquals(artists, newSongList.artistsInPlaylist());
        assertEquals(titles, newSongList.songTitlesInPlaylist());
    }

    @Test
    public void testArrangeByTime() {
        EventLog.getInstance().clear();
        List<Integer> times = Arrays.asList(3, 60, 120, 138);
        Song song4 = new Song("Song 4", "Eden", 3, "Orchestral");
        playlist.addSong(song4);
        playlist.arrangeByTime();

        assertEquals(times, playlist.songTimesInPlaylist());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nAdded Song 4 to Eden's Playlist\n\n"
                        + time + "\nArranged Eden's Playlist in ascending order of song durations\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testArrangeByTimeSameTime() {
        List<Integer> times = Arrays.asList(10, 10, 11);
        List<String> titles = Arrays.asList("Song 2", "Song 3", "Song 1");
        Playlist newSongList = new Playlist("playlist 2");
        Song newSong1 = new Song("Song 1", "eden", 11, "Pop");
        Song newSong2 = new Song("Song 2", "eden", 10, "Pop");
        Song newSong3 = new Song("Song 3", "eden", 10, "Pop");

        newSongList.addSong(newSong1);
        newSongList.addSong(newSong2);
        newSongList.addSong(newSong3);
        newSongList.arrangeByTime();

        assertEquals(times, newSongList.songTimesInPlaylist());
        assertEquals(titles, newSongList.songTitlesInPlaylist());
    }

    @Test
    public void testArrangeByArtist() {
        EventLog.getInstance().clear();
        List<String> artists = Arrays.asList("EK", "Eden", "Eun");
        playlist.arrangeByArtist();

        assertEquals(artists, playlist.artistsInPlaylist());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nArranged Eden's Playlist in alphabetical order of artist names\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testArrangeByArtistSameName() {
        List<String> titles = Arrays.asList("song", "Song");
        List<String> artists = Arrays.asList("eden", "eden");
        Playlist newSongList = new Playlist("playlist2");
        Song newSong1 = new Song("Song", "eden", 10, "genre");
        Song newSong2 = new Song("song", "eden", 111, "genre");

        newSongList.addSong(newSong2);
        newSongList.addSong(newSong1);
        newSongList.arrangeByArtist();

        assertEquals(titles, newSongList.songTitlesInPlaylist());
        assertEquals(artists, newSongList.artistsInPlaylist());
    }

    @Test
    public void testArrangeByGenre() {
        EventLog.getInstance().clear();
        List<String> genres = Arrays.asList("Classical", "Hip-Hop", "Orchestral");
        playlist.arrangeByGenre();

        assertEquals(genres, playlist.genresInPlaylist());
        Date time = Calendar.getInstance().getTime();
        assertEquals(message + time + "\nArranged Eden's Playlist in alphabetical order of genres\n\n",
                printLog(EventLog.getInstance()));
    }

    @Test
    public void testArrangeByGenreSameGenre() {
        List<String> genres = Arrays.asList("Pop", "Pop");
        List<String> titles = Arrays.asList("b", "a");
        Playlist testPlaylist = new Playlist("playlist");
        Song song1 = new Song("a", "eden", 100, "Pop");
        Song song2 = new Song("b", "eden", 100, "Pop");

        testPlaylist.addSong(song2);
        testPlaylist.addSong(song1);
        testPlaylist.arrangeByGenre();

        assertEquals(genres, testPlaylist.genresInPlaylist());
        assertEquals(titles, testPlaylist.songTitlesInPlaylist());
    }

    @Test
    public void testToString() {
        assertEquals("Eden's Playlist has 3 song(s)\nSongs:Eden's SongMy SongA Song", playlist.toString());
    }


    /*
     * EFFECTS: helper method that prints out events in the event log
     */
    private String printLog(EventLog el) {
        String message = "";
        for (Event next : el) {
            message += next.toString() + "\n\n";
        }
        return message;
    }
}