package com.rabbitfighter.wordsleuth.ListItems;

/**
 * A result item
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-05-28.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'Android custom list viws - Assosciated XML: item_result.xml'
 * @since 0.1
 */
public class ResultItem {

    // Instance vars
    private String result;
    private int length;
    private int iconID;
    private int iconExpandID;

    /**
     * Contrustor for a result item
     * @param result -  the result
     * @param length - length
     * @param iconID -  the left icon id
     * @param iconExpandID -  the right icon id
     */
    public ResultItem(String result, int length, int iconID, int iconExpandID) {
        this.result = result;
        this.length = length;
        this.iconID = iconID;
        this.iconExpandID = iconExpandID;
    }

    /* --- Getters/Setters --- */

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIconID() {
        return iconID;
    }

    // Unused for now, but there if we need it.
    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public int getIconExpandID() {
        return iconExpandID;
    }

    public void setIconExpandID(int iconExpandID) {
        this.iconExpandID = iconExpandID;
    }
}
