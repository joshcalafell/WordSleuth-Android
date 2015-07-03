package com.rabbitfighter.wordsleuth.Services;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter;
import com.rabbitfighter.wordsleuth.Objects.Result;
import com.rabbitfighter.wordsleuth.Utils.RoutineTimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Bound service for searching dictionary asynchronously.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/components/bound-services.html'
 * @see 'http://developer.android.com/reference/java/io/FileInputStream.html' // ***Fixed speed***
 * @since 0.1
 */
public class BoundSearchService extends Service  {

    private static final String TAG = "BoundSearchService";
    private static final boolean TESTING = true;

    /*
    Create an object that is going to be the binder object
    The bridge that connects app to service
    */
    public final IBinder myBinder = new MyLocalBinder();

    // Lists to hold results
    ArrayList<Result> anagrams;
    ArrayList<Result> subwords;
    ArrayList<Result> combos;

    // For reading dictionary
    AssetManager assetManager;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    // Database
    ResultsDbAdapter dbAdapter;

    //ContentValues contentValues;

    public BoundSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) throws UnsupportedOperationException {

        // Database
        dbAdapter = new ResultsDbAdapter(this);

        return myBinder;
        // Autogen: TODO: Return the communication channel to the service.

    }

    public boolean prepareDictionary() {
        // Load the dictionary
        Log.i(TAG, "Loading dictionary...");
        RoutineTimer dictionaryTimer = new RoutineTimer();
        dictionaryTimer.start();
        try {
            assetManager = this.getAssets();
            inputStream = assetManager.open("dictionaries/ospd.txt");
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            dictionaryTimer.stop();
            Log.i(TAG, "Dictionary prepared in " + dictionaryTimer.getTotal() + " milliseconds.");
        }
    }

    /**
     * Searches the dictionary array for matches.
     * @param userQuery
     */
    public void searchDictionary(String userQuery) {
        Log.i(TAG, "Search started...");

        RoutineTimer timer = new RoutineTimer();
        timer.start();

        // Delete items from old the table
        dbAdapter.deleteEntries();

        // Lists to hold results
        anagrams = new ArrayList<>();
        subwords = new ArrayList<>();
        combos = new ArrayList<>();

        String query = sorted(userQuery);

        try {
            String receiveString;
            if (inputStream != null) {
                while ((receiveString = bufferedReader.readLine()) != null) {
                    String dictWord = receiveString;
                    // First condition will fail if dictionary word is longer than query
                    if (dictWord.length() <= query.length()) {
                        if (dictWord.length() == query.length() && isAnagram(query, dictWord)) {
                            // Get result object
                            Result r = new Result(dictWord);
                            // Insert item into database
                            long id = dbAdapter.insertData(
                                "anagram",
                                String.valueOf(r.getWord()),
                                String.valueOf(r.getNumLetters()),
                                String.valueOf(r.getPointsScrabble()),
                                String.valueOf(r.getPointsWordsWithFriends())
                            );
                            if (id<0) {
                                Log.i(TAG, "Database anagram insertion unsuccessful :(");
                            } else {
                                Log.i(TAG, "Database anagram insertion successful :)");
                            }

                        } else {
                            if (isSubword(query, dictWord)) { // sortWord returns a String

                                // Get result object
                                Result r = new Result(dictWord);
                                subwords.add(r);
                                // Insert item into database
                                long id = dbAdapter.insertData(
                                    "subword",
                                    r.getWord(),
                                    String.valueOf(r.getNumLetters()),
                                    String.valueOf(r.getPointsScrabble()),
                                    String.valueOf(r.getPointsWordsWithFriends())
                                );
                                if (id<0) {
                                    Log.i(TAG, "Database subword insertion unsuccessful :(");
                                } else {
                                    Log.i(TAG, "Database subword insertion successful :)");
                                }
                            }
                        }
                    }
                }
                // Find combos
                for (int i = 0; i < subwords.size(); i++) {
                    String foo = subwords.get(i).getWord();
                    for (int j = 0; j < subwords.size(); j++) {
                        String bar = subwords.get(j).getWord();
                        String foobar = sorted(String.valueOf("" + foo + bar));
                        if (isAnagram(query, foobar)) {
                            // Get result object
                            Result r = new Result(String.valueOf(foo + " " + bar));
                            // Insert item into database
                            long id = dbAdapter.insertData(
                                "combo",
                                r.getWord(),
                                String.valueOf(r.getNumLetters()),
                                String.valueOf(r.getPointsScrabble()),
                                String.valueOf(r.getPointsWordsWithFriends())
                            );
                            if (id<0) {
                                Log.i(TAG, "Database combo insertion unsuccessful :(");
                            } else {
                                Log.i(TAG, "Database combo insertion successful :)");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Stop timer
            timer.stop();
            // Close resources
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.i(TAG, "Search complete...");
    }

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
     * Returns whether a word in the dictionary is a subword os the user query
     * @param query the query list
     * @param word the dictionary word
     * @return whether it is a subword or not
     */
    private boolean isSubword(String query, String word) {

        char[] queryArray = query.toCharArray();
        ArrayList<Character> queryList = new ArrayList<>();
        for (char c : queryArray ) {
            queryList.add(c);
        }

        char[] wordArray = word.toCharArray();
        ArrayList<Character> wordList = new ArrayList<>();
        for (char c : wordArray ) {
            wordList.add(c);
        }

        ArrayList<Character> tempList = new ArrayList<>();

        for (int i = 0; i < wordList.size(); i++) {
           for (int j = 0; j < queryList.size(); j++) {
               if (wordList.get(i) == queryList.get(j)) {
                    tempList.add(wordList.get(i));
                    queryList.remove(j);
                    j+=1;
               }
           }
        }
        // this is the trick...
        return (tempList.size() == wordList.size());
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
