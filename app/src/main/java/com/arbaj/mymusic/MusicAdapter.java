package com.arbaj.mymusic;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Contract;

import java.io.File;

import java.util.ArrayList;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {

    private Context mContext;
    static ArrayList<MusicFiles> mFiles;
    MusicAdapter(Context mContext,ArrayList<MusicFiles> mFiles)
    {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.music_items, parent,false);
        return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());


        byte[] image= getAlbumArt(mFiles.get(position).getPath());
        if (image!=null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_art);
        }
        else {
            Glide.with(mContext)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.album_art);
        }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(mContext,PlayerActivity.class);
        intent.putExtra("position",position);
        mContext.startActivity(intent);

    }
});
//    holder.menuMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( final View v) {
//                PopupMenu popupMenu= new PopupMenu(mContext,v);
//                PopupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//                popupMenu.show();
//                popupMenu.setOnMenuItemClickListener((item  )-> {
//                    switch (item.getItemId()) {
//                        case R.id.delete:
//
//                            Toast.makeText(mContext, "Succesfully Deleted", Toast.LENGTH_SHORT).show();
//                            deleteFile(position, v);
//                            break;
//                    }
//                    return true;
//                });
//            }
//
//        });


    }
//    private void  deleteFile(int position,View v){
//        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                Long.parseLong(mFiles.get(position).getId());
//
//        File file=new File(mFiles.get(position).getPath());
//        boolean deleted=file.delete();
//        if (deleted) {
//            mContext.getContentResolver().delete(contentUri,null,null);
//            mFiles.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, mFiles.size());
//            Snackbar.make(v, "File Deleted", Snackbar.LENGTH_SHORT).show();
//        }
//        else {
//            Snackbar.make(v, "File not Deleted", Snackbar.LENGTH_SHORT).show();
//        }
//    }


    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyVieHolder extends RecyclerView.ViewHolder
    {
        TextView file_name;
        ImageView album_art,menuMore;
        public MyVieHolder(@NonNull View itemView) {

            super(itemView);
            file_name= itemView.findViewById(R.id.music_file_name);
            album_art=itemView.findViewById(R.id.music_img);
        //     menuMore=itemView.findViewById(R.id.menuMore);
        }
    }

    private byte[] getAlbumArt(String uri)  {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();

        return art;
    }
void  updateList(ArrayList<MusicFiles> musicFilesArrayList){
        mFiles= new ArrayList<>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
}

    }


