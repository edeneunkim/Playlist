package ui;

import model.Playlist;
import model.Song;
import model.EventLog;
import model.Event;
import persistence.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents graphical user interface of playlist application
 * References: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 *             https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
 *             https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
 *             https://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
 *             https://docs.oracle.com/javase/7/docs/api/java/awt/event/WindowListener.html
 */
public class PlaylistUI extends JFrame {
    private static final int WIDTH = 700;
    private static final int LENGTH = 600;
    private static final String sep = System.getProperty("file.separator");
    private Playlist playlist;
    private JPanel panel;
    private static final String JSON_STORE = "./data/playlist.json";
    private JList<Song> songs;
    private DefaultListModel<Song> songModel;
    private JSplitPane separateList;
    private JLabel songLabel;
    private ImageIcon startIcon;
    private ImageIcon errorIcon;
    private ImageIcon loadIcon;
    private ImageIcon saveIcon;
    private ImageIcon confirmIcon;

    /**
     * Constructs the main window and sets up the start menu
     */
    public PlaylistUI() {
        songs = new JList<>();
        songModel = new DefaultListModel<>();
        separateList = new JSplitPane();
        panel = new JPanel();
        songLabel = new JLabel();
        startIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Start.png");
        startIcon = changeIconSize(startIcon);
        errorIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Error.png");
        errorIcon = changeIconSize(errorIcon);
        loadIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Load.png");
        loadIcon = changeIconSize(loadIcon);
        saveIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Save.png");
        saveIcon = changeIconSize(saveIcon);
        confirmIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Confirm.png");
        confirmIcon = changeIconSize(confirmIcon);

        startMenu();

        setSize(WIDTH, LENGTH);
        addMenu();
        setUpPlaylist();
        setUpWindow();
    }

