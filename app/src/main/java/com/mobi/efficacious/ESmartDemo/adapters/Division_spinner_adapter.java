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

public class Division_spinner_adapter extends BaseAdapter {
    ViewHolder holder = null;
    private Activity activity;
    private ArrayList<HashMap<Object, Object>> data;
    private static LayoutInflater inflater = null;
    HashMap<Object, Object> result2 = new HashMap<Object, Object>();
    HashMap<Object, Object> result;

    String value1 = "";

    public Division_spinner_adapter(Activity a, ArrayList<HashMap<Object, Object>> dataList, String value) {
        activity = a;
        data = dataList;
        value1 = value;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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

        holder = new ViewHolder();
        vi = inflater.inflate(R.layout.spinner_item, null);


        holder.std_div_name = (TextView) vi.findViewById(R.id.textview1);

        result = data.get(position);
        result2 = result;
        if (value1.contentEquals("NoticeUserType")) {
            holder.std_div_name.setText(result.get("UserType").toString());

        } else if (value1.contentEquals("NoticeStandard")) {
            holder.std_div_name.setText(result.get("vchStandard_name").toString());
        } else if (value1.contentEquals("Division_name")) {
            holder.std_div_name.setText(result.get("div_name").toString());
        } else if (value1.contentEquals("SchoolName")) {
            holder.std_div_name.setText(result.get("School_name").toString());
        } else if (value1.contentEquals("SubjectName")) {
            holder.std_div_name.setText(result.get("SubjectName").toString());
        } else {
            holder.std_div_name.setText(result.get("AcademicYear").toString());
        }


        vi.setTag(holder);


        return vi;
    }

}

class ViewHolder {

    public ViewHolder() {
        // TODO Auto-generated constructor stub
    }

    TextView std_div_name;


}



