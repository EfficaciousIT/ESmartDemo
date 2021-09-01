package com.mobi.efficacious.ESmartDemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Result_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_student;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.Standard;
import com.mobi.efficacious.ESmartDemo.fragment.StudentResultFragment;

import java.util.ArrayList;


public class Student_division_adapter extends ArrayAdapter<Standard> {
    private ArrayList<Standard> objects;
    private Context context;
    private String pagename;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private String role_id;

    public Student_division_adapter(Context context, ArrayList<Standard> objects, String value) {
        super(context, R.layout.standard_division,objects);
        this.objects = objects;
        this.context = context;
        this.pagename = value;

    }

    static class ImageHolder
    {
        TextView textView;


    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
       ImageHolder holder = null;
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.standard_division, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        holder.textView = (TextView) row.findViewById(R.id.textView38);
        holder.textView.setText(objects.get(position).getStandarad_div());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagename.contentEquals("Standarad_division")) {
                    ((Activity)context).finish();

                    StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                    Bundle args = new Bundle();
                    args.putString("std_id", StandardAdapter.Std_id_division);
                    args.putString("std_name", StandardAdapter.std_name_division);
                    args.putString("std_div", objects.get(position).getDivision_id());
                    args.putString("selected_layout", "Stdwise_name");
                    studentAttendanceActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,studentAttendanceActivity).commitAllowingStateLoss();

                }
                else if(pagename.contentEquals("Standarad_Result"))
                {
                    ((Activity)context).finish();
//                    AllStudentName_result studentResultFragment = new AllStudentName_result ();
//                    Bundle args = new Bundle();
//                    args.putString("std_id", StandardAdapter.Std_id_division);
//                    args.putString("std_name", StandardAdapter.std_name_division);
//                    args.putString("std_div", objects.get(position).getDivision_id());
//                    args.putString("selected_layout", "Stdwise_name");
//                    studentResultFragment.setArguments(args);
//                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,studentResultFragment).commitAllowingStateLoss();


                        if(role_id.contentEquals("3"))
                        {
                            Result_Tab studentResultFragment = new Result_Tab();
                            Bundle args = new Bundle();
                            args.putString("Div_name", objects.get(position).getStandarad_div());
                            args.putString("Std_name", StandardAdapter.std_name_division);
                            args.putInt("Div_id", Integer.parseInt(objects.get(position).getDivision_id()));
                            args.putInt("Std_id",Integer.parseInt(StandardAdapter.Std_id_division));
//                            args.putString("Stud_name", menus.get(position).getName());
//                            args.putInt("stud_id12", menus.get(position).getStudent_id());
                            studentResultFragment.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragment).commitAllowingStateLoss();
                        }else {
                            StudentResultFragment studentResultFragmen = new StudentResultFragment();
                            Bundle args = new Bundle();
                            args.putString("Div_name", objects.get(position).getStandarad_div());
                            args.putString("Std_name", StandardAdapter.std_name_division);
                            args.putInt("Div_id", Integer.parseInt(objects.get(position).getDivision_id()));
                            args.putInt("Std_id", Integer.parseInt(StandardAdapter.Std_id_division));
//                            args.putString("Stud_name", menus.get(position).getName());
//                            args.putInt("stud_id12", menus.get(position).getStudent_id());
                            studentResultFragmen.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragmen).commitAllowingStateLoss();
                        }



                }
                else
                {
                    TimetableActivity_student timetableActivity_student = new TimetableActivity_student ();
                    Bundle args = new Bundle();
                    args.putString("std_id", objects.get(position).getStandard_id());
                    args.putString("div_id", objects.get(position).getDivision_id());
                    timetableActivity_student.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,timetableActivity_student).commitAllowingStateLoss();

                }
            }
        });

        return row;
    }

}
