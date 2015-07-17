package com.rabbitfighter.wordsleuth.SearchFragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionHelpDialogFragment;
import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Utils.HelpDialogUtil;
import com.rabbitfighter.wordsleuth.Utils.RobotoFontsHelper;

/**
 * Search input fragment handles search input.
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/topics/search/search-dialog.html'
 * @since 0.1 2015-06-17.
 */
public class SearchInputFragment extends Fragment {

    private final static String TAG = "SearchInputFragment";

    /*
    The system calls this when creating the fragment. Within your implementation, you
    should initialize essential components of the fragment that you want to retain when
    the fragment is paused or stopped, then resumed.
    */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called");
        showHelpDialog();
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
        Log.i(TAG, "onCreateView() called");
        // View
        View rootView = inflater.inflate(R.layout.fragment_instructions_search_input, container, false);
        // Widgets
        TextView tv_title, tv_content;
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        // Fonts
        tv_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_black));
        tv_content.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_condensed_light));
        // Return the view
        return rootView;

    }

    /*
    The system calls this method as the first indication that the user is leaving the fragment
    (though it does not always mean the fragment is being destroyed). This is usually where you
    should commit any changes that should be persisted beyond the current user session (because
    the user might not come back).
    */
    @Override
    public void onPause() {
        Log.i(TAG, "onPause() called");
        super.onPause();
    }

    /**
     * Shows the help dialog if enabled
     */
    public void showHelpDialog() {
        boolean isHelpOn = HelpDialogUtil.isHelpEnabledOnAppStart(this.getActivity());

        if (isHelpOn) {
            DialogFragment helpDisableDialog = new InstructionHelpDialogFragment();

            // The second argument, "helpDisableInstructionsDialog", is a unique tag name that the
            // system uses to save and restore the fragment state when necessary. The tag also allows
            // you to get a handle to the fragment by calling findFragmentByTag().
            helpDisableDialog.show(getFragmentManager(), "helpDisableInstructionsDialog");
        }
    }


}
