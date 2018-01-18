package com.example.aub.callreminder.remindersfragment;


import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.aub.callreminder.events.CallNowEvent;
import com.example.aub.callreminder.events.ContactIdEvent;
import com.example.aub.callreminder.events.ContactListEvent;
import com.example.aub.callreminder.events.DeleteLogAdapterEvent;
import com.example.aub.callreminder.events.UpdateContactAsLogEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class RemindersFragment extends Fragment implements ReminderFragView {

    private static final String TAG = "RemindersFragment";

    @BindView(R.id.reminder_frag_rv) RecyclerView mReminderFragRv;
    @BindView(R.id.tv_emptyView) TextView mTvEmptyView;
    private Unbinder unbinder;

    private ReminderFragPresenter mPresenter;
    private RemindersAdapter mRemindersAdapter;

    public RemindersFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ReminderFragPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        mPresenter.loadData();
    }

    @Subscribe public void displayData(ContactListEvent event) {
        List<Contact> contactList = event.getContactList();
        if (contactList.isEmpty()) {
            mReminderFragRv.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mReminderFragRv.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);

            mRemindersAdapter = new RemindersAdapter(contactList);
            mReminderFragRv.setAdapter(mRemindersAdapter);
            mReminderFragRv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe public void onEvent(ContactIdEvent event) {
        mRemindersAdapter.notifyItemInserted((int) event.getId());
        mReminderFragRv.scrollToPosition((int) event.getId());
    }

    @Subscribe public void onEvent(DeleteLogAdapterEvent event) {
        mPresenter.loadData();
    }

    @Subscribe public void updateAdapter(UpdateContactAsLogEvent event) {
        mPresenter.loadData();
    }

    @Subscribe public void onEvent(CallNowEvent event) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + event.getContactPhoneNumber()));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(getContext(), permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            getContext().startActivity(callIntent);
        }
    }
}
