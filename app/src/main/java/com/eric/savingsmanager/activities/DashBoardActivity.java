package com.eric.savingsmanager.activities;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eric.savingsmanager.R;
import com.eric.savingsmanager.data.SavingsBean;
import com.eric.savingsmanager.data.SavingsContentProvider;
import com.eric.savingsmanager.data.SavingsItemEntry;
import com.eric.savingsmanager.utils.Constants;
import com.eric.savingsmanager.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mSavingsItemListView;
    private SavingsItemListAdapter mListAdapter;
    ArrayList<SavingsBean> mSavingsBeanList = new ArrayList<>();
    private ProgressBar mProgressBar;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
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
        mListAdapter = new SavingsItemListAdapter();

        mSavingsItemListView.setAdapter(mListAdapter);
        mSavingsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavingsBean savings = mSavingsBeanList.get(position);
                startEditSavingsItemScreen(savings);
            }
        });

    }

    private void initData() {

        if (mSavingsBeanList != null) {
            mSavingsBeanList.clear();
        }

        // load from database and add into list
        Cursor cursor = getContentResolver().query(SavingsContentProvider.CONTENT_URI, null, null, null, "_id asc", null);
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
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        mListAdapter.notifyDataSetChanged();

    }


    private class SavingsItemListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSavingsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSavingsBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_savings_bean, null);
                viewHolder = new ViewHolder();
                viewHolder.bankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_account_amount);
                viewHolder.startTime = (TextView) convertView.findViewById(R.id.tv_start_time);
                viewHolder.endTime = (TextView) convertView.findViewById(R.id.tv_end_time);
                viewHolder.yield = (TextView) convertView.findViewById(R.id.tv_yield);
                viewHolder.interest = (TextView) convertView.findViewById(R.id.tv_interest);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            SavingsBean savingsBean = mSavingsBeanList.get(position);
            viewHolder.bankName.setText(savingsBean.getBankName());
            viewHolder.amount.setText(Utils.formatMoney(savingsBean.getAmount()));
            viewHolder.startTime.setText(Utils.formatDate(new Date(savingsBean.getStartDate()),
                    Constants.FORMAT_DATE_YEAR_MONTH_DAY));
            viewHolder.endTime.setText(Utils.formatDate(new Date(savingsBean.getEndDate()),
                    Constants.FORMAT_DATE_YEAR_MONTH_DAY));
            viewHolder.yield.setText(getString(R.string.formatted_yield, savingsBean.getYield()));
            viewHolder.interest.setText(Utils.formatMoney(savingsBean.getInterest()));
            return convertView;
        }

        class ViewHolder {
            TextView bankName;
            TextView amount;
            TextView startTime;
            TextView endTime;
            TextView yield;
            TextView interest;
        }
    }

}
