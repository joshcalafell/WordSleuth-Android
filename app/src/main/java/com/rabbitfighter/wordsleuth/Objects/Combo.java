package com.rabbitfighter.wordsleuth.Objects;

/**
 * TODO: Implement this for combos instead of words.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1
 */
public class Combo extends Word{

    private String word1;
    private String word2;
    private String wholeWord;
    private int numLetters;

    public Combo(String result, String word1, String word2) {

        super(result);
        this.word1 = word1;
        this.word2 = word2;
        this.wholeWord = word1  + word2;
        this.numLetters = this.wholeWord.length();
    }

    /**
     * Gets the formatted string with a space in the middle of the two words
     * @return the formatted string
     */
    public String getFormattedString() {
        return this.getWord1() + " " + this.getWord2();
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWholeWord() {
        return wholeWord;
    }

    public void setWholeWord(String wholeWord) {
        this.wholeWord = wholeWord;
    }

    public int getNumLetters() {
        return numLetters;
    }

    public void setNumLetters(int numLetters) {
        this.numLetters = numLetters;
    }
}
