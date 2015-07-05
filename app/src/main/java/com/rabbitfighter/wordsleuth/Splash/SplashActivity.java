package com.rabbitfighter.wordsleuth.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Activities.InstructionActivity;
import com.rabbitfighter.wordsleuth.R;


/**
 * Splash screen
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android
 * @see 'http://developer.android.com/guide/components/loaders.html'
 * @since on 2015-05-20.
 */
@SuppressWarnings("unused")
public class SplashActivity extends Activity {

    private final String TAG = "splashActivity";

    public void startNewActivity() {
        Intent i = new Intent(this, InstructionActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "splashActivity() started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        showSplashScreen();

    }

    public void showSplashScreen() {
        /*
        Runnable thread for waiting for the splash screen
        while not hogging resources.
         */
        Thread myThread;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis() + 2000; // 2 seconds
                while (System.currentTimeMillis() < futureTime) {
                    synchronized (this) {
                        try {
                            wait(futureTime-System.currentTimeMillis());
                        } catch (Exception e) {
                            // TODO: IDK, handle this some way...
                        } finally {
                            startNewActivity();
                        }
                    }
                }
            }
        });
        myThread.start();
    }

    @Override
    protected void onPause() {
        // Destroy assets
        super.onPause();
        finish();
    }

}
