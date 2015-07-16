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
import java.util.List;

/**
 * Bound service for searching dictionary asynchronously.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/components/bound-services.html'
 * @see 'http://developer.android.com/reference/java/io/FileInputStream.html' // ***Fixed speed***
 * @since 0.1 2015-06-17.
 */
public class BoundSearchService extends Service  {

    private static final String TAG = "BoundSearchService";

    /*
    Create an object that is going to be the binder object
    The bridge that connects app to service
    */
    public final IBinder myBinder = new MyLocalBinder();
    // Lists to hold results
    private ArrayList<Result> anagrams;
    private ArrayList<Result> subwords;
    private ArrayList<Result> combos;
    private ArrayList<Result> matches;
    // Results DB
    private ResultsDbAdapter dbAdapter;
    // Dictionary DB
    private DictionaryDbHelper helper;
    // Query
    private Entry query;
    // Search type (regular, blank-tile, crossword)
    private int searchType;


    // Constructor
    public BoundSearchService() {
        this.setAnagrams(new ArrayList<Result>());
        this.setSubwords(new ArrayList<Result>());
        this.setCombos(new ArrayList<Result>());
        this.setMatches(new ArrayList<Result>());
    }

    @Override
    public IBinder onBind(Intent intent) throws UnsupportedOperationException {
        // Return binder
        return myBinder;
        // Autogen: TODO: Return the communication channel to the service.
    }

