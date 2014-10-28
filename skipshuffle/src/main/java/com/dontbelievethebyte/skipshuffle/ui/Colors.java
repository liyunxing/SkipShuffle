package com.dontbelievethebyte.skipshuffle.ui;

import android.content.Context;
import android.graphics.Color;

import com.dontbelievethebyte.skipshuffle.ui.mapper.ColorMapper;

public class Colors {

    public int background;
    public int emptyListText;
    public int listDivider;
    public int navDrawerBackground;
    public int navDrawerHeaderText;
    public int navDrawerText;
    public int songLabel;

    public Colors(int uiType)
    {
        background = ColorMapper.getBackground(uiType);
        emptyListText = ColorMapper.getEmptyListText(uiType);
        listDivider = ColorMapper.getListDivider(uiType);
        navDrawerBackground = ColorMapper.getNavDrawerBackground(uiType);
        navDrawerHeaderText = ColorMapper.getNavHeaderText(uiType);
        navDrawerText = ColorMapper.getNavDrawerText(uiType);
        songLabel = ColorMapper.getSongLabel(uiType);
    }

    public Color toColor(Context context)
    {
        return new Color();
    }
}
