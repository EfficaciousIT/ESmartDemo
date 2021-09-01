package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_student;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.dialogbox.Standard_division_dialog;
import com.mobi.efficacious.ESmartDemo.entity.Standard;
import com.mobi.efficacious.ESmartDemo.fragment.StandardName_By_Lectures;
import com.mobi.efficacious.ESmartDemo.fragment.StandardWise_Book;
import com.mobi.efficacious.ESmartDemo.fragment.StudentExamFragment;
import com.mobi.efficacious.ESmartDemo.fragment.StudentSyllabusFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class StandardAdapter extends ArrayAdapter<Standard> {


    String value;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    String[] divisionoption = new String[10];
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id,academic_id;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String Standard_id,userid;
    private ArrayList<Standard> objects;
    private Context context;
    private String pagename;
    public static String Std_id_division,std_name_division,page_name,std_id;
    public StandardAdapter(Context context, ArrayList<Standard> objects, String value) {
        super(context, R.layout.standard_row, objects);
        this.objects = objects;
        this.context = context;
        this.pagename = value;
    }

    static class ImageHolder
    {
        TextView id;
        TextView name;
        RelativeLayout linear;
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
        dataList = new ArrayList<HashMap<Object, Object>>();
        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.standard_row, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        String kjhk=objects.get(position).getStandard_name();

        holder.id = (TextView) row.findViewById(R.id.id_standard);
        holder.name = (TextView) row.findViewById(R.id.name_standard);
        holder.linear = (RelativeLayout) row.findViewById(R.id.Linear);
        holder.id.setText(objects.get(position).getStandard_id());
        holder.name.setText(objects.get(position).getStandard_name());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (pagename.equalsIgnoreCase("Exam")) {

                    StudentExamFragment studentExamActivity = new StudentExamFragment();
                    Bundle args = new Bundle();
                    args.putString("std_id", objects.get(position).getStandard_id());
                    studentExamActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,studentExamActivity).commit();
                }
                else if (pagename.equalsIgnoreCase("Syllabus"))
                {
                    StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                    Bundle args = new Bundle();
                    args.putString("std_id", objects.get(position).getStandard_id());
                    subjectFragment.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, subjectFragment).commit();
                }
                else if(pagename.equalsIgnoreCase("Std"))
                {
                    page_name="Standarad_division";
                    String stn1=objects.get(position).getStandard_name();
                    Std_id_division=objects.get(position).getStandard_id();
                    std_name_division=objects.get(position).getStandard_name();
//                    Standard_division_dialog notifDialog = new Standard_division_dialog(getContext());
//                    notifDialog.setCanceledOnTouchOutside(true);
//
//                    notifDialog.show();
                    Intent intent=new Intent(context,Standard_division_dialog.class);
                    intent.putExtra("std_id", objects.get(position).getStandard_id());
                    intent.putExtra("std_name", objects.get(position).getStandard_name());
                    intent.putExtra("selected_layout", "Stdwise_name");
                    context.startActivity(intent);
//                    StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
//                    Bundle args = new Bundle();
//                    args.putString("std_id", objects.get(position).getStandard_id());
//                    args.putString("std_name", objects.get(position).getStandard_name());
//                   // args.putString("std_div", objects.get(position).getDivision_id());
//                    args.putString("selected_layout", "Stdwise_name");
//
//                    studentAttendanceActivity.setArguments(args);
//                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,studentAttendanceActivity).commit();

                } else if (pagename.equalsIgnoreCase("timetable"))
                {
                    TimetableActivity_student timetableActivity_student = new TimetableActivity_student ();
                    Bundle args = new Bundle();
                    args.putString("std_id", objects.get(position).getStandard_id());
                    timetableActivity_student.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,timetableActivity_student).commit();

                }
                else if (pagename.equalsIgnoreCase("LibraryTeacher"))
                {
                    StandardName_By_Lectures.listview.setVisibility(View.GONE);
                    std_id=objects.get(position).getStandard_id();
                    FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.Contain_main, new StandardWise_Book());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(pagename.equalsIgnoreCase("Standarad_Result"))
                {
                    page_name="Standarad_Result";
                    String stn1=objects.get(position).getStandard_name();
                    Std_id_division=objects.get(position).getStandard_id();
                    std_name_division=objects.get(position).getStandard_name();
                    Intent intent=new Intent(context,Standard_division_dialog.class);
                    intent.putExtra("std_id", objects.get(position).getStandard_id());
                    intent.putExtra("std_name", objects.get(position).getStandard_name());
                    intent.putExtra("selected_layout", "Stdwise_name");
                    context.startActivity(intent);
                }
//                else if (pagename.equalsIgnoreCase("HomeWork"))
//                {
//                    StudentHomeworkFragment StudentHomeworkFragment = new StudentHomeworkFragment();
//                    Bundle args = new Bundle();
//                    args.putString("std_id", objects.get(position).getStandard_id());
//                    StudentHomeworkFragment.setArguments(args);
//                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, StudentHomeworkFragment).commit();
//                }else if (pagename.equalsIgnoreCase("DailyDiary"))
//                {
//                    StudentHomeworkFragment StudentHomeworkFragment = new StudentHomeworkFragment();
//                    Bundle args = new Bundle();
//                    args.putString("std_id", objects.get(position).getStandard_id());
//                    StudentHomeworkFragment.setArguments(args);
//                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, StudentHomeworkFragment).commit();
//                }
                else
                {
                    StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                    Bundle args = new Bundle();
                    args.putString("std_id", objects.get(position).getStandard_id());
                    subjectFragment.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, subjectFragment).commit();

                }
            }
        });
//        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.slidedown);
//        row.startAnimation(animFadein6);
        return row;
    }


}