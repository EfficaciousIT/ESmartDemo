package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_teacher;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.AllTeacher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by EFF-4 on 9/3/2017.
 */

public class AllTeacherAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    ArrayList<AllTeacher> menus = new ArrayList<AllTeacher>();
    public ArrayList<AllTeacher> categories;
    public ArrayList<AllTeacher> orig;
    String url="";
    ImageHolder holder = null;
String page_selected;
    public AllTeacherAdapter(Context context, ArrayList<AllTeacher> Menus, String page) {
        super();
        this.context = context;
        this.menus = Menus;
        this.page_selected=page;
    }
    static class ImageHolder
    {
        TextView id;
        TextView name;
        CircleImageView teacher_image;
        LinearLayout linear;
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<AllTeacher> results = new ArrayList<AllTeacher>();
                if (orig == null)
                    orig = menus;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final AllTeacher g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                menus = (ArrayList<AllTeacher>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
      //  ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.allteacher_name, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        holder.teacher_image=row.findViewById(R.id.teacher_image);
        holder.id = (TextView) row.findViewById(R.id.Dept_allteacher1);
        holder.name = (TextView) row.findViewById(R.id.name_allteacher1);

        holder.linear = (LinearLayout) row.findViewById(R.id.Linear_allteacher1);

        holder.id.setText(menus.get(position).getDesignation());
        holder.name.setText(menus.get(position).getName());

        holder.teacher_image.setVisibility(View.VISIBLE);
        String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+menus.get(position).getTeacher_profile()+"";
        Picasso.with(context).load(url).resize(100,100)
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.teacher_image);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    if(page_selected.contentEquals("attendence")) {

        Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab ();
        Bundle args = new Bundle();
        args.putString("teachername", menus.get(position).getName());
        args.putString("designation", menus.get(position).getDesignation());
        args.putString("teacher_id", menus.get(position).getTeacher_id());
        args.putString("attendence", "teacher_attendence");
        attendence_sliding_tab.setArguments(args);
       MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,attendence_sliding_tab).commit();

    }
    else
    {
        TimetableActivity_teacher timetableActivity_teacher = new TimetableActivity_teacher ();
        Bundle args = new Bundle();
        args.putString("teacher_id", menus.get(position).getTeacher_id());
        timetableActivity_teacher.setArguments(args);
        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,timetableActivity_teacher).commit();


        }
            }
        });
//        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.slidedown);
//        row.startAnimation(animFadein6);
        return row;
    }



}