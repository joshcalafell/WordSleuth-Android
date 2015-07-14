package com.rabbitfighter.wordsleuth.Entries;

import java.util.HashMap;

/**
 * A result is a type of Word. Read about Scrabble(TM) and Words(TM)
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'https://en.wikipedia.org/wiki/Scrabble_letter_distributions'
 * @see 'https://en.wikipedia.org/wiki/Words_with_Friends'
 * @since 0.1 2015-06-17.
 */
@SuppressWarnings("unused")
public class Result extends Word {

    private static final String TAG = "Result";

    private int pointsScrabble;
    private int pointsWordsWithFriends;

    static final HashMap<Character, Integer> pointMap_scrabble = new HashMap<>();
    static final HashMap<Character, Integer> pointMap_wordsWithFriends = new HashMap<>();

    /* ---------------------------- */
    /* --- Set static hash maps --- */ //Note: They will only load once if they are static.
    /* ---------------------------- */

    // Scrabble(TM) point map
    static {
        pointMap_scrabble.put('*', 0);
        pointMap_scrabble.put('a', 1);
        pointMap_scrabble.put('b', 3);
        pointMap_scrabble.put('c', 3);
        pointMap_scrabble.put('d', 2);
        pointMap_scrabble.put('e', 1);
        pointMap_scrabble.put('f', 4);
        pointMap_scrabble.put('g', 2);
        pointMap_scrabble.put('h', 4);
        pointMap_scrabble.put('i', 1);
        pointMap_scrabble.put('j', 8);
        pointMap_scrabble.put('k', 5);
        pointMap_scrabble.put('l', 1);
        pointMap_scrabble.put('m', 3);
        pointMap_scrabble.put('n', 1);
        pointMap_scrabble.put('o', 1);
        pointMap_scrabble.put('p', 3);
        pointMap_scrabble.put('q', 8);
        pointMap_scrabble.put('r', 1);
        pointMap_scrabble.put('s', 1);
        pointMap_scrabble.put('t', 1);
        pointMap_scrabble.put('u', 1);
        pointMap_scrabble.put('v', 4);
        pointMap_scrabble.put('w', 4);
        pointMap_scrabble.put('x', 8);
        pointMap_scrabble.put('y', 4);
        pointMap_scrabble.put('z', 10);
        pointMap_scrabble.put('*', 0);
        pointMap_scrabble.put('-', 0);
    }

    // Words With Friends(TM) point map
    static {
        pointMap_wordsWithFriends.put('*', 0);
        pointMap_wordsWithFriends.put('a', 1);
        pointMap_wordsWithFriends.put('b', 4);
        pointMap_wordsWithFriends.put('c', 4);
        pointMap_wordsWithFriends.put('d', 2);
        pointMap_wordsWithFriends.put('e', 1);
        pointMap_wordsWithFriends.put('f', 4);
        pointMap_wordsWithFriends.put('g', 3);
        pointMap_wordsWithFriends.put('h', 3);
        pointMap_wordsWithFriends.put('i', 1);
        pointMap_wordsWithFriends.put('j', 10);
        pointMap_wordsWithFriends.put('k', 5);
        pointMap_wordsWithFriends.put('l', 2);
        pointMap_wordsWithFriends.put('m', 4);
        pointMap_wordsWithFriends.put('n', 2);
        pointMap_wordsWithFriends.put('o', 1);
        pointMap_wordsWithFriends.put('p', 4);
        pointMap_wordsWithFriends.put('q', 10);
        pointMap_wordsWithFriends.put('r', 1);
        pointMap_wordsWithFriends.put('s', 1);
        pointMap_wordsWithFriends.put('t', 1);
        pointMap_wordsWithFriends.put('u', 2);
        pointMap_wordsWithFriends.put('v', 5);
        pointMap_wordsWithFriends.put('w', 4);
        pointMap_wordsWithFriends.put('x', 8);
        pointMap_wordsWithFriends.put('y', 3);
        pointMap_wordsWithFriends.put('z', 10);
        pointMap_wordsWithFriends.put('*', 0);
        pointMap_wordsWithFriends.put('-', 0);
    }

    /* -------------------- */
    /* --- Constructors --- */
    /* -------------------- */

    public Result(String result) {
        super(result);
        this.pointsScrabble = determinePointsScrabble(result);
        this.pointsWordsWithFriends = determinePointsWordsWithFriends(result);
    }

    /* --------------- */
    /* --- Methods --- */
    /* --------------- */

    /**
     * Determines the number of raw points for Scrabble(TM)
     * @param result - the result passed in
     * @return the number of points
     */
    private int determinePointsScrabble(String result) {
        result = result.replace(" ", "");
        int points = 0;
        for (int i = 0; i < result.length(); i++) {
            points += pointMap_scrabble.get(result.charAt(i));
        }
        return points;
    }

    /**
     * Determines the number of raw points for Words With Friends(TM)
     * @param result - the result passed in
     * @return the number of points
     */
    private int determinePointsWordsWithFriends(String result) {
        result = result.replace(" ", "");
        int points = 0;
        for (int i = 0; i < result.length(); i++) {
            points += pointMap_wordsWithFriends.get(result.charAt(i));
        }
        return points;
    }

    /* ----------------------- */
    /* --- Getters/Setters --- */
    /* ----------------------- */

    public static String getTag() {
        return TAG;
    }

    public int getPointsScrabble() {
        return pointsScrabble;
    }

    public void setPointsScrabble(int pointsScrabble) {
        this.pointsScrabble = pointsScrabble;
    }

    public int getPointsWordsWithFriends() {
        return pointsWordsWithFriends;
    }

    public void setPointsWordsWithFriends(int pointsWordsWithFriends) {
        this.pointsWordsWithFriends = pointsWordsWithFriends;
    }
}
