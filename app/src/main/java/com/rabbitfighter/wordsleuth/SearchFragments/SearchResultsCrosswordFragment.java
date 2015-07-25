package com.rabbitfighter.wordsleuth.SearchFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rabbitfighter.wordsleuth.Activities.ResultsListActivity;
import com.rabbitfighter.wordsleuth.Database.ResultsDbAdapter;
import com.rabbitfighter.wordsleuth.Entries.Entry;
import com.rabbitfighter.wordsleuth.ListItems.ResultTypeItem;
import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Utils.RobotoFontsHelper;

import net.dicesoft.net.apprater.AppRater;

import java.util.ArrayList;
import java.util.List;

/**
 * Search results fragment displays the result types.
 * A list populated by a custom list view (W.I.P.)
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/guide/components/fragments.html'
 * @since 0.1 2015-06-17.
 */
public class SearchResultsCrosswordFragment extends Fragment {

    public final static String TAG = "SearchResultsCrosswordFragment";
    private List<ResultTypeItem> resultTypeItemList;

    // Query
    String query;

    // Database
    ResultsDbAdapter dbAdapter;

    // Vars
    View itemView;
    ResultTypeItem resultType;
    TextView tv_num_matches, tv_num_matches_title, tv_result_type, tv_title, tv_query, tv_query_title, tv_number_letters, tv_num_letters_title, tv_count_wildcards_title, tv_count_wildcards;
    View rootView;

