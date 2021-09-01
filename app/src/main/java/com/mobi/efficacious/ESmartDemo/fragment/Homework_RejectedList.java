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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.DailyDiary_Tab;
import com.mobi.efficacious.ESmartDemo.adapters.DailyDiaryAdapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.DailyDiary;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class Homework_RejectedList extends Fragment {
    View myview;
    ListView DairyListview;
    ArrayAdapter adapter;
    ArrayList<DailyDiary> diary=new ArrayList<DailyDiary>();
    ConnectionDetector cd;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String userid,role_id,value,school_id,intDivision_id,usertype;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fragment_leavelist,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        DairyListview=(ListView)myview.findViewById(R.id.leavelist_list);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        role_id = settings.getString("TAG_USERTYPEID", "");
        if(role_id.contentEquals("5")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                userid = settings.getString("TAG_USERID", "");
                value= DailyDiary_Tab.value;
                usertype="Admin";
                DailyDiaryAsync adm = new DailyDiaryAsync(usertype);
                adm.execute();
            }
        }


        return myview ;
    }
    private class DailyDiaryAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String Usertype;
        public DailyDiaryAsync(String usertype) {
            Usertype=usertype;
        }


        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "DailyDairyAdmin");
                request.addProperty("vchType",value);
                request.addProperty("intSchool_id",school_id);

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
                                DailyDiary Diary =new DailyDiary();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "vchFileName"))
                                    {
                                        Diary.setVchFileName(str2.getProperty("vchFileName").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchFilePath"))
                                    {
                                        Diary.setVchFilePath(str2.getProperty("vchFilePath").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchFilePath2"))
                                    {
                                        Diary.setVchFilePath2(str2.getProperty("vchFilePath2").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchFilePath3"))
                                    {
                                        Diary.setVchFilePath3(str2.getProperty("vchFilePath3").toString().trim());
                                    }
                                    if(isValidProperty(str2, "dtDatetime"))
                                    {
                                        Diary.setDtDatetime(str2.getProperty("dtDatetime").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchComment"))
                                    {
                                        Diary.setVchComment(str2.getProperty("vchComment").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchDivisionName"))
                                    {
                                        Diary.setVchDivision(str2.getProperty("vchDivisionName").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchStandard_name"))
                                    {
                                        Diary.setVchStandard(str2.getProperty("vchStandard_name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        Diary.setVchSubject(str2.getProperty("vchSubjectName").toString().trim());
                                    }
                                    if(role_id.contentEquals("5"))
                                    {
                                        if(isValidProperty(str2, "name"))
                                        {
                                            Diary.setTeacherName(str2.getProperty("name").toString().trim());
                                        }
                                        if(isValidProperty(str2, "intstandard_id"))
                                        {
                                            Diary.setIntstandard_id(str2.getProperty("intstandard_id").toString().trim());
                                        }
                                        if(isValidProperty(str2, "intDivision_id"))
                                        {
                                            Diary.setIntDivision_id(str2.getProperty("intDivision_id").toString().trim());
                                        }
                                        if(value.contentEquals("HomeWork"))
                                        {
                                            Diary.setIntApproval(str2.getProperty("int_Approval").toString().trim());

                                            Diary.setIntMy_id(str2.getProperty("intMy_id").toString().trim());

                                            Diary.setTeacherProfileImage(str2.getProperty("vchProfile").toString().trim());
                                        }
                                    }
                                    else
                                    {
                                        if(value.contentEquals("HomeWork"))
                                        {
                                            Diary.setIntApproval(str2.getProperty("int_Approval").toString().trim());

                                        }
                                    }
                                    if(role_id.contentEquals("5")) {
                                        if(value.contentEquals("HomeWork") && str2.getProperty("int_Approval").toString().trim().contentEquals("2"))
                                        {
                                            diary.add(Diary);
                                        }else
                                        {

                                        }

                                    }
                                    else
                                    {
                                        diary.add(Diary);
                                    }
                                }

                            }

                        }

                        adapter = new DailyDiaryAdapter(getActivity(),diary,value,Usertype);

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
            DairyListview.setAdapter(adapter);
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


    boolean isValidProperty(SoapObject soapObject, String PropertyName)
    {
        if(soapObject!=null)
        {
            if(soapObject.getProperty(PropertyName) != null)
            {
                if(!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("")&&!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
                    return true;
                else
                    return false;
            }
            return false;
        }
        else
            return false;
    }
}
