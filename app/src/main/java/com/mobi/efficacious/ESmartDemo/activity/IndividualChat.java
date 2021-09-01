package com.mobi.efficacious.ESmartDemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.FCMServices.FirebaseChatMainApp;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.MessageCenterAdapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class IndividualChat extends AppCompatActivity {
    Toolbar toolbar;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    public static int status = 0;
    Databasehelper mydb;
    public static RecyclerView mrecyclerView;
    public static RecyclerView.Adapter madapter;
    String ReceiverName, ReceiverId, ReceiverFCMToken, SenderName, ReceiverUserTypeId, role_id;
    ImageButton btnSend;
    EditText sendmessage;
    String message = "";
    String messagesend,Academic_id,Schooli_id,SenderId, SenderFCMToken, currentdate;
    String Standardid="", Division_id="",GroupName="",Teacher_id="";
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    Cursor cursor;

    ConnectionDetector cd;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_individual);

        cd = new ConnectionDetector(getApplicationContext());
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Academic_id= settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        SenderName = settings.getString("TAG_NAME", "");
        SenderId = settings.getString("TAG_USERID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        SenderFCMToken = settings.getString("TAG_USERFIREBASETOKEN", "");
        Intent intent = getIntent();
        try {
            ReceiverFCMToken = intent.getStringExtra("ReceiverFCMToken");
            if (ReceiverFCMToken.contentEquals("Group")) {
                Standardid = intent.getStringExtra("StandardId");
                Division_id = intent.getStringExtra("DivisionId");
                GroupName = intent.getStringExtra("GroupName");
                if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                    Teacher_id = intent.getStringExtra("Teacher_ID");
                }
                getSupportActionBar().setTitle(GroupName);

            } else {
                ReceiverName = intent.getStringExtra("ReceiverName");
                ReceiverId = intent.getStringExtra("ReceiverId");
                ReceiverUserTypeId = intent.getStringExtra("ReceiverUserTypeId");
                getSupportActionBar().setTitle(ReceiverName);
            }
        }catch (Exception ex)
        {

        }
