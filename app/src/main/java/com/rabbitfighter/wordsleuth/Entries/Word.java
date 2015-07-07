package com.rabbitfighter.wordsleuth.Entries;

import java.util.Arrays;

/**
 * A Word object
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see
 * @since 0.1
 */
@SuppressWarnings("unused")
public class Word {

    // Instance vars
    private String word;
    private String wordSorted;
    private int numLetters;

    /**
     * Constructor
     * @param result - the result passed in
     */
    public Word(String result) {
        this.word = result.toLowerCase().trim();
        this.wordSorted = sortWord(result);
        this.numLetters = result.replaceAll("/[^a-zA-Z]/", "").length();
    }

    /**
     * Returns the sorted word
     * @param result
     * @return
     */
    private String sortWord(String result) {
        StringBuilder sb = new StringBuilder();
        char[] resultCharArray = result.toCharArray();
        Arrays.sort(resultCharArray);
        for (char c : resultCharArray) sb.append(c);
        return sb.toString();
    }

    /* --- Getters/Setters --- */

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordSorted() {
        return this.wordSorted;
    }

    public void setWordSorted(String wordSorted) {
        this.wordSorted = wordSorted;
    }

    public int getNumLetters() {
        return this.numLetters;
    }

    public void setNumLetters(int numLetters) {
        this.numLetters = numLetters;
    }

}// EOF
