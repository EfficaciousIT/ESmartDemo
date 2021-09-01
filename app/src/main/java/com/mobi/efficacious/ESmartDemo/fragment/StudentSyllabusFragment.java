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
import com.mobi.efficacious.ESmartDemo.adapters.Syllabus_adapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.Syllabus;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class StudentSyllabusFragment extends Fragment {
    View myview;
    ListView DairyListview;
    ArrayAdapter adapter;
    ArrayList<Syllabus> diary=new ArrayList<Syllabus>();
    ConnectionDetector cd;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String userid,role_id,value,intStandard_id,intDivision_id,school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fragment_leavelist,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        DairyListview=(ListView)myview.findViewById(R.id.leavelist_list);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        role_id = settings.getString("TAG_USERTYPEID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        if(role_id.contentEquals("2")||role_id.contentEquals("1")) {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                intStandard_id = settings.getString("TAG_STANDERDID", "");
                intDivision_id = settings.getString("TAG_DIVISIONID", "");
                value= "Syllabus";
                DailyDiaryAsync adm = new DailyDiaryAsync();
                adm.execute();
            }
        }else
        {
            if (!cd.isConnectingToInternet()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {
                value= "Syllabus";
                intStandard_id =getArguments().getString("std_id");
                DailyDiaryAsync adm = new DailyDiaryAsync();
                adm.execute();
            }

        }
        return myview ;
    }
    private class DailyDiaryAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());


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
                    request.addProperty("command", "SyllabuuDetail");
                    request.addProperty("intStandard_id",intStandard_id);
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
                                Syllabus syllabus =new Syllabus();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "vchSyllabusNm"))
                                    {
                                        syllabus.setSyllabusDetail(str2.getProperty("vchSyllabusNm").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Name"))
                                    {
                                        syllabus.setFileName(str2.getProperty("Name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "FilePath"))
                                    {
                                        syllabus.setFilePath(str2.getProperty("FilePath").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchFilePath2"))
                                    {
                                        syllabus.setFilePath2(str2.getProperty("vchFilePath2").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchFilePath3"))
                                    {
                                        syllabus.setFilePath3(str2.getProperty("vchFilePath3").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchDivisionName"))
                                    {
                                        syllabus.setDivisionName(str2.getProperty("vchDivisionName").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchStandard_name"))
                                    {
                                        syllabus.setStandardName(str2.getProperty("vchStandard_name").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        syllabus.setSubjectName(str2.getProperty("vchSubjectName").toString().trim());
                                    }

                                    diary.add(syllabus);
                                }

                            }

                        }

                        adapter = new Syllabus_adapter(getActivity(),diary,value);

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