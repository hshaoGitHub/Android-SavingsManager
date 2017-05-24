package com.eric.savingsmanager.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eric.savingsmanager.R;
import com.eric.savingsmanager.data.SavingsBean;
import com.eric.savingsmanager.data.SavingsContentProvider;
import com.eric.savingsmanager.data.SavingsItemEntry;
import com.eric.savingsmanager.manager.AlarmsManager;
import com.eric.savingsmanager.manager.DataManager;
import com.eric.savingsmanager.utils.Constants;
import com.eric.savingsmanager.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    private ListView mSavingsItemListView;
    private SavingsItemListAdapter mListAdapter;
    ArrayList<SavingsBean> mSavingsBeanList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Date mNextDueSavingsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddSavingsItemScreen();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Start add savings item screen
     */
    private void startAddSavingsItemScreen() {
        Intent intent = new Intent(this, AddSavingsItemActivity.class);
        startActivity(intent);
    }

    /**
     * Start edit savings item screen
     */
    private void startEditSavingsItemScreen(SavingsBean savings) {
        Intent intent = new Intent(this, AddSavingsItemActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_SAVINGS_ITEM_PARCEL, savings);
        startActivity(intent);
    }

    private void initViews() {

        mSavingsItemListView = (ListView) findViewById(R.id.lv_savings);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mSavingsItemListView.setEmptyView(mProgressBar);

        mListAdapter = new SavingsItemListAdapter(this, null, true);
        mSavingsItemListView.setAdapter(mListAdapter);

        mSavingsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavingsBean savings = mSavingsBeanList.get(position);
                startEditSavingsItemScreen(savings);
            }
        });

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        return new CursorLoader(this, SavingsContentProvider.CONTENT_URI,
                null, null, null, "_id asc");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)

        initData(data);
        mListAdapter.swapCursor(data);

    }

    private void initData(Cursor cursor) {

        if (mSavingsBeanList != null) {
            mSavingsBeanList.clear();
        }

        while (cursor != null && cursor.moveToNext()) {
            SavingsBean savingsBean = new SavingsBean();
            long id = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry._ID));
            String bankName = cursor.getString(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_BANK_NAME));
            long startDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_START_DATE));
            long endDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_END_DATE));
            float amount = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_AMOUNT));
            float yield = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_YIELD));
            float interest = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_INTEREST));
            savingsBean.setId(id);
            savingsBean.setBankName(bankName);
            savingsBean.setStartDate(startDate);
            savingsBean.setEndDate(endDate);
            savingsBean.setAmount(amount);
            savingsBean.setYield(yield);
            savingsBean.setInterest(interest);
            mSavingsBeanList.add(savingsBean);
        }

        if (!Utils.isNullOrEmpty(mSavingsBeanList)) {
            mNextDueSavingsDate = DataManager.getNextDueSavingsItemDate(mSavingsBeanList);
            if (mNextDueSavingsDate != null) {
                // schedule an alarm
                AlarmsManager.scheduleAlarm(this, mNextDueSavingsDate);
            }
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        if (mSavingsBeanList != null) {
            mSavingsBeanList.clear();
        }
        mListAdapter.swapCursor(null);
    }



    private class SavingsItemListAdapter extends CursorAdapter {

        public SavingsItemListAdapter(Context context, Cursor c, boolean autoRequery){
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return getLayoutInflater().inflate(R.layout.item_savings_bean, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String bankName = cursor.getString(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_BANK_NAME));
            long startDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_START_DATE));
            long endDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_END_DATE));
            float amount = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_AMOUNT));
            float yield = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_YIELD));
            float interest = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUMN_NAME_INTEREST));

            TextView bankNameTxt = (TextView) view.findViewById(R.id.tv_bank_name);
            TextView amountTxt = (TextView) view.findViewById(R.id.tv_account_amount);
            TextView startTimeTxt = (TextView) view.findViewById(R.id.tv_start_time);
            TextView endTimeTxt = (TextView) view.findViewById(R.id.tv_end_time);
            TextView yieldTxt = (TextView) view.findViewById(R.id.tv_yield);
            TextView interestTxt = (TextView) view.findViewById(R.id.tv_interest);


            bankNameTxt.setText(bankName);
            amountTxt.setText(Utils.formatMoney(amount));
            startTimeTxt.setText(Utils.formatDate(startDate));
            endTimeTxt.setText(Utils.formatDate(endDate));
            yieldTxt.setText(getString(R.string.formatted_yield, yield));
            interestTxt.setText(Utils.formatMoney(interest));

            // Date color
            if (Utils.isToday(endDate)) {
                // Is today
                endTimeTxt.setTextColor(Color.BLUE);
            } else if (endDate < new Date().getTime()) {
                // Before today
                endTimeTxt.setTextColor(Color.LTGRAY);
            } else if (mNextDueSavingsDate != null
                    && endDate == mNextDueSavingsDate.getTime()) {
                // Next due date
                endTimeTxt.setTextColor(Color.RED);
            }
        }
    }

}
