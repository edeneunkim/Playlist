package ui;

import model.Playlist;
import model.Song;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//Playlist application
public class PlaylistApp {
    private static final String JSON_STORE = "./data/playlist.json";
    private Playlist playlist;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: initializes Playlist and runs the Playlist application
    public PlaylistApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        init();

        runPlaylist();
    }

    private void init() {
        System.out.println("Load");
        System.out.println("New");

        String choice = input.next();

        if (choice.equalsIgnoreCase("new")) {
            System.out.println("Playlist Name: ");
            String name = input.next();
            playlist = new Playlist(name);
        } else if (choice.equalsIgnoreCase("load")) {
            loadPlaylist();
        } else {
            System.out.println("Invalid choice");
            init();
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runPlaylist() {
        boolean run = true;
        String command = null;

        while (run) {
            playlistMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                System.out.println("Would you like to save?");
                System.out.println("Y\tN");
                command = input.next();
                if (command.equalsIgnoreCase("y")) {
                    savePlaylist();
                }
                run = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Playlist " + playlist.getPlaylistName() + " has terminated");
    }

    // EFFECTS: shows menu of options to user
    private void playlistMenu() {
        System.out.println("\na -> Add a song");
        System.out.println("r -> Remove a song");
        System.out.println("s -> Shuffle playlist");
        System.out.println("f -> Find a song");
        System.out.println("title -> Arrange by alphabet order of song titles");
        System.out.println("time -> Arrange by ascending order of song playtime");
        System.out.println("artist -> Arrange by alphabet order of artist name");
        System.out.println("genre -> Arrange by alphabet order of genre");
        System.out.println("reverse -> Reverse the playlist order");
        System.out.println("show -> Show all songs in playlist");
        System.out.println("save -> Save playlist to file");
        System.out.println("load -> Load playlist from file");
        System.out.println("q -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processCommand(String command) {
        if (command.equals("a")) {
            addASong();
        } else if (command.equals("r")) {
            removeASong();
        } else if (command.equals("s")) {
            playlist.shuffle();
        } else if (command.equals("f")) {
            findSong();
        } else if (command.equals("title")) {
            playlist.arrangeByTitle();
        } else if (command.equals("time")) {
            playlist.arrangeByTime();
        } else if (command.equals("artist")) {
            playlist.arrangeByArtist();
        } else if (command.equals("genre")) {
            playlist.arrangeByGenre();
        } else if (command.equals("show")) {
            showSongs();
        } else if (command.equals("reverse")) {
            playlist.reverse();
        } else if (command.equals("save")) {
            savePlaylist();
        } else if (command.equals("load")) {
            loadPlaylist();
        } else {
            System.out.println("Invalid Choice");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a song to playlist
    private void addASong() {
        System.out.println("Title: ");
        String title = input.next();

        System.out.println("Artist: ");
        String artist = input.next();

        System.out.println("Duration: ");
        int duration = input.nextInt();

        System.out.println("Genre: ");
        String genre = input.next();

        Song song = new Song(title, artist, duration, genre);
        playlist.addSong(song);
    }

    // MODIFIES: this
    // EFFECTS: removes a song from playlist
    private void removeASong() {
        System.out.println("Title: ");
        String title = input.next();

        if (playlist.isInPlaylist(title)) {
            playlist.removeSong(title);
        } else {
            System.out.println(title + " not in " + playlist.getPlaylistName());
        }
    }

    // EFFECTS: finds a song in playlist
    private void findSong() {
        System.out.println("Title: ");
        String title = input.next();

        if (playlist.isInPlaylist(title)) {
            Song song = playlist.getSong(playlist.findSongIndex(title));
            System.out.println(song.toString());
        } else {
            System.out.println(title + " not in " + playlist.getPlaylistName());
        }
    }

    // EFFECTS: saves playlist to file
    private void savePlaylist() {
        try {
            jsonWriter.open();
            jsonWriter.write(playlist);
            jsonWriter.close();
            System.out.println("Saved " + playlist.getPlaylistName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads playlist from file
    private void loadPlaylist() {
        try {
            playlist = jsonReader.read();
            System.out.println("Loaded " + playlist.getPlaylistName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: shows the songs in playlist
    private void showSongs() {
        System.out.println("\n" + playlist.toString());
    }

}
