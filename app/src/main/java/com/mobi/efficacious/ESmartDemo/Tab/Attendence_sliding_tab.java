package com.mobi.efficacious.ESmartDemo.Tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.fragment.StudentAttendanceDetail;
import com.mobi.efficacious.ESmartDemo.fragment.Student_Calender_attendence;
import com.mobi.efficacious.ESmartDemo.fragment.TeacherAttendanceDetail;
import com.mobi.efficacious.ESmartDemo.fragment.Teacher_Calender_attendence;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EFF-4 on 3/19/2018.
 */

public class Attendence_sliding_tab extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    String attendence;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String name1,name2,teachr_id,teachr_name;
    int stud_id1;
    public static String stud_name;
    public static String stud_id;
    public static String teacher_id,teacher_name,teacher_designation;
    public static String staff_name,staff_dept;
    public static int staff_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calender, container, false);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        name1 = settings.getString("TAG_NAME", "");
        name2 =  settings.getString("TAG_NAME2", "");

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity.getSupportActionBar() != null;
        // activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightorange)));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        // after you set the adapter you have to check if view is laid out, i did a custom method for it
        if (ViewCompat.isLaidOut(tabLayout)) {
            setViewPagerListener();
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    setViewPagerListener();
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    private void setViewPagerListener() {
        tabLayout.setupWithViewPager(viewPager);
        // use class TabLayout.ViewPagerOnTabSelectedListener
        // note that it's a class not an interface as OnTabSelectedListener, so you can't implement it in your activity/fragment
        // methods are optional, so if you don't use them, you can not override them (e.g. onTabUnselected)
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
         attendence = getArguments().getString("attendence");
        if (attendence.contentEquals("student_self_attendence")) {
            stud_name=settings.getString("TAG_NAME", "");
            stud_id= settings.getString("TAG_STUDENTID", "");
            viewPagerAdapter.addFragment(new Student_Calender_attendence(), "Calender");
            viewPagerAdapter.addFragment(new StudentAttendanceDetail(), "List");
        }
        else  if (attendence.contentEquals("student_attendence")) {
            stud_name = getArguments().getString("Stud_name");
            stud_id1 = getArguments().getInt("stud_id12", 0);
            stud_id = String.valueOf(stud_id1);
             viewPagerAdapter.addFragment(new Student_Calender_attendence(), "Calender");
             viewPagerAdapter.addFragment(new StudentAttendanceDetail(), "List");

        }
        else if (attendence.contentEquals("staff_attendence")) {

//             viewPagerAdapter.addFragment(new Staff_Calender_attendence(), "Calender");
//             viewPagerAdapter.addFragment(new StaffAttendanceDetailsActivity(), "List");

        }
        else if(attendence.contentEquals("teacher_self_attendence"))
        {
            settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            teachr_id = settings.getString("TAG_USERID", "");
            teachr_name= settings.getString("TAG_NAME", "");
            teacher_id=teachr_id;
            teacher_name=teachr_name;
            teacher_designation = getArguments().getString("designation");
             viewPagerAdapter.addFragment(new Teacher_Calender_attendence(), "Calender");
             viewPagerAdapter.addFragment(new TeacherAttendanceDetail(), "List");
        }
        else
        {
            teacher_id=getArguments().getString("teacher_id");
            teacher_name=getArguments().getString("teachername");
            teacher_designation = getArguments().getString("designation");

            viewPagerAdapter.addFragment(new Teacher_Calender_attendence(), "Calender");
             viewPagerAdapter.addFragment(new TeacherAttendanceDetail(), "List");
        }

        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }
}