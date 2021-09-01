package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Studentlist;

import java.util.ArrayList;


public class StudentListAdapter extends ArrayAdapter<Studentlist> {

    private final Context context;
    private final ArrayList<Studentlist> itemsArrayList;

    public StudentListAdapter(Context context, ArrayList<Studentlist> itemsArrayList) {
        super(context, R.layout.studentlist_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.studentlist_row, parent, false);

        TextView Standard=(TextView)rowView.findViewById(R.id.Std_studentlistrow);
        TextView Division = (TextView)rowView.findViewById(R.id.div_studentlistrow);
        TextView Male = (TextView)rowView.findViewById(R.id.male_studentlistrow);
        TextView Female = (TextView)rowView.findViewById(R.id.female_studentlistrow);
        TextView Total = (TextView)rowView.findViewById(R.id.total_studentlistrow);

        Standard.setText(itemsArrayList.get(position).getStandard());
        Division.setText(itemsArrayList.get(position).getDivision());
        Male.setText(itemsArrayList.get(position).getMale());
        Female.setText(itemsArrayList.get(position).getFemale());
        Total.setText(itemsArrayList.get(position).getTotal());

        return rowView;
    }
}
