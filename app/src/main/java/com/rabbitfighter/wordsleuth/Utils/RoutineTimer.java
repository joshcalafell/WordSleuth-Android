package com.rabbitfighter.wordsleuth.Utils;

import android.util.Log;

/**
 * Created by rabbitfighter on 6/25/15 for Stephen. To time dictionaries.
 */
@SuppressWarnings("unused")
public class RoutineTimer {
    private final static String TAG = "RoutineTimer";
    private final static long MILLISECONDS_IN_SECOND = 1000;

    private long startTime;
    private long endTime;
    private long total;

    public RoutineTimer() {
        //Empty constructor
        Log.i(TAG, "Timer created");
    }

    public void start() {
        Log.i(TAG, "Timer started");
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
        Log.i(TAG, "Timer stopped");
        this.total = endTime - startTime;
        Log.i(TAG, "Timer recorded " + this.totalSeconds());
    }

    public String totalSeconds() {
        return String.valueOf(this.getTotal() + " milliseconds (1000ths)");
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getTotal() {
        return total;
    }

}
