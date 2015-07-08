package com.rabbitfighter.wordsleuth.Entries;

/**
 * TODO: Implement this for combos instead of words.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1 2015-06-17.
 * //TODO: Decide whether to use this or not
 */
@SuppressWarnings("unused")
public class Combo {

    private Result result1;
    private Result result2;
    private String wholeWord;
    private int numLetters;

    public Combo(String result, Result result1, Result result2) {
        this.result1 = result1;
        this.result2 = result2;
        this.numLetters = result1.getNumLetters() + result2.getNumLetters();
        this.wholeWord = result1.getWord() + result2.getWord();
    }

    public Result getResult1() {

        return result1;
    }

    public void setResult1(Result result1) {

        this.result1 = result1;
    }

    public Result getResult2() {

        return result2;
    }

    public void setResult2(Result result2) {

        this.result2 = result2;
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

        this.numLetters = numLetters - 1;
    }
}