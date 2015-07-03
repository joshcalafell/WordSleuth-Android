package com.rabbitfighter.wordsleuth.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Roboto fonts loader
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android
 * @see 'http://www.google.com/design/spec/style/typography.html#typography-language-categorization'
 * @since on 2015-05-21.
 */
public class RobotoFontsHelper {

    // fonts
    public static final int roboto_black = 0;
    public static final int roboto_black_italic = 1;
    public static final int roboto_bold = 2;
    public static final int roboto_bold_italic = 3;
    public static final int roboto_italic = 4;
    public static final int roboto_light = 5;
    public static final int roboto_light_italic = 6;
    public static final int roboto_medium = 7;
    public static final int roboto_medium_italic = 8;
    public static final int roboto_regular = 9;
    public static final int roboto_thin = 10;
    public static final int roboto_thin_italic = 11;
    public static final int roboto_condensed_bold = 12;
    public static final int roboto_condensed_bold_italic = 13;
    public static final int roboto_condensed_italic = 14;
    public static final int roboto_condensed_light = 15;
    public static final int roboto_condensed_light_italic = 16;
    public static final int roboto_condensed_regular = 17;

    // Paths
    public static final String[] fontPath = {
            "fonts/Roboto-Black.ttf",
            "fonts/Roboto-BlackItalic.ttf",
            "fonts/Roboto-Bold.ttf",
            "fonts/Roboto-BoldItalic.ttf",
            "fonts/Roboto-Italic.ttf",
            "fonts/Roboto-Light.ttf",
            "fonts/Roboto-LightItalic.ttf",
            "fonts/Roboto-Medium.ttf",
            "fonts/Roboto-MediumItalic.ttf",
            "fonts/Roboto-Regular.ttf",
            "fonts/Roboto-Thin.ttf",
            "fonts/Roboto-ThinItalic.ttf",
            "fonts/Roboto-Light.ttf",
            "fonts/RobotoCondensed-Bold.ttf",
            "fonts/RobotoCondensed-BoldItalic.ttf",
            "fonts/RobotoCondensed-Italic.ttf",
            "fonts/RobotoCondensed-Light.ttf",
            "fonts/RobotoCondensed-LightItalic.ttf",
            "fonts/RobotoCondensed-Regular.ttf",
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

