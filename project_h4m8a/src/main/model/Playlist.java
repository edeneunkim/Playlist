package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;


// Represents a playlist with a name and no songs
public class Playlist implements Writable {
    private List<Song> songs = new ArrayList<>();   // a list of songs in the playlist
    private String name;    // name of the playlist
    private int counter = 0;    // the index of the current song

    /*
     * REQUIRES: name has a non-zero length
     * EFFECTS: name of Playlist is set to name; numSongs of Playlist is set to 0;
     */
    public Playlist(String name) {
        this.name = name;
    }

    /*
     * REQUIRES: Playlist object has non-zero size
     * MODIFIES: this
     * EFFECTS: order of songs in Playlist is shuffled
     */
    public void shuffle() {
        Collections.shuffle(this.songs);
        this.counter = 0;
        EventLog.getInstance().logEvent(new Event("Shuffled " + this.name));
    }

    /*
     * REQUIRES: Playlist object has non-zero size
     * MODIFIES: this
     * EFFECTS: returns the Song object that is currently playing
     */
    public Song nowPlaying() {
        return songs.get(this.counter);
    }

    /*
     * REQUIRES: Playlist object is not empty
     * MODIFIES: this
     * EFFECTS: plays the next song and returns that Song object or
     *          plays the current song if there is only one Song object
     *          in the playlist; if the current song is the last song in
     *          the playlist, it will play the first song in the playlist
     */
    public void playNextSong() {
        if (this.counter < getNumSongs() - 1) {
            this.counter += 1;
        } else {
            this.counter = 0;
        }
    }