    /**
     * On creation of fragment
     * @param savedInstanceState -  the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Get query from bundle
        query =  getArguments().get("query").toString();
        dbAdapter = new ResultsDbAdapter(getActivity());
        resultTypeItemList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    /**
     * When the view gets created
     * @param inflater - the LayoutInflater object
     * @param container - The ViewGroup object
     * @param savedInstanceState - The Bundle
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Logger
        Log.i("Crossword", "onCreateView called");
        // View
        rootView = inflater.inflate(R.layout.fragment_search_crossword_results, container, false);
        // Populate the list
        populateResultTypeList();
        // Populate the list view with the list
        populateListView();
        // Control the callbacks from item clicks
        registerClickCallback(rootView);

        // Set components
        tv_query = (TextView) rootView.findViewById(R.id.tv_query);
        tv_query_title = (TextView) rootView.findViewById(R.id.tv_query_title);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_number_letters = (TextView) rootView.findViewById(R.id.tv_length);
        tv_num_letters_title = (TextView) rootView.findViewById(R.id.tv_length_title);
        tv_num_letters_title = (TextView) rootView.findViewById(R.id.tv_length_title);
        tv_count_wildcards = (TextView) rootView.findViewById(R.id.tv_wildcard_number);
        tv_count_wildcards_title = (TextView) rootView.findViewById(R.id.tv_count_wildcard_title);
        tv_num_matches = (TextView) rootView.findViewById(R.id.tv_numResults);
        tv_num_matches_title = (TextView) rootView.findViewById(R.id.tv_results_title);
        tv_result_type = (TextView) rootView.findViewById(R.id.tv_resultType);

        // Set the text fields for query && num letters
        tv_query.setText("\"" +query+ "\"");
        tv_number_letters.setText(query.length() + " letters");
        tv_count_wildcards.setText(String.valueOf(new Entry(query).getCount_blank_tiles()));

        // Fonts
        tv_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_black)); // Black
        tv_query_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light
        tv_query.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light
        tv_num_letters_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light
        tv_number_letters.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light
        tv_count_wildcards.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light
        tv_count_wildcards_title.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext().getApplicationContext(), RobotoFontsHelper.roboto_light)); // Light

         /* Return the root view */
        return rootView;

    }

    private void runAppRater() {
        AppRater appRater = new AppRater(getActivity());
        appRater.setDaysBeforePrompt(3);
        appRater.setLaunchesBeforePrompt(7);
        appRater.setTargetUri("http://google.com");
        appRater.setPhrases(
                R.string.rate_title,
                R.string.rate_explanation,
                R.string.rate_now,
                R.string.rate_later,
                R.string.rate_never);

        AlertDialog appRateDialog = appRater.getDialog();

        if (appRateDialog != null) {
            appRateDialog.show();
        }
    }

    /* --------------- */
    /* --- Adapter --- */
    /* --------------- */

    /**
     * List adapter.
     * @see 'http://developer.android.com/reference/android/widget/ArrayAdapter.html'
     */
    public class ResultTypeListAdapter extends ArrayAdapter<ResultTypeItem> {
        /**
         * Constructor that calls super. TODO: Research
         */
        public ResultTypeListAdapter() {
            super(getActivity(), R.layout.item_result_type, resultTypeItemList);
            Log.i("Crossword", "ResultTypeListAdapter constructor called super");
        }

        /**
         * These are the list views
         * @param position -  the position of the item
         * @param convertView -  the view passed in
         * @param parent -  the parent
         * @return the view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("Crossword", "getView() called");

            // The view passed in may be null, just an F.Y.I.
            itemView = getActivity().getLayoutInflater().inflate(R.layout.item_result_type, parent, false);

            // Find Result Type
            resultType = resultTypeItemList.get(position);

            // Result type
            tv_result_type = (TextView) itemView.findViewById(R.id.tv_resultType);
            tv_num_matches = (TextView) itemView.findViewById(R.id.tv_numMatches);
            tv_num_matches.setText(String.valueOf(resultType.getNumMatches()) + " results found");

            if (position == 0) {
                tv_result_type.setText("Matches Item");
            } else {
                Log.i("Crossword", "Type not found");
            }

            tv_num_matches.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext(), RobotoFontsHelper.roboto_light)); // Light
            tv_result_type.setTypeface(RobotoFontsHelper.getTypeface(rootView.getContext(), RobotoFontsHelper.roboto_black));
            // Return the view
            return itemView;

        }
    }

    /* --------------- */
    /* --- Methods --- */
    /* --------------- */

    /**
     * Opens the results list activity with query and result type bundled in intent
     * @param resultType the resultType to lookup
     */
    private void openResultListActivity(String resultType) {
        query =  getArguments().get("query").toString();
        Intent intent = new Intent(getActivity(), ResultsListActivity.class);
        Bundle b = new Bundle();
        b.putString("query", query);
        b.putString("resultType", resultType);
        b.putString("searchType", "crosswordSearch");
        b.putInt("sortType", 0);
        intent.putExtras(b);
        startActivity(intent);

    }

    /**
     * Populate the results item list
     */
    public void populateResultTypeList() {
        if (resultTypeItemList == null || resultTypeItemList.isEmpty()) {
            resultTypeItemList.add(new ResultTypeItem("anagram", dbAdapter.getNumberAnagrams(), R.mipmap.ic_action_good, R.mipmap.ic_action_new));
        } else {
            resultTypeItemList.get(0).setNumMatches(dbAdapter.getNumberAnagrams());
        }
    }

    /**
     * Populate the list view.
     */
    private void populateListView() {
        Log.i("Crossword", "populateListView() called");
        query =  getArguments().get("query").toString();
        // Vars
        ArrayAdapter<ResultTypeItem> adapter;
        ListView list;
        // Create adapter
        adapter = new ResultTypeListAdapter();
        // Assert not null

        // Set list component
        if (getView() != null) {
            list = (ListView) getView().findViewById(R.id.lv_results);
            // Assign te list adapter
            list.setAdapter(adapter);
        }

    }

    /* ---------------- */
    /* --- Callbacks --- */
    /* ---------------- */

    /**
     * Callback for item clicks.
     * @param rootView - the root view passed in.
     */
    private void registerClickCallback(final View rootView) {
        Log.i("Crossword", "registerClickCallback() called");
        ListView list;
        // Assert the view not null
        if (rootView == null) throw new AssertionError();
        // Set the list view component
        list = (ListView) rootView.findViewById(R.id.lv_results);
        // Set listener
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    /**
                     * When an item is clicked
                     * @param parent - the parent
                     * @param viewClicked - the view that was clicked
                     * @param position -  the position of the clicked item
                     * @param id - ???
                     */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                        ResultTypeItem clickedItem = resultTypeItemList.get(position);
                        openResultListActivity(clickedItem.getResultType().toString());
                    }


                }
        );
    }

    /* -------------- */
    /* --- States --- */
    /* -------------- */

    /**
     * On resume
     */
    @Override
    public void onResume() {
        // Populate the list and list view again, then call super on resume
        query =  getArguments().get("query").toString();
        populateResultTypeList();
        populateListView();
        super.onResume();
    }

    /**
     * On start
     */
    @Override
    public void onStart() {
        query =  getArguments().get("query").toString();
        // Populate the list and list view again, then call super on resume
        populateResultTypeList();
        populateListView();
        runAppRater();
        super.onStart();
    }

}//EOF
