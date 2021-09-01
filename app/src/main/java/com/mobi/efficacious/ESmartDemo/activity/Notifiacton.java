package com.mobi.efficacious.ESmartDemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.AdminApproval_Tab;
import com.mobi.efficacious.ESmartDemo.fragment.DailyDiaryListFragment;
import com.mobi.efficacious.ESmartDemo.fragment.Event_list_fragment;
import com.mobi.efficacious.ESmartDemo.fragment.Gallery_fragment;
import com.mobi.efficacious.ESmartDemo.fragment.LeaveListFragment;

public class Notifiacton extends AppCompatActivity {
    String value;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        FragmentManager mfragment = getSupportFragmentManager();
        Intent intent = getIntent();
            value = intent.getStringExtra("pagename");
            if(value.contentEquals("Gallery"))
            {
                Gallery_fragment gallery_fragment = new Gallery_fragment();
                mfragment.beginTransaction().replace(R.id.content_main, gallery_fragment).commit();
            }
        if(value.contentEquals("Event"))
        {
            Event_list_fragment event_list_fragment = new Event_list_fragment();
            mfragment.beginTransaction().replace(R.id.content_main, event_list_fragment).commit();
        }
        if(value.contentEquals("LeaveApply"))
        {
            AdminApproval_Tab adminApproval_tab = new AdminApproval_Tab();
            mfragment.beginTransaction().replace(R.id.content_main, adminApproval_tab).commit();
        }
        if(value.contentEquals("Leave Approval"))
        {
            LeaveListFragment leaveListFragment = new LeaveListFragment();
            mfragment.beginTransaction().replace(R.id.content_main, leaveListFragment).commit();
        }
        if(value.contentEquals("DailyDiary"))
        {
            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
            Bundle args = new Bundle();
            args.putString("value", "DailyDiary");
            dailyDiaryListFragment.setArguments(args);
            mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commit();
        }
        if(value.contentEquals("HomeWork"))
        {
            DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
            Bundle args = new Bundle();
            args.putString("value", "HomeWork");
            dailyDiaryListFragment.setArguments(args);
            mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commit();
        }
    }
}