    /**
     * Constructs and sets up the window settings
     */
    public void setUpWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                printLog(EventLog.getInstance());
            }
        });
    }

    /**
     * Constructs the start menu
     */
    private void startMenu() {
        Object[] options = {"Load", "New"};
        int startMenuOption = JOptionPane.showOptionDialog(null, "Load or New", "Start",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, startIcon, options, options[1]);

        if (startMenuOption == JOptionPane.OK_OPTION) {
            loadPlaylist();
        } else if (startMenuOption == JOptionPane.NO_OPTION) {
            String start = (String) JOptionPane.showInputDialog(null, "Playlist name: ",
                     null, JOptionPane.QUESTION_MESSAGE, startIcon, null, null);
            if (start == null) {
                System.exit(0);
            }
            checkName(start);
        } else {
            System.exit(0);
        }
    }

    /**
     * Constructs the menu where user makes a playlist with given name
     * @param playlistName  name of the playlist that will be constructed
     */
    private void checkName(String playlistName) {
        if (playlistName.equals("")) {
            JOptionPane.showMessageDialog(null, "Error creating playlist",
                    "Error", JOptionPane.QUESTION_MESSAGE, errorIcon);
            new PlaylistUI();
        } else {
            playlist = new Playlist(playlistName);
        }
    }

    /**
     * Sets up the display that shows playlist and song information
     */
    private void setUpPlaylist() {
        songModel = new DefaultListModel<>();
        songs = new JList<>();

        setTitle(playlist.getPlaylistName());
        songs.setModel(songModel);

        for (Song song : playlist.getSongs()) {
            songModel.addElement(song);
        }

        songs.getSelectionModel().addListSelectionListener(e -> {
            Song s = songs.getSelectedValue();
            songLabel.setText(s.guiString());
        });

        separateList.setLeftComponent(new JScrollPane(songs));
        panel.add(songLabel);
        separateList.setRightComponent(panel);
        separateList.setDividerLocation(300);
        add(separateList);
    }

    /**
     * Adds a menu bar
     */
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        addMenuItem(fileMenu, new SaveAction(), KeyStroke.getKeyStroke("control S"));
        addMenuItem(fileMenu, new LoadAction(), KeyStroke.getKeyStroke("control L"));
        addMenuItem(fileMenu, new InfoAction(), null);
        menuBar.add(fileMenu);

        JMenu playlistMenu = new JMenu("Edit");
        playlistMenu.setMnemonic('E');
        addMenuItem(playlistMenu, new AddSongAction(), null);
        addMenuItem(playlistMenu, new RemoveSongAction(), null);
        menuBar.add(playlistMenu);

        JMenu arrangeMenu = new JMenu("Arrange");
        arrangeMenu.setMnemonic('A');
        addMenuItem(arrangeMenu, new ShuffleAction(), null);
        addMenuItem(arrangeMenu, new ArrangeTitleAction(), null);
        addMenuItem(arrangeMenu, new ArrangeArtistAction(), null);
        addMenuItem(arrangeMenu, new ArrangeTimeAction(), null);
        addMenuItem(arrangeMenu, new ArrangeGenreAction(), null);
        addMenuItem(arrangeMenu, new ReverseAction(), null);
        menuBar.add(arrangeMenu);

        JMenu quitMenu = new JMenu("Quit");
        addMenuItem(quitMenu, new QuitAction(), KeyStroke.getKeyStroke("control Q"));
        menuBar.add(quitMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Adds an item with given handler to the given menu
     * @param theMenu   menu to which new item is added
     * @param action    handler for new menu item
     * @param accelerator   keystroke accelerator for this menu item
     */
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    /**
     * Helper method for loading playlist from given file
     * Used only for loading from start menu
     */
    private void loadPlaylist() {
        JsonReader reader = new JsonReader(JSON_STORE);
        try {
            playlist = reader.read();
            JOptionPane.showMessageDialog(null, "Loaded " + playlist.getPlaylistName(),
                    null, JOptionPane.QUESTION_MESSAGE, loadIcon);
            setUpPlaylist();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading playlist.", null,
                    JOptionPane.QUESTION_MESSAGE, errorIcon);
        }
    }

    /**
     * Helper method for saving playlist from given file
     * Used only for saving playlist by quitting through quit option
     */
    private void savePlaylist() {

        JsonWriter writer = new JsonWriter(JSON_STORE);
        try {
            writer.open();
            writer.write(playlist);
            writer.close();
            JOptionPane.showMessageDialog(null,playlist.getPlaylistName() + " saved.",
                    null, JOptionPane.QUESTION_MESSAGE, saveIcon);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Error saving " + playlist.getPlaylistName(), null, JOptionPane.QUESTION_MESSAGE,
                    errorIcon);
        }
    }

    /**
     * Helper method to centre the application window
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * Helper method to change icon size
     * @param icon icon which size will be changed
     * @return a resized icon
     */
    private ImageIcon changeIconSize(ImageIcon icon) {
        Image tempIcon = icon.getImage();
        Image scaledIcon = tempIcon.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledIcon);
    }

    /**
     * Prints the log to the console
     * @param el  the event log to be printed
     */
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n\n");
        }
    }


    /**
     * Represents action to be taken when user wants to add a song to the playlist.
     */
    private class AddSongAction extends AbstractAction {
        private JTextField titleField;
        private JTextField artistField;
        private JTextField timeField;
        private JTextField genreField;
        private ImageIcon addIcon;
        Object[] songFields;

        AddSongAction() {
            super("Add");
            titleField = new JTextField();
            artistField = new JTextField();
            timeField = new JTextField();
            genreField = new JTextField();
            addIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep
            + "Add.png");
            addIcon = changeIconSize(addIcon);
            songFields = new Object[]{
                    "Title", titleField,
                    "Artist", artistField,
                    "Duration", timeField,
                    "Genre", genreField
            };
        }

        @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
        @Override
        public void actionPerformed(ActionEvent evt) {
            int addSong = JOptionPane.showConfirmDialog(null, songFields, "Add Song",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, addIcon);

            if (addSong == JOptionPane.OK_OPTION) {
                if (!Objects.equals(titleField.getText(), "") && !Objects.equals(artistField.getText(), "")
                        && !Objects.equals(timeField.getText(), "")
                        && !Objects.equals(genreField.getText(), "")) {
                    try {
                        Song song = new Song(titleField.getText(), artistField.getText(),
                                Integer.parseInt(timeField.getText()), genreField.getText());
                        if (playlist.isInPlaylist(song)) {
                            JOptionPane.showMessageDialog(null, "Song already in playlist",
                                    null, JOptionPane.QUESTION_MESSAGE, errorIcon);
                        } else {
                            playlist.addSong(song);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Duration is not a number",
                                null, JOptionPane.QUESTION_MESSAGE, errorIcon);
                    }
                    setUpPlaylist();
                } else {
                    JOptionPane.showMessageDialog(null, "Error adding song", null,
                            JOptionPane.QUESTION_MESSAGE, errorIcon);
                }
            }
        }
    }


    /**
     * Represents the action to be taken when the user wants to remove a song from the playlist.
     */
    private class RemoveSongAction extends AbstractAction {
        private ImageIcon removeIcon;

        RemoveSongAction() {
            super("Remove");
            removeIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep
                    + "Remove.png");
            removeIcon = changeIconSize(removeIcon);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String songTitle = (String) JOptionPane.showInputDialog(null, "Title",
                    "Remove Song", JOptionPane.QUESTION_MESSAGE, removeIcon, null,
                    null);
            if (songTitle != null && !songTitle.equals("")) {
                if (!playlist.isInPlaylist(songTitle)) {
                    JOptionPane.showMessageDialog(null, "Cannot find song", null,
                            JOptionPane.QUESTION_MESSAGE, errorIcon);
                } else {
                    playlist.removeSong(songTitle);
                    setUpPlaylist();
                }
            } else if (Objects.equals(songTitle, "")) {
                JOptionPane.showMessageDialog(null, "Error removing song", null,
                        JOptionPane.QUESTION_MESSAGE, errorIcon);
            }
        }
    }


    /**
     * Represents the action to be taken when the user wants to shuffle the playlist.
     * Playlist order may be maintained by random chance.
     */
    private class ShuffleAction extends AbstractAction {

        ShuffleAction() {
            super("Shuffle");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.shuffle();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to arrange the playlist in alphabetical order of song
     * titles.
     */
    private class ArrangeTitleAction extends AbstractAction {

        ArrangeTitleAction() {
            super("Arrange (Title)");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.arrangeByTitle();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to arrange the playlist in alphabetical order of artist
     * name.
     */
    private class ArrangeArtistAction extends AbstractAction {

        ArrangeArtistAction() {
            super("Arrange (Artist)");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.arrangeByArtist();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to arrange the playlist in ascending order of song
     * play times.
     */
    private class ArrangeTimeAction extends AbstractAction {

        ArrangeTimeAction() {
            super("Arrange (Time)");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.arrangeByTime();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to arrange the playlist in alphabetical order of song
     * genre.
     */
    private class ArrangeGenreAction extends AbstractAction {

        ArrangeGenreAction() {
            super("Arrange (Genre)");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.arrangeByGenre();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to reverse the playlist order.
     */
    private class ReverseAction extends AbstractAction {

        ReverseAction() {
            super("Reverse");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            playlist.reverse();
            setUpPlaylist();
        }
    }


    /**
     * Represents the action to be taken when the user wants to save the playlist to given file.
     */
    private class SaveAction extends AbstractAction {
        private JsonWriter jsonWriter;
        private static final String JSON_STORE = "./data/playlist.json";

        SaveAction() {
            super("Save");
            jsonWriter = new JsonWriter(JSON_STORE);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int savePlaylist = JOptionPane.showConfirmDialog(null,
                    "Save " + playlist.getPlaylistName() + "?",
                    "Save Playlist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, saveIcon);

            if (savePlaylist == JOptionPane.OK_OPTION) {
                try {
                    jsonWriter.open();
                    jsonWriter.write(playlist);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(null,playlist.getPlaylistName() + " saved.",
                            null, JOptionPane.QUESTION_MESSAGE, confirmIcon);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error saving " + playlist.getPlaylistName(), null,
                            JOptionPane.QUESTION_MESSAGE, errorIcon);
                }
            }

        }
    }


    /**
     * Represents the action to be taken when the user wants to load a playlist from given file.
     */
    private class LoadAction extends AbstractAction {
        private JsonReader jsonReader;
        private static final String JSON_STORE = "./data/playlist.json";

        LoadAction() {
            super("Load");
            jsonReader = new JsonReader(JSON_STORE);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int loadPlaylist = JOptionPane.showConfirmDialog(null,
                    "Load playlist from file?",
                    "Load Playlist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, loadIcon);

            if (loadPlaylist == JOptionPane.OK_OPTION) {
                try {
                    playlist = jsonReader.read();
                    JOptionPane.showMessageDialog(null, "Loaded " + playlist.getPlaylistName(),
                            null, JOptionPane.QUESTION_MESSAGE, confirmIcon);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error loading playlist.", null,
                            JOptionPane.QUESTION_MESSAGE, errorIcon);
                }
                setUpPlaylist();
            }
        }
    }


    /**
     * Represents the action to be taken when the user wants to quit the application by using the quit option in the
     * menu.
     */
    private class QuitAction extends AbstractAction {
        private ImageIcon quitIcon;

        QuitAction() {
            super("Quit");
            quitIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Quit.png");
            quitIcon = changeIconSize(quitIcon);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int quitPlaylist = JOptionPane.showConfirmDialog(null,
                    "Would you like to save before quitting?", "Quit", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, quitIcon);
            if (quitPlaylist == JOptionPane.YES_OPTION) {
                savePlaylist();
                printLog(EventLog.getInstance());
                System.exit(0);
            } else if (quitPlaylist == JOptionPane.NO_OPTION) {
                printLog(EventLog.getInstance());
                System.exit(0);
            }
        }
    }


    /**
     * Represents the action to be taken when the user wants to get information about the playlist.
     */
    private class InfoAction extends AbstractAction {
        private ImageIcon infoIcon;

        InfoAction() {
            super("Information");
            infoIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Icons" + sep + "Info.png");
            infoIcon = changeIconSize(infoIcon);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String message = "Name: " + playlist.getPlaylistName() + "\nNumber of songs: " + playlist.getNumSongs()
                    + "\nTotal Duration: " + playlist.totalTime();
            JOptionPane.showMessageDialog(null, message,
                    "Playlist Information", JOptionPane.QUESTION_MESSAGE, infoIcon);
        }
    }


    // starts the application
    public static void main(String[] args) {
        new PlaylistUI();
    }
}
