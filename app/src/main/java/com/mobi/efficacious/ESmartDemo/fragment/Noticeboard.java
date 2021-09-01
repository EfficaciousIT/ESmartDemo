package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.NoticeBoardAdapter;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.entity.NoticeBoard;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;


public class Noticeboard extends Fragment {
  View myview;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Databasehelper mydb;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.notification_activity_layout,null);
        mrecyclerView=(RecyclerView)myview.findViewById(R.id.chat_recyclerview);
        mydb=new Databasehelper(getActivity(),"Notifications",null,1);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        dataList = new ArrayList<HashMap<Object, Object>>();
        MessageCenterAsync messageCenterAsync=new MessageCenterAsync();
        messageCenterAsync.execute();
        NoticeboardAsync noticeboardAsync=new NoticeboardAsync();
        noticeboardAsync.execute();
        return myview;
    }


    private class MessageCenterAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
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
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    private class NoticeboardAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "AdminNoticeboard";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "NoticeBoard");
                request.addProperty("school_id", school_id);
                //request.addProperty("std_id", Standard_id);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug=true;
                ht.call(SOAP_ACTION, envelope);
                if(!(envelope.bodyIn instanceof SoapFault))
                {
                    if(envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject)envelope.bodyIn;
                    if(response != null)
                    {
                        result=response.getProperty(0).toString();
                        if(response!=null)
                        {
                            SoapObject str = null;
                            for(int i=0;i<response.getPropertyCount();i++)
                                str=(SoapObject) response.getProperty(i);

                            SoapObject str1 = (SoapObject) str.getProperty(0);

                            SoapObject str2 = null;

                            for(int j=0;j<str1.getPropertyCount();j++)
                            {
                                str2 = (SoapObject) str1.getProperty(j);
                                String res = str2.toString();
                                NoticeBoard not =new NoticeBoard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map = new HashMap<Object, Object>();
                                    map.put("Subject",str2.getProperty("Subject").toString().trim());
                                    map.put("Notice",str2.getProperty("Notice").toString().trim());
                                    map.put("IssueDate",str2.getProperty("Issue_Date").toString().trim());
                                    map.put("LastDate",str2.getProperty("End_Date").toString().trim());
                                    dataList.add(map);


                                }

                            }

                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        }
    }

}