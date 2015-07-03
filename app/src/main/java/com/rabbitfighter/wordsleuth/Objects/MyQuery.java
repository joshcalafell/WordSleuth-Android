package com.rabbitfighter.wordsleuth.Objects;

/**
 * A query is a type of Word
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see
 * @since 0.1
 */
@SuppressWarnings("unused")
public class MyQuery extends Word {

    private static final char WILDCARD = '*';
    private int wildcards;

    public MyQuery(String result, int wildcards) {
        super(result);
        this.wildcards = wildcards;
    }

    public MyQuery(String result) {
        super(result);
        this.wildcards = getNumWildcards(result);
    }

    public int getNumWildcards(String result) {
        int numWildcards = 0;
        for (int i = 0; i < result.length(); i++) {

            if (result.charAt(i)==WILDCARD) {
                numWildcards++;
            }
        }
        return numWildcards;
    }

    public int getWildcards() {
        return wildcards;
    }

    public void setWildcards(int wildcards) {
        this.wildcards = wildcards;
    }
}
