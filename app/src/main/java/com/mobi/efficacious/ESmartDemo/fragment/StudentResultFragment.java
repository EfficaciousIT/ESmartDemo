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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Result_Tab;
import com.mobi.efficacious.ESmartDemo.adapters.SubjectResult;
import com.mobi.efficacious.ESmartDemo.adapters.spinner_std_name;
import com.mobi.efficacious.ESmartDemo.entity.Subject;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentResultFragment extends Fragment {
    View myview;
    Toolbar toolbar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id,role_id,Year_id,Division_id,Student_id,Stud_name,Exam_selected_name,Exam_id,StandatdName,DivisionName;;
int std_,div_;
String Subject_id,Subject_name,Subject_marks;
    TextView standardname,divisionname;
    ArrayAdapter adapter;
    ArrayList<Subject> subjects=new ArrayList<Subject>();
    ListView listview;
    String value;
    HashMap<Object, Object> map;
    spinner_std_name adapter1;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList3;
Spinner spinnerexamname,STudentName;
int resultStatus=1,total_marks=0;
    int marks;
    String selected_studentName,school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.student_result_fragment,null);
        value = "StudentResult";
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        dataList = new ArrayList<HashMap<Object, Object>>();
        spinnerexamname=(Spinner)myview.findViewById(R.id.spinnerexamname) ;
        listview = (ListView) myview.findViewById(R.id.resultListview);
        standardname = (TextView) myview.findViewById(R.id.stdtv);
        STudentName = (Spinner) myview.findViewById(R.id.nametv);
        divisionname = (TextView) myview.findViewById(R.id.divtv);
        dataList3 = new ArrayList<HashMap<Object, Object>>();
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            DivisionName=settings.getString("TAG_DivisionName", "");
            StandatdName=settings.getString("TAG_StandardName", "");
            Standard_id = settings.getString("TAG_STANDERDID", "");
            Division_id=settings.getString("TAG_DIVISIONID", "");
            Student_id=settings.getString("TAG_USERID", "");
            Stud_name=settings.getString("TAG_NAME", "");
            standardname.setText(StandatdName);
            divisionname.setText(DivisionName);
        }else if(role_id.contentEquals("3"))
        {
            DivisionName= Result_Tab.DivisionName;
            StandatdName=Result_Tab.StandatdName;
            Standard_id =Result_Tab.Standard_id;
            Division_id=Result_Tab.Division_id;
            standardname.setText(StandatdName);
            divisionname.setText(DivisionName);

        }
        else
        {
            DivisionName=getArguments().getString("Div_name");
            StandatdName=getArguments().getString("Std_name");
            std_=getArguments().getInt("Std_id",0);
            Standard_id =String.valueOf(std_);
            div_=getArguments().getInt("Div_id",0);
            Division_id=String.valueOf(div_);

            standardname.setText(StandatdName);
            divisionname.setText(DivisionName);

        }
        STudentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_studentName= String.valueOf(dataList3.get(position).get("Name"));
                Student_id= String.valueOf(dataList3.get(position).get("Student_id"));

                ExamAsync examAsync=new ExamAsync();
                examAsync.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerexamname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Exam_selected_name= String.valueOf(dataList.get(position).get("Exam_Name"));
                Exam_id= String.valueOf(dataList.get(position).get("Exam_id"));
                if(Exam_id.contentEquals("--Select Exam--")||Student_id.contentEquals("--Select Name--"))
                {
                    Toast.makeText(getActivity(),"Please Select Proper Data",Toast.LENGTH_SHORT).show();
                }else
                {
                    ResultDeclareAsync resultDeclareAsync=new ResultDeclareAsync();
                    resultDeclareAsync.execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StudentNameAsync studentNameAsync=new StudentNameAsync();
        studentNameAsync.execute();
        return myview;
    }

    private class ExamAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            dataList.clear();

            map = new HashMap<Object, Object>();

            map.put("Exam_id","--Select Exam--");
            map.put("Exam_Name","--Select Exam--");
            dataList.add(map);
            OPERATION_NAME = "SchoolName";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectExam");
                request.addProperty("School_id", school_id);
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
                                map = new HashMap<Object, Object>();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map.put("Exam_id", str2.getProperty("intExam_id").toString().trim());
                                    map.put("Exam_Name", str2.getProperty("vchExamination_name").toString().trim());
                                    dataList.add(map);
                                }

                            }

                        }


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
            adapter1 = new spinner_std_name(getActivity(), dataList,"ResultDate");
            spinnerexamname.setAdapter(adapter1);
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }
    }

    private class ResultAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            subjects.clear();
            OPERATION_NAME = "Student";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectSubject");
                request.addProperty("standard_id",Standard_id);
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
                                Subject sub =new Subject();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intSubject_id"))
                                    {
                                        sub.setSubject_id(str2.getProperty("intSubject_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        sub.setSubject_name(str2.getProperty("vchSubjectName").toString().trim());
                                    }

                                    subjects.add(sub);
                                }
                            }
                        }
                        
                            adapter=new SubjectResult(getContext(),subjects,value);


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
            this.dialog.dismiss();
            listview.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }
    }
    private class ResultDeclareAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            resultStatus=1;
            subjects.clear();
            OPERATION_NAME = "ResultDeclaration";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "ResultDeclaration");
                request.addProperty("intDivision_id",Division_id);
                request.addProperty("intExam_id",Exam_id);
                request.addProperty("intStudent_id",Student_id);
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
                                Subject sub =new Subject();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "decMark"))
                                    {
                                        sub.setSubject_Marks(str2.getProperty("decMark").toString().trim());
                                         marks=Integer.parseInt(str2.getProperty("decMark").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchSubjectName"))
                                    {
                                        sub.setSubject_name(str2.getProperty("vchSubjectName").toString().trim());
                                    }
                                    total_marks=(total_marks+marks);
                                    subjects.add(sub);
                                }
                            }

                        }




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
                resultStatus=0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if(resultStatus==0)
            {
                ResultAsync resultAsync=new ResultAsync();
                resultAsync.execute();
            }else
            {
                Subject sub =new Subject();
                sub.setSubject_Marks(String.valueOf(total_marks));
                sub.setSubject_name("Total  ");
                subjects.add(sub);
                adapter=new SubjectResult(getContext(),subjects,"Result");
                listview.setAdapter(adapter);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading Result...");
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
    private class StudentNameAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            dataList3.clear();

            if(role_id.contentEquals("1")||role_id.contentEquals("2"))
            {
                map = new HashMap<Object, Object>();
                map.put("Student_id", Student_id);
                map.put("Name", Stud_name);
                dataList3.add(map);
            }else {
                map = new HashMap<Object, Object>();
                map.put("Student_id", "--Select Name--");
                map.put("Name", "--Select Name--");
                dataList3.add(map);
                OPERATION_NAME = "Student";
                SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
                final Responce responce = new Responce();
                SoapObject response = null;
                String result = null;
                try {
                    SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                    request.addProperty("command", "select");
                    request.addProperty("standard_id", Standard_id);
                    request.addProperty("division_id", Division_id);
                    request.addProperty("academic_id", Year_id);
                    request.addProperty("intSchool_id",school_id);
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
                                    map = new HashMap<Object, Object>();
                                    if (res.contains("No record found")) {

                                    } else {
                                        map.put("Student_id", str2.getProperty("Student_id").toString().trim());
                                        map.put("Name", str2.getProperty("Name").toString().trim());
                                        dataList3.add(map);

                                    }

                                }

                            }

                        } else {
                            Toast.makeText(getActivity(), "Null Response", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter1 = new spinner_std_name(getActivity(), dataList3,"StudentName");
            STudentName.setAdapter(adapter1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
