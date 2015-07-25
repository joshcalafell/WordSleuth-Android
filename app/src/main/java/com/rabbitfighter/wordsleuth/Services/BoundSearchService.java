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
    public static final int REGULAR_SEARCH = 0;
    public static final int BLANK_TILE_SEARCH = 1;
    public static final int CROSSWORD_SEARCH = 2;

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
    private ArrayList<Result> matchesCopy;
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
        this.setMatchesCopy(new ArrayList<Result>());
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

    /**
     * @param query -  the query passed in, which has been sanitized already.
     */
    public void search(String query) {

        this.setAnagrams(new ArrayList<Result>());
        this.setSubwords(new ArrayList<Result>());
        this.setCombos(new ArrayList<Result>());
        this.setMatches(new ArrayList<Result>());
        this.setMatchesCopy(new ArrayList<Result>());

        // Set the query
        this.setQuery(new Entry(query));


        // Set the array lists
        resetLists();

        // Set the database adapter for the results
        this.setDbAdapter(new ResultsDbAdapter(this));

        // Delete entries
        this.getDbAdapter().deleteEntries();

        /**
         *  --- Regular or Blank Tile Search (they both work the same) ---
         **/
        if (this.getSearchType()==REGULAR_SEARCH || this.getSearchType()==BLANK_TILE_SEARCH) {
            if (this.getSearchType()==REGULAR_SEARCH) {
                Log.i(TAG, "Regular Search results are being processed...");
            } else if (this.getSearchType()==BLANK_TILE_SEARCH) {
                Log.i(TAG, "Blank Tile Search results are being processed...");
            }
            // Reset matches
            this.setMatches(null);
            /**
             * Start the db query. This will give us all subwords. The anagrams will just
             * be subwords that have the length as the query...
             */
            this.setMatches(
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

            );
            // Make a copy of the matches
            this.setMatchesCopy(this.getMatches());
            /**
             * Get the subwords and anagrams
             */
            for (Result result : this.getMatches()) {
                // If it's the same length as the query, it's an anagram
                if (result.getNumLetters() == this.getQuery().getNumLetters()       // 7==7
                        && !result.getWord().equals(this.getQuery().getWord())) {   // word != query
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
                // ------------------------------------
                // --- If it's not the same length: ---
                // ------------------------------------
                // --- 1) It must be less length
                // --- 2) it must be a subword
                // ------------------------------------
                } else if (this.getQuery().getNumLetters() > result.getNumLetters()){
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
             * Get the combos
             */
            for (Result r1 : this.getMatches()) {
                String foo = r1.getWord();
                for (Result r2 : this.getMatchesCopy()) {
                    String bar = r2.getWord();
                    // If "foo" != "bar"
                    if (foo.compareToIgnoreCase(bar)!=0) {
                        // Make a new result out of foo and bar, called foobar
                        Result foobar = new Result(r1.getWord().concat(r2.getWord()));
                        if (foobar.getWordSorted().compareToIgnoreCase(this.getQuery().getWordSorted())==0) {
                            Result insertionResult = new Result(foo + " " + bar);
                            this.getCombos().add(insertionResult);
                            long id = this.getDbAdapter().insertData(
                                    "combo",
                                    insertionResult.getWord(),
                                    String.valueOf(insertionResult.getNumLetters()),
                                    String.valueOf(insertionResult.getPointsScrabble()),
                                    String.valueOf(insertionResult.getPointsWordsWithFriends())
                            );
                            if (id < 0) {
                                Log.i(TAG, "Database combo insertion of " + insertionResult.getWord() + " unsuccessful :(");
                            } else {
                                Log.i(TAG, "Database combo insertion of " + insertionResult.getWord() + " successful :)");
                            }
                        }
                    }
                }
            }
        } // End Blank Tile || Regular Search

        /**
         *  --- Crossword Search ---
        **/
        if (this.getSearchType()==CROSSWORD_SEARCH) {
            Log.i(TAG, "Crossword search results being processed...");
            // Do the crossword database search
            this.setMatches(this.getHelper().crosswordSearch(this.getQuery().getWord()));
            // Add matches to anagrams
            for (Result result : this.getMatches()) {
                if (!this.getAnagrams().isEmpty()) this.getAnagrams().clear();
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
                }
            }
        }
    }// End search



    /**
     * Reset the lists
     */
    private void resetLists() {
        List<ArrayList<Result>> lists = new ArrayList<>();
        lists.add(this.getAnagrams());
        lists.add(this.getSubwords());
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

    /* ----------------------- */
    /* --- Getters/Setters --- */
    /* ----------------------- */
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

    public void setMatchesCopy(ArrayList<Result> matchesCopy) {
        this.matchesCopy = matchesCopy;
    }

    public ArrayList<Result> getMatchesCopy() {
        return this.matchesCopy;
    }
}
