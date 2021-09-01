package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_student;
import com.mobi.efficacious.ESmartDemo.adapters.StudentTimetableAdapter;
import com.mobi.efficacious.ESmartDemo.entity.StudentTimetable;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ThursdayFragment extends Fragment {
    String Std_id,school_id;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<StudentTimetable> student =new ArrayList<StudentTimetable>();
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;

    public ThursdayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.thursdayProgressBar);
        listView = (ListView) getActivity().findViewById(R.id.thursday_list);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
      //  Std_id = settings.getString("TAG_STANDERDID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        Std_id= TimetableActivity_student.stdid;
//        Div_id=TimetableActivity_student.divid;
        if(adapter==null)
        {

        }else
        {
            adapter.notifyDataSetChanged();
        }
        thrAsync syn = new thrAsync();
        syn.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thursday, container, false);
    }

    private class thrAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "StudentTimeTable";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("std_id", Std_id);
                request.addProperty("day", "Thursday");
                request.addProperty("intSchool_id",school_id);
//                request.addProperty("division_id", Div_id);
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
                                StudentTimetable stud =new StudentTimetable();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "vchDay"))
                                    {
                                        stud.setDay(str2.getProperty("vchDay").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchLecture_name"))
                                    {
                                        stud.setLectureName(str2.getProperty("vchLecture_name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        stud.setSubjectName(str2.getProperty("vchSubjectName").toString().trim());
                                    }

                                    if(isValidProperty(str2, "FrmTm"))
                                    {
                                        stud.setFromTime(str2.getProperty("FrmTm").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Time"))
                                    {
                                        stud.setTime(str2.getProperty("Time").toString().trim());
                                    }

                                    if(isValidProperty(str2, "btrecess"))
                                    {
                                        stud.setRecess(str2.getProperty("btrecess").toString().trim());
                                    }

                                    if(isValidProperty(str2, "T_Name"))
                                    {
                                        stud.setTeacherName(str2.getProperty("T_Name").toString().trim());
                                    }

                                    student.add(stud);
                                }

                            }

                        }

                        adapter = new StudentTimetableAdapter(getActivity(), student);

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