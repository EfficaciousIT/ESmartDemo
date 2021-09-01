package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Vacation;

import java.util.ArrayList;


public class VacationAdapter extends ArrayAdapter<Vacation> {
	 
    private final Context context;
    private final ArrayList<Vacation> itemsArrayList;

    public VacationAdapter(Context context, ArrayList<Vacation> itemsArrayList) {
        super(context, R.layout.vacation_row, itemsArrayList);
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
    	View rowView = inflater.inflate(R.layout.vacation_row, parent, false);

        TextView name=(TextView)rowView.findViewById(R.id.name_vacation);
        TextView fromdate = (TextView)rowView.findViewById(R.id.fromdate_vacation);
        TextView todate = (TextView)rowView.findViewById(R.id.todate_vacation);

        name.setText(itemsArrayList.get(position).getVacation_name());
        fromdate.setText(itemsArrayList.get(position).getFromDate());
        todate.setText(itemsArrayList.get(position).getToDate());
     
        return rowView;
    }
}
