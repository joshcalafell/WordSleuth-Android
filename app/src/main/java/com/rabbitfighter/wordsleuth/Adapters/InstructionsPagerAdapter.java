package com.rabbitfighter.wordsleuth.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionFragment1;
import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionFragment2;
import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionFragment3;
import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionFragment4;
import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionFragment5;

/**
 * InstructionsPagerAdapter Extends FragmentStatePagerAdapter and is the controller for
 * the slide view content for the instructions page
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1
 */
public class InstructionsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUMBER_OF_INTRODUCTION_PAGES = 5;

    protected Context myContext;

    public InstructionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        myContext = context;
    }


    /**
     * Returns the fragment associated with the specified position.
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                fragment = new InstructionFragment1();
                args.putInt("page_position", position + 1);
                fragment.setArguments(args);
                return fragment;
            case 1:
                fragment = new InstructionFragment2();
                args.putInt("page_position", position + 1);
                fragment.setArguments(args);
                return fragment;
            case 2:
                fragment = new InstructionFragment3();
                args.putInt("page_position", position + 1);
                fragment.setArguments(args);
                return fragment;
            case 3:
                fragment = new InstructionFragment4();
                args.putInt("page_position", position + 1);
                fragment.setArguments(args);
                return fragment;
            case 4:
                fragment = new InstructionFragment5();
                args.putInt("page_position", position + 1);
                fragment.setArguments(args);
                return fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_INTRODUCTION_PAGES;
    }

}

