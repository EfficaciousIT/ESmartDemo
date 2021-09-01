package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.StudentTimetable;

import java.util.ArrayList;


public class StudentTimetableAdapter extends ArrayAdapter<StudentTimetable> {

    private final Context context;
    private final ArrayList<StudentTimetable> itemsArrayList;
String wer,sub,teach;
    public StudentTimetableAdapter(Context context, ArrayList<StudentTimetable> itemsArrayList) {
        super(context, R.layout.studenttimetable_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.studenttimetable_row, parent, false);

        TextView time=(TextView)rowView.findViewById(R.id.PeriodTime_studenttimetable);
        TextView subject = (TextView)rowView.findViewById(R.id.Subject_studenttimetable);
        TextView teacher = (TextView)rowView.findViewById(R.id.Teacher_studenttimetable);

         wer=itemsArrayList.get(position).getTime();
                 sub=itemsArrayList.get(position).getSubjectName();
                 teach=itemsArrayList.get(position).getTeacherName();
        time.setText(itemsArrayList.get(position).getTime());
        subject.setText(itemsArrayList.get(position).getSubjectName());
        teacher.setText(itemsArrayList.get(position).getTeacherName());

        return rowView;
    }
}
