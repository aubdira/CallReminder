package com.example.aub.callreminder.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.adapters.LogsFragAdapter.ViewHolder;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.utils.DateTimeConverter;
import java.util.List;
import javax.inject.Inject;


/**
 * Created by aub on 1/3/18.
 * Project: CallReminder
 */

public class LogsFragAdapter extends Adapter<ViewHolder> {
    
    @Inject ContactRepository mRepository;
    private View.OnClickListener deleteClickListener;
    private List<Contact> mContactList;
    
    public LogsFragAdapter(List<Contact> contactList, View.OnClickListener deleteClickListener) {
        App.getContactRepositoryComponent().inject(this);
        mContactList = contactList;
        this.deleteClickListener = deleteClickListener;
    }
    
    public void setData(List<Contact> contacts) {
        mContactList = contacts;
        notifyDataSetChanged();
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_log_contact, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact currentContact = mContactList.get(position);
        
        String contactName = currentContact.getContactName();
        if (contactName.isEmpty()) {
            holder.mTvLogContactName.setVisibility(View.GONE);
        } else {
            holder.mTvLogContactName.setText(contactName);
        }
        
        holder.mTvLogPhoneNumber.setText(currentContact.getContactPhoneNumber());
        holder.mTvLogDateTime
                .setText(DateTimeConverter.getFormattedDateTime(currentContact.getReminderTime()));
        
        String reminderReason = currentContact.getReminderReason();
        if (reminderReason.isEmpty()) {
            holder.mTvLogReminderReason.setVisibility(View.GONE);
        } else {
            holder.mTvLogReminderReason.setText(reminderReason);
        }
        
        // handel delete button
        holder.mDeleteBtn.setTag(currentContact);
        holder.mDeleteBtn.setOnClickListener(deleteClickListener);
    }
    
    @Override
    public int getItemCount() {
        return mContactList.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        
        @BindView(R.id.tv_log_contact_name) TextView mTvLogContactName;
        @BindView(R.id.tv_log_phone_number) TextView mTvLogPhoneNumber;
        @BindView(R.id.tv_log_date_time) TextView mTvLogDateTime;
        @BindView(R.id.tv_log_reminder_reason) TextView mTvLogReminderReason;
        @BindView(R.id.delete_btn) Button mDeleteBtn;
        @BindView(R.id.log_card_view) CardView mLogCardView;
        
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    
}
