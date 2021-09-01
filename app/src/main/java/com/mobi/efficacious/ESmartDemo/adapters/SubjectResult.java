package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Subject;

import java.util.ArrayList;

public class SubjectResult extends ArrayAdapter<Subject> {
    private ArrayList<Subject> objects;
    private Context context;
    private String valuee,role_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public SubjectResult(Context context, ArrayList<Subject> objects, String value) {
        super(context, R.layout.student_result, objects);
        this.objects = objects;
        this.context = context;
        this.valuee = value;
    }

    static class ImageHolder
    {
        TextView sub_marks;

      TextView sub_name;

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
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.student_result, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        holder.sub_name = (TextView) row.findViewById(R.id.subjectName);
        holder.sub_marks = (TextView) row.findViewById(R.id.subMarks);
        holder.sub_name.setText(objects.get(position).getSubject_name()+" : ");

        if(valuee.contentEquals("Result"))
        {
            holder.sub_marks.setText(objects.get(position).getSubject_Marks());
        }
        return row;
    }


}