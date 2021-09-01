package com.mobi.efficacious.ESmartDemo.FCMServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.activity.IndividualChat;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.activity.MessageCenterActivity;
import com.mobi.efficacious.ESmartDemo.activity.NoticeboardActivity;
import com.mobi.efficacious.ESmartDemo.activity.Notifiacton;
import com.mobi.efficacious.ESmartDemo.adapters.MessageCenterAdapter;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by haripal on 7/25/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    int status = 0;
    Databasehelper mydb;
    MediaPlayer mediaPlayer;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    String subject,issuedate,lastdate,RecieverFCMTOken,ReceivrName,Receiver_userid,sendername,senderFCMToken,MessageDate,message1,UserType_id,GroupName;
    String Standard_id,Division_id,title;
    Cursor cursor;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        mydb=new Databasehelper(getApplicationContext(),"Notifications",null,1);
        Log.e("dataChat", remoteMessage.getData().toString());
        {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON_OBJECT", object.toString());

            title = params.get("title");
            String message = params.get("message");
            MessageDate=params.get("time");
            if(title.contentEquals("Notice Board"))
            {
                subject=params.get("subject");
                issuedate=params.get("issueDate");
                lastdate=params.get("todate");
            }else if(title.contentEquals("Group"))
            {
                dataList = new ArrayList<HashMap<Object, Object>>();
                message1 = params.get("message");
                sendername=params.get("Sendername");
                GroupName=params.get("GroupName");
                Standard_id=params.get("Standard_id");
                Division_id=params.get("Division_id");
                if (!FirebaseChatMainApp.isChatActivityOpen()) {
                    sendNotification(title,
                            message);
                } else {

                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                String SenderMessage="";
                                mydb.query("Insert into EILGroupChat(SenderMessage,ReciverMessage,SenderName,RecieverName,GroupName,MessageDate)values('"+ SenderMessage +"','" + message1 + "','"+SenderMessage+"','"+sendername+"','"+GroupName+"','" + MessageDate + "')");

                                IndividualChatAsync individualChatAsync=new IndividualChatAsync();
                                individualChatAsync.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }).start();


                }
            }
            else if(title.contentEquals("IndividualChat"))
            {
                dataList = new ArrayList<HashMap<Object, Object>>();
                message1 = params.get("message");
                RecieverFCMTOken=params.get("RecieverFCMTOken");
                ReceivrName=params.get("ReceivrName");
                Receiver_userid=params.get("userid");
                sendername=params.get("Sendername");
                senderFCMToken=params.get("senderFCMToken");
                UserType_id=params.get("UserTypeid");
                if (!FirebaseChatMainApp.isChatActivityOpen()) {
                    sendNotification(title,
                            message);
                } else {

                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                String SenderMessage="";
                                mydb.query("Insert into EILChat(ReciverMessage,ReciverId,SenderMessage,MessageDate,ReceiverUserTypeId)values('"+message1+"','"+Receiver_userid+"','"+SenderMessage+"','"+MessageDate+"','"+UserType_id+"')");
                                IndividualChatAsync individualChatAsync=new IndividualChatAsync();
                                individualChatAsync.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }).start();


                }
