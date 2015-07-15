package com.rabbitfighter.wordsleuth.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.SearchFragments.SearchInputFragment;
import com.rabbitfighter.wordsleuth.Splash.SplashActivity;

/**
 * Created by stephen on 7/14/15.
 */
public class HelpDialogUtil {
    /**
     * Determine if help is enabled (Thanks bro :) )
     * @return
     * @param ctx
     */
    public static boolean isHelpEnabledOnAppStart(Context ctx) {
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(
                ctx.getString(R.string.PREFERENCE_FILE_KEY),
                Context.MODE_PRIVATE);
        boolean defaultSetting = true;

        // this will return true if the user wants help on app start
        boolean userSetting = sharedPrefs.getBoolean(
                ctx.getString(R.string.app_setting_disable_help), defaultSetting);
        return  userSetting;
    }
}
