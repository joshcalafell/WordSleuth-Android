package com.rabbitfighter.wordsleuth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.ResultsFragments.BlankTileResultFragment;
import com.rabbitfighter.wordsleuth.ResultsFragments.CrosswordResultFragment;
import com.rabbitfighter.wordsleuth.ResultsFragments.RegularResultFragment;
import com.rabbitfighter.wordsleuth.SearchFragments.SearchResultsRegularFragment;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Results list activity
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'Android lists: http://developer.android.com/guide/topics/ui/layout/listview.html'
 * @since 0.1 2015-06-17.
 */
public class ResultsListActivity extends ActionBarActivity {
    // Debugging TAG
    private static final String TAG = "ResultsListActivity";

    // Vars
    Fragment fragment;
    Bundle bundle;
    Intent intent;
    String resultType;
    String query;
    String searchType;

    /* ------------------------- */
    /* --- @Override methods --- */
    /* ------------------------- */

    /**
     * On creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // The new bundle
        if (savedInstanceState == null) {
            intent = getIntent();
            bundle = intent.getExtras();
            resultType = bundle.get("resultType").toString();
            searchType = bundle.get("searchType").toString();
            Log.i(TAG, resultType);
            query = bundle.get("query").toString();
            Log.i(TAG, query);
        }
        Log.i(TAG, "Search type: " + bundle.get("searchType").toString());

        // During initial setup, plug in the details fragment.
        if (searchType.compareTo("regularSearch")==0) {
            fragment = new RegularResultFragment();
        } else if (searchType.compareTo("blankTileSearch")==0) {
            fragment = new BlankTileResultFragment();
        } else if (searchType.compareTo("crosswordSearch")==0) {
            fragment = new CrosswordResultFragment();
        } else {
            Log.i(TAG, "Something went wrong with the search type bundle info");
        }

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.contentFragment, fragment).commit();

        // Action bar stuff
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /**
     * When the menu is created...
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() triggered");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_results, menu);
        // Note: Returns true if the menu properly inflates
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu items selected
     * @param item - The menu item passed in.
     * @return false to allow normal menu processing to
     *         proceed, true to consume it here.
     *         <- From the notes in superclass' docs.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected(MenuItem: " + item + ")");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection Simplifiadid not call through to super.onCreate()bleIfStatement
        if (id == R.id.search_bar) {
            Log.i(TAG, "search bar clicked");

            return true;
        }
        if (id == R.id.sort) {
            // Sort the items
            Log.i(TAG, "Sort menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    /* ------------------ */
    /* ----- States ----- */
    /* ------------------ */

    /**
     * When the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Resume
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume() called");
        super.onResume();
    }

    /**
     * On pause.
     * TODO: Decide what to do with resources.
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause() called");
        super.onPause();
    }


}//EOF
