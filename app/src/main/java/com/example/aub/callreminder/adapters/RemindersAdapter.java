package com.example.aub.callreminder.adapters;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.adapters.RemindersAdapter.ViewHolder;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.CallNowEvent;
import com.example.aub.callreminder.events.DeleteAdapterEvent;
import com.example.aub.callreminder.utils.DateTimeConverter;
import java.util.List;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class RemindersAdapter extends Adapter<ViewHolder> {

    private List<Contact> mContactList;
    private ContactRepository mRepository;
    private Context mContext;

    public RemindersAdapter(List<Contact> contactList, Context context) {
        mContactList = contactList;
        mRepository = new ContactRepository(context);
        mContext = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contact currentContact = mContactList.get(position);

        String contactName = currentContact.getContactName();
        if (contactName.isEmpty()) {
            holder.mContactName.setVisibility(View.GONE);
        } else {
            holder.mContactName.setText(contactName);
        }

        holder.mPhoneNumber.setText(currentContact.getContactPhoneNumber());
        holder.mDateTime
                .setText(DateTimeConverter.getFormattedDateTime(currentContact.getReminderTime()));

        String reminderReason = currentContact.getReminderReason();
        if (reminderReason.isEmpty()) {
            holder.mReminderReason.setVisibility(View.GONE);
        } else {
            holder.mReminderReason.setText(reminderReason);
        }

        // handel cancel button
        holder.mCancelBtn.setOnClickListener(new OnClickListener() {
            @SuppressLint("StaticFieldLeak") @Override public void onClick(View v) {
                // delete(for now) the reminder from the database
                new AsyncTask<Void, Void, Void>() {
                    @Override protected Void doInBackground(Void... voids) {
                        mRepository.deleteContact(currentContact);

                        // cancel the alarm manager
                        Intent intent = new Intent(mContext, NotificationPublisher.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                mContext,
                                (int) currentContact.getReminderTime(),
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager = (AlarmManager) mContext
                                .getSystemService(Context.ALARM_SERVICE);
                        if (alarmManager != null) {
                            alarmManager.cancel(pendingIntent);
                        }

                        return null;
                    }

                    @Override protected void onPostExecute(Void v) {
                        super.onPostExecute(v);
                        // update the adapter
                        DeleteAdapterEvent event = new DeleteAdapterEvent();
                        EventBus.getDefault().post(event);
                    }
                }.execute();
            }
        });

        // handel call button
        holder.mCallNowBtn.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                CallNowEvent event = new CallNowEvent(currentContact.getContactPhoneNumber());
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override public int getItemCount() {
        return mContactList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view) CardView mCardView;
        @BindView(R.id.tv_contact_name) TextView mContactName;
        @BindView(R.id.tv_phone_number) TextView mPhoneNumber;
        @BindView(R.id.tv_date_time) TextView mDateTime;
        @BindView(R.id.tv_reminder_reason) TextView mReminderReason;
        @BindView(R.id.cancel_btn) Button mCancelBtn;
        @BindView(R.id.call_now_btn) Button mCallNowBtn;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