    /**
     * Prepare the dictionary
     * @return
     */
    public boolean prepareSearch(int searchType) {
        // Load the dictionary
        Log.i(TAG, "Loading dictionary...");
        // Start the timer
        RoutineTimer dictionaryTimer = new RoutineTimer();
        dictionaryTimer.start();
        // Try to prepare the dictionary database
        try {
            // Set search type    // Set the search type
            this.setSearchType(searchType);

            // Set the helper
            this.setHelper(new DictionaryDbHelper(this));

            // Create the database
            try {
                this.getHelper().createDataBase();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Unable to create database");
                throw new Error("Unable to create database");
            }

            // Open the database
            try {

                this.getHelper().openDataBase();
                // Get readable database
                //getHelper().getReadableDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(TAG, "Unable to open database");
                throw new Error("Unable to open database");
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.i(TAG, "Couldn't prepare dictionary database.");
            return false;
        } finally {
            // Stop timer
            dictionaryTimer.stop();
            Log.i(TAG, "Dictionary prepared in " + dictionaryTimer.getTotal() + " milliseconds.");
        }
        return true;
    }

    // Search the dictionary database for matches
    public void search(String query) {

        // Set the query
        this.setQuery(new Entry(query));

        // Set the array lists
        resetLists();

        // Set the database adapter for the results
        this.setDbAdapter(new ResultsDbAdapter(this));

        // Delete entries
        this.getDbAdapter().deleteEntries();

        // Test for query not null
        if (query == null) {
            Log.i(TAG, "query is null");
        } else {
            Log.i(TAG, "Query: " + this.getQuery().getWord());
        }

        // Regular search
        if (this.getSearchType()==0) {
            // Check to see if wildcards are 2 or less
            if (this.getQuery()!=null) {
                Log.i(TAG, "Got to matches");
                this.setMatches(
                        new ArrayList<>(
                                this.getHelper().wildcardSearch(
                                        this.getQuery().getCount_A(), this.getQuery().getCount_B(),
                                        this.getQuery().getCount_C(), this.getQuery().getCount_D(),
                                        this.getQuery().getCount_E(), this.getQuery().getCount_F(),
                                        this.getQuery().getCount_G(), this.getQuery().getCount_H(),
                                        this.getQuery().getCount_I(), this.getQuery().getCount_J(),
                                        this.getQuery().getCount_K(), this.getQuery().getCount_L(),
                                        this.getQuery().getCount_M(), this.getQuery().getCount_N(),
                                        this.getQuery().getCount_O(), this.getQuery().getCount_P(),
                                        this.getQuery().getCount_Q(), this.getQuery().getCount_R(),
                                        this.getQuery().getCount_S(), this.getQuery().getCount_T(),
                                        this.getQuery().getCount_U(), this.getQuery().getCount_V(),
                                        this.getQuery().getCount_W(), this.getQuery().getCount_X(),
                                        this.getQuery().getCount_Y(), this.getQuery().getCount_Z(),
                                        0
                                        // wildcards not in use yet
                                )
                        )
                );
                // If matches aren't null and the matches aren't empty
                if (this.getMatches() != null && !this.getMatches().isEmpty()) {
                    // Put anagrams and subwords in the database
                    for (Result result : this.getMatches()) {
                        if (result.getNumLetters() == this.getQuery().getNumLetters()
                                && this.getQuery().getWord().compareToIgnoreCase(result.getWord())!=0) {
                            this.getAnagrams().add(result);
                            long id = this.getDbAdapter().insertData(
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
                        } else if (this.getQuery().getWord().compareToIgnoreCase(result.getWord())!=0) {
                            this.getSubwords().add(result);
                            long id = this.getDbAdapter().insertData(
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

                    /**
                     * Go through the subwords to find the combos
                     */
                    if (!this.getSubwords().isEmpty()) {
                        // Put combos in the database
                        for (int i = 0; i < this.getSubwords().size(); i++) {
                            String foo = this.getSubwords().get(i).getWord();
                            for (int j = 0; j < this.getSubwords().size(); j++) {
                                String bar = this.getSubwords().get(j).getWord();
                                String foobar = sorted(String.valueOf("" + foo + bar));
                                if (isAnagram(this.getQuery().getWordSorted(), foobar)) {
                                    // Get result object
                                    Result result = new Result(String.valueOf(foo + " " + bar));
                                    // Insert item into database
                                    this.getCombos().add(result);
                                    long id = this.getDbAdapter().insertData(
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

                } else {
                    Log.i(TAG, "Query had " + this.getQuery().getCount_blank_tiles() + " wildcard(s)");
                    Log.i(TAG, "matches was empty, WTF?");
                }
            } else {
                Log.i(TAG, "Query was " + this.getQuery().getCount_blank_tiles());
                //Message.msgLong(getBaseContext(), "No more than 2 wildcards allowed");
            }
        }

        // Blank Tile Search
        if (this.getSearchType()==1) {
            // Check to see if wildcards are 2 or less
            if (this.getQuery().getCount_blank_tiles()<=2 ) {
                Log.i(TAG, "Got to matches");
                this.setMatches(
                        new ArrayList<>(
                                this.getHelper().wildcardSearch(
                                        this.getQuery().getCount_A(), this.getQuery().getCount_B(),
                                        this.getQuery().getCount_C(), this.getQuery().getCount_D(),
                                        this.getQuery().getCount_E(), this.getQuery().getCount_F(),
                                        this.getQuery().getCount_G(), this.getQuery().getCount_H(),
                                        this.getQuery().getCount_I(), this.getQuery().getCount_J(),
                                        this.getQuery().getCount_K(), this.getQuery().getCount_L(),
                                        this.getQuery().getCount_M(), this.getQuery().getCount_N(),
                                        this.getQuery().getCount_O(), this.getQuery().getCount_P(),
                                        this.getQuery().getCount_Q(), this.getQuery().getCount_R(),
                                        this.getQuery().getCount_S(), this.getQuery().getCount_T(),
                                        this.getQuery().getCount_U(), this.getQuery().getCount_V(),
                                        this.getQuery().getCount_W(), this.getQuery().getCount_X(),
                                        this.getQuery().getCount_Y(), this.getQuery().getCount_Z(),
                                        this.getQuery().getCount_blank_tiles()
                                )
                        )
                );
                // If matches aren't null and the matches aren't empty

                for (Result result : this.getMatches()) {
                    if (result.getNumLetters() == this.getQuery().getNumLetters()) {
                        this.getAnagrams().add(result);
                        long id = this.getDbAdapter().insertData(
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
                    } else  {
                        this.getSubwords().add(result);
                        long id = this.getDbAdapter().insertData(
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

                /**
                 * Go through the subwords to find the combos
                 */
                /**
                 * Go through the subwords to find the combos
                 */
                if (!this.getSubwords().isEmpty()) {
                    // Put combos in the database
                    for (int i = 0; i < this.getSubwords().size(); i++) {
                        String foo = this.getSubwords().get(i).getWord();
                        for (int j = 0; j < this.getSubwords().size(); j++) {
                            String bar = this.getSubwords().get(j).getWord();
                            String foobar = sorted(String.valueOf("" + foo + bar));
                            if (isAnagram(this.getQuery().getWordSorted(), foobar)) {
                                // Get result object
                                Result result = new Result(String.valueOf(foo + " " + bar));
                                // Insert item into database
                                this.getCombos().add(result);
                                long id = this.getDbAdapter().insertData(
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

            } else {
                Log.i(TAG, "Query had " + this.getQuery().getCount_blank_tiles() + " wildcard(s)");
                Log.i(TAG, "matches was empty, WTF?");
            }

        }

        /* --- Crossword Search --- */
        if (this.getSearchType()==2) {
            // Check to see if wildcards are 2 or less
            if (this.getQuery()!=null) {
                Log.i(TAG, "Got to matches");
                // do the crossword search
                this.setMatches(this.getHelper().crosswordSearch(this.getQuery().getWord()));
                // If matches aren't null and the matches aren't empty

                for (Result result : this.getMatches()) {
                    Log.i(TAG, result.getNumLetters() + " " + this.getQuery().getNumLetters());
                    if (result.getNumLetters() == this.getQuery().getNumLetters()) {
                        this.getAnagrams().add(result);
                        long id = this.getDbAdapter().insertData(
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
                    } else  {
                        this.getSubwords().add(result);
                        long id = this.getDbAdapter().insertData(
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

                /**
                 * Go through the subwords to find the combos
                 */
                /**
                 * Go through the subwords to find the combos
                 */
                if (!this.getSubwords().isEmpty()) {
                    // Put combos in the database
                    for (int i = 0; i < this.getSubwords().size(); i++) {
                        String foo = this.getSubwords().get(i).getWord();
                        for (int j = 0; j < this.getSubwords().size(); j++) {
                            String bar = this.getSubwords().get(j).getWord();
                            String foobar = sorted(String.valueOf("" + foo + bar));
                            if (isAnagram(this.getQuery().getWordSorted(), foobar)) {
                                // Get result object
                                Result result = new Result(String.valueOf(foo + " " + bar));
                                // Insert item into database
                                this.getCombos().add(result);
                                long id = this.getDbAdapter().insertData(
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

            } else {
                Log.i(TAG, "Query had " + this.getQuery().getCount_blank_tiles() + " wildcard(s)");
                Log.i(TAG, "matches was empty, WTF?");
            }

        }






    }// End search



    /**
     * Reset the lists
     */
    private void resetLists() {
        List<ArrayList<Result>> lists = new ArrayList<>();
        lists.add(this.getAnagrams()); lists.add(this.getSubwords());
        lists.add(this.getMatches());
        lists.add(this.getCombos());
        // Clear lists
        for (ArrayList<Result> list : lists) {
            list.clear();
        }
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

    public ArrayList<Result> getAnagrams() {
        return anagrams;
    }

    public void setAnagrams(ArrayList<Result> anagrams) {
        this.anagrams = anagrams;
    }

    public ArrayList<Result> getSubwords() {
        return subwords;
    }

    public void setSubwords(ArrayList<Result> subwords) {
        this.subwords = subwords;
    }

    public ArrayList<Result> getCombos() {
        return combos;
    }

    public void setCombos(ArrayList<Result> combos) {
        this.combos = combos;
    }

    public ArrayList<Result> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Result> matches) {
        this.matches = matches;
    }

    public ResultsDbAdapter getDbAdapter() {
        return dbAdapter;
    }

    public void setDbAdapter(ResultsDbAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    public DictionaryDbHelper getHelper() {
        return helper;
    }

    public void setHelper(DictionaryDbHelper helper) {
        this.helper = helper;
    }

    public Entry getQuery() {
        return query;
    }

    public void setQuery(Entry query) {
        this.query = query;
    }

    public int getSearchType() {

        return searchType;
    }

    public void setSearchType(int searchType) {

        this.searchType = searchType;
    }
}
