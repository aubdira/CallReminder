package com.example.aub.callreminder.adapters;

import android.content.Context;
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
import com.example.aub.callreminder.adapters.RemindersAdapter.ViewHolder;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.utils.DateTimeConverter;
import java.util.List;
import javax.inject.Inject;


/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class RemindersAdapter extends Adapter<ViewHolder> {
    
    @Inject ContactRepository mRepository;
    @Inject Context mContext;
    private List<Contact> mContactList;
    
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener callNowClickListener;
    
    public RemindersAdapter(List<Contact> contacts, View.OnClickListener cancelClickListener,
            View.OnClickListener callNowClickListener) {
        App.getContactRepositoryComponent().inject(this);
        mContactList = contacts;
        this.cancelClickListener = cancelClickListener;
        this.callNowClickListener = callNowClickListener;
    }
    
    public void setData(List<Contact> data) {
        this.mContactList = data;
        notifyDataSetChanged();
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_item_contact, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contact currentContact = mContactList.get(position);
        
        String contactName = currentContact.getContactName();
        if (contactName.isEmpty()) {
            holder.mContactName.setVisibility(View.GONE);
        } else {
            holder.mContactName.setText(contactName);
        }
        
        holder.mPhoneNumber.setText(currentContact.getContactPhoneNumber());
        holder.mDateTime.setText(DateTimeConverter.getFormattedDateTime(currentContact.getReminderTime()));
        
        String reminderReason = currentContact.getReminderReason();
        if (reminderReason.isEmpty()) {
            holder.mReminderReason.setVisibility(View.GONE);
        } else {
            holder.mReminderReason.setText(reminderReason);
        }
        
        // handel cancel button
        holder.mCancelBtn.setTag(currentContact);
        holder.mCancelBtn.setOnClickListener(cancelClickListener);
        
        // handel call button
        holder.mCallNowBtn.setTag(currentContact);
        holder.mCallNowBtn.setOnClickListener(callNowClickListener);
    }
    
    @Override
    public int getItemCount() {
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
