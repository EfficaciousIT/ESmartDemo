package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Student_Exam_Timetable;
import com.mobi.efficacious.ESmartDemo.entity.StudentExamTimetable;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class StudentExamFragment extends Fragment {
View myview;
    Toolbar toolbar;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<StudentExamTimetable> StudentExam=new ArrayList<StudentExamTimetable>();
    Context mContext;
    String Std_id,school_id;
    String stand_id,academic_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.activity_studentexam,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Std_id = settings.getString("TAG_STANDERDID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        mContext = getActivity();
        stand_id=getArguments().getString("std_id");
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        progressBar = (ProgressBar) myview.findViewById(R.id.examProgressBar);
        listView = (ListView) myview.findViewById(R.id.exam_list);
        ExamAsync exam = new ExamAsync();
        exam.execute();
        return myview;
    }



    private class ExamAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "StudentExamTimetable";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("std_id", stand_id);
                request.addProperty("academic_id", academic_id);
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
                                StudentExamTimetable studExam =new StudentExamTimetable();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    if(isValidProperty(str2, "ExamTypeName"))
                                    {
                                        studExam.setExamName(str2.getProperty("ExamTypeName").toString().trim());
                                    }
                                    if(isValidProperty(str2, "dtExamination_date"))
                                    {
                                        studExam.setExamination_date(str2.getProperty("dtExamination_date").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        studExam.setSubjectName(str2.getProperty("vchSubjectName").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtExamination_Time"))
                                    {
                                        studExam.setExamination_Time(str2.getProperty("dtExamination_Time").toString().trim());
                                    }




                                    StudentExam.add(studExam);
                                }

                            }

                        }

                       adapter = new Student_Exam_Timetable(getActivity(), StudentExam);

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
