package com.rabbitfighter.wordsleuth.Entries;

import java.util.Arrays;

/**
 * A Word object
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see
 * @since 0.1 2015-06-17.
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
        this.numLetters = determineNumLetters(this.getWord());
    }

    /**
     * Determine the length of the word. Subwords will have a space
     * @param word
     * @return
     */
    private int determineNumLetters(String word) {
        if (word.contains(" ")) {
            return word.length()-1;
        } else {
            return word.length();
        }
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
