package com.example.shubhamkackar.music.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shubhamkackar.music.R;
import com.example.shubhamkackar.music.helper.Song;
import com.example.shubhamkackar.music.services.MusicService;
import com.example.shubhamkackar.music.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Shubham Kackar on 09-02-2016.
 */
public class SongListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Song> songList; //Data Source For List VIew

    public SongListAdapter (Context context, ArrayList<Song> list){
        mContext = context;
        this.songList = list;

    }


    @Override
    public int getCount(){ return songList.size();}

    @Override
    public Song getItem(int position) {return songList.get(position);}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            //Layout inflate for List Item
            convertView = LayoutInflater.from(mContext).inflate(R.layout.song_list_item, null);
        }

        ImageView mImgSong = (ImageView) convertView.findViewById(R.id.img_listitem_file);
        TextView mtxtSongName = (TextView) convertView.findViewById(R.id.txt_listitem_filename);
        TextView mtxtSongAlbumName = (TextView) convertView.findViewById(R.id.txt_listitem_albumname);
        TextView mtxtSongDuration = (TextView) convertView.findViewById(R.id.txt_listitem_duration);

        mImgSong.setImageResource(R.drawable.musical_note);
        mtxtSongName.setText(songList.get(position).getSongName());
        mtxtSongAlbumName.setText(songList.get(position).getSongAlbumName());
        mtxtSongDuration.setText(songList.get(position).getSongDuration());

        return  convertView;
    }

    public void setSongList(ArrayList<Song> list){

        songList = list;
        this.notifyDataSetChanged();
    }
}
