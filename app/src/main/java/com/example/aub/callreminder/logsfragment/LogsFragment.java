package com.example.aub.callreminder.logsfragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aub.callreminder.R;
import com.example.aub.callreminder.adapters.LogsFragAdapter;
import com.example.aub.callreminder.database.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogsFragment extends Fragment {

    @BindView(R.id.logs_frag_rv)
    RecyclerView mLogsFragRv;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    Unbinder unbinder;

    private LogsFragViewModel logsFragViewModel;
    private List<Contact> mContacts = new ArrayList<>();
    private LogsFragAdapter mAdapter;

    private View.OnClickListener deleteClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Contact contact = (Contact) v.getTag();
            logsFragViewModel.deleteContact(contact);
        }
    };

    public LogsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        unbinder = ButterKnife.bind(this, view);

        setupRecyclerView();

        logsFragViewModel = ViewModelProviders.of(this).get(LogsFragViewModel.class);
        logsFragViewModel.getData().observe(this, contacts -> {
            mContacts = contacts;
            mAdapter.setData(contacts);
            updateUi(contacts);
        });

        return view;
    }

    public void setupRecyclerView() {
        mAdapter = new LogsFragAdapter(mContacts, deleteClickListener);
        mLogsFragRv.setAdapter(mAdapter);
        mLogsFragRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void updateUi(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            mLogsFragRv.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mLogsFragRv.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
