package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Syllabus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Syllabus_adapter extends ArrayAdapter<Syllabus> {

    private final Context context;
    private final ArrayList<Syllabus> itemsArrayList;
    String Pagename,url;
    public Syllabus_adapter(Context context, ArrayList<Syllabus> itemsArrayList,String pagename) {
        super(context, R.layout.syllabus_detail_list, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.Pagename=pagename;

    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.syllabus_detail_list, parent, false);
        TextView standard = (TextView)rowView.findViewById(R.id.StdName);
        TextView Subject=(TextView)rowView.findViewById(R.id.subname);
        TextView comment = (TextView)rowView.findViewById(R.id.subdetail);
        CircleImageView dowload_img = (CircleImageView)rowView.findViewById(R.id.downloadimg);
        CircleImageView dowload_img2 = (CircleImageView)rowView.findViewById(R.id.downloadimg2);
        CircleImageView dowload_img3 = (CircleImageView)rowView.findViewById(R.id.downloadimg3);


        standard.setText(itemsArrayList.get(position).getStandardName()+" / "+itemsArrayList.get(position).getDivisionName());
        Subject.setText(itemsArrayList.get(position).getSubjectName());
        if(itemsArrayList.get(position).getFilePath().contentEquals("0"))
        {
            dowload_img.setVisibility(View.GONE);

        }else
        {
            dowload_img.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img);
        }
        if(itemsArrayList.get(position).getFilePath2().contentEquals("0"))
        {
            dowload_img2.setVisibility(View.GONE);

        }else
        {
            dowload_img2.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath2()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img2);
            //  Attachmntfile2.setText(itemsArrayList.get(position).getVchFilePath2());
        }
        if(itemsArrayList.get(position).getFilePath3().contentEquals("0"))
        {
            dowload_img3.setVisibility(View.GONE);
        }else
        {
            dowload_img3.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath3()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img3);
            //Attachmntfile3.setText(itemsArrayList.get(position).getVchFilePath3());
        }
        comment.setText(itemsArrayList.get(position).getSyllabusDetail());

        dowload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        dowload_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath2()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        dowload_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getFilePath3()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        return rowView;
    }
}