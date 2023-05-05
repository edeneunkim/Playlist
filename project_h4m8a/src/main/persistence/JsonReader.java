package persistence;

import model.Song;
import model.Playlist;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads playlist from JSON data stored in file
// Modeled from JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonReader {
    private String source;

    // EFFECTS: creates a reader that reads from source file
    public JsonReader(String source) {
        this.source = source;
    }

    /*
     * EFFECTS: reads playlist data from source file and returns it;
     *          throws IOException if an error occurs while reading
     */
    public Playlist read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePlaylist(jsonObject);
    }

    // EFFECTS: reads source file as String and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses playlist data from JSON object and returns it
    private Playlist parsePlaylist(JSONObject jsonObject) {
        String name = jsonObject.getString("Name");
        Playlist playlist = new Playlist(name);
        addSongs(playlist, jsonObject);
        return playlist;
    }

    /*
     * MODIFIES: playlist
     * EFFECTS: parses songs from JSON object and adds them to playlist
     */
    private void addSongs(Playlist playlist, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Songs");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSong(playlist, nextSong);
        }
    }

    /*
     * MODIFIES: playlist
     * EFFECTS: adds song from JSON object to playlist
     */
    private void addSong(Playlist playlist, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String artist = jsonObject.getString("artist");
        int time = jsonObject.getInt("duration");
        String genre = jsonObject.getString("genre");
        Song song = new Song(title, artist, time, genre);
        playlist.addSongJson(song);
    }
}
