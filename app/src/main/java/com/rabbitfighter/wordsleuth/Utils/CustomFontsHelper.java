package com.rabbitfighter.wordsleuth.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Custom fonts loader
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android
 * @see 'http://www.google.com/design/spec/style/typography.html#typography-language-categorization'
 * @since on 2015-05-21.
 */
public class CustomFontsHelper {

    // fonts
    public static final int blox = 0;
    public static final int folks_in_cube = 1;
    public static final int gm__ll = 2;
    public static final int gm_lr = 3;
    public static final int gm_ul = 4;
    public static final int gm_ur = 5;
    public static final int highlevel = 6;
    public static final int intaglio = 7;
    public static final int vintgeone = 8;

    // Paths
    public static final String[] fontPath = {
            "fonts/Blox2.ttf",
            "fonts/FolksInCube.ttf",
            "fonts/GautsMotelLowerLeft.ttf",
            "fonts/GautsMotelLowerRight.ttf",
            "fonts/GautsMotelUpperLeft.ttf",
            "fonts/GautsMotelUpperRight.ttf",
            "fonts/HighLevel.ttf",
            "fonts/Intaglio.ttf",
            "fonts/VintageOne.ttf"
    };

    public static boolean fontsLoaded = false;

    public static Typeface[] fonts = new Typeface[fontPath.length];

    /**
     * Returns a loaded custom font based on its identifier.
     *
     * @param context        - the current context
     * @param fontIdentifier - the identifier of the requested font
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {

        //If fonts aren't loaded, load them
        if (!fontsLoaded) {
            loadRobotoFonts(context);
        }

        //Return the typeface selected
        return fonts[fontIdentifier];
    }

    /**
     * Load all the fonts
     *
     * @param context - the current context
     */
    public static void loadRobotoFonts(Context context) {
        for (int i = 0; i < fontPath.length; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}


