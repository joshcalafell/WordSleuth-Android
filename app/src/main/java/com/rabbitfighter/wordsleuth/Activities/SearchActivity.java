package com.rabbitfighter.wordsleuth.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.SearchFragments.SearchInputFragment;
import com.rabbitfighter.wordsleuth.SearchFragments.SearchLoadingFragment;
import com.rabbitfighter.wordsleuth.SearchFragments.SearchResultsFragment;
import com.rabbitfighter.wordsleuth.Services.BoundSearchService;
import com.rabbitfighter.wordsleuth.Utils.Message;

/**
 * Search activity. 3 Frags. There are many like it but this one is mine...
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/topics/search/search-dialog.html'
 * @since 0.1
 */
public class SearchActivity extends ActionBarActivity {
    // Debugging TAG
    private static final String TAG = "SearchInputActivity";

    /* ------------ */
    /* --- Vars---- */
    /* ------------ */

    // View
    SearchView searchView;

    // Search manager
    SearchManager searchManager;

    // Fragment
    Fragment fragment;

    // Service connection class.
    BoundSearchService searchService;

    // bound or not.
    boolean isBound;

    /* This is the lifecylcle of the states... */
    private final static int SEARCH_STATE = 0x0;
    private final static int LOADER_STATE = 0x1; // TODO: Not implemented. State for progress bar
    private final static int RESULT_STATE = 0x2;
    // Array to hold fragment states
    public Fragment[] frags = new Fragment[] // [0x0,0x1,0x2]
            {new SearchInputFragment(),new SearchLoadingFragment(),new SearchResultsFragment()};

    /* ------------------------- */
    /* --- @Override methods --- */
    /* ------------------------- */

    /**
     * On creation of activity
     * @param savedInstanceState - the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Boolean for bound or not
        isBound = false;

        // Orientation change
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // This does virtually nothing except hog room in the memory stack somewhere now...
            // But... if you wanted to do something when the orientation is set to landscape,
            // do it here -->
            return;
        }
        /* The bundle 'savedInstanceState' should contain the current fragment, but if
         * it is a new activity, it won't, so only start the process if it is indeed null */
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            fragment = frags[SEARCH_STATE];
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.contentFragment, fragment).commit();
        }
        // Action bar stuff
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Handle the intent
        handleIntent(getIntent());

        // Bound service intent, different that received intent
        Intent searchIntent = new Intent(this, BoundSearchService.class);
        // We want to bind to this. Params: (Intent, ServiceConnection, How to bind it)
        bindService(searchIntent, connection, Context.BIND_AUTO_CREATE);

        /*
        Keyboard popping up bug fix
        @see 'http://stackoverflow.com/questions/2496901/android-on-screen-keyboard-auto-popping-up'
         */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /**
     * When the menu is created...
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() triggered");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        // Associate searchable configuration with the SearchView.
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Declare the search view.
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Set the searchable info (This is straight from the dev pages, so I don't fully get it yet
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Set submit button to enabled.
        searchView.setSubmitButtonEnabled(true);

        /**
         * Listener for search input
         */
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    // Submit
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.i(TAG, "onQueryTextSubmit(" + query + ")");
                        return false;
                    }
                    // Text changed --> (i.e. F, FO, FOO, FOOB, FOOBA, FOOBAR)
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.i(TAG, "onQueryTextChange() called: " + newText);
                        return false;
                    }
                }
        );
        // Note: Returns true if the menu properly inflates
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu items selected
     * @param item - The me
        }nu item passed in.
     * @return false to allow normal menu processing to
     *         proceed, true to consume it here.
     *         <- From the notes in superclass' docs.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected(MenuItem" + item + ")");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_bar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* ---------------- */
    /* --- Services --- */
    /* ---------------- */

    /**
     * Service Connection
     */
    public ServiceConnection connection = new ServiceConnection() {
        // when we connect to the service.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Service connected...");
            // Reference to our binder class
            BoundSearchService.MyLocalBinder binder = (BoundSearchService.MyLocalBinder) service;
            // Once we have access, we get the class container IBinder with cool methods.
            searchService = binder.getService();
            // Set bound to true, because we are now bound to a service
            isBound = true;
        }

        // When we disconnect from a service
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service disconnected...");
            isBound = false;
        }
    };

    /* ---------------------- */
    /* --- Intent related --- */
    /* ---------------------- */

    /**
     * When a new search intent comes in
     * @param intent - the intent passed in
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handles the search intent
     * @param intent -  the intent passed in
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).replaceAll("/[^a-zA-Z ]/", "");
            // Query may be empty...
            if (query.isEmpty()) {
                Log.i(TAG, "Query is empty");
                Message.msgLong(getApplicationContext(), "Query cannot be empty");
            } else {
                Log.i(TAG, "Query is: " + query);
                performSearch(query);
            }
        }
    }

    /**
     * Transitions to the results fragment using a fragment manager transition
     * @param query - the user's query
     */
    private void transitionFragment(String query) {
        // Fragment manager stuff
        fragment = frags[RESULT_STATE];
        // @NOTE: This solved a bug 'java.lang.IllegalStateException: Fragment already active'
        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            b.putString("query", query);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.contentFragment, fragment).commit();
        }
    }


    /* ---------------------- */
    /* --- Custom methods --- */
    /* ---------------------- */

    /**
     * Performs the search
     * @param query - the user's query
     */
    public void performSearch(String query) {
        // MSG query
        Message.msgLong(getApplicationContext(), "\"" + query + "\" submitted");
        // If dictionary is loaded, start the search service
        try {
            if (searchService.prepareDictionary()) {
                searchService.searchDictionary(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "An error occurred performing search");
        } finally {
            Log.i(TAG, "Search completed");
            // Transition to results fragment
            transitionFragment(query);
        }
    }


    /* ------------------ */
    /* ----- States ----- */
    /* ------------------ */

    /**
     * When the activity is started
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart() called");
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
