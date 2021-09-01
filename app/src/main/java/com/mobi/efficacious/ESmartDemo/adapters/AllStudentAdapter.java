package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.ESmartDemo.Tab.Result_Tab;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.AllStudent;
import com.mobi.efficacious.ESmartDemo.fragment.StudentResultFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllStudentAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    ArrayList<AllStudent> menus = new ArrayList<AllStudent>();
    public ArrayList<AllStudent> categories;
    public ArrayList<AllStudent> orig;
    String pagename,role_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public AllStudentAdapter(Context context, ArrayList<AllStudent> Menus, String Pagename) {
        super();
        this.context = context;
        this.menus = Menus;
        this.pagename = Pagename;
    }

    static class ImageHolder {
        CircleImageView student_image;
        TextView id;
        TextView name;
        RelativeLayout linear;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<AllStudent> results = new ArrayList<AllStudent>();
                if (orig == null)
                    orig = menus;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final AllStudent g : orig) {
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
                menus = (ArrayList<AllStudent>) results.values;
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
        ImageHolder holder = null;
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.allstudent_row, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        holder.student_image=row.findViewById(R.id.student_image);
        holder.name = (TextView) row.findViewById(R.id.name_allstudent);
        holder.linear = (RelativeLayout) row.findViewById(R.id.linear_allstudent);
        // holder.id.setText(menus.get(position).getStud_num());
        holder.name.setText(menus.get(position).getName());

        holder.student_image.setVisibility(View.VISIBLE);
        String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+menus.get(position).getStudentProfile()+"";
        Picasso.with(context).load(url).resize(100,100)
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.student_image);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int st = menus.get(position).getStudent_id();
                if (pagename.contentEquals("Result")) {

                    if(role_id.contentEquals("3"))
                    {
                        Result_Tab studentResultFragment = new Result_Tab();
                        Bundle args = new Bundle();
                        args.putString("Div_name", menus.get(position).getDivision_name());
                        args.putString("Std_name", menus.get(position).getStandrad_name());
                        args.putInt("Div_id", menus.get(position).getDivision_id());
                        args.putInt("Std_id", menus.get(position).getStandard_id());
                        args.putString("Stud_name", menus.get(position).getName());
                        args.putInt("stud_id12", menus.get(position).getStudent_id());
                        studentResultFragment.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragment).commit();
                    }else {
                        StudentResultFragment studentResultFragment = new StudentResultFragment();
                        Bundle args = new Bundle();
                        args.putString("Div_name", menus.get(position).getDivision_name());
                        args.putString("Std_name", menus.get(position).getStandrad_name());
                        args.putInt("Div_id", menus.get(position).getDivision_id());
                        args.putInt("Std_id", menus.get(position).getStandard_id());
                        args.putString("Stud_name", menus.get(position).getName());
                        args.putInt("stud_id12", menus.get(position).getStudent_id());
                        studentResultFragment.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentResultFragment).commit();
                    }
                    } else {
                    Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                    Bundle args = new Bundle();
                    args.putString("Stud_name", menus.get(position).getName());
                    args.putInt("stud_id12", menus.get(position).getStudent_id());
                    args.putString("attendence", "student_attendence");
                    attendence_sliding_tab.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commit();

                }

            }
        });
        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slidedown);
        row.startAnimation(animFadein6);
        return row;
    }
}