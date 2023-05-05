package persistence;

import model.Playlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.*;

// Represents a writer that writes and stores playlist data to file as JSON data
// Modeled from JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonWriter {
    private static final int INDENT = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: creates a writer that writes to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /*
     * MODIFIES: this
     * EFFECTS: opens writer; throws FileNotFoundException if destination file
     *          cannot be opened
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    /*
     * MODIFIES: this
     * EFFECTS: writes playlist data in JSON format to destination file
     */
    public void write(Playlist playlist) {
        JSONObject json = playlist.toJson();
        saveToFile(json.toString(INDENT));
    }

    /*
     * MODIFIES: this
     * EFFECTS: closes writer
     */
    public void close() {
        writer.close();
    }

    /*
     * MODIFIES: this
     * EFFECTS: writes string to file
     */
    private void saveToFile(String json) {
        writer.print(json);
    }

}
