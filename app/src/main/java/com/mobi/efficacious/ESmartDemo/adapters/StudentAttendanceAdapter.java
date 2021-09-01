package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.StudentAttendance;

import java.util.ArrayList;


/**
 * Created by EFF on 2/22/2017.
 */

public class StudentAttendanceAdapter extends ArrayAdapter<StudentAttendance> {

    private final Context context;
    private final ArrayList<StudentAttendance> itemsArrayList;

    public StudentAttendanceAdapter(Context context, ArrayList<StudentAttendance> itemsArrayList) {
        super(context, R.layout.studentattendance_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.studentattendance_row, parent, false);

        TextView date = (TextView) rowView.findViewById(R.id.date_studentattendance);
        TextView status = (TextView) rowView.findViewById(R.id.status_studentattendance);

        date.setText(itemsArrayList.get(position).getDate());
        status.setText(itemsArrayList.get(position).getStatus());

        return rowView;
    }
}