package com.mobi.efficacious.ESmartDemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;

import java.util.ArrayList;
import java.util.HashMap;


public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder> {
    private ArrayList<HashMap<Object, Object>> data;
    HashMap<Object, Object> result2 = new HashMap<Object, Object>();
    HashMap<Object, Object> result;
    public NoticeBoardAdapter(ArrayList<HashMap<Object, Object>> dataList) {
        data = dataList;
    }

    @Override
    public NoticeBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.noticeboard_row,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }



    public void onBindViewHolder(final ViewHolder holder, int position) {
        final  int pos=position;
        result = new HashMap<Object, Object>();
        result = data.get(position);
        result2 = result;
        holder.Issuedate.setText(result.get("IssueDate").toString());
        holder.Lastdate.setText(result.get("LastDate").toString());
        holder.Notice.setText("Notice:"+result.get("Notice").toString());
        holder.Subject.setText("Subject:"+result.get("Subject").toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Issuedate,Lastdate,Notice,Subject;
        public ViewHolder(View itemView) {
            super(itemView);
            Issuedate=(TextView)itemView.findViewById(R.id.fromdate_noticeboard);
            Lastdate=(TextView)itemView.findViewById(R.id.todate_noticeboard);
            Notice=(TextView)itemView.findViewById(R.id.message_noticeboard);
            Subject=(TextView)itemView.findViewById(R.id.title_noticeboard);
        }
    }
}