//
            }

            if(!title.contentEquals("IndividualChat") && !title.contentEquals("Group"))
            {
                sendNotification(title,
                        message);
            }

        }



    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message){
//                                  String receiver,
//                                  String receiverUid,
//                                  String firebaseToken) {
        Class c=MainActivity.class;
        String SenderMessage="";
        switch (title)
        {
            case "Group":c=IndividualChat.class;
                mydb.query("Insert into EILGroupChat(SenderMessage,ReciverMessage,SenderName,RecieverName,GroupName,MessageDate)values('"+ SenderMessage +"','" + message + "','"+SenderMessage+"','"+sendername+"','"+GroupName+"','" + MessageDate + "')");
                break;
            case "Message Center":c=MessageCenterActivity.class;
                mydb.query("Insert into MessageCenter(Message,MessageDate)values('"+message+"','"+MessageDate+"')");
                break;
            case "Attendance":c=MessageCenterActivity.class;
                mydb.query("Insert into MessageCenter(Message,MessageDate)values('"+message+"','"+MessageDate+"')");
                break;
            case "Notice Board":c= NoticeboardActivity.class;
                mydb.query("Insert into NoticeBoard(Subject,Notice,IssueDate,LastDate)values('"+subject+"','"+message+"','"+issuedate+"','"+lastdate+"')");
                break;

            case "Gallery":c= Notifiacton.class;
                break;

            case "HomeWork":c= Notifiacton.class;
                break;
            case "DailyDiary":c= Notifiacton.class;
                break;
            case "Event":c= Notifiacton.class;
                break;
            case "LeaveApply":c= Notifiacton.class;
                break;
            case "Leave Approval":c= Notifiacton.class;
                break;
//            case "NoticeBoard":c= NoticeBoard_tab.class;
//                break;
            case "IndividualChat":c= IndividualChat.class;
                try
                {
                    mydb.query("Insert into EILChat(ReciverMessage,ReciverId,SenderMessage,MessageDate,ReceiverUserTypeId)values('"+message+"','"+Receiver_userid+"','"+SenderMessage+"','"+MessageDate+"','"+UserType_id+"')");
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
        }


        Intent intent = new Intent(this,c);
        if(title.contentEquals("IndividualChat"))
        {
            intent.putExtra("ReceiverName", sendername);
            intent.putExtra("ReceiverId", Receiver_userid);
            intent.putExtra("ReceiverFCMToken", senderFCMToken);
            intent.putExtra("ReceiverUserTypeId",UserType_id);
            title=sendername;
        }
        if(title.contentEquals("Group"))
        {
            intent.putExtra("StandardId",Standard_id);
            intent.putExtra("DivisionId",Division_id);
            intent.putExtra("GroupName",GroupName);
            intent.putExtra("ReceiverFCMToken","Group");

            title=GroupName;
        }
        if(title.contentEquals("Gallery"))
        {
            intent.putExtra("pagename","Gallery");
        }
        if(title.contentEquals("Event"))
        {
            intent.putExtra("pagename","Event");
        }
        if(title.contentEquals("LeaveApply"))
        {
            intent.putExtra("pagename","LeaveApply");
        }
        if(title.contentEquals("Leave Approval"))
        {
            intent.putExtra("pagename","Leave Approval");
        }
        if(title.contentEquals("HomeWork"))
        {
            intent.putExtra("pagename","HomeWork");
            //intent.putExtra("value","HomeWork");
        }
        if(title.contentEquals("DailyDiary"))
        {
            intent.putExtra("pagename","DailyDiary");
            //  intent.putExtra("value","DailyDiary");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


    }

    private class IndividualChatAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.message);
            mediaPlayer.start();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            try {
                if (title.contentEquals("Group")) {
                    cursor = mydb.querydata("Select SenderName,SenderMessage,ReciverMessage,MessageDate,RecieverName from EILGroupChat where GroupName='" + GroupName + "'");
                    int count = cursor.getCount();
                }else
                {
                    cursor = mydb.querydata("Select SenderMessage,ReciverMessage,MessageDate from EILChat where ReciverId='" + Receiver_userid + "' and ReceiverUserTypeId='" + UserType_id + "'");
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
                            if (title.contentEquals("Group"))
                            {
                                map.put("MessageDate", cursor.getString(cursor.getColumnIndex("MessageDate")));
                                if(!cursor.getString(cursor.getColumnIndex("SenderMessage")).contentEquals(""))
                                {
                                    map.put("SenderMessage", cursor.getString(cursor.getColumnIndex("SenderName"))+"\n"+cursor.getString(cursor.getColumnIndex("SenderMessage")));
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
                                map.put("SenderMessage", cursor.getString(cursor.getColumnIndex("SenderMessage")));
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

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            IndividualChat.mrecyclerView.setHasFixedSize(true);
            IndividualChat.madapter=new MessageCenterAdapter(dataList,"IndividualChat");
            IndividualChat.mrecyclerView.setAdapter(IndividualChat.madapter);
            IndividualChat.mrecyclerView.smoothScrollToPosition(dataList.size()-1);
//            mediaPlayer.stop();
//            mediaPlayer.release();
        }
    }
}