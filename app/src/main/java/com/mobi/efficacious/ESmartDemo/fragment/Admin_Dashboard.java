package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.NoticeBoardAdapter;
import com.mobi.efficacious.ESmartDemo.adapters.StudentListAdapter;
import com.mobi.efficacious.ESmartDemo.entity.NoticeBoard;
import com.mobi.efficacious.ESmartDemo.entity.Studentlist;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.roomorama.caldroid.CaldroidFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Admin_Dashboard extends Fragment {
    View myview;
    TextView TextView_student;
    TextView TextView_staff;
    TextView TextView_teacher;
    TextView TextView_pendingfee;
    TextView TextView_receivedfee;
    TextView TextView_totalfee;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Studpresent;
    String Studtotal;
    String Teacherpresent;
    String Teachertotal;
    String Staffpresent;
    String Stafftotal;
    String Totalfee;
    String Receivedfee;
    String Pendingfee;
    ListView studentlist;
    Toolbar toolbar;
    ScrollView scrollView;
    ArrayAdapter dashadapter;
    ArrayAdapter studadapter;
    ArrayList<Studentlist> stud=new ArrayList<Studentlist>();
    ArrayList<NoticeBoard> notice=new ArrayList<NoticeBoard>();
    Context mContext;

    String Admin_id,academic_id;
    FrameLayout calenderview;
    Date holidayDay;
    ArrayList<String> dates_absent = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String a;
    String status,school_id;
    RecyclerView noticeboard;
    RecyclerView.Adapter madapter;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.activity_dashboard,null);
        scrollView = (ScrollView) myview.findViewById(R.id.scrollview);
        mContext = getActivity();
        progressBar = (ProgressBar) myview.findViewById(R.id.dashProgressBar);
        TextView_student = (TextView) myview.findViewById(R.id.tv_Student);
        TextView_staff = (TextView) myview.findViewById(R.id.tv_Staff);
        TextView_teacher = (TextView) myview.findViewById(R.id.tv_Teacher);
        TextView_pendingfee = (TextView) myview.findViewById(R.id.tv_pendingfee);
        TextView_receivedfee = (TextView) myview.findViewById(R.id.tv_receivedfee);
        TextView_totalfee = (TextView) myview.findViewById(R.id.tv_totalfee);
        noticeboard = (RecyclerView) myview.findViewById(R.id.dashnoticeboard_list);
        dataList = new ArrayList<HashMap<Object, Object>>();
        studentlist = (ListView) myview.findViewById(R.id.dashstud_list);
//        CalendarView simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView); // get the reference of CalendarView
//        long selectedDate = simpleCalendarView.getDate();
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Admin_id = settings.getString("TAG_USERID", "");
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY );
        calenderview=(FrameLayout)myview.findViewById(R.id.cal_container);
        mCaldroidFragment.setArguments( args );

        dashboard_calender attendence = new dashboard_calender();
        attendence.execute();
        studentAsync student = new studentAsync();
        student.execute();
        teacherAsync teacher = new teacherAsync();
        teacher.execute();
        staffAsync staff = new staffAsync();
        staff.execute();
        feeAsync fee = new feeAsync();
        fee.execute();
        StudentListAsync studentlist = new StudentListAsync();
        studentlist.execute();
        NoticeboardAsync notice = new NoticeboardAsync();
        notice.execute();
        return myview;
    }
    private class studentAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Studentcount";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;

            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "StudentCount");
                request.addProperty("school_id", school_id);
                request.addProperty("academic_id", academic_id);
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
                        SoapObject root = (SoapObject) response.getProperty(0);
                        SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                        SoapObject column = (SoapObject) table.getProperty("Studentcount");
                        Studpresent = column.getProperty(1).toString();
                        Studtotal = column.getProperty(0).toString();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Studpresent="0";
                Studtotal="0";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            TextView_student.setText("Student: "+ Studpresent +"/"+Studtotal);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class staffAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Staffcount";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;

            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "Staffcount");
                request.addProperty("school_id", school_id);
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

                        SoapObject root = (SoapObject) response.getProperty(0);
                        SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                        SoapObject column = (SoapObject) table.getProperty("Staffcount");
                        Staffpresent = column.getProperty(1).toString();
                        Stafftotal = column.getProperty(0).toString();

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Staffpresent="0";
                Stafftotal="0";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            TextView_staff.setText("Staff: "+ Staffpresent +"/"+ Stafftotal);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class teacherAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Teachercount";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;

            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "Teachercount");
                request.addProperty("school_id", school_id);
