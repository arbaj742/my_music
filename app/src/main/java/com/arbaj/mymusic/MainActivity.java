package com.arbaj.mymusic;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;



import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    static ArrayList<MusicFiles> albums = new ArrayList<>();
    private String MY_SORT_PREF = "SortOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();

    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.
                            WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);

        } else {

            musicFiles = getAllAudio(this);
            initViewpager();

        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                musicFiles = getAllAudio(this);
                initViewpager();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);
        }

    }

    private void initViewpager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Songsfragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            for (Fragment fragment : this.fragments = new ArrayList<>()) {

            }

            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<String> duplicate = new ArrayList<>();
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID

        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                //take a log.e for check

                tempAudioList.add(musicFiles);
                if (!duplicate.contains(album)) {
                    albums.add(musicFiles);
                    duplicate.add(album);
                }
            }
            cursor.close();
        }
        return tempAudioList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toUpperCase();
        ArrayList<MusicFiles> myFiles = new ArrayList<>();
        for (MusicFiles song : musicFiles) {
            if (song.getTitle().toLowerCase().contains(userInput)) {
                myFiles.add(song);
            }
        }
        Songsfragment.musicAdapter.updateList(myFiles);

        return true;
    }




}