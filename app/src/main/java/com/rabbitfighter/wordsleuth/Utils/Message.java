package com.rabbitfighter.wordsleuth.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rabbitfighter on 6/20/15 to make toasting easier
 */
public class Message {
    public static void msgLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void msgShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
