package com.mobi.efficacious.ESmartDemo.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.activity.IndividualChat;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.Chat;
import com.mobi.efficacious.ESmartDemo.fragment.Chat_StandardWise_Student_Fragment;
import com.mobi.efficacious.ESmartDemo.fragment.Student_Chat_Fragment;

import java.util.ArrayList;


public class ChatAllUser_Adapter extends BaseAdapter implements Filterable {
    ArrayList<Chat> userList = new ArrayList<Chat>();
    public ArrayList<Chat> orig;
    Context context;
    String role_id;
    ConnectionDetector cd;
  public  static String StandradId,DivisionId;
    public ChatAllUser_Adapter(ArrayList<Chat> UserList, Context context, String Role_id) {
        this.userList = UserList;
        this.context = context;
this.role_id=Role_id;
    }
    static class ImageHolder
    {
        TextView UserName;
        TextView UserProfile;
        LinearLayout UserLayout;


    }
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final  ArrayList<Chat> results = new  ArrayList<Chat>();
                if (orig == null)
                    orig = userList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Chat g : orig) {
                            if (g.getUserName().toLowerCase()
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
                userList = (ArrayList<Chat>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        cd = new ConnectionDetector(context);
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chat_adapter, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        holder.UserName = (TextView) row.findViewById(R.id.text_view_username);
        holder.UserProfile = (TextView) row.findViewById(R.id.text_view_user_alphabet);
        holder.UserLayout = (LinearLayout) row.findViewById(R.id.relativeLayout2);
        String Name = (userList.get(position).getUserName());
        String alphabet = Name.substring(0, 1);
        holder.UserProfile.setText(alphabet);
        holder.UserName.setText(userList.get(position).getUserName());
        holder.UserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userList.get(position).getFCMToken().contentEquals("Group")) {

                    if (!cd.isConnectingToInternet())
                    {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK",null);
                        alert.show();

                    }
                    else {

                        Intent intent = new Intent(context, IndividualChat.class);
                        intent.putExtra("StandardId", userList.get(position).getStandardId());
                        intent.putExtra("DivisionId", userList.get(position).getDivisionId());
                        intent.putExtra("GroupName", userList.get(position).getUserName());
                        intent.putExtra("ReceiverFCMToken", userList.get(position).getFCMToken());
                        if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                            intent.putExtra("Teacher_ID", userList.get(position).getTeacher_id());
                        }
                        context.startActivity(intent);
                    }
                }else if(userList.get(position).getFCMToken().contentEquals("StandardWiseName"))
                {
                    if (!cd.isConnectingToInternet())
                    {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("No InternetConnection");
                        alert.setPositiveButton("OK",null);
                        alert.show();

                    }
                    else {

                        StandradId = userList.get(position).getStandardId();
                        DivisionId = userList.get(position).getDivisionId();
                        Student_Chat_Fragment.fulllayout.setVisibility(View.GONE);
                        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.contain_main, new Chat_StandardWise_Student_Fragment());
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
//                    mfragment= context.getSupportFragmentManager();
//                    mfragment.beginTransaction().replace(R.id.contain_main, new CreateGroupFragment()).commit();
                }
                else {
                    if(userList.get(position).getUserName().contentEquals("No Data Available"))
                    {
                        Toast.makeText(context,"No Data Available",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setMessage("No InternetConnection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {

                            Intent intent = new Intent(context, IndividualChat.class);
                            intent.putExtra("ReceiverName", userList.get(position).getUserName());
                            intent.putExtra("ReceiverId", userList.get(position).getUser_id());
                            intent.putExtra("ReceiverFCMToken", userList.get(position).getFCMToken());
                            intent.putExtra("ReceiverUserTypeId", userList.get(position).getUserType_id());
                            context.startActivity(intent);
                        }
                    }

                }

            }
        });
        return row;
    }
}