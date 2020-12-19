package com.example.pomiplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.pomiplayer.MainActivity.musicFiles;

public class PlayMusicActivity extends AppCompatActivity {

    TextView judul_lagi_play, nama_artis_play, text_duration, total_duration;
    ImageView gambarDetailAlbum, skip_next, skip_prev;
    SeekBar seekBar;
    FloatingActionButton playPause;
    int position =-1;
    static ArrayList<Music>playSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initPage();
        getInitMehotd();
        //ketika seekBar di klik maju mundur
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //set seekBar jalan
        PlayMusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentposition);
                    text_duration.setText(formatedTime(mCurrentposition));
                }
                handler.postDelayed(this, 100);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                skip_prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnCliked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnCliked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (playSongs.size() -1 ) : (position - 1));
            uri = Uri.parse(playSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
//
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (playSongs.size() -1 ) : (position - 1));
            uri = Uri.parse(playSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                skip_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnCliked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnCliked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % playSongs.size());
            uri = Uri.parse(playSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);


            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % playSongs.size());
            uri = Uri.parse(playSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);


            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()){
            playPause.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }


    private void metaData(Uri uri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(playSongs.get(position).getDuration()) / 1000;
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        total_duration.setText(formatedTime(durationTotal));
        nama_artis_play.setText(playSongs.get(position).getArtist());
        judul_lagi_play.setText(playSongs.get(position).getTitle());
        byte[] art  = mmr.getEmbeddedPicture();
        if (art != null){
            Glide.with(this).asBitmap().load(art).into(gambarDetailAlbum);
        } else {
            Glide.with(this).load(R.drawable.playlist).into(gambarDetailAlbum);
        }
    }

    private String formatedTime(int mCurrentposition) {
        String totalout = "";
        String totalnew = "";
        String seconds = String.valueOf(mCurrentposition % 60);
        String minute =String.valueOf(mCurrentposition / 60);
        totalout = minute + ":" + seconds;
        totalnew = minute + ":" + "0" + seconds;

        if (seconds.length() == 1 ){
            return totalnew;
        } else {
            return totalout;
        }
    }

    private void getInitMehotd() {
        position = getIntent().getIntExtra("position", -1);
        playSongs = musicFiles;

        if (playSongs != null){
            playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(playSongs.get(position).getPath());
        }

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        metaData(uri);
    }

    private void initPage() {
        judul_lagi_play = findViewById(R.id.judul_lagi_play);
        nama_artis_play = findViewById(R.id.nama_artis_play);
        text_duration = findViewById(R.id.text_duration);
        total_duration = findViewById(R.id.total_duration);
        gambarDetailAlbum = findViewById(R.id.gambarDetailAlbum);
        skip_next = findViewById(R.id.skip_next);
        skip_prev = findViewById(R.id.skip_prev);
        seekBar = findViewById(R.id.seekBar);
        playPause = findViewById(R.id.floatingActionButton);
    }
}