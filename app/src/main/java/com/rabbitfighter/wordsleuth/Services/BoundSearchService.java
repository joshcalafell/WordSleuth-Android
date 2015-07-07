package com.rabbitfighter.wordsleuth.Services;

import android.app.Service;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Database.DictionaryDbHelper;
import com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter;
import com.rabbitfighter.wordsleuth.Entries.Entry;
import com.rabbitfighter.wordsleuth.Entries.Result;
import com.rabbitfighter.wordsleuth.Utils.RoutineTimer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Bound service for searching dictionary asynchronously.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/components/bound-services.html'
 * @see 'http://developer.android.com/reference/java/io/FileInputStream.html' // ***Fixed speed***
 * @since 0.1
 */
public class BoundSearchService extends Service  {

    private static final String TAG = "BoundSearchService";

    /*
    Create an object that is going to be the binder object
    The bridge that connects app to service
    */
    public final IBinder myBinder = new MyLocalBinder();

    // Lists to hold results
    ArrayList<Result> anagrams;
    ArrayList<Result> subwords;
    ArrayList<Result> combos;
    ArrayList<Result> matches;

    // Database
    ResultsDbAdapter dbAdapter;

    DictionaryDbHelper helper;

    public BoundSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) throws UnsupportedOperationException {
        // Database
        helper = new DictionaryDbHelper(this);
        dbAdapter = new ResultsDbAdapter(this);
        return myBinder;
        // Autogen: TODO: Return the communication channel to the service.
    }

    public boolean prepareDictionary() {
        // Load the dictionary
        Log.i(TAG, "Loading dictionary...");
        helper = new DictionaryDbHelper(this);
        RoutineTimer dictionaryTimer = new RoutineTimer();
        dictionaryTimer.start();
            try {

                // Create the database
                try {
                    helper.createDataBase();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Unable to create database");
                    throw new Error("Unable to create database");
                }
                // Create the database
                try {
                    helper.openDataBase();
                }catch(SQLException e) {
                    e.printStackTrace();
                    Log.i(TAG, "Unable to open database");
                    throw new Error("Unable to open database");
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.i(TAG, "Couldn't prepare dictionary database.");
                return false;
            } finally {
                dictionaryTimer.stop();
                Log.i(TAG, "Dictionary prepared in " + dictionaryTimer.getTotal() + " milliseconds.");
            }
        return true;
    }

    // Search the dictionary database for matches
    public void search(String userQuery) {
        dbAdapter.deleteEntries();
        anagrams = new ArrayList<>();
        combos = new ArrayList<>();
        subwords = new ArrayList<>();
        Entry query = new Entry(userQuery);
        if (query.getWord()!=null) {
            try {
                matches = new ArrayList<>(helper.getMatches(
                        query.getCount_A(), query.getCount_B(), query.getCount_C(), query.getCount_D(),
                        query.getCount_E(), query.getCount_F(), query.getCount_G(), query.getCount_H(),
                        query.getCount_I(), query.getCount_J(), query.getCount_K(), query.getCount_L(),
                        query.getCount_M(), query.getCount_N(), query.getCount_O(), query.getCount_P(),
                        query.getCount_Q(), query.getCount_R(), query.getCount_S(), query.getCount_T(),
                        query.getCount_U(), query.getCount_V(), query.getCount_W(), query.getCount_X(),
                        query.getCount_Y(), query.getCount_Z(), query.getCount_Wildcards() // wildcards not in use yet
                ));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "There was an error in the search");
            } finally {
                //helper.close();
            }

            // Put anagrams and subwords in the database
            for (Result result : matches) {
                if (result.getNumLetters() == query.getNumLetters() && result.getWord().compareToIgnoreCase(userQuery)!=0) {
                    anagrams.add(result);
                    long id = dbAdapter.insertData(
                            "anagram",
                            result.getWord(),
                            String.valueOf(result.getNumLetters()),
                            String.valueOf(result.getPointsScrabble()),
                            String.valueOf(result.getPointsWordsWithFriends())
                    );
                    if (id < 0) {
                        Log.i(TAG, "Database anagram insertion of " + result.getWord() + " unsuccessful :(");
                    } else {
                        Log.i(TAG, "Database anagram insertion of " + result.getWord() + " successful :)");
                    }
                } else {
                    subwords.add(result);
                    long id = dbAdapter.insertData(
                            "subword",
                            result.getWord(),
                            String.valueOf(result.getNumLetters()),
                            String.valueOf(result.getPointsScrabble()),
                            String.valueOf(result.getPointsWordsWithFriends())
                    );
                    if (id < 0) {
                        Log.i(TAG, "Database subword insertion of " + result.getWord() + " unsuccessful :(");
                    } else {
                        Log.i(TAG, "Database subword insertion of " + result.getWord() + " successful :)");
                    }
                }
            }

            if (!subwords.isEmpty()) {
                // Put combos in the database
                for (int i = 0; i < subwords.size(); i++) {
                    String foo = subwords.get(i).getWord();
                    for (int j = 0; j < subwords.size(); j++) {
                        String bar = subwords.get(j).getWord();
                        String foobar = sorted(String.valueOf("" + foo + bar));
                        if (isAnagram(query.getWordSorted(), foobar)) {
                            // Get result object
                            Result result = new Result(String.valueOf(foo + " " + bar));
                            // Insert item into database
                            combos.add(result);
                            long id = dbAdapter.insertData(
                                    "combo",
                                    result.getWord(),
                                    String.valueOf(result.getNumLetters()),
                                    String.valueOf(result.getPointsScrabble()),
                                    String.valueOf(result.getPointsWordsWithFriends())
                            );
                            if (id < 0) {
                                Log.i(TAG, "Database combo insertion of " + result.getWord() + " unsuccessful :(");
                            } else {
                                Log.i(TAG, "Database combo insertion of " + result.getWord() + " successful :)");
                            }
                        }
                    }
                }
            }
        }
    }// End search

    /**
     * Returns whether or not a word Lists an anagram
     * @param query the query
     * @param dictWord the dictionary word
     * @return whether it is an anagram or not
     */
    private boolean isAnagram(String query, String dictWord) {
        return (query.compareToIgnoreCase(sorted(dictWord))==0);
    }


    /**
     * Returns a sanitized sorted word
     * @param word the string passed in
     * @return the sorted word
     */
    public String sorted(String word) {
        char[] temp = word.toCharArray();
        Arrays.sort(temp);
        String sortedWord = String.valueOf(temp);
        return sortedWord;
    }

    /**
     * Whenever you want to bind an app to a service you need
     * to create an object that extends the binder class. We
     * only want this to return a reference to the superclass.
     * Note: It's kind of weird you need this class to get at
     * what's inside this class, but whatever...
     */
    public class MyLocalBinder extends Binder {
        public BoundSearchService getService() {
            return BoundSearchService.this;
        }
    }
}
