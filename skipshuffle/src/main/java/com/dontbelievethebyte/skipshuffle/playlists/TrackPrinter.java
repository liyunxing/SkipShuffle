package com.dontbelievethebyte.skipshuffle.playlists;

import android.content.Context;
import android.provider.MediaStore;

import com.dontbelievethebyte.skipshuffle.R;

public class TrackPrinter {

    private Context context;

    public TrackPrinter(Context context)
    {
        this.context = context;
    }

    public String printTitle(Track track)
    {
        if (null == track || null == track.getTitle() || MediaStore.UNKNOWN_STRING.equals(track.getTitle()) ) {
            return (null == track.getPath()) ?
                    context.getString(R.string.meta_data_unknown_title) :
                    track.getPath().substring(track.getPath().lastIndexOf("/") + 1);
        }
        return track.getTitle();
    }

    public String printArtist(Track track)
    {
        if (null == track || null == track.getArtist() || MediaStore.UNKNOWN_STRING.equals(track.getArtist()))
            return context.getString(R.string.meta_data_unknown_artist);
        return track.getArtist();
    }
}
