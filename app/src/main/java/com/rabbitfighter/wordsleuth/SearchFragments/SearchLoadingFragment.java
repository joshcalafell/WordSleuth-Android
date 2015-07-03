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
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/topics/search/search-dialog.html'
 * @since 0.1
 */
public class SearchLoadingFragment extends Fragment {

    private final static String TAG = "SearchLoadingFragment";

    /*
    The system calls this when it's time for the fragment to draw its user interface
    for the FIRST TIME. To draw a UI for your fragment, you must return a View from this
    method that is the root of your fragment's layout. You can return null if the fragment
    does not provide a UI.
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View
        View rootView = inflater.inflate(R.layout.fragment_search_input, container, false);

        // Widgets
        TextView tv_title;
        ProgressBar progressBar;
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        // Fonts
        tv_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_black));

        // Progress bar
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Return the view
        return rootView;

    }
}
