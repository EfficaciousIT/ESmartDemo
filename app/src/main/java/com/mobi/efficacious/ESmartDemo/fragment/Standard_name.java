package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.StandardAdapter;
import com.mobi.efficacious.ESmartDemo.entity.Standard;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/22/2018.
 */

public class Standard_name extends Fragment {
    View myview;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Standard_id;
    Context mContext;
    ArrayAdapter adapter;
    ArrayList<Standard> standard=new ArrayList<Standard>();
    ListView listview;
    String value,academic_id,school_id;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.student_std_layout,null);
        mContext = getActivity();
//        if(Library_tab.pagename.contentEquals("Library"))
//        {
//            value = "Library";
//        }else
//        {
//            value = "timetable";
//        }
        value = "timetable";
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Standard_id = settings.getString("TAG_STANDERDID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        listview = (ListView) myview.findViewById(R.id.stad_standard);
          progressBar = (ProgressBar) myview.findViewById(R.id.standard_ProgressBar);
        LoginAsync login = new LoginAsync();
        login.execute();
        return myview;
    }

    private class LoginAsync extends AsyncTask<Void, Void, Void> {

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

                request.addProperty("command", "select");
                request.addProperty("intSchool_id",school_id);
                //request.addProperty("std_id", Standard_id);
//                request.addProperty("academic_id", academic_id);
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
                                Standard std =new Standard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intStandard_Id"))
                                    {
                                        std.setStandard_id(str2.getProperty("intStandard_Id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchStandard_name"))
                                    {
                                        std.setStandard_name(str2.getProperty("vchStandard_name").toString().trim());
                                    }

                                    standard.add(std);
                                }
                            }
                        }
                        adapter=new StandardAdapter(mContext,standard,value);
                        // adapter=new StandardAdapter(mContext,standard,value);
                    }
                    else
                    {
                        Toast.makeText(mContext, "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
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
            listview.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
