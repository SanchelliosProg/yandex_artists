package com.portfolio.sanchellios.yandexmusictraining.views;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.portfolio.sanchellios.yandexmusictraining.R;
import com.portfolio.sanchellios.yandexmusictraining.artist.Artist;
import com.portfolio.sanchellios.yandexmusictraining.artist.Oeuvre;
import com.portfolio.sanchellios.yandexmusictraining.database.ImageDbManager;
import com.portfolio.sanchellios.yandexmusictraining.string_formating.ArtistInfoFormatter;
import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {
    public static String ARTIST = "ARTIST";
    private Artist artist;
    private ImageView cover;
    private TextView genres;
    private TextView albumsAndSongs;
    private TextView bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getArtistFromIntent();
        initTheActivityElements();
        populateTheScreenWithData();
    }

    @Override
    protected void onDestroy() {
        //saveCoverToDb();
        super.onDestroy();
    }

    private void getArtistFromIntent(){
        Intent intent = getIntent();
        artist = intent.getParcelableExtra(ARTIST);
    }

    private void initTheActivityElements(){
        cover = (ImageView)findViewById(R.id.d_frag_cover_image_view);
        genres = (TextView)findViewById(R.id.d_frag_artist_genres);
        albumsAndSongs = (TextView)findViewById(R.id.d_frag_artist_albums_and_songs);
        bio = (TextView)findViewById(R.id.d_frag_artist_bio);
    }

    private void populateTheScreenWithData(){
        setTitle(artist.getName());
        setTheCover();
        albumsAndSongs.setText(
                ArtistInfoFormatter
                        .getAlbumsAndSongsWithInterpunct(new Oeuvre(artist)));
        bio.setText(artist.getDescription());
        genres.setText(ArtistInfoFormatter.formGenreString(artist.getGenres()));
    }

    private void setTheCover(){
        Picasso.with(getApplicationContext())
                .load(artist.getCover().getBigCover())
                .placeholder(R.drawable.ic_music_note_black_48dp)
                .error(R.drawable.unknown_artist)
                .into(cover);
    }

    private void saveCoverToDb(){
        Bitmap bitmap = ((BitmapDrawable)cover.getDrawable()).getBitmap();
        ImageDbManager imageDbManager = new ImageDbManager(getApplicationContext());
        imageDbManager.insertSmallCoverIntoDb(bitmap);
    }
}