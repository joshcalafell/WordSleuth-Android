package com.rabbitfighter.wordsleuth.Splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Activities.InstructionActivity;
import com.rabbitfighter.wordsleuth.Activities.SearchActivity;
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

}
