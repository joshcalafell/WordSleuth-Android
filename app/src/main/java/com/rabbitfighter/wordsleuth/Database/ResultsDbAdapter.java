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

import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.*;
import static com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter.ResultsDbHelper.ResultsDbContract.ResultEntry.LENGTH;


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
                public static final String ID_INCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
                public static final String TEXT_TYPE = " TEXT";
                public static final String INTEGER_TYPE = " INTEGER";
                public static final String AND = " AND ";
                public static final String EQUALS = " = ";
                public static final String COMMA_SEP = ", ";
                public static final String PARENTHESIS_LEFT = " (";
                public static final String PARENTHESIS_RIGHT = " )";
                public static final String SINGLE_QUOTE = "'";
                // Table commands
                public static final String CREATE_TABLE = "CREATE TABLE ";
                public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
                public static final String DELETE_FROM_ALL = "DELETE * FROM ";
                public static final String DESCENDING = " DESC";
                public static final String ASCENDING = " ASC";
                public static final String LENGTH = " length";

                // Table creation
                public static final String SQL_CREATE_ENTRIES =
                        CREATE_TABLE + TABLE_NAME + PARENTHESIS_LEFT +             // Create table
                                UID + ID_INCREMENT + COMMA_SEP +                       // _id
                                COLUMN_NAME_RESULT_TYPE + TEXT_TYPE + COMMA_SEP +      // result type
                                COLUMN_NAME_WORD + TEXT_TYPE + COMMA_SEP +             // word
                                COLUMN_NAME_WORD_LENGTH + TEXT_TYPE + COMMA_SEP +      // word length
                                COLUMN_NAME_SCRABBLE_POINTS + TEXT_TYPE + COMMA_SEP +  // sc points
                                COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS + TEXT_TYPE +    // wwf points
                                PARENTHESIS_RIGHT;
                // Table deletion
                public static final String SQL_DELETE_ENTRIES = DROP_TABLE + TABLE_NAME;
                public static final String SQL_DELETE_ALL_ENTRIES = DROP_TABLE + TABLE_NAME;

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

    /* -------------------- */
    /* --- Get anagrams --- */
    /* -------------------- */

    /**
    Parameters
    ----------
    table - The table name to compile the query against.
    columns	- A list of which columns to return. Passing null will return all columns, which is
             discouraged to prevent reading data from storage that isn't going to be used.
    selection -  A filter declaring which rows to return, formatted as an SQL WHERE clause
                (excluding the WHERE itself). Passing null will return all rows for the given table.
    selectionArgs - You may include ?s in selection, which will be replaced by the values from
                    selectionArgs, in order that they appear in the selection. The values will
                    be bound as Strings.
    groupBy - A filter declaring how to group rows, formatted as an SQL GROUP BY clause
             (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
    having - A filter declare which row groups to include in the cursor, if row grouping is being
             used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null
             will cause all row groups to be included, and is required when row grouping is
             not being used.
    orderBy	- How to order the rows, formatted as an SQL ORDER BY clause
             (excluding the ORDER BY itself). Passing null will use the default sort order,
             which may be unordered.
    limit - Limits the number of rows returned by the query, formatted as LIMIT clause.
            Passing null denotes no LIMIT clause.

    Returns
    -------
    A Cursor object, which is positioned before the first entry. Note that Cursors are not
    synchronized, see the documentation for more details.

    @see 'http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#query(
      java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String,
      java.lang.String, java.lang.String, java.lang.String)'
     */
    public ArrayList<Result> getAnagrams(int sortOrder) {
        Cursor cursor;
        // Get the sort order string based on the sort order passed in
        String sort = getSortOrderString(sortOrder);
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {COLUMN_NAME_WORD};
        String[] selectionArgs = null;
        String selection = COLUMN_NAME_RESULT_TYPE + EQUALS + SINGLE_QUOTE +
                           RESULT_TYPE_ANAGRAM + SINGLE_QUOTE;
        String groupBy = null;
        String having = null;
        // Order by
        String orderBy = sort;
        // Cursor
        cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        // Add results to list
        while (cursor.moveToNext()) {
            int columnResult = cursor.getColumnIndex(COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult)));
        }
        // close the cursor
        cursor.close();
        // Return the list
        return resultList;

    }


    /* -------------------- */
    /* --- Get Subwords --- */
    /* -------------------- */

    public ArrayList<Result> getSubwords(int sortOrder) {
        Cursor cursor;
        // Get the sort order string based on the sort order passed in
        String sort = getSortOrderString(sortOrder);
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {COLUMN_NAME_WORD};
        String[] selectionArgs = null;
        // Result type = 'anagram'
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_SUBWORD+ "'";
        String groupBy = null;
        String having = null;
        // Order by
        String orderBy = sort;
        // Cursor
        cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,groupBy,having,orderBy);
        while (cursor.moveToNext()) {
            int columnResult = cursor.getColumnIndex(COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult)));
        }
        cursor.close();
        // Return the list
        return resultList;

    }

    /* ------------------ */
    /* --- Get combos --- */
    /* ------------------ */

    public ArrayList<Result> getCombos(int sortOrder) {
        Cursor cursor;
        // Get the sort order string based on the sort order passed in
        String sort = getSortOrderString(sortOrder);
        // Array list to hold results
        ArrayList<Result> resultList = new ArrayList<>();
        // Get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        // Designate the columns
        String[] columns = {COLUMN_NAME_WORD};
        String[] selectionArgs = null;
        // Result type = 'anagram'
        String selection = COLUMN_NAME_RESULT_TYPE + " = '" +RESULT_TYPE_COMBO+ "'";
        String groupBy = null;
        String having = null;
        // Order by
        String orderBy = sort;
        // Cursor
        cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,groupBy,having,
                orderBy
        );
        while (cursor.moveToNext()) {
            int columnResult = cursor.getColumnIndex(COLUMN_NAME_WORD);
            // Add the result to the list to return
            resultList.add(new Result(cursor.getString(columnResult)));
        }
        cursor.close();
        // Return the list
        return resultList;

    }

    /* ---------------------------------------------------------- */
    /* --- Get numbers of anagram, subword, and combo matches --- */
    /* ---------------------------------------------------------- */

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
     * Returns the sort order string to use in SQLite query.
     * @param sortOrder - the integer that represents the sort order
     * @return the sort order
     */
    private String getSortOrderString(int sortOrder) {
        // String Builder guy
        StringBuilder sort = new StringBuilder();
        // Sort order is determined here
        switch (sortOrder) {
            // By word length, ascending
            case 0:
                sort.append(LENGTH).append(ASCENDING);
                break;
            // By word length, descending
            case 1:
                sort.append(LENGTH).append(DESCENDING);
                break;
            // By scrabble(TM) points, ascending
            case 2:
                sort.append(COLUMN_NAME_SCRABBLE_POINTS).append(DESCENDING);
                break;
            // By scrabble(TM) descending
            case 3:
                sort.append(COLUMN_NAME_WORDS_WITH_FRIENDS_POINTS).append(DESCENDING);
                break;
            // By words With Friends(TM) points ascending

            default:
                Log.i(TAG, "Something went wrong with sortOrder in getAnagrams("+sortOrder+")");
                break;
        }
        // Return the sort order...
        return sort.toString();
    }


}