package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Subject;

import java.util.ArrayList;

public class SubjectAdapter extends ArrayAdapter<Subject> {

    private ArrayList<Subject> objects;
    private Context context;
//String Std_id;
    public SubjectAdapter(Context context, ArrayList<Subject> objects) {
        super(context, R.layout.subject_row, objects);
        this.objects = objects;
        this.context = context;
//        this.Std_id=Standard_id;
    }

    static class ImageHolder
    {
        TextView id;
        TextView name;
        LinearLayout syllabus;
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

        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.subject_row, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }

        holder.id=(TextView)row.findViewById(R.id.subid_subject);
        holder.name=(TextView)row.findViewById(R.id.subName_subject);
        holder.syllabus=(LinearLayout)row.findViewById(R.id.linearsubjt);
//        holder.id.setText(objects.get(position).getSubject_id());
        holder.name.setText(objects.get(position).getSubject_name());

       holder.syllabus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(objects.get(position).getSubject_name().contentEquals("No Data Available"))
               {
                   Toast.makeText(context,"No Data Available",Toast.LENGTH_SHORT).show();
               }else
               {
                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(objects.get(position).getSubject_url()));
                   context.startActivity(browserIntent);
               }

           }
       });
        return row;
    }

}