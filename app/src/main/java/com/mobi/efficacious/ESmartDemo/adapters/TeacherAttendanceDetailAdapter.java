package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.TeacherAttendaceDetail;

import java.util.ArrayList;


/**
 * Created by Hemant on 01-10-17.
 */

public class TeacherAttendanceDetailAdapter extends ArrayAdapter<TeacherAttendaceDetail> {

    private final Context context;
    private final ArrayList<TeacherAttendaceDetail> itemsArrayList;

    public TeacherAttendanceDetailAdapter(Context context, ArrayList<TeacherAttendaceDetail> itemsArrayList) {
        super(context, R.layout.teacherattendance_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.teacherattendance_row, parent, false);

        TextView date = (TextView) rowView.findViewById(R.id.date_teacherattendance);
        TextView status = (TextView) rowView.findViewById(R.id.status_teacherattendance);

        date.setText(itemsArrayList.get(position).getDate());
        status.setText(itemsArrayList.get(position).getStatus());

        return rowView;
    }
}