package com.example.aub.callreminder.logsfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.aub.callreminder.adapters.LogsFragAdapter;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.events.ContactLogsListEvent;
import com.example.aub.callreminder.events.DeleteLogAdapterEvent;
import com.example.aub.callreminder.events.UpdateContactAsLogEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class LogsFragment extends Fragment implements LogsFragView {

    @BindView(R.id.logs_frag_rv) RecyclerView mLogsFragRv;
    @BindView(R.id.tv_emptyView) TextView mTvEmptyView;
    Unbinder unbinder;

    private LogsFragPresenter mPresenter;
    private LogsFragAdapter mAdapter;

    public LogsFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LogsFragPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        mPresenter.loadData();
    }

    @Subscribe public void displayData(ContactLogsListEvent event) {
        List<Contact> contactLogsList = event.getContactLogsList();
        if (contactLogsList.isEmpty()) {
            mLogsFragRv.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mLogsFragRv.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);

            mAdapter = new LogsFragAdapter(contactLogsList, getContext());
            mLogsFragRv.setLayoutManager(new LinearLayoutManager(getContext()));
            mLogsFragRv.setAdapter(mAdapter);
        }
    }

    @Subscribe public void deleteAdapter(DeleteLogAdapterEvent event) {
        mPresenter.loadData();
    }

    @Subscribe public void updateAdapter(UpdateContactAsLogEvent event) {
        mPresenter.loadData();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
