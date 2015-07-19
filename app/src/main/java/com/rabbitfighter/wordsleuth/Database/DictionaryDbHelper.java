package com.rabbitfighter.wordsleuth.Database;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Entries.Result;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * SQLite Dictionary Database. There are many like it but this one is mine...
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/reference/android/database/sqlite/package-summary.html'
 * @since 0.1 2015-07-17.
 */
public class DictionaryDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";


    // The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.rabbitfighter.wordsleuth/databases/";
    // Database name
    private static String DB_NAME = "dictionary.db";

    // Database, and context
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    // Table columns
    public static final String TABLE_NAME = "dictionary";
    public static final String UID = "_id";
    public static final String COLUMN_NAME_WORD = "word";
    public static final String COLUMN_NAME_WORD_SORTED ="wordSorted";
    public static final String COLUMN_NAME_WORD_LENGTH = "length";
    public static final String COLUMN_NAME_COUNT_A = "count_A";
    public static final String COLUMN_NAME_COUNT_B = "count_B";
    public static final String COLUMN_NAME_COUNT_C = "count_C";
    public static final String COLUMN_NAME_COUNT_D = "count_D";
    public static final String COLUMN_NAME_COUNT_E = "count_E";
    public static final String COLUMN_NAME_COUNT_F = "count_F";
    public static final String COLUMN_NAME_COUNT_G = "count_G";
    public static final String COLUMN_NAME_COUNT_H = "count_H";
    public static final String COLUMN_NAME_COUNT_I = "count_I";
    public static final String COLUMN_NAME_COUNT_J = "count_J";
    public static final String COLUMN_NAME_COUNT_K = "count_K";
    public static final String COLUMN_NAME_COUNT_L = "count_L";
    public static final String COLUMN_NAME_COUNT_M = "count_M";
    public static final String COLUMN_NAME_COUNT_N = "count_N";
    public static final String COLUMN_NAME_COUNT_O = "count_O";
    public static final String COLUMN_NAME_COUNT_P = "count_P";
    public static final String COLUMN_NAME_COUNT_Q = "count_Q";
    public static final String COLUMN_NAME_COUNT_R = "count_R";
    public static final String COLUMN_NAME_COUNT_S = "count_S";
    public static final String COLUMN_NAME_COUNT_T = "count_T";
    public static final String COLUMN_NAME_COUNT_U = "count_U";
    public static final String COLUMN_NAME_COUNT_V = "count_V";
    public static final String COLUMN_NAME_COUNT_W = "count_W";
    public static final String COLUMN_NAME_COUNT_X = "count_X";
    public static final String COLUMN_NAME_COUNT_Y = "count_Y";
    public static final String COLUMN_NAME_COUNT_Z = "count_Z";
    public static final String COLUMN_NAME_COUNT_= "count_";

    // SQL statement constants
    public static final String UNION = " UNION ";
    private static final String ORDER_BY_LENGTH = " ORDER BY length";
    private static final String SEMICOLON = "; ";
    private static final String SELECT_FROM_DICT_WHERE = " SELECT * FROM dictionary WHERE ";
    private static final String AND = " AND ";
    private static final String LENGTH_EQUALS = " length=";
    private static final String PLUS = " + ";
    private static final String VALID_CHAR_REGEX = "[^a-z]";
    private static final String WORD_LIKE = " word LIKE ";
    
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application
     * assets and resources.
     * @param context
     */
    public DictionaryDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /* ------------------------ */
    /* --- Override Methods --- */
    /* ------------------------ */

    /**
     * On creation
     * @param db -  the database
     */
    public void onCreate(SQLiteDatabase db) {
        //Nothing to do here
    }

    /**
     * On upgrade
     * @param db - the database
     * @param oldVersion -  the old database version int
     * @param newVersion -  the new database version int
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Database " + db + " version " + oldVersion + " upgraded to " + newVersion);
    }

    /**
     * On downgrade
     * @param db - the database
     * @param oldVersion -  the old database version int
     * @param newVersion -  the new database version int
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Database " + db + " version " + oldVersion + " downgraded to " + newVersion);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
        if(checkDataBase()){
            //do nothing - database already exist
            Log.i(TAG, "Database already exists... Nothing to do.");
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
                Log.i(TAG, "Successfully copied database");
            } catch (IOException e) {
                Log.i(TAG, "Error copying");
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database doesn't exist yet.
            Log.i(TAG, "Database doesn't exist yet");
        }
        // Check for null db
        if(checkDB != null){
            checkDB.close();
            Log.i(TAG, "Closed dict database");
        }
        // Return null or not null
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     * */
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null) {
            Log.i(TAG, "Closing dictionary database");
            myDataBase.close();
        }
        super.close();
    }

    /* -------------------------- */
    /* --- Database retrieval --- */
    /* -------------------------- */

    /**
     * Get the Anagrams in the database
     * @return the number of anagrams
     */
    @SuppressWarnings("unused")
    public ArrayList<Result> wildcardSearch(

            // Params. Yeah, I know...
            int count_A, int count_B, int count_C, int count_D, int count_E, int count_F,
            int count_G, int count_H, int count_I, int count_J, int count_K, int count_L,
            int count_M, int count_N, int count_O, int count_P, int count_Q, int count_R,
            int count_S, int count_T, int count_U, int count_V, int count_W, int count_X,
            int count_Y, int count_Z, int count_WILDCARDS
    ) {

        // Get the database helper to get access to everything...
        SQLiteDatabase db = this.getWritableDatabase();
        // List to hold matches
        ArrayList<Result> resultList = new ArrayList<>();

        // 1) Create a hash map of the words individual letters
        Map<Character, Integer> valueMap = new HashMap<>();
        valueMap.put('a', count_A); valueMap.put('b', count_B); valueMap.put('c', count_C);
        valueMap.put('d', count_D); valueMap.put('e', count_E); valueMap.put('f', count_F);
        valueMap.put('g', count_G); valueMap.put('h', count_H); valueMap.put('i', count_I);
        valueMap.put('j', count_A); valueMap.put('k', count_K); valueMap.put('l', count_L);
        valueMap.put('m', count_M); valueMap.put('n', count_N); valueMap.put('o', count_O);
        valueMap.put('p', count_P); valueMap.put('q', count_Q); valueMap.put('r', count_R);
        valueMap.put('s', count_S); valueMap.put('t', count_T); valueMap.put('u', count_U);
        valueMap.put('v', count_V); valueMap.put('w', count_W); valueMap.put('x', count_X);
        valueMap.put('y', count_Y); valueMap.put('z', count_Z);
        valueMap.put('*', count_WILDCARDS);

        // 2) Make an array list of all the letters that have a value greater than zero
        Map<Character, Integer> chars = new HashMap<>();
        StringBuilder charString = new StringBuilder();

        // For all pairs in the set...
        for (Map.Entry<Character, Integer> entry : valueMap.entrySet()) {
            // If there is one or more, add it to the list of not null chars
            if (entry.getValue()>0) {
                chars.put(entry.getKey(), entry.getValue());
                charString.append(entry.getKey());
            }
        }// Good, now we have a list of chars used for our bounding in sql. 'charString'

        // For the raw SQL query
        StringBuilder query_set1 = new StringBuilder(constructQuerySet1(chars,count_WILDCARDS, chars.size()));
        StringBuilder query_set2 = new StringBuilder(constructQuerySet2(chars,count_WILDCARDS));
        StringBuilder query_whole = new StringBuilder();

        // Whole query
        query_whole.append(query_set1).append(UNION).append(query_set2).append(ORDER_BY_LENGTH + SEMICOLON);

        // Query the database
        Cursor cursor = db.rawQuery(query_whole.toString(), null);

        // Log
        Log.i(TAG, query_whole.toString());

        // Move through the results with a cursor and return the word field
        while (cursor.moveToNext()) {
            // Get the word from the cursor
            String word =  cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WORD));
            Log.i(TAG, word);
            // Add the result to the list to return
            //Log.i(TAG, word);
            resultList.add(new Result(word));
        }

        // Close the cursor
        cursor.close();

        // Return the list
        return resultList;
    }


    /* -----------------------------------*/
    /* --- Set 1 of the two set union --- */
    /* -----------------------------------*/

    /**
     * Constructs the first set of the query
     * @param chars
     * @param count_WILDCARDS
     * @return
     */
    private String constructQuerySet1(Map<Character, Integer> chars, int count_WILDCARDS, int length) {

        // Then we are going to need a string builder for our query
        StringBuilder dbQuery = new StringBuilder();
        // A boolean to use for concatenating + signs and other sql stuff.
        boolean firstIteration = true;
        // start off the statement
        dbQuery.append(SELECT_FROM_DICT_WHERE);
        // Add the letters we have in the not null list to the query
        for (Map.Entry<Character, Integer> entry : chars.entrySet()) {
            // If there's one or more instances of the character...
            if (entry.getValue()>0) {

                if (!firstIteration && (entry.getKey()!='*' && entry.getKey()!='-')) {
                    dbQuery.append(AND);
                } else {
                    firstIteration = false;
                }
                // Get the next key
                switch (entry.getKey()) {
                    case 'a': dbQuery.append(" count_A>="+entry.getValue()+ " AND count_A<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'b': dbQuery.append(" count_B>="+entry.getValue()+ " AND count_B<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'c': dbQuery.append(" count_C>="+entry.getValue()+ " AND count_C<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'd': dbQuery.append(" count_D>="+entry.getValue()+ " AND count_D<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'e': dbQuery.append(" count_E>="+entry.getValue()+ " AND count_E<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'f': dbQuery.append(" count_F>="+entry.getValue()+ " AND count_F<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'g': dbQuery.append(" count_G>="+entry.getValue()+ " AND count_G<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'h': dbQuery.append(" count_H>="+entry.getValue()+ " AND count_H<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'i': dbQuery.append(" count_I>="+entry.getValue()+ " AND count_I<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'j': dbQuery.append(" count_J>="+entry.getValue()+ " AND count_J<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'k': dbQuery.append(" count_K>="+entry.getValue()+ " AND count_K<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'l': dbQuery.append(" count_L>="+entry.getValue()+ " AND count_L<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'm': dbQuery.append(" count_M>="+entry.getValue()+ " AND count_M<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'n': dbQuery.append(" count_N>="+entry.getValue()+ " AND count_N<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'o': dbQuery.append(" count_O>="+entry.getValue()+ " AND count_O<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'p': dbQuery.append(" count_P>="+entry.getValue()+ " AND count_P<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'q': dbQuery.append(" count_Q>="+entry.getValue()+ " AND count_Q<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'r': dbQuery.append(" count_R>="+entry.getValue()+ " AND count_R<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 's': dbQuery.append(" count_S>="+entry.getValue()+ " AND count_S<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 't': dbQuery.append(" count_T>="+entry.getValue()+ " AND count_T<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'u': dbQuery.append(" count_U>="+entry.getValue()+ " AND count_U<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'v': dbQuery.append(" count_V>="+entry.getValue()+ " AND count_V<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'w': dbQuery.append(" count_W>="+entry.getValue()+ " AND count_W<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'x': dbQuery.append(" count_X>="+entry.getValue()+ " AND count_X<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'y': dbQuery.append(" count_Y>="+entry.getValue()+ " AND count_Y<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    case 'z': dbQuery.append(" count_Z>="+entry.getValue()+ " AND count_Z<="+Integer.valueOf(entry.getValue() + count_WILDCARDS)); break;
                    default: Log.i(TAG, "Something went wrong in the switch..."); break;
                }
            }
        }

        // Add constraints for the second "set"
        dbQuery.append(AND + "length <=" + length);



        return dbQuery.toString();

    }

    /* -----------------------------------*/
    /* --- Set 2 of the two set union --- */
    /* -----------------------------------*/

    /**
     * Constructs the second set of the query
     * @param chars
     * @param count_WILDCARDS
     * @return
     */
    private String constructQuerySet2(Map<Character, Integer> chars, int count_WILDCARDS) {
        // reset boolean for second query in union statement
        boolean firstIteration = true;

        StringBuilder dbQuery = new StringBuilder();

        // Set 1 Union Set 2
        dbQuery.append(SELECT_FROM_DICT_WHERE);
        for (Map.Entry<Character, Integer> entry : chars.entrySet()) {

            // Try this...
            if (!firstIteration && (entry.getKey()!='*' && entry.getKey()!='-')) {
                dbQuery.append(AND);
            } else {
                firstIteration = false;
            }
            // Get the next key
            switch (entry.getKey()) {
                case 'a': dbQuery.append(" count_A<="+entry.getValue()); break;
                case 'b': dbQuery.append(" count_B<="+entry.getValue()); break;
                case 'c': dbQuery.append(" count_C<="+entry.getValue()); break;
                case 'd': dbQuery.append(" count_D<="+entry.getValue()); break;
                case 'e': dbQuery.append(" count_E<="+entry.getValue()); break;
                case 'f': dbQuery.append(" count_F<="+entry.getValue()); break;
                case 'g': dbQuery.append(" count_G<="+entry.getValue()); break;
                case 'h': dbQuery.append(" count_H<="+entry.getValue()); break;
                case 'i': dbQuery.append(" count_I<="+entry.getValue()); break;
                case 'j': dbQuery.append(" count_J<="+entry.getValue()); break;
                case 'k': dbQuery.append(" count_K<="+entry.getValue()); break;
                case 'l': dbQuery.append(" count_L<="+entry.getValue()); break;
                case 'm': dbQuery.append(" count_M<="+entry.getValue()); break;
                case 'n': dbQuery.append(" count_N<="+entry.getValue()); break;
                case 'o': dbQuery.append(" count_O<="+entry.getValue()); break;
                case 'p': dbQuery.append(" count_P<="+entry.getValue()); break;
                case 'q': dbQuery.append(" count_Q<="+entry.getValue()); break;
                case 'r': dbQuery.append(" count_R<="+entry.getValue()); break;
                case 's': dbQuery.append(" count_S<="+entry.getValue()); break;
                case 't': dbQuery.append(" count_T<="+entry.getValue()); break;
                case 'u': dbQuery.append(" count_U<="+entry.getValue()); break;
                case 'v': dbQuery.append(" count_V<="+entry.getValue()); break;
                case 'w': dbQuery.append(" count_W<="+entry.getValue()); break;
                case 'x': dbQuery.append(" count_X<="+entry.getValue()); break;
                case 'y': dbQuery.append(" count_Y<="+entry.getValue()); break;
                case 'z': dbQuery.append(" count_Z<="+entry.getValue()); break;
                default: Log.i(TAG, "Non a-z char entered the switch:" + entry.getKey()); break;
            }

        }

        // Add constraints for the second "set"
        dbQuery.append(AND + LENGTH_EQUALS);

        // Reset flag
        firstIteration = true;

        // Append the length clauses
        for (char c: chars.toString().replaceAll(VALID_CHAR_REGEX, "").toCharArray()) {
            if (!firstIteration) {
                dbQuery.append(PLUS).append(COLUMN_NAME_COUNT_ + String.valueOf(c).toUpperCase());
            } else {
                dbQuery.append(COLUMN_NAME_COUNT_ + String.valueOf(c).toUpperCase());
                firstIteration = false;
            }
        }

        // Add the count of wildcards to account for blank tiles.
        dbQuery.append(PLUS + count_WILDCARDS);

        return dbQuery.toString();
    }

    @SuppressWarnings("unused")
    public ArrayList<Result> crosswordSearch(String query) {
        // Builder for query
        StringBuilder dbQuery = new StringBuilder("");
        dbQuery.append(SELECT_FROM_DICT_WHERE);
        // Eliminate non a-zA-Z or "-" chars that may have gotten in, although, this isn't where
        // I want to do validation, that should be done in the Service
        String searchTerm = query.replaceAll("-", "_");

        Log.i(TAG, searchTerm);
        // Add to query
        dbQuery.append(WORD_LIKE);

        dbQuery.append("'"+searchTerm+"'");
        dbQuery.append(ORDER_BY_LENGTH);
        // Get the database helper to get access to everything...
        SQLiteDatabase db = this.getWritableDatabase();
        // List to hold matches
        ArrayList<Result> resultList = new ArrayList<>();
        // Query the database
        Cursor cursor = db.rawQuery(dbQuery.toString(), null);
        // Log
        Log.i(TAG, dbQuery.toString());

        // Move through the results with a cursor and return the word field
        while (cursor.moveToNext()) {
            // Get the word from the cursor
            String word =  cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WORD));
            // Add the result to the list to return
            Log.i(TAG, word);
            resultList.add(new Result(word));
        }
        // Close the cursor
        cursor.close();
        // Return the list
        return resultList;
    }
}//EOF