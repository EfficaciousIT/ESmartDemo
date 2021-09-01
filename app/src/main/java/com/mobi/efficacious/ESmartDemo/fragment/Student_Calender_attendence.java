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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.ESmartDemo.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.ESmartDemo.dialogbox.Image_zoom_dialog_student;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.roomorama.caldroid.CaldroidFragment;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by EFF-4 on 3/19/2018.
 */

public class Student_Calender_attendence extends Fragment {
    View myview;
    TextView nametxtvw,designationtxtvw;

    Date holidayDay;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id;
public static  String image;
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<String> dates_absent = new ArrayList<String>();
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String a;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String Stud_id,stud_name,standard;
    String status,academic_id;
   ProgressBar progressBar2;

    String school_id;

    public static String intMobileNo;
    CircleImageView profileimg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.teacher_calender,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        nametxtvw = (TextView) myview.findViewById(R.id.textView);
        designationtxtvw = (TextView) myview.findViewById(R.id.textView2);
        progressBar2=(ProgressBar)myview.findViewById(R.id.progressBar2);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        profileimg=(CircleImageView)myview.findViewById(R.id.imageView13);
        role_id = settings.getString("TAG_USERTYPEID", "");
        if(role_id.contentEquals("1")|| role_id.contentEquals("2"))
        {
            Stud_id= settings.getString("TAG_USERID", "");
            stud_name= settings.getString("TAG_NAME", "");
            standard=settings.getString("TAG_NAME2", "");
        }
        else
        {
            stud_name= Attendence_sliding_tab.stud_name;
            Stud_id= Attendence_sliding_tab.stud_id;
            standard= StudentAttendanceActivity.stname;
        }
        role_id = settings.getString("TAG_USERTYPEID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        nametxtvw.setText(stud_name);
        designationtxtvw.setText(standard);

        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY );

        mCaldroidFragment.setArguments( args );

        AdminAsync adm = new AdminAsync();
        adm.execute();
        ProfileAsync profile = new ProfileAsync();
        profile.execute();
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_zoom_dialog_student notifDialog = new Image_zoom_dialog_student(getContext());
                notifDialog.show();
            }
        });
        return myview;
    }
    private class ProfileAsync extends AsyncTask<Void, Void, Void> {
        String status = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+image+"";
            Picasso.with(getActivity()).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(profileimg);
            progressBar2.setVisibility(View.GONE);
        }


        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Profiler";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {


                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "GetStudentProfile");
                request.addProperty("user_id", Stud_id);
                request.addProperty("academic_id", academic_id);
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
                                if (res.contains("No record found")) {

                                } else {
                                    image = str2.getProperty("vchImageURL").toString().trim();

                                    intMobileNo = str2.getProperty("intMobileNo").toString().trim();
                                    if (intMobileNo.equals("") || intMobileNo.equals("null") || intMobileNo.equals("anyType{}")) {
                                        intMobileNo = "-";
                                    }

                                }

                            }

                        }


                    } else {
                        Toast.makeText(getContext(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AdminAsync extends AsyncTask<Void, Void, Void> {
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
                request.addProperty("student_id", Stud_id);
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
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    if(isValidProperty(str2, "dtdate"))
                                    {
                                        a=(str2.getProperty("dtdate").toString().trim());

                                    }

                                    if(isValidProperty(str2, "status"))
                                    {
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
            ColorDrawable bgToday = new ColorDrawable(Color.RED);
            mCaldroidFragment.setBackgroundDrawableForDate(bgToday,holidayDay);


        }
    }
    public void colors() {
        ColorDrawable bgToday = new ColorDrawable(Color.GREEN);
        mCaldroidFragment.setBackgroundDrawableForDate(bgToday,holidayDay);

    }





}

