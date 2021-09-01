package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Result_Tab;
import com.mobi.efficacious.ESmartDemo.adapters.Division_spinner_adapter;
import com.mobi.efficacious.ESmartDemo.adapters.spinner_std_name;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class Result_EntryFragment extends Fragment {
    View myview;
    Spinner spinnerexamname,spinnerSubject,nametv;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Standard_id,role_id,Year_id,Division_id,Student_id,Stud_name,Exam_selected_name,Exam_id,StandatdName,DivisionName;;
    String Subject_marks="",Selected_subjectName,Selected_subject_id;
    TextView standardname,divisionname;
    HashMap<Object, Object> map;
    spinner_std_name adapter1;
    Division_spinner_adapter adapter2;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList2;
    private ArrayList<HashMap<Object, Object>> dataList3;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    EditText edtMarks;
    Button btnsave;
    int resultStatus=0;
    String selected_studentName,Selected_StudentId,school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.enter_marks_result,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        dataList3 = new ArrayList<HashMap<Object, Object>>();
        spinnerexamname=(Spinner)myview.findViewById(R.id.spinnerexamname) ;
        spinnerSubject=(Spinner)myview.findViewById(R.id.spinnersubject) ;
        edtMarks= (EditText) myview.findViewById(R.id.edtMarks);
        btnsave= (Button) myview.findViewById(R.id.btnsave);
        standardname = (TextView) myview.findViewById(R.id.stdtv);
      //  STudentName = (TextView) myview.findViewById(R.id.nametv);
        nametv=(Spinner)myview.findViewById(R.id.nametv);
        divisionname = (TextView) myview.findViewById(R.id.divtv);

        DivisionName= Result_Tab.DivisionName;
        StandatdName=Result_Tab.StandatdName;
        Standard_id =Result_Tab.Standard_id;
        Division_id=Result_Tab.Division_id;
        //Student_id=Result_Tab.Student_id;
       // Stud_name=Result_Tab.Stud_name;
        standardname.setText(StandatdName);
        divisionname.setText(DivisionName);
        StudentNameAsync studentNameAsync=new StudentNameAsync();
        studentNameAsync.execute();
     //   STudentName.setText(Stud_name);
        nametv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_studentName= String.valueOf(dataList3.get(position).get("Name"));
                Selected_StudentId= String.valueOf(dataList3.get(position).get("Student_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_subjectName= String.valueOf(dataList2.get(position).get("SubjectName"));
                Selected_subject_id= String.valueOf(dataList2.get(position).get("intSubject_id"));
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Subject_marks.contentEquals("")&&!Exam_selected_name.contentEquals("--Select Exam--")&&!Selected_subject_id.contentEquals("--Select Subject--")&& !Selected_StudentId.contentEquals("--Select Name--"))
                {
                    SubmitASYNC resultEntryASync=new SubmitASYNC();
                    resultEntryASync.execute();
                }else
                {
                    if(TextUtils.isEmpty(Subject_marks)) {
                        edtMarks.setError("Enter Valid Marks ");
                    }
                    if(Exam_selected_name.contentEquals("--Select Exam--"))
                    {
                        setSpinnerError(spinnerexamname,"Select valid Exam ");

                    }
                    if(Selected_subject_id.contentEquals("--Select Subject--"))
                    {
                        setSpinnerError(spinnerSubject,"Select valid Subject ");

                    }

                    if(Selected_StudentId.contentEquals("--Select Name--"))
                    {
                        setSpinnerError(nametv,"Select Student ");

                    }
                }
            }
        });
        ExamAsync examAsync=new ExamAsync();
        examAsync.execute();
        ResultAsync resultAsync=new ResultAsync();
        resultAsync.execute();
        return myview;
    }
    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }
    private class ResultAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            dataList2.clear();

            map = new HashMap<Object, Object>();

            map.put("intSubject_id","--Select Subject--");
            map.put("SubjectName","--Select Subject--");
            dataList2.add(map);
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
                                map = new HashMap<Object, Object>();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map.put("intSubject_id", str2.getProperty("intSubject_id").toString().trim());
                                    map.put("SubjectName", str2.getProperty("vchSubjectName").toString().trim());
                                    dataList2.add(map);
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
            adapter2 = new Division_spinner_adapter(getActivity(), dataList2,"SubjectName");
            spinnerSubject.setAdapter(adapter2);
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
                request.addProperty("School_id",school_id);
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

    private class SubmitASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "ResultEntry";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "Insert");
                request.addProperty("intExam_id",Exam_id);
                request.addProperty("intSubject_id",Selected_subject_id);
                request.addProperty("decMark",Subject_marks);
                request.addProperty("intDivision_id",Division_id);
                request.addProperty("intSchool_id", school_id);
                request.addProperty("intStudent_id",Selected_StudentId);
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
                    result = response.getProperty(0).toString();
                    if(result.contentEquals("true"))
                    {
                        resultStatus=1;
                    }else
                    {
                        resultStatus=0;
                    }
                } else {
                }
            } catch (Exception e) {
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
                Toast.makeText(getActivity(), "Error While Uploading ", Toast.LENGTH_LONG).show();
                edtMarks.setText("");
            }else
            {
                Toast.makeText(getActivity(), "Result Uploaded Successfully ", Toast.LENGTH_LONG).show();
                edtMarks.setText("");
            }
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

    private class StudentNameAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            dataList3.clear();

            map = new HashMap<Object, Object>();

            map.put("Student_id","--Select Name--");
            map.put("Name","--Select Name--");
            dataList3.add(map);
            OPERATION_NAME = "Student";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("standard_id", Standard_id);
                request.addProperty("division_id",Division_id);
                request.addProperty("academic_id",Year_id);
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
                                map = new HashMap<Object, Object>();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    map.put("Student_id", str2.getProperty("Student_id").toString().trim());
                                    map.put("Name", str2.getProperty("Name").toString().trim());
                                    dataList3.add(map);

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
            adapter1 = new spinner_std_name(getActivity(), dataList3,"StudentName");
            nametv.setAdapter(adapter1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
