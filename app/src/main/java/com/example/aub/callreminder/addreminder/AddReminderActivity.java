package com.example.aub.callreminder.addreminder;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.dateandtimepickers.DatePickerFragment;
import com.example.aub.callreminder.dateandtimepickers.DatePickerFragment.onDatePickerListener;
import com.example.aub.callreminder.dateandtimepickers.TimePickerFragment;
import com.example.aub.callreminder.dateandtimepickers.TimePickerFragment.onTimePickerListener;
import com.example.aub.callreminder.events.ContactNamePhoneEvent;
import com.example.aub.callreminder.events.NotifyMeEvent;
import com.example.aub.callreminder.events.StoreContactFinishedEvent;
import com.example.aub.callreminder.utils.DateTimeConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class AddReminderActivity extends FragmentActivity implements
        onTimePickerListener, onDatePickerListener, AddReminderView {

    private static final String TAG = "AddReminderActivity";
    public static final int REQUEST_CODE = 113;

    @BindView(R.id.et_contact_name) TextInputEditText mContactNameET;
    @BindView(R.id.et_contact_phone_number) TextInputEditText mContactPhoneNumberET;
    @BindView(R.id.et_reminder_reason) EditText mReminderReasonET;
    @BindView(R.id.btn_time_picker) Button mTimePickerBtn;
    @BindView(R.id.btn_date_picker) Button mDatePickerBtn;

    boolean btnIsRed = false;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private long mTimeInMills;
    private String mContactName;
    private String mPhoneNumber;
    private String mReminderReason;

    private AddReminderPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        mPresenter = new AddReminderPresenterImpl(this);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_time_picker)
    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        mTimePickerBtn.setText(getString(
                R.string.time_displayed, String.valueOf(mHour), String.valueOf(mMinute)));
    }

    @OnClick(R.id.btn_date_picker)
    public void showDatePickerDialog(View v) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mDatePickerBtn.setText(getString(R.string.date_displayed,
                String.valueOf(mDay), String.valueOf(mMonth + 1),
                String.valueOf(mYear)));
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
                    mPresenter.queryResolverForContactInfo(resolver, data);
                    break;
            }
        } else {
            Log.d(TAG, "pickContactInfo: Failed to pick contact");
        }
    }

    @OnClick(R.id.btn_set_reminder) public void setReminder() {
        mTimeInMills = DateTimeConverter.getTimeInMills(mYear, mMonth, mDay, mHour, mMinute);
        mContactName = mContactNameET.getText().toString();
        mPhoneNumber = mContactPhoneNumberET.getText().toString();
        mReminderReason = mReminderReasonET.getText().toString();

        mPresenter.validateContactFields(mContactName, mPhoneNumber, mTimeInMills, mReminderReason);

        Log.d(TAG, "setReminder: phone number " + mPhoneNumber);
        Log.d(TAG, "setReminder: date is " + mTimeInMills);
    }

    @Override public void phoneNumberRequiredError() {
        mContactPhoneNumberET.setError(getString(R.string.phone_number_required));
        mContactPhoneNumberET.requestFocus();
    }

    @Override public void displayFutureDateError() {
        mDatePickerBtn.setTextColor(Color.RED);
        mDatePickerBtn.requestFocus();
        btnIsRed = true;
        Toast.makeText(this, "Date should be in the future", Toast.LENGTH_SHORT).show();
    }

    @Subscribe public void onEvent(StoreContactFinishedEvent event) {
        mPresenter.createNotification(mContactName, mPhoneNumber, mReminderReason, mTimeInMills);
    }

    @Subscribe public void onEvent(NotifyMeEvent event) {
        finish();
    }

    @Subscribe public void onEvent(ContactNamePhoneEvent event) {
        Log.d(TAG, "EventBus onEvent: name " + event.getName());
        mContactNameET.setText(event.getName());
        mContactPhoneNumberET.setText(event.getPhone());
    }

    @Override protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mPresenter.onDetach();
        super.onDestroy();
    }
}