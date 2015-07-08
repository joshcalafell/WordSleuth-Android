package com.rabbitfighter.wordsleuth.Splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Activities.InstructionActivity;
import com.rabbitfighter.wordsleuth.Activities.SearchActivity;
import com.rabbitfighter.wordsleuth.R;

/**
 * Splash screen
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android
 * @see 'Splash screens, Async tasks'
 * @since on 2015-05-20.
 */
@SuppressWarnings("unused")
public class SplashActivity extends Activity {

    private final String TAG = "splashActivity";

    /**
     * On creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new AsyncSplashTask().execute();
    }

    /**
     * On pause
     */
    @Override
    protected void onPause() {
        // Destroy assets
        super.onPause();
        finish();
    }


    /**
     * This is to make the searches bound service asynchronous.
     */
    private class AsyncSplashTask extends AsyncTask<Void, Void, Void> {
        /**
         * 1) Pre execution
         */
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Program started.");
        }
        /**
         * 2) Async
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            showSplashScreen();
            return null;
        }
        /**
         * 3) Post execute
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            startNewActivity();
        }
    }

    /**
     * Show the splash screen
     */
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
                            Log.i(TAG, "Splash failed...");
                        } finally {
                            Log.i(TAG, "Splash screen finished");
                        }
                    }
                }
            }
        });
        myThread.start();
    }

    /**
     * Determine if help is enabled (Thanks bro :) )
     * @return
     */
    private boolean isHelpEnabledOnAppStart() {
        Context context = SplashActivity.this;
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
        boolean defaultSetting = true;

        // this will return true if the user wants help on app start
        boolean userSetting = sharedPrefs.getBoolean(
                getString(R.string.app_setting_disable_help), defaultSetting);
        return  userSetting;
    }

    /**
     * Start the new activity
     */
    public void startNewActivity() {
        final boolean isHelpOn = isHelpEnabledOnAppStart();

        if(isHelpOn) {
            Intent instructionStart = new Intent(this, InstructionActivity.class);

            // For future use when we need to check if we need to display a dialog.
            instructionStart.putExtra("isHelpOn", isHelpOn);
            startActivity(instructionStart);
        } else {
            Intent searchStart = new Intent(this, SearchActivity.class);
            startActivity(searchStart);
        }
    }

}//EOF
