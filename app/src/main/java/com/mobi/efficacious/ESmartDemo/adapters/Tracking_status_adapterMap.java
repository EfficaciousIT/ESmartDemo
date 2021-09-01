package com.mobi.efficacious.ESmartDemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by EFF-4 on 3/28/2018.
 */

public class Tracking_status_adapterMap extends BaseAdapter {
    ViewHolder1 holder = null;
    private Activity activity;
    private ArrayList<HashMap<Object, Object>> data;
    private static LayoutInflater inflater = null;
    HashMap<Object, Object> result2 = new HashMap<Object, Object>();
    HashMap<Object, Object> result;

String value1="";

    public Tracking_status_adapterMap(Activity a, ArrayList<HashMap<Object, Object>> dataList, String value  ) {
        activity = a;
        data = dataList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

value1=value;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        data.clear();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View vi, ViewGroup parent) {

        result = new HashMap<Object, Object>();

        holder = new ViewHolder1();
        vi = inflater.inflate(R.layout.spinner_item_map, null);


        holder.stdname = (TextView) vi.findViewById(R.id.textview1);

        result = data.get(position);
        result2 = result;


if(value1.contentEquals("DAte"))
{
    holder.stdname.setText(result.get("date").toString());
}else if(value1.contentEquals("StandradName"))
{
    holder.stdname.setText(result.get("vchStandard_name").toString());
}else if(value1.contentEquals("Exam_Name"))
{
    holder.stdname.setText(result.get("Exam_Name").toString());
}
else if(value1.contentEquals("DivisionName"))
{
    holder.stdname.setText(result.get("vchDivisionName").toString());
}else if(value1.contentEquals("SubjectName"))
{
    holder.stdname.setText(result.get("vchSubjectName").toString());
}
else
{
    holder.stdname.setText(result.get("VehicleNo").toString());
}



        vi.setTag(holder);


        return vi;
    }

}

class ViewHolder12 {

    public ViewHolder12() {
        // TODO Auto-generated constructor stub
    }
TextView stdname;


}


