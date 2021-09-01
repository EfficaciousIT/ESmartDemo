package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_teacher;


/**
 * Created by EFF-4 on 3/26/2018.
 */

public class Single_name_fragment extends Fragment {
    View myview;
TextView name,designation;
LinearLayout linearLayout;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String name_,ID_,school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.allteacher_name,null);
        name=(TextView)myview.findViewById(R.id.name_allteacher1);
       designation=(TextView)myview.findViewById(R.id.Dept_allteacher1) ;
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        name_ = settings.getString("TAG_NAME", "");
        ID_=settings.getString("TAG_USERID","");
        name.setText(name_);
        designation.setText("Teacher");
        linearLayout=(LinearLayout) myview.findViewById(R.id.Linear_allteacher1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimetableActivity_teacher timetableActivity_teacher = new TimetableActivity_teacher ();
                Bundle args = new Bundle();
                args.putString("teacher_id",ID_);
                timetableActivity_teacher.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,timetableActivity_teacher).commit();

            }
        });
        return  myview;
    }
}
