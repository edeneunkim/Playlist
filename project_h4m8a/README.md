# My Personal Project

## Playlist

An idea that I have proposed is a Playlist application. This application creates a playlist using information
given by the user. With these pieces of information, the playlist then allows the user to:
- add songs
- remove songs 
- find the length of a song
- find the author of a song
- check how many songs are in the playlist

I find this idea interesting since I enjoy listening to music and use playlists a lot. I want to learn how playlists
can be coded and try creating one where I would have no problems using. 

### User Stories
- As a user, I want to be able to add a song
- As a user, I want to be able to remove a song
- As a user, I want to be able to shuffle the playlist
- As a user, I want to be able to check how many songs are in the playlist
- As a user, I want to be able to change the order
- As a user, I want to be able to save my playlist including the songs and order and which song was playing
- As a user, I want to be able to load my playlist including the songs and order and which song was playing
- As a user, I want to be able to have the option to save or not save my playlist after quitting


## Instructions for Grader
### How to add a song
To add a song in the PlaylistUI, the user can click the edit button from the menu at the top and select add from the 
drop-down menu, which then opens a window where the user can input the information of the song that they want to add.

### How to add another song
The user just needs to do the same steps as adding a single song.

### Saving the playlist
To save the playlist, the user can click the file button from the menu at the top and select save from the drop-down 
menu, then click the OK option from the pop-up window. Additionally, the user can also click quit at the top menu,
where a pop-up window with a save option will appear.

### Loading the playlist
To load a previously saved playlist, the user can click the file button from the top menu and select load from the 
drop-down menu, then click the OK option. This will load the playlist that was saved to the playlist.json file of this
project. Additionally, the user will be give the option to load a previously saved playlist.

### Visual Component
The visual components in this application are icons, which are located in the Icons folder of this project file. The
icons are resized by the helper method *changeIconSize(ImageIcon icon)* in the *PlaylistUI* class. The icons are all 
type ImageIcon.

## Phase 4: Task 2
Breaks are added to the editor to show how the log looks in the console in the preview mode of the README file
<br/><br/>Thu Dec 01 15:54:19 PST 2022
<br/>Added Eden's Song to Eden's Playlist
<br/><br/><br/>Thu Dec 01 15:54:40 PST 2022
<br/>Added Song to Eden's Playlist
<br/><br/><br/>Thu Dec 01 15:54:54 PST 2022
<br/>Added asdf to Eden's Playlist
<br/><br/><br/>Thu Dec 01 15:55:00 PST 2022
<br/>Shuffled Eden's Playlist
<br/><br/><br/>Thu Dec 01 15:55:03 PST 2022
<br/>Arranged Eden's Playlist in alphabetical order of song titles
<br/><br/><br/>Thu Dec 01 15:55:07 PST 2022
<br/>Arranged Eden's Playlist in alphabetical order of artist names
<br/><br/><br/>Thu Dec 01 15:55:10 PST 2022
<br/>Arranged Eden's Playlist in ascending order of song durations
<br/><br/><br/>Thu Dec 01 15:55:12 PST 2022
<br/>Reversed playlist order
<br/><br/><br/>Thu Dec 01 15:55:14 PST 2022
<br/>Arranged Eden's Playlist in alphabetical order of genres
<br/><br/><br/>Thu Dec 01 15:55:21 PST 2022
<br/>Removed asdf from Eden's Playlist
<br/><br/><br/>Thu Dec 01 15:55:25 PST 2022
<br/>Shuffled Eden's Playlist


## Phase 4: Task 3
- refactor SaveAction and LoadAction classes in the PlaylistUI Class
<ul>
    - The actionPerformed() method in SaveAction and LoadAction are very similar to savePlaylist()
and loadPlaylist() and there is some duplicate code between each other. However, since there are some differences with 
loading/saving from opening the app compared to loading/saving using the load/save button, I implemented a separate 
load/save method that is exclusively used when loading/saving right after opening the app <br>
    - By finding a way where loadPlaylist() and savePlaylist() are not required, then there would be no need for the 
PlaylistUI class to directly be associated with the JsonReader and JsonWriter classes, and it would only be SaveAction 
and LoadAction being directly associated. <br>

</ul>
 
- create exceptions and use them instead of if-statements

<ul>
    - there are some places in the PlaylistUI class where instead of catching an exception, an if-statement is used
to prevent unwanted behaviour and show a window. We can create a parent exception class and create more exception 
classes that extend the parent exception class and differs in when they are caught and what window they create.
</ul>