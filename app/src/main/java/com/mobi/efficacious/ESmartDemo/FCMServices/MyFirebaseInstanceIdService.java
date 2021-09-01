package com.mobi.efficacious.ESmartDemo.FCMServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by haripal on 7/25/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id,command,User_id,school_id;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            role_id = settings.getString("TAG_USERTYPEID", "");
            school_id=settings.getString("TAG_SCHOOL_ID", "");
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            if(role_id.contentEquals(""))
            {

            }else
            {
                sendTokenToServer(refreshedToken);
            }

        }
    }

    public void sendTokenToServer(final String strToken) {
        // API call to send token to Server
//        settings.edit().putString("TAG_USERFIREBASETOKEN",strToken).clear();
      String  email = settings.getString("TAG_USEREMAILID", "");
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            User_id= settings.getString("TAG_STUDENTID", "");
        }else {
            User_id = settings.getString("TAG_USERID", "");
        }
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        switch (Integer.parseInt(role_id))
        {
            case 1:command="FcmTokenStudent";
                break;
            case  2:command="FcmTokenStudent";
                break;
            case 3:command="FcmTokenTeacher";
                break;
            case 4:command="FcmTokenStaff";
                break;
            case 5:command="FcmTokenAdmin";
                break;

        }

        settings.edit().putString("TAG_USERFIREBASETOKEN",strToken).apply();
        FCMTOKENASYNC fcmtokenasync=new FCMTOKENASYNC(command,strToken,email);
        fcmtokenasync.execute();
    }
    private class FCMTOKENASYNC extends AsyncTask<Void, Void, Void> {
        String Command;
        String firebasetoken,Email;
        public FCMTOKENASYNC(String command,String Firebasetoken,String EMail) {
            Command=command;
            firebasetoken=Firebasetoken;
            Email=EMail;
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "FCMTokenUpdate";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command",Command);
                request.addProperty("vchFCMToken",firebasetoken);
                request.addProperty("intUser_id",User_id);
                request.addProperty("vchEmail",Email);
                request.addProperty("intSchool_id",school_id);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
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
    }
}