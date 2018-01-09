package com.example.aub.callreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.aub.callreminder.adapters.MainPagerAdapter;
import com.example.aub.callreminder.addreminder.AddReminderActivity;
import com.example.aub.callreminder.events.NotifyMeEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "MainActivity";

    @BindView(R.id.toolBar) Toolbar mToolBar;
    @BindView(R.id.main_view_pager) ViewPager mViewPager;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolBar);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.fab_add_reminder) void addReminder() {
        Intent intent = new Intent(this, AddReminderActivity.class);
        startActivity(intent);
    }

    @Subscribe public void onEvent(NotifyMeEvent event) {
        Toast.makeText(this, "We will notify you in the future", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
