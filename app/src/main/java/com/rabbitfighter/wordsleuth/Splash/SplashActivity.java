package com.rabbitfighter.wordsleuth.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Activities.InstructionActivity;
import com.rabbitfighter.wordsleuth.Activities.SearchActivity;
import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Utils.HelpDialogUtil;

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
     * Start the new activity
     */
    public void startNewActivity() {

        final boolean isHelpOn = HelpDialogUtil.isHelpEnabledOnAppStart(this);

        if(isHelpOn) {
            Intent instructionStart = new Intent(this, InstructionActivity.class);
            startActivity(instructionStart);
        } else {
            Intent searchStart = new Intent(this, SearchActivity.class);
            startActivity(searchStart);
        }
    }

}//EOF
