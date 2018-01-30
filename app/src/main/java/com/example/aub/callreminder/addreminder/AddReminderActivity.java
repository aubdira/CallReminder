package com.example.aub.callreminder.addreminder;

import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.dateandtimepickers.DatePickerFragment;
import com.example.aub.callreminder.dateandtimepickers.DatePickerFragment.onDatePickerListener;
import com.example.aub.callreminder.dateandtimepickers.TimePickerFragment;
import com.example.aub.callreminder.dateandtimepickers.TimePickerFragment.onTimePickerListener;
import com.example.aub.callreminder.utils.DateTimeConverter;


public class AddReminderActivity extends FragmentActivity implements onTimePickerListener,
        onDatePickerListener {
    
    public static final int REQUEST_CODE = 113;
    private static final String TAG = "AddReminderActivity";
    @BindView(R.id.et_contact_name) TextInputEditText mContactNameET;
    @BindView(R.id.et_contact_phone_number) TextInputEditText mContactPhoneNumberET;
    @BindView(R.id.et_reminder_reason) TextInputEditText mReminderReasonET;
    @BindView(R.id.btn_time_picker) Button mTimePickerBtn;
    @BindView(R.id.btn_date_picker) Button mDatePickerBtn;
    
    boolean btnIsRed = false;
    
    private AddReminderViewModel mAddReminderViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ButterKnife.bind(this);
        
        setupClickListeners();
        setupViewModel();
    }
    
    private void setupViewModel() {
        mAddReminderViewModel = ViewModelProviders.of(this).get(AddReminderViewModel.class);
        mContactNameET.setText(mAddReminderViewModel.getContactName());
        mContactPhoneNumberET.setText(mAddReminderViewModel.getContactPhoneNumber());
        mReminderReasonET.setText(mAddReminderViewModel.getReminderReason());
        mTimePickerBtn.setText(mAddReminderViewModel.getTime());
        mDatePickerBtn.setText(mAddReminderViewModel.getDate());
    }
    
    private void setupClickListeners() {
        mContactNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                mAddReminderViewModel.setContactName(s.toString());
            }
        });
        
        mContactPhoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                mAddReminderViewModel.setContactPhoneNumber(s.toString());
            }
        });
        
        mReminderReasonET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                mAddReminderViewModel.setReminderReason(s.toString());
            }
        });
        
        mTimePickerBtn.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            timeFragment.show(getFragmentManager(), "timePicker");
        });
        
        mDatePickerBtn.setOnClickListener(v -> {
            DialogFragment dateFragment = new DatePickerFragment();
            dateFragment.show(getFragmentManager(), "datePicker");
        });
    }
    
    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        mAddReminderViewModel.setHour(hourOfDay);
        mAddReminderViewModel.setMinute(minute);
        String time = getString(R.string.time_displayed, String.valueOf(hourOfDay), String.valueOf(minute));
        
        mAddReminderViewModel.setTime(time);
        mTimePickerBtn.setText(mAddReminderViewModel.getTime());
    }
    
    
    @Override
    public void onDateSet(int year, int month, int day) {
        mAddReminderViewModel.setYear(year);
        mAddReminderViewModel.setMonth(month);
        mAddReminderViewModel.setDay(day);
        String date = getString(R.string.date_displayed, String.valueOf(day), String.valueOf(month + 1),
                String.valueOf(year));
        
        mAddReminderViewModel.setDate(date);
        mDatePickerBtn.setText(mAddReminderViewModel.getDate());
        if (btnIsRed) {
            mDatePickerBtn.setTextColor(Color.BLACK);
            btnIsRed = false;
        }
    }
    
    @OnClick(R.id.btn_from_contact)
    public void retrieveContactInfo(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    ContentResolver resolver = getContentResolver();
                    mAddReminderViewModel.getContactInfo(resolver, data);
                    mContactNameET.setText(mAddReminderViewModel.getContactName());
                    mContactPhoneNumberET.setText(mAddReminderViewModel.getContactPhoneNumber());
                    break;
            }
        } else {
            Log.d(TAG, "pickContactInfo: Failed to pick contact");
        }
    }
    
    @OnClick(R.id.btn_set_reminder)
    public void setReminder() {
        if (mAddReminderViewModel.getContactPhoneNumber().isEmpty()) {
            phoneNumberRequiredError();
            return;
        }
        if (DateTimeConverter.getNowDate() > mAddReminderViewModel.getTimeInMillis()) {
            displayFutureDateError();
            return;
        }
        
        mAddReminderViewModel.addContact();
        mAddReminderViewModel.notifyMe();
        finish();
    }
    
    public void phoneNumberRequiredError() {
        mContactPhoneNumberET.setError(getString(R.string.phone_number_required));
        mContactPhoneNumberET.requestFocus();
    }
    
    public void displayFutureDateError() {
        mDatePickerBtn.setTextColor(Color.RED);
        mDatePickerBtn.requestFocus();
        btnIsRed = true;
        Toast.makeText(this, "We can try, but we can't promise to set the reminder in the past",
                Toast.LENGTH_SHORT).show();
    }
}