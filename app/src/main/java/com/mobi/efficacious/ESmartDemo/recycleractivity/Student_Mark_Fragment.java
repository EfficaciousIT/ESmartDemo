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
import com.mobi.efficacious.ESmartDemo.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.entity.AllStudent;
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
 * Created by EFF-4 on 3/19/2018.
 */

public class Student_Mark_Fragment extends Fragment implements
        View.OnClickListener  {
    View myview;
    Databasehelper mydb;
    Cursor cursor;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    ArrayList<AllStudent> studentList=new ArrayList<AllStudent>();
    int stdid;
    SharedPreferences settings;
    String role_id,std_div;
    String gender_selected="",gender;
    String Standard_id,stdname,stddivision,academic_id,FCMToken;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    public static ProgressBar progressbar;
    String attendence_status,Selected_Date1;
    Button button_dateSelection;
    TextView tv_dateSelection;
    String Selected_Date="";
    String currentdate,school_id;
Button UpdateBtn;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private ProgressDialog progress;
    boolean attendence_status_present,attendence_status_absent;
    String status;

    int intStudent_id,intStandard_id,intDivision_id,TotalStudent=0,Absent_Count=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_main_recyclerview,null);
        mrecyclerView=(RecyclerView)myview.findViewById(R.id.teacher_recyclerview);
        progressbar=(ProgressBar)myview.findViewById(R.id.progressbar);
        button_dateSelection=(Button)myview.findViewById(R.id.button_dateSelection);
        tv_dateSelection=(TextView) myview.findViewById(R.id.tv_dateSelection);
        UpdateBtn=(Button)myview.findViewById(R.id.UpdateBtn) ;
        currentdate = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault()).format(new Date());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");
        Standard_id= StudentAttendanceActivity.stdno;
        stdname= StudentAttendanceActivity.stname;
      stddivision=StudentAttendanceActivity.stddiv;
        stdid= Integer.parseInt(StudentAttendanceActivity.stdno);
        mydb=new Databasehelper(getActivity(),"Teacher_record",null,1);
        gender_selected="Female";
        studentList=new ArrayList<AllStudent>();
//        LoginAsync login = new LoginAsync();
//        login.execute();
        UpdateBtn.setVisibility(View.GONE);
        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attendence_mark attendence_mark=new Attendence_mark();
                attendence_mark.execute();
            }
        });

        button_dateSelection.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(View v) {
        UpdateBtn.setVisibility(View.VISIBLE);
        if (v == button_dateSelection) {

            // Get Current Date
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
                            Selected_Date1=((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year );
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
//                               Toast.makeText(getActivity(),"Please select proper Date \n Selected date must be less than or equal to current Date",Toast.LENGTH_LONG).show();
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

    }


    private class Attendence_mark extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        //
//        int jumpTime = 0;
//        final int totalProgressTime = 100;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Student_Mark_activity.progressbar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();

//            progress=new ProgressDialog(getContext());
//            progress.setMessage("Updating  Attendence");
//            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Student_Mark_activity.progressbar.setVisibility(View.GONE);
            this.dialog.dismiss();

            Toast.makeText(getActivity(),"Attendence Mark Successfully",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<AllStudent> studentlist=((Student_mark_Adapter)madapter).getStudentList();
            for(int i=0;i<studentlist.size();i++) {

                AllStudent singleteacher = studentlist.get(i);
                intStudent_id = singleteacher.getStudent_id();
                intStandard_id = singleteacher.getStandard_id();
                intDivision_id = singleteacher.getDivision_id();
                FCMToken = singleteacher.getFCMToken();
                attendence_status_present = singleteacher.isP_selected();
                attendence_status_absent = singleteacher.isSelected();
                if (attendence_status_present == true && attendence_status_absent == false) {
                    status = "Present";
                } else if (attendence_status_present == false && attendence_status_absent == true) {
                    status = "Absent";

                } else {
                    status="";
                }
                if(!status.contentEquals("")) {
            OPERATION_NAME = "MarkAttendence";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "Insert");
                request.addProperty("intUserType_id", "1");
                request.addProperty("intUser_id",intStudent_id);
                request.addProperty("dtDate",Selected_Date);
                request.addProperty("intschool_id", school_id);
                request.addProperty("intstanderd_id", intStandard_id);
                request.addProperty("intdivision_id",stddivision);
                request.addProperty("intAcademic_id", academic_id);
                request.addProperty("status",status);
                request.addProperty("FCMToken",FCMToken);
                request.addProperty("dtDateSelected",Selected_Date1);
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
                } else {
                }
//                jumpTime += 5;
//                progress.setMax(jumpTime);
//                progress.setProgress(jumpTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

                }
            }
            return null;
        }
    }
    private class LoginAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


                    OPERATION_NAME = "GetMarkAttendence";
                    SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
                    final Responce responce = new Responce();
                    SoapObject response = null;
                    String result = null;
                    try {
                        SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                        request.addProperty("command", "BackDate");
                        request.addProperty("intschool_id", school_id);
                        request.addProperty("intUserType_id", "1");
                        request.addProperty("dtDate", Selected_Date);
                        request.addProperty("intstanderd_id", Standard_id);
                        request.addProperty("intdivision_id", stddivision);
                        request.addProperty("intAcademic_id", academic_id);
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
                                        AllStudent allstud = new AllStudent();
                                        if (res.contains("No record found")) {

                                        } else {

                                            if (isValidProperty(str2, "User_id")) {
                                                allstud.setStudent_id(Integer.parseInt(str2.getProperty("User_id").toString().trim()));

                                            }

                                            if (isValidProperty(str2, "Attstatus")) {
                                                if (str2.getProperty("Attstatus").toString().trim().contentEquals("Present")) {
                                                    allstud.setP_selected(true);
                                                } else if (str2.getProperty("Attstatus").toString().trim().contentEquals("Absent")) {
                                                    allstud.setSelected(true);
                                                } else {
                                                    allstud.setP_selected(false);
                                                    allstud.setSelected(false);
                                                }

                                            }

                                            if (isValidProperty(str2, "Standard_id")) {
                                                allstud.setStandard_id(Integer.parseInt(str2.getProperty("Standard_id").toString().trim()));

                                            }

                                            if (isValidProperty(str2, "name")) {
                                                allstud.setName(str2.getProperty("name").toString().trim());

                                            }
                                            if (isValidProperty(str2, "Gender")) {
                                                gender = str2.getProperty("Gender").toString().trim();


                                            }
                                            if (isValidProperty(str2, "FCMToken")) {
                                                allstud.setFCMToken(str2.getProperty("FCMToken").toString().trim());
                                            }
//
                                            // allstud.setDivision_id(Integer.parseInt(stddivision));

                                            studentList.add(allstud);
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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            madapter=new Student_mark_Adapter(studentList,Selected_Date,academic_id,getContext());
            mrecyclerView.setAdapter(madapter);
            progressbar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
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
