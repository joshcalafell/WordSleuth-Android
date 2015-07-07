package com.rabbitfighter.wordsleuth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rabbitfighter.wordsleuth.Adapters.InstructionsPagerAdapter;
import com.rabbitfighter.wordsleuth.InstructionFragments.InstructionHelpDialogFragment;
import com.rabbitfighter.wordsleuth.R;

/**
 * App instructions. Five fragments
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.1 (pre-beta) 2015-06-17.
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1
 */
@SuppressWarnings("unused")
public class InstructionActivity extends ActionBarActivity {

    public final static String TAG = "InstructionActivity";

    InstructionsPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called ");
        super.onCreate(savedInstanceState);

        Intent in = getIntent();
        Bundle b = in.getExtras();

        boolean isHelpOn = b.getBoolean("isHelpOn");

        if (isHelpOn) {
            showDisableHelpDialog();
        }

        setContentView(R.layout.activity_instructions);

        // Setting up the ViewPager
        adapter = new InstructionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        // Action bar stuff
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /**
     * Constructor calls super.
     */
    public InstructionActivity() {

    }

    /**
     * Options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Do menu stuff here..

        return super.onCreateOptionsMenu(menu);
    }

    private void showDisableHelpDialog() {
        DialogFragment helpDisableDialog = new InstructionHelpDialogFragment();

        // The second argument, "helpDisableInstructionsDialog", is a unique tag name that the
        // system uses to save and restore the fragment state when necessary. The tag also allows
        // you to get a handle to the fragment by calling findFragmentByTag().
        helpDisableDialog.show(getSupportFragmentManager(), "helpDisableInstructionsDialog");

    }

    /**
     * Item selected
     *
     * @param item - the menu item
     * @return the state of the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected() called with item " + item);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() called");
    }

}//EOF