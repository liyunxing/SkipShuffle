package com.dontbelievethebyte.skipshuffle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "skipshuffle.db";
    private static final String TABLE_TRACKS = "tracks";
    private static final String TABLE_PLAYLIST = "playlist";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRACKS = "tracks";
    public static final String COLUMN_PATH = "path";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TRACKS_TABLE = "CREATE TABLE " +
                TABLE_TRACKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PATH + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_TRACKS_TABLE);

        String CREATE_PLAYLIST_TABLE = "CREATE TABLE " +
                TABLE_PLAYLIST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TRACKS + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_PLAYLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        onCreate(sqLiteDatabase);
    }

    //int return is newly added id of the row or -1 on error
    public long addTrack(Track track) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH, track.getPath());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        long trackId = sqLiteDatabase.insert(TABLE_TRACKS, null, values);
        sqLiteDatabase.close();
        return trackId;
    }

    public long addPlaylist(PlaylistInterface playlistInterface){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH, playlistInterface.toString());
        values.put(COLUMN_TRACKS, new JSONArray(playlistInterface.getList()).toString());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long playlistId = sqLiteDatabase.insert(TABLE_TRACKS, null, values);
        sqLiteDatabase.close();
        return playlistId;
    };

    public Track getTrack(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_TRACKS, new String[]{COLUMN_ID, COLUMN_PATH}, "WHERE " + COLUMN_ID + "=?", new String[]{}, null, null, null);
        Track track = new Track();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            track.setId(cursor.getInt(0));
            track.setPath(cursor.getString(1));
        }
        sqLiteDatabase.close();
        return track;
    }

    public List<Track> getAllPlaylistTracks(List<Integer> tracksIds){
        List<Track> tracks = new ArrayList<Track>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        for(Integer trackId : tracksIds){
            Track track = new Track();
            Cursor cursor = sqLiteDatabase.query(TABLE_TRACKS, new String[]{COLUMN_ID, COLUMN_PATH}, "WHERE "+ COLUMN_ID+"=?", new String[]{}, null, null, null);
            if(cursor.moveToFirst()){
                cursor.moveToFirst();
                track.setId(cursor.getInt(0));
                track.setPath(cursor.getString(1));
                tracks.add(track);
            }
        }
        sqLiteDatabase.close();
        return tracks;
    }
    public void removeAllTracks(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TRACKS, null, null);
        sqLiteDatabase.delete(TABLE_PLAYLIST, null, null);
        sqLiteDatabase.close();
    }

    public void deletePlaylist(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PLAYLIST, null, null);
        sqLiteDatabase.close();
    }

    public void addToPlaylist(){

    }

    public void getLastPlayed(){
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        sqLiteDatabase.query(TABLE_PLAYLIST, COLUMN_CHECKSUM_ID, "WHERE ",null );
    }

    public List<Integer> loadPlaylist(Integer playlistId) throws JSONException {
        List<Integer> playlistTracks = new ArrayList<Integer>();
        if(playlistId != null){
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(TABLE_PLAYLIST, new String[]{COLUMN_ID}, "WHERE " + COLUMN_ID + "=?", new String[]{playlistId.toString()}, null, null, null);
            if(cursor.moveToFirst()){
                JSONArray jsonArray = new JSONArray(cursor.getString(0));
                Log.d("DERP", cursor.getString(0));
                for(int i=0; i<jsonArray.length(); i++){
                    playlistTracks.add(jsonArray.getInt(i));
                }
            }
            return playlistTracks;
        } else {
            return null;
        }
    }
    public void savePlaylist(Integer playlistId, List<Integer> trackIndexes){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //@TODO not error safe.
        JSONArray jsonArray = new JSONArray(trackIndexes);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TRACKS, jsonArray.toString());

        if(playlistId != null){
            contentValues.put(COLUMN_ID, playlistId);
        }
        int insert = (int) sqLiteDatabase.insertWithOnConflict(TABLE_PLAYLIST, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if(insert == -1){
            sqLiteDatabase.update(TABLE_PLAYLIST, contentValues, COLUMN_ID+"=?", new String[]{playlistId.toString()});
        }
        sqLiteDatabase.close();
    }
}
