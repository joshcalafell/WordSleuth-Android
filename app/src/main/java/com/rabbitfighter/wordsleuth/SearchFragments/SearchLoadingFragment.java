package com.rabbitfighter.wordsleuth.SearchFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Utils.RobotoFontsHelper;


/**
 * Search loading fragment handles search input.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/topics/search/search-dialog.html'
 * @since 0.1 2015-06-17.
 */
public class SearchLoadingFragment extends Fragment {

    private final static String TAG = "SearchLoadingFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    /*
        The system calls this when it's time for the fragment to draw its user interface
        for the FIRST TIME. To draw a UI for your fragment, you must return a View from this
        method that is the root of your fragment's layout. You can return null if the fragment
        does not provide a UI.
        */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View
        View rootView = inflater.inflate(R.layout.fragment_search_loading, container, false);

        // Widgets

        ProgressBar progressBar;

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        // Progress bar
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Return the view
        return rootView;

    }

}
