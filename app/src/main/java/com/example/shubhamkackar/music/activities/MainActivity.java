package com.example.shubhamkackar.music.activities;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.shubhamkackar.music.R;
import com.example.shubhamkackar.music.helper.Song;
import com.example.shubhamkackar.music.services.MusicService;
import com.example.shubhamkackar.music.adapters.SongListAdapter;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button mBtnImport;
    private ListView mListSongs;
    private LinearLayout mLinearListImportedFiles;
    private RelativeLayout mRelativeBtnImport;
    private SongListAdapter mAdapterlistFiles;
    private String[] STAR = {"*"};
    private ArrayList<Song> mSongList;
    private MusicService serviceMusic;
    private Intent playIntent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void init() {
        getActionBar();
        mBtnImport = (Button) findViewById(R.id.btn_import_files);
        mLinearListImportedFiles = (LinearLayout) findViewById(R.id.liner_list_imported_files);
        mRelativeBtnImport = (RelativeLayout) findViewById(R.id.relative_btn_import);
        mListSongs = (ListView) findViewById(R.id.list_song_actimport);

        mBtnImport.setOnClickListener(this);
        mListSongs.setOnItemClickListener(this);

        mSongList = new ArrayList<Song>();
        mAdapterlistFiles = new SongListAdapter(MainActivity.this, mSongList);
        mListSongs.setAdapter(mAdapterlistFiles);

    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.PlayerBinder binder = (MusicService.PlayerBinder) service;
            //get service

            serviceMusic = binder.getService();
            serviceMusic.setSongList(mSongList);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        mSongList = listAllSongs();
        mAdapterlistFiles.setSongList(mSongList);
        mLinearListImportedFiles.setVisibility(View.VISIBLE);
        mRelativeBtnImport.setVisibility(View.GONE);
        serviceMusic.setSongList(mSongList);

    }

    private ArrayList<Song> listAllSongs() {//fetch path to all the files from internal and external storage and store it in songList

        Cursor cursor;
        ArrayList<Song> songList = new ArrayList<>();
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        if (isSdPresent()){

            cursor =managedQuery(allSongsUri, STAR, selection, null, null);
            if (cursor != null){
                if(cursor.moveToFirst()){

                    do {
                        Song song = new Song();
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String[] res = data.split("\\.");
                        song.setSongName(res[0]);

                        song.setSongID(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                        song.setSongFullPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        song.setSongAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        song.setSongUri(ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))));
                        String duration = getDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                        song.SetSongDuration(duration);

                        songList.add(song);


                    } while (cursor.moveToNext());
                    return songList;

                }
                cursor.close();
            }
        }
        return null;
    }

    //check if SD card is present or not
    private static boolean isSdPresent(){

        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED
        );
    }
    //method to convert millisecond to min & sec
    private static String getDuration(long millis) {
        if (millis<0){
            throw new IllegalArgumentException("Duration Must Be greater than 0");

        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(6);
        sb.append(minutes<10 ? "0" + minutes : minutes);
        sb.append(":");
        sb.append(seconds < 10 ? "0" + seconds : seconds);
        //sb.append(" Secs");
        return sb.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        serviceMusic.setSelectedSong(position, MusicService.NOTIFICATION_ID);
        //present in music service.java
    }

    @Override
    public void onStart(){
        //Start Service
        super.onStart();
        if (playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onDestroy(){
        //Stop Service
        stopService(playIntent);
        serviceMusic = null;
        super.onDestroy();
    }


}
