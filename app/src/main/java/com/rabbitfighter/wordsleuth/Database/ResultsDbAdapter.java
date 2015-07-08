package com.rabbitfighter.wordsleuth.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Entries.Result;

import java.util.ArrayList;

import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_RESULT_TYPE;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_SCRABBLE_POINTS;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_WORD;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_WORD_LENGTH;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.TABLE_NAME;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.UID;


/**
 * SQLite Database. There are many like it but this one is mine...
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/reference/android/database/sqlite/package-summary.html'
 * @since 0.1 2015-06-17.
 */
public class ResultsDbAdapter {

    // Debugging tag
    public final static String TAG = "DictionaryDbAdapter";

    // Result types
    private static final String RESULT_TYPE_ANAGRAM = "anagram";
    private static final String RESULT_TYPE_SUBWORD = "subword";
    private static final String RESULT_TYPE_COMBO   = "combo";

    // Handle for helper
    ResultsDbHelper helper;

    // Constructor gets access to inner helper class
    public ResultsDbAdapter(Context context) {
        helper = new ResultsDbHelper(context);
    }

    /* -------------- */
    /* --- Helper --- */
    /* -------------- */

    /**
     * SQLite open helper class
     */
     static class ResultsDbHelper extends SQLiteOpenHelper {

        public static final String TAG = "ResultsDbHelper";

        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 55;
        public static final String DATABASE_NAME = "wordsleuth.db";

        /* ------------------------------ */
        /* --- Contract and constants --- */
        /* ------------------------------ */
        /**
         * Database contract
         */
        public class ResultsDbContract {

            // To prevent someone from accidentally instantiating the contract class,
            // give it an empty constructor.
            public ResultsDbContract() {}

            /* --- Inner class that defines the table contents --- */

            /*
            By implementing the BaseColumns interface, your inner class can inherit a primary key
            field called _ID that some Android classes such as cursor adaptors will expect it to have.
            It's not required, but this can help your database work harmoniously with the Android
            framework.
            */
            public abstract class ResultEntry implements BaseColumns {
                // Table name
                public static final String TABLE_NAME = "results";
                // Table columns
                public static final String UID = "_id";
                public static final String COLUMN_NAME_RESULT_TYPE = "resultype";
                public static final String COLUMN_NAME_WORD = "word";
                public static final String COLUMN_NAME_WORD_LENGTH = "length";
                public static final String COLUMN_NAME_SCRABBLE_POINTS = "scrabblepoints";
                public static final String COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS = "wordspoints";
                // Table text
                private static final String ID_INCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
                private static final String TEXT_TYPE = " TEXT";
                private static final String INTEGER_TYPE = " INTEGER";
                private static final String AND = " AND ";
                private static final String COMMA_SEP = ", ";
                private static final String PARENTHESIS_LEFT = " (";
                private static final String PARENTHESIS_RIGHT = " )";
                // Table commands
                private static final String CREATE_TABLE = "CREATE TABLE ";
                private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
                private static final String DELETE_FROM_ALL = "DELETE * FROM ";
                // Table creation
                private static final String SQL_CREATE_ENTRIES =
                        CREATE_TABLE + TABLE_NAME + PARENTHESIS_LEFT +             // Create table
                            UID + ID_INCREMENT + COMMA_SEP +                       // _id
                            COLUMN_NAME_RESULT_TYPE + TEXT_TYPE + COMMA_SEP +      // result type
                            COLUMN_NAME_WORD + TEXT_TYPE + COMMA_SEP +             // word
                            COLUMN_NAME_WORD_LENGTH + TEXT_TYPE + COMMA_SEP +      // word length
                            COLUMN_NAME_SCRABBLE_POINTS + TEXT_TYPE + COMMA_SEP +  // sc points
                            COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS + TEXT_TYPE +    // wwf points
                        PARENTHESIS_RIGHT;
                // Table deletion
                private static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;
                private static final String SQL_DELETE_ALL_ENTRIES = DROP_TABLE + TABLE_NAME;

            }
        }// End contract

        /* ------------------- */
        /* --- Constructor --- */
        /* ------------------- */

        /**
         *
         * @param context - the context
         */
        public ResultsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.i(TAG, "Database " + DATABASE_NAME + " version " + DATABASE_VERSION + " created");
        }


