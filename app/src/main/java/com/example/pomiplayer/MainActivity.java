package com.example.pomiplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    static ArrayList<Music> musicFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userPermission();
    }

    private void userPermission(){
        //cek permission apakah Allowed atau Tidak
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            musicFiles = getAllAudio(this);
            showListSongs();
        }
    }

    private void showListSongs() {
        RecyclerView recyclerView = findViewById(R.id.recylerViewList);
        SongsAdapter adapter = new SongsAdapter(MainActivity.this, musicFiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicFiles = getAllAudio(this);
                showListSongs();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }

    }

    private ArrayList<Music> getAllAudio(Context context) {

        ArrayList<Music> tmpMusic = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //tampung data dari media
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM, // path
                MediaStore.Audio.Media.ARTIST,
        };

        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null) {
            while(cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(5);

                Music music = new Music(path,title,artist,album,duration);
                tmpMusic.add(music);
            }
            cursor.close();
        }
        return tmpMusic;
    }


}