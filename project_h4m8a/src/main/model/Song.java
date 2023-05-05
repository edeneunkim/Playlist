package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// Represents a song with a title, artist, length of the song, and genre of the song
public class Song implements Writable {
    private String title;   // title of the song
    private String artist;  // artist of the song
    private int time;    // how long the song is
    private String genre;   // genre of the song

    /*
     * REQUIRES: title has a non-zero length; artist has a non-zero length;
     *           time has a non-zero value; genre has a non-zero length
     * EFFECTS: title of Song is set to title; artist of Song is set to artist;
     *          time of Song is set to time in seconds; genre of Song is set to genre
     */
    public Song(String title, String artist, int time, String genre) {
        this.title = title;
        this.artist = artist;
        this.time = time;
        this.genre = genre;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public int getTime() {
        return this.time;
    }

    public String getGenre() {
        return this.genre;
    }

    /*
     * EFFECTS: returns a string representation of Song
     */
    @Override
    public String toString() {
        return this.title;
    }


    /*
     * EFFECTS: returns a string representation of Song using breaks that appear only for GUI
     */
    public String guiString() {
        String line = "Title: " + this.title + "<br/>Artist: " + this.artist
                + "<br/>Duration: " + this.time + " seconds<br/>Genre: " + this.genre;
        return "<html>" + line + "<html>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return time == song.time && title.equals(song.title) && artist.equals(song.artist) && genre.equals(song.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, time, genre);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", this.title);
        json.put("artist", this.artist);
        json.put("duration", this.time);
        json.put("genre", this.genre);
        return json;
    }

}
