package com.arbaj.mymusic;

import static com.arbaj.mymusic.MainActivity.musicFiles;
import static com.arbaj.mymusic.R.id.recyclerView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arbaj.mymusic.R.id;


public class Songsfragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;



    public Songsfragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_songsfragment, container, false);
        recyclerView=view.findViewById(id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if(!(musicFiles.size()<1)){
            musicAdapter=new MusicAdapter(getContext(),musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        }
        return view;

    }
}