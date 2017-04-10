package com.eric.savingsmanager.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.eric.savingsmanager.R;
import com.eric.savingsmanager.utils.Constants;
import com.eric.savingsmanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSavingsItemActivity extends AppCompatActivity {

    // Edit text for the fields
    private EditText mEditBankName;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    private EditText mEditAmount;
    private EditText mEditYield;
    private EditText mEditInterest;

    // Data
    private Calendar mCalendar = Calendar.getInstance();
    private Date mStartDate;
    private Date mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings_item);
        setupUI();
    }

    /**
     * Setup UI elements
     */
    private void setupUI() {

        // ID from layout to java
        mEditBankName = (EditText) findViewById(R.id.edit_bank_name);
        mEditStartDate = (EditText) findViewById(R.id.edit_start_date);
        mEditEndDate = (EditText) findViewById(R.id.edit_end_date);
        mEditAmount = (EditText) findViewById(R.id.edit_amount);
        mEditYield = (EditText) findViewById(R.id.edit_yield);
        mEditInterest = (EditText) findViewById(R.id.edit_interest);

        // set text watcher to update interest
        mEditAmount.addTextChangedListener(mInterestTextWatcher);
        mEditYield.addTextChangedListener(mInterestTextWatcher);

        // set date field listener
        mEditStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker((EditText) v, true);
                }
            }
        });
        mEditStartDate.setInputType(InputType.TYPE_NULL);
        mEditStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.hasFocus()) {
                    showDatePicker((EditText) v, true);
                }
            }
        });
        mEditEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker((EditText) v, false);
                }
            }
        });
        mEditEndDate.setInputType(InputType.TYPE_NULL);
        mEditEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.hasFocus()) {
                    showDatePicker((EditText) v, false);
                }
            }
        });
    }


    /**
     * A text watcher for fields which impacts interest
     */
    private TextWatcher mInterestTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateInterest();
        }
    };

    /**
     * Show date picker for edit field of date
     *
     * @param edit date field
     */
    private void showDatePicker(final EditText edit, final boolean startDate) {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

        // show date picker
        DatePickerDialog picker = new DatePickerDialog(AddSavingsItemActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // set the date to EditText
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date date = new Date(calendar.getTimeInMillis());
                        edit.setText(Utils.formatDate(date, Constants.FORMAT_DATE_YEAR_MONTH_DAY));
                        if (startDate) {
                            mStartDate = new Date(date.getTime());
                        } else {
                            mEndDate = new Date(date.getTime());
                        }
                        // has to do here
                        updateInterest();
                    }
                },
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    /**
     * Update interest based on current values
     */
    private void updateInterest() {

        String amountStr = mEditAmount.getText().toString();
        String yieldStr = mEditYield.getText().toString();

        if (mStartDate != null && mEndDate != null
                && !Utils.isNullOrEmpty(amountStr)
                && !Utils.isNullOrEmpty(yieldStr)) {
            // update the interest value in UI
            double days = Utils.getDiffDays(mStartDate, mEndDate);
            double amount = Double.valueOf(amountStr);
            double yield = Double.valueOf(yieldStr);
            double interest = amount * (yield / 100) * (days / Constants.DAYS_OF_ONE_YEAR);
            mEditInterest.setText(String.format(Locale.US, "%.2f", interest));
            Log.d(Constants.LOG_TAG, "start = " + mStartDate.toString() + "\nend = " + mEndDate.toString()
                    + "\ndays = " + days + "\namount = " + amount + "\nyield = " + yield + "\ninterest = " + interest);
        }

    }


}
