package com.example.aub.callreminder.remindersfragment;


import android.Manifest.permission;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.aub.callreminder.R;
import com.example.aub.callreminder.adapters.RemindersAdapter;
import com.example.aub.callreminder.database.Contact;
import java.util.ArrayList;
import java.util.List;


public class RemindersFragment extends Fragment {
    
    //    private static final String TAG = "RemindersFragment";
    
    @BindView(R.id.reminder_frag_rv) RecyclerView mReminderFragRv;
    @BindView(R.id.tv_emptyView) TextView mTvEmptyView;
    private Unbinder unbinder;
    
    private List<Contact> contactList = new ArrayList<>();
    private RemindersAdapter mRemindersAdapter;
    
    RemindersFragViewModel remindersFragViewModel;
    
    private View.OnClickListener cancelClickListener = v -> {
        Contact contact = (Contact) v.getTag();
        remindersFragViewModel.cancelReminder(contact);
    };
    
    private View.OnClickListener callNowClickListener = v -> {
        Contact contact = (Contact) v.getTag();
        Intent callIntent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + contact.getContactPhoneNumber()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        if (ActivityCompat.checkSelfPermission(getContext(), permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            getContext().startActivity(callIntent);
        }
    };
    
    public RemindersFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        
        unbinder = ButterKnife.bind(this, view);
        
        setupRecyclerView();
        
        remindersFragViewModel = ViewModelProviders.of(this).get(RemindersFragViewModel.class);
        remindersFragViewModel.getData().observe(this, contacts -> {
            contactList = contacts;
            mRemindersAdapter.setData(contacts);
            updateUi(contacts);
        });
        
        return view;
    }
    
    private void setupRecyclerView() {
        mRemindersAdapter = new RemindersAdapter(contactList, cancelClickListener, callNowClickListener);
        mReminderFragRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mReminderFragRv.setAdapter(mRemindersAdapter);
    }
    
    private void updateUi(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            mReminderFragRv.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mReminderFragRv.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    
}
