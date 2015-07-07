package com.rabbitfighter.wordsleuth.Database;

import android.content.ContentValues;
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

/**
 * SQLite Database. There are many like it but this one is mine...
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-07-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/reference/android/database/sqlite/package-summary.html'
 * @since 0.1
 */
public class DictionaryDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.rabbitfighter.wordsleuth/databases/";
    private static String DB_NAME = "dictionary.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    // Table name
    public static final String TABLE_NAME = "dictionary";
    // Table columns
    public static final String UID = "_id";
    public static final String COLUMN_NAME_WORD = "word";
    public static final String COLUMN_NAME_WORD_LENGTH = "length";
    // For all the letters, yes this is necessary.
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

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
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
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
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
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
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
        if(myDataBase != null)
            myDataBase.close();
        super.close();

    }

    /* -------------------------- */
    /* --- Database retrieval --- */
    /* -------------------------- */

    /**
     * Get the Anagrams in the database
     * @return the number of anagrams
     */
    public ArrayList<Result> getMatches(
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
        // Here are the columns we care about in our search
        String[] columns = {
                UID,
                COLUMN_NAME_WORD,
                COLUMN_NAME_WORD_LENGTH,
                COLUMN_NAME_COUNT_A,
                COLUMN_NAME_COUNT_B,
                COLUMN_NAME_COUNT_C,
                COLUMN_NAME_COUNT_D,
                COLUMN_NAME_COUNT_E,
                COLUMN_NAME_COUNT_F,
                COLUMN_NAME_COUNT_G,
                COLUMN_NAME_COUNT_H,
                COLUMN_NAME_COUNT_I,
                COLUMN_NAME_COUNT_J,
                COLUMN_NAME_COUNT_K,
                COLUMN_NAME_COUNT_L,
                COLUMN_NAME_COUNT_M,
                COLUMN_NAME_COUNT_N,
                COLUMN_NAME_COUNT_O,
                COLUMN_NAME_COUNT_P,
                COLUMN_NAME_COUNT_Q,
                COLUMN_NAME_COUNT_R,
                COLUMN_NAME_COUNT_S,
                COLUMN_NAME_COUNT_T,
                COLUMN_NAME_COUNT_U,
                COLUMN_NAME_COUNT_V,
                COLUMN_NAME_COUNT_W,
                COLUMN_NAME_COUNT_X,
                COLUMN_NAME_COUNT_Y,
                COLUMN_NAME_COUNT_Z,
        };
        String[] selectionArgs = null;
        // The selection (WHERE ...)
        String selection =
                COLUMN_NAME_COUNT_A +"<="+ count_A + " AND " +
                COLUMN_NAME_COUNT_B +"<="+ count_B + " AND " +
                COLUMN_NAME_COUNT_C +"<="+ count_C + " AND " +
                COLUMN_NAME_COUNT_D +"<="+ count_D + " AND " +
                COLUMN_NAME_COUNT_E +"<="+ count_E + " AND " +
                COLUMN_NAME_COUNT_F +"<="+ count_F + " AND " +
                COLUMN_NAME_COUNT_G +"<="+ count_G + " AND " +
                COLUMN_NAME_COUNT_H +"<="+ count_H + " AND " +
                COLUMN_NAME_COUNT_I +"<="+ count_I + " AND " +
                COLUMN_NAME_COUNT_J +"<="+ count_J + " AND " +
                COLUMN_NAME_COUNT_K +"<="+ count_K + " AND " +
                COLUMN_NAME_COUNT_L +"<="+ count_L + " AND " +
                COLUMN_NAME_COUNT_M +"<="+ count_M + " AND " +
                COLUMN_NAME_COUNT_N +"<="+ count_N + " AND " +
                COLUMN_NAME_COUNT_O +"<="+ count_O + " AND " +
                COLUMN_NAME_COUNT_P +"<="+ count_P + " AND " +
                COLUMN_NAME_COUNT_Q +"<="+ count_Q + " AND " +
                COLUMN_NAME_COUNT_R +"<="+ count_R + " AND " +
                COLUMN_NAME_COUNT_S +"<="+ count_S + " AND " +
                COLUMN_NAME_COUNT_T +"<="+ count_T + " AND " +
                COLUMN_NAME_COUNT_U +"<="+ count_U + " AND " +
                COLUMN_NAME_COUNT_V +"<="+ count_V + " AND " +
                COLUMN_NAME_COUNT_W +"<="+ count_W + " AND " +
                COLUMN_NAME_COUNT_X +"<="+ count_X + " AND " +
                COLUMN_NAME_COUNT_Y +"<="+ count_Y + " AND " +
                COLUMN_NAME_COUNT_Z +"<="+ count_Z;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME, // Table name
                columns,         // Columns
                selection,       // Selection
                selectionArgs,   // Selection arguments
                groupBy,         // Group by...
                having,          // having
                orderBy          // Order by
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
}