package com.rabbitfighter.wordsleuth.Utils;

import android.content.Context;

/**
 * Created by stephen on 7/26/15.
 */
public class AppRaterUtil {
    private static final String PLAY_STORE_APP_ID = "com.android.vending";

    // Checks if the app came from Google Play.
    public static boolean IsInstalledByGooglePlay(final Context context) {

        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // Returns true if installer is not true and Google Play installer was used.
        return installer != null && installer.startsWith(PLAY_STORE_APP_ID);

    }

}