//                request.addProperty("academic_id", academic_id);
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
                        SoapObject root = (SoapObject) response.getProperty(0);
                        SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                        SoapObject column = (SoapObject) table.getProperty("Teachercount");
                        Teacherpresent = column.getProperty(1).toString();
                        Teachertotal = column.getProperty(0).toString();

                    } else
                    {
                        Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Teacherpresent="0";
                Teachertotal="0";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            TextView_teacher.setText("Teacher: " + Teacherpresent + "/" + Teachertotal);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class feeAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Feecollection";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;

            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("school_id", school_id);
                request.addProperty("academic_id", academic_id);
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
                        SoapObject root = (SoapObject) response.getProperty(0);
                        SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                        SoapObject column = (SoapObject) table.getProperty("Feecollection");
                        if(column.getProperty(0).toString().equalsIgnoreCase(""))
                        {
                            Totalfee = "0";
                        }
                        else
                        {
                            Totalfee = column.getProperty(0).toString();
                        }

                        if(column.getProperty(1).toString().equalsIgnoreCase(""))
                        {
                            Receivedfee = "0";
                        }
                        else
                        {
                            Receivedfee = column.getProperty(1).toString();
                        }

                        if(column.getProperty(2).toString().equalsIgnoreCase(""))
                        {
                            Pendingfee = "0";
                        }
                        else
                        {
                            Pendingfee = column.getProperty(2).toString();
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No Response from Server", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Totalfee="0";
                Receivedfee="0";
                Pendingfee="0";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            TextView_totalfee.setText("Total Fee: "+ Totalfee);
            TextView_receivedfee.setText("Received: "+ Receivedfee);
            TextView_pendingfee.setText("Pending: "+ Pendingfee);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class StudentListAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "StudentList";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "genderwiseStudent");
                request.addProperty("school_id", school_id);
                request.addProperty("academic_id", academic_id);
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
                                Studentlist std =new Studentlist();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "Standard"))
                                    {
                                        std.setStandard(str2.getProperty("Standard").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Division"))
                                    {
                                        std.setDivision(str2.getProperty("Division").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Male"))
                                    {
                                        std.setMale(str2.getProperty("Male").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Female"))
                                    {
                                        std.setFemale(str2.getProperty("Female").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Total"))
                                    {
                                        std.setTotal(str2.getProperty("Total").toString().trim());
                                    }

                                    stud.add(std);
                                }

                            }

                        }

                        studadapter = new StudentListAdapter(mContext, stud);
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
            studentlist.setAdapter(studadapter);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class NoticeboardAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "AdminNoticeboard";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "NoticeBoard");
                request.addProperty("school_id", school_id);

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
                                NoticeBoard not =new NoticeBoard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map = new HashMap<Object, Object>();
                                    map.put("Subject",str2.getProperty("Subject").toString().trim());
                                    map.put("Notice",str2.getProperty("Notice").toString().trim());
                                    map.put("IssueDate",str2.getProperty("Issue_Date").toString().trim());
                                    map.put("LastDate",str2.getProperty("End_Date").toString().trim());
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
            noticeboard.setHasFixedSize(true);
            noticeboard.setLayoutManager(new LinearLayoutManager(getActivity()));
            madapter=new NoticeBoardAdapter(dataList);
            noticeboard.setAdapter(madapter);
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
    private class dashboard_calender extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "AdminAttendanceSummery";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "select");
                request.addProperty("admin_id",Admin_id);
                request.addProperty("academic_id",academic_id);
                request.addProperty("school_id", school_id);
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
                                // Holiday hol =new Holiday();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "dtdate"))
                                    {
                                        a=str2.getProperty("dtdate").toString().trim();
                                        // hol.setFromDate(str2.getProperty("dtFromDate").toString().trim());
                                    }
                                    if(isValidProperty(str2, "status"))
                                    {
                                        //student.setStatus(str2.getProperty("status").toString().trim());
                                        status= ((str2.getProperty("status").toString().trim()));
                                    }
                                    if(status.contentEquals("Present"))
                                    {
                                        dates.add(a);
                                    }
                                    else
                                    {
                                        dates_absent.add(a);
                                    }


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
                attendence_present();
                attendence_absent();

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
            getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , mCaldroidFragment ).commit();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public  void attendence_present()
    {
        int day = 0;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        for (int i = 0; i < dates.size(); i++) {
            String inputString2 = dates.get(i);
            String inputString1 = myFormat.format(date);

            try {
                //Converting String format to date format
                Date date1 = null;
                try {
                    date1 = myFormat.parse(inputString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = myFormat.parse(inputString2);
                //Calculating number of days from two dates
                long diff = date2.getTime() - date1.getTime();
                long datee = diff / (1000 * 60 * 60 * 24);
                //Converting long type to int type
                day = (int) datee;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, day);
            holidayDay = cal.getTime();

            colors();


        }
    }
    public  void attendence_absent() {
        int day = 0;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        for (int i = 0; i < dates_absent.size(); i++) {
            String inputString2 = dates_absent.get(i);
            String inputString1 = myFormat.format(date);

            try {
                //Converting String format to date format
                Date date1 = null;
                try {
                    date1 = myFormat.parse(inputString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = myFormat.parse(inputString2);
                //Calculating number of days from two dates
                long diff = date2.getTime() - date1.getTime();
                long datee = diff / (1000 * 60 * 60 * 24);
                //Converting long type to int type
                day = (int) datee;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, day);
            holidayDay = cal.getTime();
//            mCaldroidFragment.setTextColorForDate(R.color.red,holidayDay);
            //  colors();
            ColorDrawable bgToday = new ColorDrawable(Color.RED);
            mCaldroidFragment.setBackgroundDrawableForDate(bgToday,holidayDay);
        }
    }
    public void colors() {
        ColorDrawable bgToday = new ColorDrawable(Color.GREEN);
        mCaldroidFragment.setBackgroundDrawableForDate(bgToday,holidayDay);
//        mCaldroidFragment.setTextColorForDate(R.color.green,holidayDay);
    }


}

