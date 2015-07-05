package com.rabbitfighter.wordsleuth.ListItems;

/**
 * InstructionsPagerAdapter Extends FragmentStatePagerAdapter and is the controller for
 * the slide view content for the instructions page
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-05-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1
 */
public class ResultTypeItem {

    // Instance vars
    private String resultType;
    private int numMatches;
    private int iconID;
    private int iconExpandID;

    /**
     *
     * @param resultType - the result type
     * @param numMatches - the number matches
     * @param iconID -  the left icon Id
     * @param iconExpandID -  the right icon Id
     */
    public ResultTypeItem(String resultType, int numMatches, int iconID, int iconExpandID) {
        this.resultType = resultType;
        this.numMatches = numMatches;
        this.iconID = iconID;
        this.iconExpandID = iconExpandID;
    }
    /* --- Getters/Setters --- */

    public String getResultType() {
        return resultType;
    }

    public void setNumMatches(int numMatches) {
        this.numMatches = numMatches;
    }

    public int getNumMatches() {
        return numMatches;
    }

    // Unused for now, but here if we need it.

    public int getIconID() {
        return iconID;
    }


    public int getIconExpandID() {
        return iconExpandID;
    }

}// EFO
