package com.dontbelievethebyte.skipshuffle.playlists;

import android.database.Cursor;
import android.provider.MediaStore;

public class Track {
    private long id;
    private String path;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String duration;
    private int position;

    public Track(){}

    public Track(Cursor cursor, int position)
    {
        id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getDuration()
    {
        return duration;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }


//    protected String buildFormattedTitle(Track track)
//    {
//        try {
//            SkipShuffleMediaPlayer skipShuffleMediaPlayer = baseActivity.getMediaPlayer();
//            PlaylistInterface playlist = skipShuffleMediaPlayer.getPlaylist();
//            Track currentTrack = playlist.getCurrent();
//            if (null == currentTrack.getArtist() || null == currentTrack.getTitle()) {
//                return (null == currentTrack.getPath()) ?
//                        baseActivity.getString(R.string.meta_data_unknown_current_song_title) :
//                        currentTrack.getPath().substring(currentTrack.getPath().lastIndexOf("/") + 1);
//            } else {
//                return currentTrack.getArtist() + " - " + currentTrack.getTitle();
//            }
//        return baseActivity.getString(R.string.meta_data_unknown_current_song_title);
//        } catch (NoMediaPlayerException noMediaPlayerException){
//            throw new PlaylistEmptyException(0L);
//        }
//    }
}
