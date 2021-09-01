package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.DatePickerDialog;
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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Division_spinner_adapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class NoticeBoard_application extends Fragment {
    View myview;
Spinner Usertype,Standard;
EditText IssueDate,EndDate,NoticeSubject,Notice;
    private Calendar calendar;
    private int year, month, day;
    String fromdate,Usertype_selected,Standard_selected;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList2;
    Button saveBtn;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    ConnectionDetector cd;
    HashMap<Object, Object> map;
    Division_spinner_adapter adapter;
    int insertflag;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String Issue_Date,End_Date,Notice_Subject,Notice_Detail,role_id,userid,school_id;
    RelativeLayout std_relativee;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.notice_entry_layout,null);
        Usertype=(Spinner)myview.findViewById(R.id.spinner_usertype);
        Standard=(Spinner)myview.findViewById(R.id.spinner_standard);
        IssueDate=(EditText)myview.findViewById(R.id.notice_issueDate);
        EndDate=(EditText)myview.findViewById(R.id.notice_end_date);
        NoticeSubject=(EditText)myview.findViewById(R.id.subject_notice);
        Notice=(EditText)myview.findViewById(R.id.notice_et);
        saveBtn=(Button)myview.findViewById(R.id.btnSubmit) ;
        std_relativee=(RelativeLayout)myview.findViewById(R.id.std_relativee);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        userid = settings.getString("TAG_USERID", "");
        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        cd = new ConnectionDetector(getContext().getApplicationContext());

    saveBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Issue_Date=IssueDate.getText().toString();
        End_Date=EndDate.getText().toString();
        Notice_Detail=Notice.getText().toString();
        Notice_Subject=NoticeSubject.getText().toString();
        if(!Issue_Date.contentEquals("")&&!End_Date.contentEquals("")&&!Notice_Subject.contentEquals("")&&!Notice_Detail.contentEquals("")&&!Usertype_selected.contentEquals("-- Select UserType --"))
        {
            if(Usertype_selected.contentEquals("1")) {
                if(!Standard_selected.contentEquals("-- Select Standard--"))
                {
                    SubmitASYNC submitASYNC = new SubmitASYNC();
                    submitASYNC.execute();
                }else
                {
                    setSpinnerError(Standard,"Select Standard");
                    //Toast.makeText(getActivity(),"Please Fill Proper Data", Toast.LENGTH_LONG).show();
                }

            }else if(Usertype_selected.contentEquals("0"))
            {
                Standard_selected="0";
                SubmitAllASYNC submitASYNC = new SubmitAllASYNC();
                submitASYNC.execute();
            }
            else
            {
                Standard_selected="0";
                SubmitASYNC submitASYNC = new SubmitASYNC();
                submitASYNC.execute();
            }
        }else
        {
            if(TextUtils.isEmpty(Issue_Date)) {
                IssueDate.setError("Enter Valid Issue Date");
            }
            if(TextUtils.isEmpty(End_Date)) {
                EndDate.setError("Enter Valid End Date");
            }
            if(TextUtils.isEmpty(Notice_Detail)) {
                Notice.setError("Enter Notice");
            }
            if(TextUtils.isEmpty(Notice_Subject)) {
                NoticeSubject.setError("Enter Notice Subject");
            }
            if(Usertype_selected.contentEquals("-- Select UserType --"))
            {
                setSpinnerError(Usertype,"Select Usertype");
            }
        }
    }
    });
        IssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate1(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                fromdate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                Date date1 = null;
                                try {
                                    date1 = sdf.parse(((f.format(monthOfYear +1))+"-"+dayOfMonth+"-"+year ));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date date2 = null;
                                try {
                                    date2 = sdf.parse(fromdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                IssueDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate1(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                fromdate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                Date date1 = null;
                                try {
                                    date1 = sdf.parse(((f.format(monthOfYear +1))+"-"+dayOfMonth+"-"+year ));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date date2 = null;
                                try {
                                    date2 = sdf.parse(fromdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                EndDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        Usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Usertype_selected= String.valueOf(dataList.get(i).get("UserType_id"));
                Issue_Date=IssueDate.getText().toString();
                End_Date=EndDate.getText().toString();
                Notice_Detail=Notice.getText().toString();
                Notice_Subject=NoticeSubject.getText().toString();
                if(Usertype_selected.contentEquals("-- Select UserType --"))
                {
                    //Toast.makeText(getActivity(),"NO Data available for this Month", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                        if(Usertype_selected.contentEquals("1"))
                        {
                            std_relativee.setVisibility(View.VISIBLE);
                            StandardAsync standardAsync=new StandardAsync();
                            standardAsync.execute();
                        }else
                        {
                            std_relativee.setVisibility(View.GONE);

                        }

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Standard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Standard_selected= String.valueOf(dataList2.get(i).get("intStandard_Id"));
                if(Standard_selected.contentEquals("-- Select Standard--"))
                {
                   Toast.makeText(getActivity(),"Select Standard", Toast.LENGTH_LONG).show();
                }else
                {
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        UserTypeAsync userTypeAsync=new UserTypeAsync();
        userTypeAsync.execute();
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
    private class UserTypeAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            dataList.clear();

            try
            {
                map = new HashMap<Object, Object>();
                map.put("UserType","-- Select UserType --");
                map.put("UserType_id","-- Select UserType --");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType","All");
                map.put("UserType_id","0");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType","Student");
                map.put("UserType_id","1");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType","Teacher");
                map.put("UserType_id","3");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType","Staff");
                map.put("UserType_id","4");
                dataList.add(map);
                map = new HashMap<Object, Object>();
                map.put("UserType","Admin");
                map.put("UserType_id","5");
                dataList.add(map);

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
            adapter = new Division_spinner_adapter(getActivity(), dataList,"NoticeUserType");
            Usertype.setAdapter(adapter);
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
    private class StandardAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {

            dataList2.clear();
            map = new HashMap<Object, Object>();
            map.put("intStandard_Id","-- Select Standard--");
            map.put("vchStandard_name","-- Select Standard--");
            dataList2.add(map);
            map = new HashMap<Object, Object>();
            map.put("intStandard_Id","0");
            map.put("vchStandard_name","All");
            dataList2.add(map);
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
                                    map.put("intStandard_Id",str2.getProperty("intStandard_Id").toString().trim());
                                    map.put("vchStandard_name",str2.getProperty("vchStandard_name").toString().trim());
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
            adapter = new Division_spinner_adapter(getActivity(), dataList2,"NoticeStandard");
            Standard.setAdapter(adapter);
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
    private class SubmitASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());



        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "NoticeBoard";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "Insert");
                request.addProperty("intschool_id","1");
                request.addProperty("intUserType_id",Usertype_selected);
                request.addProperty("intStandard_id",Standard_selected);
                request.addProperty("intDepartment_id","0");
                request.addProperty("intTeacher_id","0");
                request.addProperty("dtIssue_date",Issue_Date);
                request.addProperty("dtEnd_date",End_Date);
                request.addProperty("vchSubject",Notice_Subject);
                request.addProperty("vchNotice",Notice_Detail);
                request.addProperty("intInserted_by",userid);
               // request.addProperty("InsertIP",);
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
                    if(result.contentEquals("false"))
                    {
                        insertflag=0;
                    }else {
                        insertflag=1;
                    }
                } else {
                }
            } catch (Exception e) {
                insertflag=0;
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if (insertflag==1)  {
                Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();

            } else {
               // Toast.makeText(getActivity(), " UnSucessfull", Toast.LENGTH_LONG).show();

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
    private class SubmitAllASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());



        protected Void doInBackground(Void... params) {
            for(int i=1;i<6;i++)
            {
                String UserAll=String.valueOf(i);
                if(UserAll.contentEquals("2"))
                {

                }else {
                    OPERATION_NAME = "NoticeBoard";
                    SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
                    final Responce responce = new Responce();
                    SoapObject response = null;
                    String result = null;
                    try {
                        SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                        request.addProperty("command", "Insert");
                        request.addProperty("intschool_id", "1");
                        request.addProperty("intUserType_id", UserAll);
                        request.addProperty("intStandard_id", Standard_selected);
                        request.addProperty("intDepartment_id", "0");
                        request.addProperty("intTeacher_id", "0");
                        request.addProperty("dtIssue_date", Issue_Date);
                        request.addProperty("dtEnd_date", End_Date);
                        request.addProperty("vchSubject", Notice_Subject);
                        request.addProperty("vchNotice", Notice_Detail);
                        request.addProperty("intInserted_by", userid);
                        // request.addProperty("InsertIP",);
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
                            result = response.getProperty(0).toString();
                            if (result.contentEquals("false")) {
                                insertflag = 0;
                            } else {
                                insertflag = 1;
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        insertflag = 0;
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if (insertflag==1)  {
                Toast.makeText(getActivity(), "Notice Created Successfully", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(getActivity(), " UnSucessfull", Toast.LENGTH_LONG).show();

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
}
