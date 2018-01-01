package com.example.aub.callreminder.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.utils.DateTimeConverter;
import java.util.List;


/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class RemindersAdapter extends Adapter<RemindersAdapter.ViewHolder> {

    private List<Contact> mContactList;

    public RemindersAdapter(List<Contact> contactList) {
        mContactList = contactList;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Contact currentContact = mContactList.get(position);

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
    }

    @Override public int getItemCount() {
        return mContactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_contact_name) TextView mContactName;
        @BindView(R.id.tv_phone_number) TextView mPhoneNumber;
        @BindView(R.id.tv_date_time) TextView mDateTime;
        @BindView(R.id.tv_reminder_reason) TextView mReminderReason;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