        /**
         * On creation
         * @param db -  the database
         */
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "" + db.getPath() + " created");
            db.execSQL(ResultsDbContract.ResultEntry.SQL_CREATE_ENTRIES);
        }

        /**
         * On upgrade
         * @param db - the database
         * @param oldVersion -  the old database version int
         * @param newVersion -  the new database version int
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Database " + db + " version " + oldVersion + " upgraded to " + newVersion);
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(ResultsDbContract.ResultEntry.SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        /**
         * On downgrade
         * @param db - the database
         * @param oldVersion -  the old database version int
         * @param newVersion -  the new database version int
         */
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Database " + db + " version " + oldVersion + " downgraded to " + newVersion);
            onUpgrade(db, oldVersion, newVersion);
        }

    }

    /**
     * Delete all entries
     */
    public void deleteEntries() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        //db.execSQL(ResultsDbHelper.ResultsDbContract.ResultEntry.DELETE_FROM_ALL + TABLE_NAME);
    }

    /* -------------------------- */
    /* --- Database insertion --- */
    /* -------------------------- */

    /**
     * Insert a result into the database
     * @param resultType
     * @param word
     * @param length
     * @param scrabblePoints
     * @param wordsPoints
     * @return
     */
    public long insertData(
            // Insertion types       // Info
            // ---------------       --------------
            String resultType,       // Result type
            String word,             // The word
            String length,           // The length
            String scrabblePoints,   // Scrabble(TM) point value
            String wordsPoints       // Words With Friends(TM) point value
    ) {
        // Insert data
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_RESULT_TYPE, resultType);
        contentValues.put(COLUMN_NAME_WORD, word);
        contentValues.put(COLUMN_NAME_WORD_LENGTH, length);
        contentValues.put(COLUMN_NAME_SCRABBLE_POINTS, scrabblePoints);
        contentValues.put(COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS, wordsPoints);
        // Returns -1 if fails, otherwise, returns
        return db.insert(TABLE_NAME, null, contentValues);
    }//End insertData()


    /* -------------------------- */
    /* --- Database retrieval --- */
    /* -------------------------- */

    /**
     * Get the number of anagrams in the database
     * @return the number of anagrams
     */
    public int getNumberAnagrams() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE
        };
        String[] selectionArgs = null;
        String selection = COLUMN_NAME_RESULT_TYPE + " = '"+RESULT_TYPE_ANAGRAM+"'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(

                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * Get the number of subwords in the database
     * @return the number of subwords
     */
    public int getNumberSubwords() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE
        };
        String[] selectionArgs = null;
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_SUBWORD+ "'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(

                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    /**
     * Get the number of combos in the database
     * @return the number of combos
     */
    public int getNumberCombos() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE
        };
        String[] selectionArgs = null;
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_COMBO+ "'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(

                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * Get all the anagrams from DB in an ArrayList and return it
     * @return the anagram results list
     */
    public ArrayList<Result> getAnagrams(){
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE,
                COLUMN_NAME_WORD,
                COLUMN_NAME_WORD_LENGTH,
        };
        String[] selectionArgs = null;
        // Result type = 'anagram'
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_ANAGRAM+ "'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // Cursor
        Cursor cursor = db.query(
                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        // String buffer
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int columnResult = cursor.getColumnIndex(ResultsDbHelper.ResultsDbContract.ResultEntry.COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult)));
        }
        cursor.close();
        // Return the list
        return resultList;
    }

    /**
     * Get all the subwords from DB in an ArrayList and return it
     * @return the subword results list
     */
    public ArrayList<Result> getSubwords(){
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE,
                COLUMN_NAME_WORD,
                COLUMN_NAME_WORD_LENGTH,
        };
        String[] selectionArgs = null;
        // Result type = 'anagram'
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_SUBWORD+ "'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // Cursor
        Cursor cursor = db.query(
                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        // String buffer
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int columnResult = cursor.getColumnIndex(COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult).toString()));
        }
        cursor.close();
        // Return the list
        return resultList;
    }

    /**
     * Get all the combos from DB in an ArrayList and return it
     * @return the combos results list
     */
    public ArrayList<Result> getCombos(){
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {
                UID,
                COLUMN_NAME_RESULT_TYPE,
                COLUMN_NAME_WORD,
                COLUMN_NAME_WORD_LENGTH,
        };
        String[] selectionArgs = null;
        // Result type = 'anagram'
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" + RESULT_TYPE_COMBO + "'";
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // Cursor
        Cursor cursor = db.query(
                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
        );
        // String buffer
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int columnResultType = cursor.getColumnIndex(COLUMN_NAME_RESULT_TYPE);
            int columnResult = cursor.getColumnIndex(COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult)));
        }
        cursor.close();
        // Return the list
        return resultList;
    }

    /* ---------------------------- */
    /* --- End Database Methods --- */
    /* ---------------------------- */

}