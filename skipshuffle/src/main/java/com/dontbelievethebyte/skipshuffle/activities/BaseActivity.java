package com.dontbelievethebyte.skipshuffle.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dontbelievethebyte.skipshuffle.R;
import com.dontbelievethebyte.skipshuffle.actionbar.CustomActionBarWrapper;
import com.dontbelievethebyte.skipshuffle.adapters.NavigationDrawerAdapter;
import com.dontbelievethebyte.skipshuffle.callbacks.HapticFeedBackChangedCallback;
import com.dontbelievethebyte.skipshuffle.callbacks.ThemeChangedCallback;
import com.dontbelievethebyte.skipshuffle.dialog.ThemeSelectionDialog;
import com.dontbelievethebyte.skipshuffle.exceptions.MenuOptionNotHandledException;
import com.dontbelievethebyte.skipshuffle.exceptions.NoHardwareMenuKeyException;
import com.dontbelievethebyte.skipshuffle.exceptions.NoMediaPlayerException;
import com.dontbelievethebyte.skipshuffle.listeners.NavDrawerClickListener;
import com.dontbelievethebyte.skipshuffle.listeners.TouchListener;
import com.dontbelievethebyte.skipshuffle.menu.CustomOptionsMenuInterface;
import com.dontbelievethebyte.skipshuffle.menu.builder.OptionsMenuBuilder;
import com.dontbelievethebyte.skipshuffle.navdrawer.MusicPlayerDrawer;
import com.dontbelievethebyte.skipshuffle.preferences.PreferencesHelper;
import com.dontbelievethebyte.skipshuffle.services.MediaPlayerServiceConnection;
import com.dontbelievethebyte.skipshuffle.services.SkipShuffleMediaPlayer;
import com.dontbelievethebyte.skipshuffle.ui.BaseUI;
import com.dontbelievethebyte.skipshuffle.ui.PlayerUIInterface;
import com.dontbelievethebyte.skipshuffle.utilities.MediaScannerHelper;
import com.dontbelievethebyte.skipshuffle.utilities.ToastHelper;

public abstract class BaseActivity extends ActionBarActivity
        implements ThemeChangedCallback, HapticFeedBackChangedCallback, View.OnTouchListener {

    public static final String TAG = "SkipShuffle";

    protected PlayerUIInterface playerUIInterface;
    protected PreferencesHelper preferencesHelper;
    protected CustomActionBarWrapper customActionBar;

    private MediaScannerHelper mediaScannerHelper;
    private CustomOptionsMenuInterface customOptionsMenu;

    protected MediaPlayerServiceConnection mediaPlayerServiceConnection;
    protected ToastHelper toastHelper;

    private BaseUI baseUI;
    protected abstract void handleBackPressed();
    protected abstract int getViewStub();

    protected void setUI(Integer type)
    {

    }


    public PreferencesHelper getPreferencesHelper()
    {
        return preferencesHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startMediaPlayerService();

        //Make sure we adjust the volume of the media player and not something else
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setUpActionBar();

        toastHelper = new ToastHelper(getApplicationContext());
        mediaScannerHelper = new MediaScannerHelper(this);
        mediaPlayerServiceConnection = new MediaPlayerServiceConnection();
    }

    private void startMediaPlayerService()
    {
        startService(
                new Intent(
                        getApplicationContext(),
                        SkipShuffleMediaPlayer.class
                )
        );
    }

    public SkipShuffleMediaPlayer getMediaPlayer() throws NoMediaPlayerException
    {
        return mediaPlayerServiceConnection.getMediaPlayer();
    }

    private void setUpActionBar()
    {
        customActionBar = new CustomActionBarWrapper(this);
        customActionBar.setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        OptionsMenuBuilder optionsMenuCreator = new OptionsMenuBuilder(this);
        optionsMenuCreator.setCustomActionBarWrapper(customActionBar);
        optionsMenuCreator.build(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        //Check if we're scanning media beforehand and.
//        if (savedInstanceState.getBoolean(MediaScannerHelper.IS_SCANNING_MEDIA))
//            mediaScannerHelper.startMediaScan();
    }

    @Override
    public void onBackPressed()
    {
        try {
            if (!customActionBar.isShowing())
                handleBackPressed();

            customActionBar.showToggle();
        } catch (NoHardwareMenuKeyException noHardwareMenuKeyException) {
            handleBackPressed();
        }
    }

    @Override
    protected void onPause()
    {
        preferencesHelper.unRegisterPrefsChangedListener();
        unbindService(mediaPlayerServiceConnection);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        preferencesHelper = new PreferencesHelper(this);

        //Set up preferences and listener but callback are implemented by child classes.
        preferencesHelper.registerPrefsChangedListener();
        preferencesHelper.registerCallBack(this);

        setUI(preferencesHelper.getUIType());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
//        outState.putBoolean(MediaScannerHelper.IS_SCANNING_MEDIA, mediaScannerHelper.isScanningMedia());
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, SkipShuffleMediaPlayer.class);
        bindService(
                intent,
                mediaPlayerServiceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        TouchListener touchHandler = new TouchListener(this);
        return touchHandler.handleTouch(view, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            return customOptionsMenu.handleSelection(item);
        } catch (MenuOptionNotHandledException menuOptionNotHandledException){
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU )
            return customOptionsMenu.handleMenuKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    public void showThemeSelectionDialog()
    {
        ThemeSelectionDialog themeSelectionDialog = new ThemeSelectionDialog(this);
        themeSelectionDialog.build();
        themeSelectionDialog.show();
    }

    public void showMediaScannerDialog()
    {
        mediaScannerHelper.showMediaScannerDialog();
    }

    protected void setNavigationDrawerContent()
    {
        MusicPlayerDrawer musicPlayerDrawer = new MusicPlayerDrawer(this, R.id.drawer_list);
        musicPlayerDrawer.setClickListener(
                new NavDrawerClickListener(
                        this,
                        (DrawerLayout) findViewById(R.id.drawer_layout)
                )
        );
        musicPlayerDrawer.setTouchListener(this);
        musicPlayerDrawer.setAdapter(
                new NavigationDrawerAdapter(
                        this,
                        R.layout.drawer_list_item,
                        getResources().getStringArray(R.array.drawer_menu),
                        getPreferencesHelper(),
                        playerUIInterface.getTypeFace()
                )
        );
    }

    @Override
    public void onHapticFeedBackChanged(boolean isHapticFeedback)
    {
        toastHelper.showShortToast(
                isHapticFeedback ?
                        getString(R.string.haptic_feedback_off) :
                        getString(R.string.haptic_feedback_on)
        );
    }

    @Override
    public void onThemeChanged(int uiType)
    {
        setUI(uiType);
    }

    protected void handleNoMediaPlayerException(NoMediaPlayerException noMediaPlayerException)
    {
        toastHelper.showLongToast(
                getString(R.string.no_media_player)
        );
    }
}