    /*
     * REQUIRES: Playlist object is not empty
     * MODIFIES: this
     * EFFECTS: plays the previous song and returns that Song object or
     *          plays the current song if there is only one Song object
     *          in the playlist; if the current song is the first song
     *          in the playlist, it will play the current song
     */
    public void playPrevSong() {
        if (this.counter != 0) {
            this.counter -= 1;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: restarts the playlist by going back to the first song
     *          in the playlist
     */
    public void replay() {
        this.counter = 0;
    }

    /*
     * EFFECTS: returns the titles of the songs in the playlist
     */
    public List<String> songTitlesInPlaylist() {
        List<String> songTitles = new ArrayList<>();
        for (Song song : this.songs) {
            songTitles.add(song.getTitle());
        }
        return songTitles;
    }

    /*
     * EFFECTS: returns the times of the songs in the playlist
     */
    public List<Integer> songTimesInPlaylist() {
        List<Integer> songTimes = new ArrayList<>();
        for (Song song : this.songs) {
            songTimes.add(song.getTime());
        }
        return songTimes;
    }

    /*
     * EFFECTS: returns the artists in the playlist
     */
    public List<String> artistsInPlaylist() {
        List<String> artists = new ArrayList<>();
        for (Song song : this.songs) {
            artists.add(song.getArtist());
        }
        return artists;
    }

    /*
     * EFFECTS: returns the genres of the songs in the playlist
     */
    public List<String> genresInPlaylist() {
        List<String> genres = new ArrayList<>();
        for (Song song : this.songs) {
            genres.add(song.getGenre());
        }
        return genres;
    }

    /*
     * REQUIRES: songTitle must be a non-zero length;
     * EFFECTS: returns the index of the song with the title songTitle;
     *          if there is no song with the title songTitle, returns -1
     */
    public int findSongIndex(String songTitle) {
        if (isInPlaylist(songTitle)) {
            return songTitlesInPlaylist().indexOf(songTitle);
        } else {
            return -1;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds song to the playlist; does not add duplicates
     */
    public void addSong(Song song) {
        if (!this.songs.contains(song)) {
            this.songs.add(song);
            EventLog.getInstance().logEvent(new Event("Added " + song.getTitle() + " to " + this.name));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds song to the playlist; does not add duplicates; used only when loading playlist from file
     */
    public void addSongJson(Song song) {
        if (!this.songs.contains(song)) {
            this.songs.add(song);
        }
    }

    /*
     * REQUIRES: Playlist is not empty
     * MODIFIES: this
     * EFFECTS: removes song from the playlist based on the
     *          title of the song and restarts the playlist
     */
    public void removeSong(String songTitle) {
        for (Song song : this.songs) {
            if (song.getTitle().equals(songTitle)) {
                this.songs.remove(song);
                EventLog.getInstance().logEvent(new Event("Removed " + songTitle + " from " + this.name));
                break;
            }
        }
        replay();
    }

    /*
     * EFFECTS: returns true if a song with title songTitle is in Playlist;
     *          false otherwise
     */
    public boolean isInPlaylist(String songTitle) {
        return songTitlesInPlaylist().contains(songTitle);
    }

    /*
     * EFFECTS: returns true if a song is in playlist; false otherwise
     */
    public boolean isInPlaylist(Song song) {
        return this.songs.contains(song);
    }

    /*
     * EFFECTS: returns the total running time of the playlist
     */
    public double totalTime() {
        double total = 0;
        for (Song song : this.songs) {
            total += song.getTime();
        }
        return total;
    }

    /*
     * REQUIRES: moveToIndex >= 1 and moveToIndex <= this.songs.size() + 1
     * MODIFIES: this
     * EFFECTS: moves the song to the index moveToIndex of the playlist
     *          (starting from index 1) and resets the playlist
     */
    public void moveSong(Song song, int moveToIndex) {
        removeSong(song.getTitle());
        this.songs.add(moveToIndex - 1, song);
        replay();
    }

    /*
     * REQUIRES: playlist has at least one song
     * MODIFIES: this
     * EFFECTS: arranges the playlist by alphabetical order of the titles
     */
    public void arrangeByTitle() {
        this.songs = arrangeByTitleHelper();
        EventLog.getInstance().logEvent(new Event("Arranged " + this.name
                + " in alphabetical order of song titles"));
    }

    /*
     * EFFECTS: returns a list of Song objects in alphabetical order based on the titles
     */
    public List<Song> arrangeByTitleHelper() {
        List<String> tempSongTitles = songTitlesInPlaylist();
        List<Song> newSongList = new ArrayList<>();
        Collections.sort(tempSongTitles);

        for (int songIndex1 = 0; songIndex1 < getNumSongs(); songIndex1++) {
            for (int songIndex2 = 0; songIndex2 < getNumSongs(); songIndex2++) {
                if (Objects.equals(this.songs.get(songIndex2).getTitle(),
                        tempSongTitles.get(songIndex1))) {
                    if (!newSongList.contains(this.songs.get(songIndex2))) {
                        newSongList.add(this.songs.get(songIndex2));
                    }
                }
            }
        }
        return newSongList;
    }

    /*
     * REQUIRES: playlist has at least one song
     * MODIFIES: this
     * EFFECTS: arranges the playlist by ascending order of playtime
     *          of the songs
     */
    public void arrangeByTime() {
        this.songs = arrangeByTimeHelper();
        EventLog.getInstance().logEvent(new Event("Arranged " + this.name
                + " in ascending order of song durations"));
    }

    /*
     * EFFECTS: returns a list of Song objects listed in ascending order
     *          of their playtime; if there are two or more songs with the same
     *          playtime, then songs are listed based on order of being added to
     *          the playlist
     */
    public List<Song> arrangeByTimeHelper() {
        List<Integer> tempSongTimes = songTimesInPlaylist();
        List<Song> newSongList = new ArrayList<>();
        Collections.sort(tempSongTimes);

        for (int songIndex1 = 0; songIndex1 < getNumSongs(); songIndex1++) {
            for (int songIndex2 = 0; songIndex2 < getNumSongs(); songIndex2++) {
                if (this.songs.get(songIndex2).getTime() == tempSongTimes.get(songIndex1)) {
                    if (!newSongList.contains(this.songs.get(songIndex2))) {
                        newSongList.add(this.songs.get(songIndex2));
                    }
                }
            }
        }
        return newSongList;
    }

    /*
     * REQUIRES: playlist has at least one song
     * MODIFIES: this
     * EFFECTS: arranges the playlist by alphabetical order of the artist name
     */
    public void arrangeByArtist() {
        this.songs = arrangeByArtistHelper();
        EventLog.getInstance().logEvent(new Event("Arranged " + this.name
                + " in alphabetical order of artist names"));
    }

    /*
     * EFFECTS: returns a list of Song objects in alphabetical order based on the
     *          artist name
     */
    public List<Song> arrangeByArtistHelper() {
        List<String> tempArtists = artistsInPlaylist();
        List<Song> newSongList = new ArrayList<>();
        Collections.sort(tempArtists);

        for (int songIndex1 = 0; songIndex1 < getNumSongs(); songIndex1++) {
            for (int songIndex2 = 0; songIndex2 < getNumSongs(); songIndex2++) {
                if (Objects.equals(this.songs.get(songIndex2).getArtist(),
                        tempArtists.get(songIndex1))) {
                    if (!newSongList.contains(this.songs.get(songIndex2))) {
                        newSongList.add(this.songs.get(songIndex2));
                    }
                }
            }
        }
        return newSongList;
    }

    /*
     * REQUIRES: playlist has at least one song
     * MODIFIES: this
     * EFFECTS: arranges the playlist by alphabetical order of the genre
     */
    public void arrangeByGenre() {
        this.songs = arrangeByGenreHelper();
        EventLog.getInstance().logEvent(new Event("Arranged " + this.name
                + " in alphabetical order of genres"));
    }

    /*
     * EFFECTS: returns a list of Song objects in alphabetical order based on the genre
     */
    public List<Song> arrangeByGenreHelper() {
        List<String> tempGenres = genresInPlaylist();
        List<Song> newSongList = new ArrayList<>();
        Collections.sort(tempGenres);

        for (int songIndex1 = 0; songIndex1 < getNumSongs(); songIndex1++) {
            for (int songIndex2 = 0; songIndex2 < getNumSongs(); songIndex2++) {
                if (Objects.equals(this.songs.get(songIndex2).getGenre(),
                        tempGenres.get(songIndex1))) {
                    if (!newSongList.contains(this.songs.get(songIndex2))) {
                        newSongList.add(this.songs.get(songIndex2));
                    }
                }
            }
        }
        return newSongList;
    }

    /*
     * MODIFIES: this
     * EFFECTS: reverses the playlist order
     */
    public void reverse() {
        Collections.reverse(this.songs);
        EventLog.getInstance().logEvent(new Event("Reversed playlist order"));
    }

    public String getPlaylistName() {
        return this.name;
    }

    public int getCounter() {
        return this.counter;
    }

    public int getNumSongs() {
        return this.songs.size();
    }

    public Song getSong(int songIndex) {
        return this.songs.get(songIndex);
    }

    // EFFECTS: returns all the songs in the playlist
    public List<Song> getSongs() {
        return this.songs;
    }

    // EFFECTS: returns a string representation of Playlist
    @Override
    public String toString() {
        String songsString = "";
        for (Song song : this.songs) {
            songsString += song.toString();
        }
        return this.name + " has " + getNumSongs() + " song(s)" + "\nSongs:" + songsString;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Name", this.name);
        json.put("Songs", songsToJson());
        return json;
    }

    // EFFECTS: returns songs in playlist as a JSON array
    private JSONArray songsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Song song : this.songs) {
            jsonArray.put(song.toJson());
        }

        return jsonArray;
    }
}
