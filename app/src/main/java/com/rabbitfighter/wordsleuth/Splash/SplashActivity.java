package com.rabbitfighter.wordsleuth.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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
 * @version 0.3 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android
 * @see 'Splash screens, Async tasks'
 * @since on 2015-05-20.
 */
@SuppressWarnings("unused")
public class SplashActivity extends Activity {

    private final String TAG = "splashActivity";

    /**
     * On creation
     *
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
     * Show the splash screen
     */
    public void showSplashScreen() {

        try {
            SystemClock.sleep(3000);
        } catch (Exception e) {
            Log.i(TAG, "Splash failed...");
        } finally {
            Log.i(TAG, "Splash screen finished");
        }
    }

    /**
     * Start the new activity
     */
    public void startNewActivity() {
        final boolean isHelpOn = HelpDialogUtil.isHelpEnabledOnAppStart(this);

        if (isHelpOn) {
            Intent instructionStart = new Intent(this, InstructionActivity.class);

            // For future use when we need to check if we need to display a dialog.
            instructionStart.putExtra("isHelpOn", isHelpOn);
            startActivity(instructionStart);
        } else {
            Intent searchStart = new Intent(this, SearchActivity.class);
            startActivity(searchStart);
        }
    }

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
         *
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
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            startNewActivity();
        }
    }

}//EOF
