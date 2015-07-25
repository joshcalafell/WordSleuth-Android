package com.rabbitfighter.wordsleuth.AboutScreen;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;

import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Utils.RobotoFontsHelper;

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
    String outText = "";
    TextView tv_names;

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

        tv_names = (TextView)findViewById(R.id.Names);

        /* Set typefaces */
        tv_names.setTypeface(RobotoFontsHelper.getTypeface(this, RobotoFontsHelper.roboto_regular));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() called");
        startMusicThread();
        startAnim();
    }

    private void startMusicThread() {
        MusicThread musicThread = new MusicThread();
        musicThread.start();
    }

    private void startAnim() {
        AnimThread animWork = new AnimThread();
        animWork.start();
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

    private void updateResults(CharSequence cs) {
        tv_names.append(cs);
    }

    public class MusicThread extends Thread {
        private static final String TAG = "MusicThread";

        @Override
        public void run() {
            Log.v(TAG, "Music Thread");

            startMusic();
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

    }

    public class AnimThread extends Thread {
        private static final String TAG = "AnimThread";
        private final String names = "Coded by Stephen Chavez and Josh\n This is a test of brain power..." +
                "\n\n\n\n" +
                "....................";
        private CharSequence inWorkText;

        @Override
        public void run() {
            Log.v(TAG, "doing work in Anim Thread");
            for (int i = 0; i < names.length(); i++) {


                inWorkText = names.substring(i, i + 1);

                publishProgress(inWorkText);
                SystemClock.sleep(250);
            }
        }

        private void publishProgress(final CharSequence newCS) {
            Log.v(TAG, "reporting back from the Anim Thread");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateResults(newCS);
                }
            });
        }
    }

} // EOF
