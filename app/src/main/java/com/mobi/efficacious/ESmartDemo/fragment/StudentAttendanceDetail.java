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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.ESmartDemo.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.ESmartDemo.adapters.StudentAttendanceAdapter;
import com.mobi.efficacious.ESmartDemo.entity.StudentAttendance;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class StudentAttendanceDetail extends Fragment {
    View myview;

    ListView listView;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id;
    Context mContext;
    ArrayAdapter adapter;
    String stud_id,academic_id;
    ArrayList<StudentAttendance> studentattendance=new ArrayList<StudentAttendance>();
    String school_id,stud_name,standard,role_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_studentattendancedetail,null);
        mContext =getContext();
        listView  = (ListView) myview.findViewById(R.id.studentattendancedetail_list);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        if(role_id.contentEquals("1")|| role_id.contentEquals("2"))
        {
            stud_id= settings.getString("TAG_USERID", "");
            stud_name= settings.getString("TAG_NAME", "");
            standard=settings.getString("TAG_NAME2", "");
        }
        else
        {
            stud_id= Attendence_sliding_tab.stud_id;
            stud_name=Attendence_sliding_tab.stud_name;
            standard= StudentAttendanceActivity.stname;
        }
      //  stud_id= Attendence_sliding_tab.stud_id;
        LoginAsync login = new LoginAsync();
        login.execute();
        return myview;
    }

    private class LoginAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "StudentAttendanceSummery";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("student_id", stud_id);
                request.addProperty("academic_id",academic_id);
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
                                StudentAttendance stud =new StudentAttendance();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intStudent_id"))
                                    {
                                        stud.setStudentId(str2.getProperty("intStudent_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtdate"))
                                    {
                                        stud.setDate(str2.getProperty("dtdate").toString().trim());
                                    }

                                    if(isValidProperty(str2, "status"))
                                    {
                                        stud.setStatus(str2.getProperty("status").toString().trim());
                                    }

                                    studentattendance.add(stud);
                                }

                            }

                        }

                        adapter = new StudentAttendanceAdapter(getActivity(), studentattendance);

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
            listView.setAdapter(adapter);
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

