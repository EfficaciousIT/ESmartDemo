package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.NoticeBoard;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 9/2/2017.
 */

public class AdminNoticeboardAdapter extends ArrayAdapter<NoticeBoard> {

    private final Context context;
    private final ArrayList<NoticeBoard> itemsArrayList;

    public AdminNoticeboardAdapter(Context context, ArrayList<NoticeBoard> itemsArrayList) {
        super(context, R.layout.noticeboard_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.noticeboard_row, parent, false);

        TextView title=(TextView)rowView.findViewById(R.id.title_noticeboard);
        TextView fromdate = (TextView)rowView.findViewById(R.id.fromdate_noticeboard);
        TextView todate = (TextView)rowView.findViewById(R.id.todate_noticeboard);
        TextView message = (TextView)rowView.findViewById(R.id.message_noticeboard);

        title.setText("Subject: "+ itemsArrayList.get(position).getSubject());
        fromdate.setText("Issue Date: "+ itemsArrayList.get(position).getIssueDate());
        todate.setText("End Date: "+ itemsArrayList.get(position).getEndDate());
        message.setText("Notice: "+itemsArrayList.get(position).getNotice());

        return rowView;
    }
}