//        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview));
//        scrollview.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        NumberFormat f = new DecimalFormat("00");
        currentdate = ((f.format(mDay)) + "/" + (f.format(mMonth + 1)) + "/" + mYear + "  " + date);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        sendmessage = (EditText) findViewById(R.id.editWriteMessage);
        mydb = new Databasehelper(getApplicationContext(), "Notifications", null, 1);

        dataList = new ArrayList<HashMap<Object, Object>>();
        mrecyclerView = (RecyclerView) findViewById(R.id.chat_recyclerview);
        if (!cd.isConnectingToInternet())
        {

            AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();

        }
        else {

            if (ReceiverFCMToken.contentEquals("Group")) {
                IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                individualChatAsync.execute();
            } else {
                IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                individualChatAsync.execute();
            }

        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = sendmessage.getText().toString();
                messagesend = message;
                if (message.contentEquals(" ") || message.contentEquals("")) {
                    Toast.makeText(IndividualChat.this, "Please Enter Message", Toast.LENGTH_SHORT).show();


                } else {
                    if (ReceiverFCMToken.contentEquals("Group")) {
                        String recievermessage = "";
                        mydb.query("Insert into EILGroupChat(SenderMessage,ReciverMessage,SenderName,RecieverName,GroupName,MessageDate)values('"+ message +"','" + recievermessage + "','"+SenderName+"','"+recievermessage+"','"+GroupName+"','" + currentdate + "')");
                        sendmessage.setText("");
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {

                            IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                            individualChatAsync.execute();
                        }
                        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
                        {
                            if (!cd.isConnectingToInternet())
                            {

                                AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK",null);
                                alert.show();

                            }
                            else {

                                GroupChatsendToTeacherAsync groupChatsendToTeacherAsync = new GroupChatsendToTeacherAsync();
                                groupChatsendToTeacherAsync.execute();
                            }
                        }
                        if(role_id.contentEquals("3")||role_id.contentEquals("1")||role_id.contentEquals("2"))
                        {
                            if (!cd.isConnectingToInternet())
                            {

                                AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK",null);
                                alert.show();

                            }
                            else {

                                GroupChatsendAsync groupChatsendAsync = new GroupChatsendAsync();
                                groupChatsendAsync.execute();
                            }
                        }


                    }
                    else
                    {
                        String recievermessage = "";
                        mydb.query("Insert into EILChat(SenderMessage,ReciverId,ReciverMessage,MessageDate,ReceiverUserTypeId)values('" + message + "','" + ReceiverId + "','" + recievermessage + "','" + currentdate + "','" + ReceiverUserTypeId + "')");
                        sendmessage.setText("");
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(IndividualChat.this);
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {

                            IndividualChatAsync individualChatAsync = new IndividualChatAsync();
                            individualChatAsync.execute();
                            IndividualChatsendAsync individualChatsendAsync = new IndividualChatsendAsync();
                            individualChatsendAsync.execute();
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    private class IndividualChatAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(IndividualChat.this);

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            try {

                if (ReceiverFCMToken.contentEquals("Group")) {
                    cursor = mydb.querydata("Select SenderMessage,ReciverMessage,MessageDate,RecieverName from EILGroupChat where GroupName='" + GroupName + "'");
                    int count = cursor.getCount();
                }else
                {
                    cursor = mydb.querydata("Select SenderMessage,ReciverMessage,MessageDate from EILChat where ReciverId='" + ReceiverId + "' and ReceiverUserTypeId='" + ReceiverUserTypeId + "'");
                    int count = cursor.getCount();
                }

//                if(count==0)
//                {
//                    map = new HashMap<Object, Object>();
//                    map.put("SenderMessage","send ");
//                    map.put("Message"," recieve");
//                    dataList.add(map);
//                }

                cursor.moveToFirst();
                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {
                            map = new HashMap<Object, Object>();
                            if (ReceiverFCMToken.contentEquals("Group"))
                            {
                                map.put("MessageDate", cursor.getString(cursor.getColumnIndex("MessageDate")));
                                if(!cursor.getString(cursor.getColumnIndex("SenderMessage")).contentEquals(""))
                                {
                                    map.put("SenderMessage", SenderName+"\n"+cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                }else {
                                    map.put("SenderMessage",cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                }
                                if(!cursor.getString(cursor.getColumnIndex("ReciverMessage")).contentEquals("")) {
                                    map.put("Message", cursor.getString(cursor.getColumnIndex("RecieverName"))+"\n"+cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                                }
                                else {
                                    map.put("Message", cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                                }

                            }else
                            {
                                map.put("MessageDate", cursor.getString(cursor.getColumnIndex("MessageDate")));
                                map.put("SenderMessage",cursor.getString(cursor.getColumnIndex("SenderMessage")));
                                map.put("Message", cursor.getString(cursor.getColumnIndex("ReciverMessage")));
                            }

                            dataList.add(map);
                        } while (cursor.moveToNext());

                    }

                }

            } catch (Exception e) {
//                map = new HashMap<Object, Object>();
//                map.put("SenderMessage"," ");
//                map.put("Message"," ");
//                dataList.add(map);

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                mrecyclerView.setHasFixedSize(true);
                mrecyclerView.setLayoutManager(new LinearLayoutManager(IndividualChat.this));
                madapter = new MessageCenterAdapter(dataList, "IndividualChat");
                mrecyclerView.setAdapter(madapter);
            }catch (Exception ex)
            {

            }

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

    private class IndividualChatsendAsync extends AsyncTask<Void, Void, Void> {
        //        private final ProgressDialog dialog = new ProgressDialog(IndividualChat.this);
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "ChatMessage";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("User_id", SenderId);
                request.addProperty("FCMToken", ReceiverFCMToken);
                request.addProperty("SenderFCMToken", SenderFCMToken.trim());
                request.addProperty("Message", messagesend.trim());
                request.addProperty("ReceiverName", ReceiverName);
                request.addProperty("SenderName", SenderName);
                request.addProperty("UserTypeId", role_id);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug = true;
                ht.call(SOAP_ACTION, envelope);
                if (!(envelope.bodyIn instanceof SoapFault)) {
                    if (envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject) envelope.bodyIn;
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setMessage("Processing...");
//            dialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }
    }
    private class GroupChatsendAsync extends AsyncTask<Void, Void, Void> {
        //        private final ProgressDialog dialog = new ProgressDialog(IndividualChat.this);
        @Override
        protected Void doInBackground(Void... params) {

            if(role_id.contentEquals("3"))
            {
                OPERATION_NAME = "ChatGroupMessageForTeacher";

            }else
            {
                OPERATION_NAME = "ChatGroupMessageForStudent";
            }
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("Standard_id", Standardid);
                request.addProperty("Division_id", Division_id);
                request.addProperty("Message", messagesend.trim());
                request.addProperty("SenderName",SenderName);
                request.addProperty("GRoupName",GroupName);
                request.addProperty("intSchool_id", Schooli_id);
                request.addProperty("Academic_id", Academic_id);

                if(role_id.contentEquals("1")||role_id.contentEquals("2"))
                {
                    request.addProperty("SenderId",SenderId);

                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug = true;
                ht.call(SOAP_ACTION, envelope);
                if (!(envelope.bodyIn instanceof SoapFault)) {
                    if (envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject) envelope.bodyIn;
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            this.dialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setMessage("Processing...");
//            dialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }
    }
    private class GroupChatsendToTeacherAsync extends AsyncTask<Void, Void, Void> {
        //        private final ProgressDialog dialog = new ProgressDialog(IndividualChat.this);
        @Override
        protected Void doInBackground(Void... params) {


                OPERATION_NAME = "ChatGroupMessageFromStudentToTeacher";

            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("Standard_id", Standardid);
                request.addProperty("Division_id", Division_id);
                request.addProperty("Message", messagesend.trim());
                request.addProperty("SenderName",SenderName);
                request.addProperty("GRoupName",GroupName);
                request.addProperty("TeacherId",Teacher_id);
                request.addProperty("intSchool_id", Schooli_id);
                request.addProperty("Academic_id", Academic_id);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug = true;
                ht.call(SOAP_ACTION, envelope);
                if (!(envelope.bodyIn instanceof SoapFault)) {
                    if (envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject) envelope.bodyIn;
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setMessage("Processing...");
//            dialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
//        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
