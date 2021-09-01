package com.mobi.efficacious.ESmartDemo.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.NoticeBoardAdapter;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;

import java.util.ArrayList;
import java.util.HashMap;


public class NoticeboardActivity  extends AppCompatActivity {
    Toolbar toolbar;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Databasehelper mydb;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);


        mrecyclerView=(RecyclerView)findViewById(R.id.chat_recyclerview);
        mydb=new Databasehelper(NoticeboardActivity.this,"Notifications",null,1);

        dataList = new ArrayList<HashMap<Object, Object>>();
        MessageCenterAsync messageCenterAsync=new MessageCenterAsync();
        messageCenterAsync.execute();

//        mydb.query("Create table if not exists MessageCenter(ID INTEGER PRIMARY KEY AUTOINCREMENT,Message varchar)");

    }

    private class MessageCenterAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(NoticeboardActivity.this);
        @Override
        protected Void doInBackground(Void... params) {

            try
            {
                Cursor  cursor =mydb.querydata("Select Subject,Notice,IssueDate,LastDate from NoticeBoard ");
                int count=cursor.getCount();
                if(count==0)
                {
                    map = new HashMap<Object, Object>();
                    map.put("Subject","");
                    map.put("IssueDate","");
                    map.put("LastDate","");
                    map.put("Notice","No Data Available");
                    dataList.add(map);
                }


                cursor.moveToFirst();
                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {
                            map = new HashMap<Object, Object>();
                            map.put("Subject",cursor.getString(cursor.getColumnIndex("Subject")));
                            map.put("Notice",cursor.getString(cursor.getColumnIndex("Notice")));
                            map.put("IssueDate",cursor.getString(cursor.getColumnIndex("IssueDate")));
                            map.put("LastDate",cursor.getString(cursor.getColumnIndex("LastDate")));
                            dataList.add(map);
                        } while (cursor.moveToNext());

                    }

                }

            }
            catch (Exception e)
            {
                map = new HashMap<Object, Object>();
                map.put("Subject","");
                map.put("IssueDate","");
                map.put("LastDate","");
                map.put("Notice","No Data Available");
                dataList.add(map);

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(NoticeboardActivity.this));
            madapter=new NoticeBoardAdapter(dataList);
            mrecyclerView.setAdapter(madapter);
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}