package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.LeaveApproval;

import java.util.ArrayList;

public class LeaveApplyAdapter extends ArrayAdapter<LeaveApproval> {

    private final Context context;
    private final ArrayList<LeaveApproval> itemsArrayList;
    String LeaveApplication_id,ApprovalStatus;
    public LeaveApplyAdapter(Context context, ArrayList<LeaveApproval> itemsArrayList) {
        super(context, R.layout.leaveapply_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.leaveapply_row, parent, false);
        RelativeLayout Rel = (RelativeLayout)rowView.findViewById(R.id.Name_relLeave);
        TextView fromDate = (TextView)rowView.findViewById(R.id.fromdate_Leave);
        TextView toDate=(TextView)rowView.findViewById(R.id.ToDate_Leave);
        TextView Days=(TextView)rowView.findViewById(R.id.Days_Leave);
        TextView Reason = (TextView)rowView.findViewById(R.id.Reason_Leave);
        TextView Approval = (TextView)rowView.findViewById(R.id.Approval_Leave);
        TextView Name = (TextView)rowView.findViewById(R.id.Name_Leave);
        LinearLayout attachmntrelative  = (LinearLayout)rowView.findViewById(R.id.attachmntrelative);
        attachmntrelative.setVisibility(View.GONE);
        ApprovalStatus=itemsArrayList.get(position).getAdminApproval();
        if(ApprovalStatus.contentEquals("0"))
        {
            Approval.setText("Pending");
            Approval.setTextColor(ContextCompat.getColor(context, R.color.lightorange));
        }else if(ApprovalStatus.contentEquals("1"))
        {
            Approval.setText("Approved");
            Approval.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else
        {
            Approval.setText("Rejected");
            Approval.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        LeaveApplication_id = itemsArrayList.get(position).getFrom_date();
        fromDate.setText("From Date: "+itemsArrayList.get(position).getFrom_date());
        toDate.setText("To Date: "+itemsArrayList.get(position).getTo_Date());
        Days.setText("Days: "+itemsArrayList.get(position).getTotalDays());
        Reason.setText("Reason: "+itemsArrayList.get(position).getReason());
        // Approval.setText(itemsArrayList.get(position).getAdminApproval());
        if(itemsArrayList.get(position).getName()=="NA")
        {
            Rel.setVisibility(View.GONE);
        }
        else
        {
            Name.setText("Name "+itemsArrayList.get(position).getAdminApproval());
        }

        return rowView;
    }
}
