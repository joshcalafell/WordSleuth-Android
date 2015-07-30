package com.rabbitfighter.wordsleuth.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rabbitfighter.wordsleuth.Adapters.InstructionsPagerAdapter;
import com.rabbitfighter.wordsleuth.R;
import com.rabbitfighter.wordsleuth.Services.BoundSearchService;

/**
 * App instructions. Five fragments
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/design/patterns/swipe-views.html'
 * @since 0.1 2015-06-17.
 */
@SuppressWarnings("unused")
public class InstructionActivity extends ActionBarActivity {

    public final static String TAG = "InstructionActivity";

    InstructionsPagerAdapter adapter;
    ViewPager viewPager;


    // Service connection class.
    BoundSearchService searchService;

    // Search intent for service connection
    Intent searchIntent;

    // Whether the service is bound or not...
    boolean isBound;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() called ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        // Setting up the ViewPager
        adapter = new InstructionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        // Action bar stuff
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        isBound = false;
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
       // inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
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

        if (!isBound) {
            // Bound service intent, different that received intent
            searchIntent = new Intent(this, BoundSearchService.class);
            // We want to bind to this. Params: (Intent, ServiceConnection, How to bind it)
            bindService(searchIntent, connection, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "Service is bound from on create");
        } else {
            Log.i(TAG, "Service was already bound. Nothing to do...");
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume() called");

        if (!isBound) {
            // Bound service intent, different that received intent
            searchIntent = new Intent(this, BoundSearchService.class);
            // We want to bind to this. Params: (Intent, ServiceConnection, How to bind it)
            bindService(searchIntent, connection, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "Service is bound from on create");
        } else {
            Log.i(TAG, "Service was already bound. Nothing to do...");
        }
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