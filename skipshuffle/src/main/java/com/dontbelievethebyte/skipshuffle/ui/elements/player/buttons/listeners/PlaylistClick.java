package com.dontbelievethebyte.skipshuffle.ui.elements.player.buttons.listeners;

import android.content.Intent;
import android.view.View;

import com.dontbelievethebyte.skipshuffle.activities.BaseActivity;
import com.dontbelievethebyte.skipshuffle.activities.PlaylistActivity;

public class PlaylistClick extends CustomAbstractClick {

    public PlaylistClick(BaseActivity baseActivity)
    {
        super(baseActivity);
    }

    @Override
    public void onClick(View view)
    {
        Intent playlistActivity = new Intent(activity, PlaylistActivity.class);
        activity.startActivity(playlistActivity);
    }
}
