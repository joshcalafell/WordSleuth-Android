package com.rabbitfighter.wordsleuth.AboutScreen;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.rabbitfighter.wordsleuth.R;

import java.io.IOException;

/**
 * App instructions. Five fragments
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1 2015-06-17.
 */

public class AboutActivity extends ActionBarActivity {
    public final static String TAG = "AboutActivity";
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Action bar stuff
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() called");
        startMusic();
    }

    @Override
    protected void onDestroy() {
        if (mp != null) {
            mp.release();
        }
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        if (mp != null) {
            mp.release();
        }
        super.onStop();
    }

    private void startAnim() {
        String names = "ertgd";
        TextView txtview1 = (TextView)findViewById(R.id.Names);
    }

    private void startMusic() {
        mp = new MediaPlayer();
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("music/tatu-dangerous_and_moving.mid");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mp.prepare();
            mp.setVolume(1f, 1f);
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} // EOF
