package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.Timetable_sliding_tab;
import com.mobi.efficacious.ESmartDemo.adapters.AllTeacherAdapter;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.entity.AllTeacher;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by EFF-4 on 3/13/2018.
 */

public class All_teacher_Name extends Fragment implements SearchView.OnQueryTextListener{
    View myview;
    Cursor cursor;
    int count;
    Databasehelper mydb;
   // ProgressBar progressBar;
    ListView listView;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    AllTeacherAdapter adapter;
    SearchView searchView;
    ArrayList<AllTeacher> allteacher=new ArrayList<AllTeacher>();
    int a;
    String b,c,d;
    String date,academic_id;
    String Page="";
    String school_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.activity_allteacher,null);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        searchView = (SearchView)myview.findViewById(R.id.search_view_teacher);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");

        school_id=settings.getString("TAG_SCHOOL_ID", "");
        mydb=new Databasehelper(getActivity(),"Teacher_record",null,1);
        mydb.query("Create table if not exists Teacher_data(ID INTEGER PRIMARY KEY AUTOINCREMENT,TeacherId INTEGER ,TeacherDesignation varchar,DepartmentName varchar,TeacherName varchar,TeacherAttandence varchar,Currentdate Date,status boolean)");
       // progressBar = (ProgressBar) myview.findViewById(R.id.allteacherProgressBar);
        listView  = (ListView) myview.findViewById(R.id.allteacher_list);

            Page= Timetable_sliding_tab.page;
      if(Page==null)
      {
           Page="attendence";
       }


        LoginAsync login = new LoginAsync();
        login.execute();
        return myview;
    }
    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Teacher Name Here");
    }

    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText))
        {
            listView.clearTextFilter();
        }
        else
        {
            listView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    private class LoginAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "Teacher";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("intSchool_id",school_id);
                //request.addProperty("std_id", Standard_id);

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
                                AllTeacher allteach =new AllTeacher();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "Teacher_id"))
                                    {
                                        allteach.setTeacher_id(str2.getProperty("Teacher_id").toString().trim());
                                        a= Integer.parseInt(str2.getProperty("Teacher_id").toString().trim());
                                    }
                                    if(isValidProperty(str2, "Designation"))
                                    {
                                        allteach.setDesignation(str2.getProperty("Designation").toString().trim());
                                        b=str2.getProperty("Designation").toString().trim();
                                    }

                                    if(isValidProperty(str2, "Department_name"))
                                    {
                                        allteach.setDept(str2.getProperty("Department_name").toString().trim());
                                        c=str2.getProperty("Department_name").toString().trim();
                                    }

                                    if(isValidProperty(str2, "Name"))
                                    {
                                        allteach.setName(str2.getProperty("Name").toString().trim());
                                        d=str2.getProperty("Name").toString().trim();
                                    }
                                    if(isValidProperty(str2, "vchProfile"))
                                    {
                                        allteach.setTeacher_profile(str2.getProperty("vchProfile").toString().trim());
                                    }

                                    allteacher.add(allteach);
                                }

                            }

                        }

                        adapter = new AllTeacherAdapter(getActivity(), allteacher,Page);

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
           // progressBar.setVisibility(View.GONE);
            listView.setTextFilterEnabled(true);
            setupSearchView();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressBar.setVisibility(View.VISIBLE);
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
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

}

