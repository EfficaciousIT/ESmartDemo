package com.mobi.efficacious.ESmartDemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;

import java.util.ArrayList;
import java.util.HashMap;


public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.ViewHolder> {
    private ArrayList<HashMap<Object, Object>> data;
    HashMap<Object, Object> result2 = new HashMap<Object, Object>();
    HashMap<Object, Object> result;
    String Pagename;
    public static int posi;
    public MessageCenterAdapter(ArrayList<HashMap<Object, Object>> dataList, String pagename) {
        data = dataList;
        Pagename=pagename;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.messagecenter_message_received,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final  int pos=position;
        result = new HashMap<Object, Object>();
        result = data.get(position);
        result2 = result;
        if(!result.get("Message").toString().contentEquals(""))
        {
            holder.leftdate.setVisibility(View.VISIBLE);
            holder.message.setText(result.get("Message").toString());
            holder.leftdate.setText(result.get("MessageDate").toString());
        }else
        {
            holder.leftdate.setVisibility(View.GONE);
            holder.message.setVisibility(View.GONE);
        }

        if(Pagename.contentEquals("IndividualChat"))
        {
            if(!result.get("SenderMessage").toString().contentEquals("")) {
                holder.reciverlayout.setVisibility(View.VISIBLE);
                holder.rightdate.setVisibility(View.VISIBLE);
                holder.rightdate.setText(result.get("MessageDate").toString());
                holder.Sendmessage.setText(result.get("SenderMessage").toString());
            }else
            {
                holder.reciverlayout.setVisibility(View.GONE);
            }
        }else
        {
            holder.reciverlayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView Sendmessage;
        TextView leftdate,rightdate;
        LinearLayout reciverlayout;
        public ViewHolder(View itemView) {
            super(itemView);
            Sendmessage=(TextView)itemView.findViewById(R.id.item_message_body_text_view1);
            message=(TextView)itemView.findViewById(R.id.item_message_body_text_view);
            reciverlayout=(LinearLayout)itemView.findViewById(R.id.item_message_parent1);
            leftdate=(TextView)itemView.findViewById(R.id.dateleft);
            rightdate=(TextView)itemView.findViewById(R.id.dateright);
    }
    }
}
