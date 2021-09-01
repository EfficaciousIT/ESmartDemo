package com.mobi.efficacious.ESmartDemo.recycleractivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.entity.Teacher_attendence;
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
import java.util.Locale;


/**
 * Created by EFF-4 on 3/16/2018.
 */

public class Teacher_Mark_Fragment extends Fragment {
    View myview;
    Databasehelper mydb;
    Cursor cursor;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    ArrayList<Teacher_attendence> teacherList=new ArrayList<Teacher_attendence>();
    Button button_dateSelection,UpdateBtn;
    TextView tv_dateSelection;
    String Selected_Date,academic_id,currentdate,school_id;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static  ProgressBar progressbar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;;
String status,FCMToken,intTeacherid;
    boolean attendence_status_present,attendence_status_absent;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myview=inflater.inflate(R.layout.activity_main_recyclerview,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        currentdate = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault()).format(new Date());
        mrecyclerView=(RecyclerView)myview.findViewById(R.id.teacher_recyclerview);
        progressbar=(ProgressBar)myview.findViewById(R.id.progressbar);
        button_dateSelection=(Button)myview.findViewById(R.id.button_dateSelection);
        tv_dateSelection=(TextView) myview.findViewById(R.id.tv_dateSelection);
        mydb=new Databasehelper(getActivity(),"Teacher_record",null,1);
        teacherList=new ArrayList<Teacher_attendence>();
        UpdateBtn=(Button)myview.findViewById(R.id.UpdateBtn) ;
        button_dateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBtn.setVisibility(View.VISIBLE);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                Selected_Date=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
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
                                    date2 = sdf.parse(currentdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (date1.compareTo(date2) > 0) {
//                                    Toast.makeText(getActivity(),"Please select proper Date \n Selected date must be less than or equal to current Date",Toast.LENGTH_LONG).show();
                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                                    LoginAsync login = new LoginAsync();
                                    login.execute();
                                } else if (date1.compareTo(date2) < 0) {

                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                                    LoginAsync login = new LoginAsync();
                                    login.execute();
                                } else if (date1.compareTo(date2) == 0) {
                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                                    LoginAsync login = new LoginAsync();
                                    login.execute();
                                } else {

                                }

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        UpdateBtn.setVisibility(View.GONE);
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attendence_mark attendence_mark=new Attendence_mark();
                attendence_mark.execute();
            }
        });

        return myview;
    }

    private class LoginAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            teacherList.clear();
            OPERATION_NAME = "GetMarkAttendence";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "BackDateTeacher");
                request.addProperty("intschool_id", school_id);
                request.addProperty("intUserType_id","3");
                request.addProperty("dtDate",Selected_Date);
                request.addProperty("intAcademic_id", academic_id);
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
                                Teacher_attendence allteach =new Teacher_attendence();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    if(isValidProperty(str2, "User_id"))
                                    {
                                        allteach.setTeacherId(Integer.parseInt(str2.getProperty("User_id").toString().trim()));

                                    }

                                    if(isValidProperty(str2, "Attstatus"))
                                    {
                                        if(str2.getProperty("Attstatus").toString().trim().contentEquals("Present"))
                                        {
                                            allteach.setP_selected(true);
                                        }
                                        else if(str2.getProperty("Attstatus").toString().trim().contentEquals("Absent"))
                                        {
                                            allteach.setSelected(true);
                                        }
                                        else
                                        {
                                            allteach.setP_selected(false);
                                            allteach.setSelected(false);
                                        }


                                    }

                                    if(isValidProperty(str2, "FCMToken"))
                                    {
                                        allteach.setFCMToken(str2.getProperty("FCMToken").toString().trim());

                                    }

                                    if(isValidProperty(str2, "name"))
                                    {
                                        allteach.setTeacherName(str2.getProperty("name").toString().trim());

                                    }
                                    teacherList.add(allteach);

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
            progressbar.setVisibility(View.GONE);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            madapter=new Teacher_Mark_Adapter(teacherList,Selected_Date,academic_id,getContext());
            mrecyclerView.setAdapter(madapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }
    }
    private class Attendence_mark extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        //
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Teacher_Mark_activity.progressbar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Teacher_Mark_activity.progressbar.setVisibility(View.GONE);
            this.dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<Teacher_attendence> tchrList=((Teacher_Mark_Adapter)madapter).getTchrList();
            for(int i=0;i<tchrList.size();i++) {

                Teacher_attendence singleteacher = tchrList.get(i);
                intTeacherid = String.valueOf(singleteacher.getTeacherId());
                FCMToken = singleteacher.getFCMToken();
                attendence_status_present = singleteacher.isP_selected();
                attendence_status_absent = singleteacher.isSelected();
                if (attendence_status_present == true && attendence_status_absent == false) {
                    status = "Present";
                } else if (attendence_status_present == false && attendence_status_absent == true) {
                    status = "Absent";

                } else {
                    status = "";
                }
                if (!status.contentEquals("")) {
                    OPERATION_NAME = "MarkAttendence";
                    SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
                    final Responce responce = new Responce();
                    SoapObject response = null;
                    String result = null;
                    try {
                        SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                        request.addProperty("command", "InsertTeacher");
                        request.addProperty("intUserType_id", "3");
                        request.addProperty("intUser_id", intTeacherid);
                        request.addProperty("dtDate", Selected_Date);
                        request.addProperty("intschool_id", school_id);
                        request.addProperty("intAcademic_id", academic_id);
                        request.addProperty("status",status);
                        request.addProperty("FCMToken",FCMToken);
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
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
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
