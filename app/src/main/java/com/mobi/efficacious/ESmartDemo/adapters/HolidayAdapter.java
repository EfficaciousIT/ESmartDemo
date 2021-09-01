package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Holiday;

import java.util.ArrayList;


public class HolidayAdapter extends ArrayAdapter<Holiday> {
	 
    private final Context context;
    private final ArrayList<Holiday> itemsArrayList;

    public HolidayAdapter(Context context, ArrayList<Holiday> itemsArrayList) {
        super(context, R.layout.holiday_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.holiday_row, parent, false);

        TextView name=(TextView)rowView.findViewById(R.id.name_holiday);
        TextView fromdate = (TextView)rowView.findViewById(R.id.fromdate_holiday);

        name.setText(itemsArrayList.get(position).getHoliday_name());
        fromdate.setText(itemsArrayList.get(position).getFromDate());

        return rowView;
    }
}
