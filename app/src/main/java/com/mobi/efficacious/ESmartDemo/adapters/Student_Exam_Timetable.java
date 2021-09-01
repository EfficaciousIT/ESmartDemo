package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.StudentExamTimetable;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/24/2018.
 */

public class Student_Exam_Timetable extends ArrayAdapter<StudentExamTimetable> {

    private final Context context;
    private final ArrayList<StudentExamTimetable> itemsArrayList;

    public Student_Exam_Timetable(Context context, ArrayList<StudentExamTimetable> itemsArrayList) {
        super(context, R.layout.student_exam_timetable, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.student_exam_timetable, parent, false);

        TextView exam_date = (TextView) rowView.findViewById(R.id.textView14);
        TextView exam_time = (TextView) rowView.findViewById(R.id.textView16);
        TextView exam_subject = (TextView) rowView.findViewById(R.id.textView17);
        TextView exam_type = (TextView) rowView.findViewById(R.id.textView18);

        exam_date.setText(itemsArrayList.get(position).getExamination_date());
        exam_time.setText(itemsArrayList.get(position).getExamination_Time());
        exam_subject.setText(itemsArrayList.get(position).getSubjectName());
        exam_type.setText(itemsArrayList.get(position).getExamName());

        return rowView;
    }
}