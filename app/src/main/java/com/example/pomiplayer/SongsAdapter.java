package com.example.pomiplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class SongsAdapter extends  RecyclerView.Adapter<SongsAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Music> mFiles;

    SongsAdapter(Context mContex, ArrayList<Music> mFiles) {
        this.mFiles = mFiles;
        this.mContext = mContex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_list_music, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.judul_lagu.setText(mFiles.get(position).getTitle());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mFiles.get(position).getPath());
        byte[] artBytes =  mmr.getEmbeddedPicture();
        if(artBytes != null)
        {
            InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm = BitmapFactory.decodeStream(is);
            Glide.with(mContext).load(bm).into(holder.album_art);
        }
        else
        {
            Glide.with(mContext).load(R.drawable.playlist).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView judul_lagu;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            judul_lagu = itemView.findViewById(R.id.titleMusik);
            album_art = itemView.findViewById(R.id.gambarAlbum);
        }
    }

}
