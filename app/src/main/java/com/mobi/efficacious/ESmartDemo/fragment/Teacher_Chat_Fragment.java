package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.ChatAllUser_Adapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.Chat;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Teacher_Chat_Fragment extends Fragment implements SearchView.OnQueryTextListener{
    View myview;
    ListView listView;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String  Academic_id,Schooli_id,role_id, Standard_id, Division_id, userid;
    ArrayList<Chat> allUser = new ArrayList<Chat>();
    SearchView searchView;
    ChatAllUser_Adapter adapter;
    ConnectionDetector cd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.chat_allusername_recylerview,null);
        listView = (ListView) myview.findViewById(R.id.chat_listview);
        cd = new ConnectionDetector(getActivity());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        searchView = (SearchView) myview.findViewById(R.id.search_view_member);
        searchView.setVisibility(View.GONE);
//        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Academic_id= settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        userid = settings.getString("TAG_USERID", "");
        if (role_id.contentEquals("2") || role_id.contentEquals("1")) {
            Standard_id = settings.getString("TAG_STANDERDID", "");
            Division_id = settings.getString("TAG_DIVISIONID", "");
            if (!cd.isConnectingToInternet())
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No InternetConnection");
                alert.setPositiveButton("OK",null);
                alert.show();

            }
            else {
                ChatTeacherAsync chatTeacherAsync = new ChatTeacherAsync();
                chatTeacherAsync.execute();
            }
        }else
        {
            if (!cd.isConnectingToInternet())
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No InternetConnection");
                alert.setPositiveButton("OK",null);
                alert.show();

            }
            else {
                ChatAllTeacherAsync chatAllTeacherAsync = new ChatAllTeacherAsync();
                chatAllTeacherAsync.execute();
            }
        }
        return myview;
    }
    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Teacher Name Here");
    }

    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText))
        {
            listView.clearTextFilter();
        }
        else
        {
            listView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    private class ChatAllTeacherAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {
            allUser.clear();
            OPERATION_NAME = "ChatUsersName";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                 if (role_id.contentEquals("3")) {
                    request.addProperty("command", "selectTeacher");
                    request.addProperty("intschool_id",Schooli_id);
                     request.addProperty("academic_id",Academic_id);
                }
                else {
                    request.addProperty("command", "selectAdmin");
                     request.addProperty("intschool_id",Schooli_id);
                     request.addProperty("academic_id",Academic_id);
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
                    if (response != null) {
                        result = response.getProperty(0).toString();
                        if (response != null) {
                            SoapObject str = null;
                            for (int i = 0; i < response.getPropertyCount(); i++)
                                str = (SoapObject) response.getProperty(i);

                            SoapObject str1 = (SoapObject) str.getProperty(0);

                            SoapObject str2 = null;

                            for (int j = 0; j < str1.getPropertyCount(); j++) {
                                str2 = (SoapObject) str1.getProperty(j);
                                String res = str2.toString();
                                Chat allusers = new Chat();
                                if (res.contains("No record found")) {

                                } else {
                                    if (isValidProperty(str2, "User_id")) {
                                        allusers.setUser_id(str2.getProperty("User_id").toString().trim());
                                    }

                                    if (isValidProperty(str2, "FCMToken")) {
                                        allusers.setFCMToken(str2.getProperty("FCMToken").toString().trim());
                                    }
                                    if (isValidProperty(str2, "intUserType_id")) {
                                        allusers.setUserType_id(str2.getProperty("intUserType_id").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Name")) {
                                        allusers.setUserName(str2.getProperty("Name").toString().trim());
                                    }
                                    if (!str2.getProperty("User_id").toString().trim().contentEquals(userid)) {
                                        allUser.add(allusers);
                                    }

                                }

                            }

                        }
                        adapter = new  ChatAllUser_Adapter(allUser,getActivity(),role_id);
                    } else {
                        Toast.makeText(getActivity(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try
            {
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);
                setupSearchView();

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
        }
    }
    private class ChatTeacherAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {
            allUser.clear();
            OPERATION_NAME = "ChatUsersName";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if (role_id.contentEquals("3")) {
//                    request.addProperty("command", "selectGroupTeacher");
//                    request.addProperty("intUserId", userid);
                } else if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                    request.addProperty("command", "selectTeacherStandardWise");
                    request.addProperty("standard_id", Standard_id);
                    request.addProperty("division_id", Division_id);
                    request.addProperty("intschool_id",Schooli_id);
                    request.addProperty("academic_id",Academic_id);
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
                    if (response != null) {
                        result = response.getProperty(0).toString();
                        if (response != null) {
                            SoapObject str = null;
                            for (int i = 0; i < response.getPropertyCount(); i++)
                                str = (SoapObject) response.getProperty(i);

                            SoapObject str1 = (SoapObject) str.getProperty(0);

                            SoapObject str2 = null;

                            for (int j = 0; j < str1.getPropertyCount(); j++) {
                                str2 = (SoapObject) str1.getProperty(j);
                                String res = str2.toString();
                                Chat allusers = new Chat();
                                if (res.contains("No record found")) {

                                } else {
                                    if (isValidProperty(str2, "User_id")) {
                                        allusers.setUser_id(str2.getProperty("User_id").toString().trim());
                                    }
                                    if (isValidProperty(str2, "vchFCMToken")) {
                                        allusers.setFCMToken(str2.getProperty("vchFCMToken").toString().trim());
                                    }
                                    if (isValidProperty(str2, "User_Name")) {
                                        allusers.setUserName(str2.getProperty("User_Name").toString().trim());
                                    }
                                    if (isValidProperty(str2, "UserTypeId")) {
                                        allusers.setUserType_id(str2.getProperty("UserTypeId").toString().trim());
                                    }

                                    allUser.add(allusers);


                                }

                            }
                        }
                            adapter = new  ChatAllUser_Adapter(allUser,getActivity(),role_id);

                    } else {
                        Toast.makeText(getActivity(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try
            {
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);
                setupSearchView();

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
        }
    }

    boolean isValidProperty(SoapObject soapObject, String PropertyName) {
        if (soapObject != null) {
            if (soapObject.getProperty(PropertyName) != null) {
                if (!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("") && !soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
                    return true;
                else
                    return false;
            }
            return false;
        } else
            return false;
    }

}
