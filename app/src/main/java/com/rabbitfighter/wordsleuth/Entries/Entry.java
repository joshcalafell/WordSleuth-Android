package com.rabbitfighter.wordsleuth.Entries;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Experiment to see if using these huge fucking entries and querying the dictionary
 * database ( DB - 31 fields: _id, word, sortedWord, length, count_A, count_B, ... count_WILDCARDS)
 * instead of reading through the dictionary text. It's my theory that this will be a pain, but
 * much faster. The benchmark test is usually "Bryan Coulter" (a friend of mine) who's name has a
 * lot of cool subwords and combos (i.e. "Contrary Blue", "Baron Cruelty", etc...)
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/WordSleuth-Android (Temporary)
 * @see ''
 * @since 0.1 2015-07-06.
 */
@SuppressWarnings("unused")
public class Entry extends Word {
    // Logger
    private static final String TAG = "Entry";
    // Character count map
    private Map<Character, Integer> charMap;
    // Vars for map... if (Overkill = true) -> {Search Results = fast}
    private int count_A; private int count_B; private int count_C; private int count_D;
    private int count_E; private int count_F; private int count_G; private int count_H;
    private int count_I; private int count_J; private int count_K; private int count_L;
    private int count_M; private int count_N; private int count_O; private int count_P;
    private int count_Q; private int count_R; private int count_S; private int count_T;
    private int count_U; private int count_V; private int count_W; private int count_X;
    private int count_Y; private int count_Z; private int count_Wildcards;

    public Entry(String result) {
        // A word has: (Word, Sorted Word, Word Length)
        super(result);
        this.charMap = new HashMap<>();
        // Initialize counts to zero to avoid null pointers
        this.count_A = 0; this.count_B = 0; this.count_C = 0; this.count_D = 0; this.count_E = 0;
        this.count_F = 0; this.count_G = 0; this.count_H = 0; this.count_I = 0; this.count_J = 0;
        this.count_K = 0; this.count_L = 0; this.count_M = 0; this.count_N = 0; this.count_O = 0;
        this.count_P = 0; this.count_Q = 0; this.count_R = 0; this.count_S = 0; this.count_T = 0;
        this.count_U = 0; this.count_V = 0; this.count_W = 0; this.count_X = 0; this.count_Y = 0;
        this.count_Z = 0; this.count_Wildcards = 0;
        // Construct the char map
        this.charMap = constructCharMap(result);
    }

    /**
     * Map that hold the number of occurrences of each given character (letter)
     * @param result - The word to map.
     * @return the map of occurrences
     */
    private Map<Character, Integer> constructCharMap(String result) {
        for (char letter : result.toLowerCase().toCharArray()) {
            switch(letter) {
                case 'a': this.count_A++; break;
                case 'b': this.count_B++; break;
                case 'c': this.count_C++; break;
                case 'd': this.count_D++; break;
                case 'e': this.count_E++; break;
                case 'f': this.count_F++; break;
                case 'g': this.count_G++; break;
                case 'h': this.count_H++; break;
                case 'i': this.count_I++; break;
                case 'j': this.count_J++; break;
                case 'k': this.count_K++; break;
                case 'l': this.count_L++; break;
                case 'm': this.count_M++; break;
                case 'n': this.count_N++; break;
                case 'o': this.count_O++; break;
                case 'p': this.count_P++; break;
                case 'q': this.count_Q++; break;
                case 'r': this.count_R++; break;
                case 's': this.count_S++; break;
                case 't': this.count_T++; break;
                case 'u': this.count_U++; break;
                case 'v': this.count_V++; break;
                case 'w': this.count_W++; break;
                case 'x': this.count_X++; break;
                case 'y': this.count_Y++; break;
                case 'z': this.count_Z++; break;
                case '*': this.count_Wildcards++; break;
                default: Log.e(TAG, "Something went wrong"); break;
            }
            charMap.put('A', this.getCount_A());
            charMap.put('B', this.getCount_B());
            charMap.put('C', this.getCount_C());
            charMap.put('D', this.getCount_D());
            charMap.put('E', this.getCount_E());
            charMap.put('F', this.getCount_F());
            charMap.put('G', this.getCount_G());
            charMap.put('H', this.getCount_H());
            charMap.put('I', this.getCount_I());
            charMap.put('J', this.getCount_J());
            charMap.put('K', this.getCount_K());
            charMap.put('L', this.getCount_L());
            charMap.put('M', this.getCount_M());
            charMap.put('N', this.getCount_N());
            charMap.put('O', this.getCount_O());
            charMap.put('P', this.getCount_P());
            charMap.put('Q', this.getCount_Q());
            charMap.put('R', this.getCount_R());
            charMap.put('S', this.getCount_S());
            charMap.put('T', this.getCount_T());
            charMap.put('U', this.getCount_U());
            charMap.put('V', this.getCount_V());
            charMap.put('W', this.getCount_W());
            charMap.put('X', this.getCount_X());
            charMap.put('Y', this.getCount_Y());
            charMap.put('Z', this.getCount_Z());
            charMap.put('*', this.getCount_Wildcards());

        }
        return charMap;
    }

    public Map<Character, Integer> getCharMap() {
        return this.charMap;
    }

    public int getCount_A() {
        return count_A;
    }

    public int getCount_B() {
        return count_B;
    }

    public int getCount_C() {
        return count_C;
    }

    public int getCount_D() {
        return count_D;
    }

    public int getCount_E() {
        return count_E;
    }

    public int getCount_F() {
        return count_F;
    }

    public int getCount_G() {
        return count_G;
    }

    public int getCount_H() {
        return count_H;
    }

    public int getCount_I() {
        return count_I;
    }

    public int getCount_J() {
        return count_J;
    }

    public int getCount_K() {
        return count_K;
    }

    public int getCount_L() {
        return count_L;
    }

    public int getCount_M() {
        return count_M;
    }

    public int getCount_N() {
        return count_N;
    }

    public int getCount_O() {
        return count_O;
    }

    public int getCount_P() {
        return count_P;
    }

    public int getCount_Q() {
        return count_Q;
    }

    public int getCount_R() {
        return count_R;
    }

    public int getCount_S() {
        return count_S;
    }

    public int getCount_T() {
        return count_T;
    }

    public int getCount_U() {
        return count_U;
    }

    public int getCount_V() {
        return count_V;
    }

    public int getCount_W() {
        return count_W;
    }

    public int getCount_X() {
        return count_X;
    }

    public int getCount_Y() {
        return count_Y;
    }

    public int getCount_Z() {
        return count_Z;
    }

    public int getCount_Wildcards() {
        return count_Wildcards;
    }
}
