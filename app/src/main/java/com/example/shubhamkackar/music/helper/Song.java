package com.example.shubhamkackar.music.helper;
import android.net.Uri;
import com.example.shubhamkackar.music.activities.MainActivity;
import com.example.shubhamkackar.music.adapters.SongListAdapter;
import com.example.shubhamkackar.music.services.MusicService;
/**
 * Created by Shubham Kackar on 08-02-2016.
 */
public class Song {
    private String mSongName, mSongAlbumName, mSongFullPath, mSongDuration;
    private Uri mSongUri;
    private int mSongId;

    public Song(){}
    public Song(String name, int id, String album_name, String full_path, String duration, Uri songuri){

        this.mSongName = name;
        this.mSongAlbumName = album_name;
        this.mSongDuration = duration;
        this.mSongFullPath = full_path;
        this.mSongId = id;
        this.mSongUri = songuri;
    }
    public String getSongName(){return mSongName;}

    public void setSongName(String mSongName){ this.mSongName = mSongName; }



    public String getSongFullPath(){ return mSongFullPath; }

    public void setSongFullPath(String mSongFullPath){ this.mSongFullPath = mSongFullPath;}



    public String getSongAlbumName(){return mSongAlbumName;}

    public void setSongAlbumName(String mSongAlbumName){ this.mSongAlbumName = mSongAlbumName;}


 // Everything below this needs to be written again
    public Integer getSongID(){return mSongId;}

    public void setSongID(Integer mSongId){ this.mSongId = mSongId;}

    public Uri getSongUri(){return mSongUri;}

    public void setSongUri(Uri mSongUri){ this.mSongUri = mSongUri;}


    public String getSongDuration(){return mSongDuration;}

    public void SetSongDuration(String mSongDuration){ this.mSongDuration = mSongDuration;}


}
