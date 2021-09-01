package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Division_spinner_adapter;
import com.mobi.efficacious.ESmartDemo.adapters.spinner_std_name;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.Standard;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by EFF-4 on 3/27/2018.
 */

public class Sms_Fragment extends Fragment {

    RelativeLayout relativeLayout_notice,relativeLayout_notice1,relativeLayout_notice2,relativeLayout2,relativeLayout3;
    Button send_button;
    TextView textView;
    ArrayList<Standard> all_option=new ArrayList<Standard>();

    Spinner spinner_usertype,spinner_std,spinner_section;
    String[] UserType;
    spinner_std_name adapter;
    Division_spinner_adapter adapter1;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList1;
    ProgressBar progressBar,progressBar1;
    ArrayList<String> sms_phone_no_array = new ArrayList<String>();
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    EditText sms_box,sms_box2,phone_no,sms_box3;
    TextView char_160,sms_count,char1_160,char2_160,sms1_count,sms2_count;
    int char_remain;
    String status="",Schooli_id;
    SharedPreferences settings;
    String std_selected_name="",std_selected_id="",role_id;
    Toolbar toolbar;
    List<String> phone_no_list;
    String std_id,academic_id,UserType_Selected;
    ConnectionDetector cd;
View myview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.sms_layout,null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        relativeLayout2=(RelativeLayout)myview.findViewById(R.id.relativelayout2);
        relativeLayout3=(RelativeLayout)myview.findViewById(R.id.relativelayout3);
        relativeLayout_notice=(RelativeLayout)myview.findViewById(R.id.notice);
        relativeLayout_notice1=(RelativeLayout)myview.findViewById(R.id.notice2);
        relativeLayout_notice2=(RelativeLayout)myview.findViewById(R.id.notice3);
        sms_box=(EditText)myview.findViewById(R.id.editText22);
        sms_box2=(EditText)myview.findViewById(R.id.editText2);
        sms_box3 =(EditText)myview.findViewById(R.id.editText288);
        progressBar=(ProgressBar)myview.findViewById(R.id.progressBar);
        progressBar1=(ProgressBar)myview.findViewById(R.id.progressBar1);
        sms1_count=(TextView)myview.findViewById(R.id.textView34);
        sms2_count=(TextView)myview.findViewById(R.id.textView311);
        char1_160=(TextView)myview.findViewById(R.id.textView27);
        char2_160=(TextView)myview.findViewById(R.id.textView291);
        phone_no=(EditText)myview.findViewById(R.id.phoneno_et) ;
        sms_count=(TextView)myview.findViewById(R.id.textView31);
        char_160=(TextView)myview.findViewById(R.id.textView29);
        send_button=(Button)myview.findViewById(R.id.button5);
        //   textView=(TextView)findViewById(R.id.textView24) ;
        spinner_usertype = (Spinner)myview.findViewById(R.id.spinner3);
        spinner_section = (Spinner)myview.findViewById(R.id.spinner5);
        spinner_std=(Spinner)myview.findViewById(R.id.spinner4);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        Schooli_id= settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        UserType = getResources().getStringArray(R.array.spinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,UserType);
        spinner_usertype.setAdapter(ad);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList1 = new ArrayList<HashMap<Object, Object>>();

        spinner_usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int usertype_selected=position;
                switch (usertype_selected)
                {
                    case 0:
                        status="Select";
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout_notice.setVisibility(View.GONE);
                        relativeLayout_notice1.setVisibility(View.VISIBLE);
                        relativeLayout_notice2.setVisibility(View.GONE);
                        sms_box3.setVisibility(View.GONE);
                        phone_no.setVisibility(View.GONE);


                        break;
                    case 1:
                        //  textView.setText("Standard");
                        status="standard";
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {
                           Standard_name login = new Standard_name();
                            login.execute();
                        }
                        relativeLayout2.setVisibility(View.VISIBLE);
                        relativeLayout3.setVisibility(View.VISIBLE);
                        relativeLayout_notice.setVisibility(View.VISIBLE);
                        relativeLayout_notice1.setVisibility(View.GONE);
                        relativeLayout_notice2.setVisibility(View.GONE);
                        sms_box3.setVisibility(View.GONE);
                        phone_no.setVisibility(View.GONE);
                        break;
                    case 2:
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout_notice.setVisibility(View.GONE);
                        relativeLayout_notice1.setVisibility(View.VISIBLE);
                        relativeLayout_notice2.setVisibility(View.GONE);
                        sms_box3.setVisibility(View.GONE);
                        phone_no.setVisibility(View.GONE);
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {
                            if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                            {
                                usertype_selected usertype_admin = new usertype_selected("AllTeacherByPrincipal");
                                usertype_admin.execute();

                            }else {
                                usertype_selected usertype_teacher = new usertype_selected("AllTeacher");
                                usertype_teacher.execute();
                            }
                        }
                        break;
                    case 3:
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout_notice.setVisibility(View.GONE);
                        relativeLayout_notice1.setVisibility(View.VISIBLE);
                        relativeLayout_notice2.setVisibility(View.GONE);
                        sms_box3.setVisibility(View.GONE);
                        phone_no.setVisibility(View.GONE);
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {
                            if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                            {
                                usertype_selected usertype_admin = new usertype_selected("AllStaffByPrincipal");
                                usertype_admin.execute();
                            }else {
                                usertype_selected usertype_staff = new usertype_selected("AllStaff");
                                usertype_staff.execute();
                            }
                        }
                        break;
                    case 4:
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout_notice.setVisibility(View.GONE);
                        relativeLayout_notice1.setVisibility(View.VISIBLE);
                        relativeLayout_notice2.setVisibility(View.GONE);
                        sms_box3.setVisibility(View.GONE);
                        phone_no.setVisibility(View.GONE);
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {
                            if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                            {
                                usertype_selected usertype_admin = new usertype_selected("AllSMSAdminByPrincipal");
                                usertype_admin.execute();
                            }else {
                                usertype_selected usertype_admin = new usertype_selected("AllSMSAdmin");
                                usertype_admin.execute();
                            }
                        }
                        break;
                    case 5:
                        status="Multiple";
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout_notice.setVisibility(View.GONE);
                        relativeLayout_notice1.setVisibility(View.GONE);
                        relativeLayout_notice2.setVisibility(View.VISIBLE);
                        sms_box3.setVisibility(View.VISIBLE);
                        phone_no.setVisibility(View.VISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sms_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int msgcount;
                int limit=160;
                int char_count= s.length();


                if(char_count>160)
                {
                    int charachtercount;
                    msgcount=(1+(char_count/160));
                    charachtercount=(char_count-160);
                    if(charachtercount<=160)
                    {
                        char_remain=(limit-charachtercount);
                    }
                    else
                    {
                        char_remain=(charachtercount-limit);
                    }
                    char_160.setText(String.valueOf(char_remain));
                    sms_count.setText(String.valueOf(msgcount));
                }
                else
                {
                    msgcount=1;
                    sms_count.setText(String.valueOf(msgcount));
                    char_remain=(limit-char_count);
                    char_160.setText(String.valueOf(char_remain));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sms_box2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int msgcount;
                int limit=160;
                int char_count= s.length();


                if(char_count>160)
                {
                    int charachtercount;
                    msgcount=(1+(char_count/160));
                    charachtercount=(char_count-160);
                    if(charachtercount<=160)
                    {
                        char_remain=(limit-charachtercount);
                    }
                    else
                    {
                        char_remain=(charachtercount-limit);
                    }
                    char1_160.setText(String.valueOf(char_remain));
                    sms1_count.setText(String.valueOf(msgcount));
                }
                else
                {
                    msgcount=1;
                    sms1_count.setText(String.valueOf(msgcount));
                    char_remain=(limit-char_count);
                    char1_160.setText(String.valueOf(char_remain));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sms_box3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int msgcount;
                int limit=160;
                int char_count= s.length();


                if(char_count>160)
                {
                    int charachtercount;
                    msgcount=(1+(char_count/160));
                    charachtercount=(char_count-160);
                    if(charachtercount<=160)
                    {
                        char_remain=(limit-charachtercount);
                    }
                    else
                    {
                        char_remain=(charachtercount-limit);
                    }
                    char2_160.setText(String.valueOf(char_remain));
                    sms2_count.setText(String.valueOf(msgcount));
                }
                else
                {
                    msgcount=1;
                    sms2_count.setText(String.valueOf(msgcount));
                    char_remain=(limit-char_count);
                    char2_160.setText(String.valueOf(char_remain));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar1.setVisibility(View.GONE);
                if(status.contentEquals("Select"))
                {
                    setSpinnerError(spinner_usertype,"Select valid Usertype ");

                }else if( status.contentEquals("standard"))
                {
                    String sms= sms_box2.getText().toString();
                    if(sms.contentEquals(""))
                    {
                        if(TextUtils.isEmpty(sms)) {
                            sms_box2.setError("Enter SMS ");
                        }
                    }else
                    {
                        sms=  sms.replace("&", "and");
                        sms=  sms.replace("%","percent");
                        sms=sms.replace("/","-");
                        sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");
                        sms = sms.replace("Upto","Up to");
                        sms = sms.replace("upto","up to");
                        sms =  sms.replace("Election","Elec-tion");
                        sms =  sms.replace("election","elec-tion");
                        sms =  sms.replace("elections","elec-tions");
                        sms = sms.replace("Elections","Elec-tions");
                        if (!cd.isConnectingToInternet())
                        {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK",null);
                            alert.show();

                        }
                        else {
                            sendsms sendsms = new sendsms(sms);
                            sendsms.execute();
                        }
                        status="";
                    }
                }
                else if(status.contentEquals("Multiple")) {

                    String phoneno = phone_no.getText().toString();
                    phone_no_list = Arrays.asList(phoneno.split(","));
                    String sms = sms_box3.getText().toString();
                    if (phoneno.contentEquals("") || sms.contentEquals("")) {
                        if(TextUtils.isEmpty(sms)) {
                            sms_box3.setError("Enter SMS ");
                        }
                        if(TextUtils.isEmpty(phoneno)) {
                            phone_no.setError("Enter Phone No. ");
                        }
                    } else
                    {
                        sms = sms.replace("&", "and");
                        sms = sms.replace("%", "percent");
                        sms = sms.replace("/", "-");
                        sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");

                        sms = sms.replace("Upto", "Up to");
                        sms = sms.replace("upto", "up to");
                        sms = sms.replace("Election", "Elec-tion");
                        sms = sms.replace("election", "elec-tion");
                        sms = sms.replace("elections", "elec-tions");
                        sms = sms.replace("Elections", "Elec-tions");
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            multiple sendsms = new multiple(sms);
                            sendsms.execute();
                        }
                        status = "";
                    }
                }
                else
                {

                    String sms= sms_box.getText().toString();
                    if (sms.contentEquals("")) {
                        if(TextUtils.isEmpty(sms)) {
                            sms_box.setError("Enter SMS ");
                        }
                    } else {
                        sms = sms.replace("&", "and");
                        sms = sms.replace("%", "percent");
                        sms = sms.replace("/", "-");
                        sms = sms.replaceAll("[^-a-zA-Z0-9-,-.- ]", "");
                        sms = sms.replace("Upto", "Up to");
                        sms = sms.replace("upto", "up to");
                        sms = sms.replace("Election", "Elec-tion");
                        sms = sms.replace("election", "elec-tion");
                        sms = sms.replace("elections", "elec-tions");
                        sms = sms.replace("Elections", "Elec-tions");
                        if (!cd.isConnectingToInternet()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        } else {
                            sendsms sendsms = new sendsms(sms);
                            sendsms.execute();
                        }
//                    sms_box.setText();
                    }
                }

            }
        });
        spinner_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                std_selected_name= String.valueOf(dataList.get(position).get("Std_name"));
                spinner_section.setVisibility(View.VISIBLE);

                std_id= String.valueOf(dataList.get(position).get("Std_Id"));

                if(std_selected_name.contentEquals("All Student"))
                {
                    spinner_section.setVisibility(View.GONE);
                    if (!cd.isConnectingToInternet())
                    {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();

                    }
                    else {
                        AllStandard_sms sms = new AllStandard_sms();
                        sms.execute();
                    }
                }
                else
                {
                    if (!cd.isConnectingToInternet())
                    {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();

                    }
                    else {
                        Division_name division_name = new Division_name(std_id);
                        division_name.execute();
                    }
                }
                //  Toast.makeText(Sms_activity.this,""+std_selected,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std_div_id= String.valueOf(dataList1.get(position).get("div_id"));
                Standard_wise_sms sms1 = new Standard_wise_sms(std_id,std_div_id);
                sms1.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return myview;
    }

    private class Standard_name extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();

            map = new HashMap<Object, Object>();

            map.put("Std_name","All Student");
            map.put("Std_Id","All Student");
            dataList.add(map);

            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                {
                    request.addProperty("command", "selectStandardByPrincipal");

                }else
                {
                    request.addProperty("command", "select");
                    request.addProperty("intSchool_id",Schooli_id);
                }

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
                                map = new HashMap<Object, Object>();
                                Standard std =new Standard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map.put("Std_name", str2.getProperty("vchStandard_name").toString().trim());
                                    map.put("Std_Id", str2.getProperty("intStandard_Id").toString().trim());
                                    if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                                    {
                                        map.put("intschool_id", str2.getProperty("intschool_id").toString().trim());
                                    }
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
            try
            {
                adapter = new spinner_std_name(getActivity(), dataList,"Standard_name");
                spinner_std.setAdapter(adapter);
            }catch (Exception ex)
            {

            }

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

    private class AllStandard_sms extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            sms_phone_no_array.clear();
            OPERATION_NAME = "AllStandardSMS";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                {
                    request.addProperty("command", "AllMessageToStudentByPrincipal");
                    request.addProperty("academic_id", academic_id);
                }else
                {
                    request.addProperty("command", "AllMessageToStudent");
                    request.addProperty("school_id", Schooli_id);
                    request.addProperty("academic_id", academic_id);
                }

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

                                    sms_phone_no_array.add(str2.getProperty("intBusAlert1").toString().trim());

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
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Total Phone No Count:"+sms_phone_no_array.size(),Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private class Standard_wise_sms extends AsyncTask<Void, Void, Void> {
        String std_id="";
        String std_div_id1="";
        public Standard_wise_sms(String std_selected_id, String std_div_id) {
            std_id=std_selected_id;
            std_div_id1=std_div_id;
        }

        @Override
        protected Void doInBackground(Void... params) {

            sms_phone_no_array.clear();

            OPERATION_NAME = "StandardSMS";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "AllMessageToDivision");
                request.addProperty("standard_id", std_id);
                request.addProperty("division_id",std_div_id1);
                  request.addProperty("school_id", Schooli_id);
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
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    sms_phone_no_array.add(str2.getProperty("intBusAlert1").toString().trim());

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

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Total Phone No Count:"+sms_phone_no_array.size(),Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class usertype_selected extends AsyncTask<Void, Void, Void> {

        String userttype_selected="";

        public usertype_selected(String usertype_command) {
            userttype_selected=usertype_command;
        }
        protected Void doInBackground(Void... params) {

            sms_phone_no_array.clear();

            OPERATION_NAME = "StaffSMS";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if(role_id.contentEquals("6")||role_id.contentEquals("7"))
                {
                    request.addProperty("command", userttype_selected);

                }else
                {
                    request.addProperty("command", userttype_selected);
                    request.addProperty("school_id", Schooli_id);
                }


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

                                    sms_phone_no_array.add(str2.getProperty("intmobileNo").toString().trim());

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
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Total Phone No Count:"+sms_phone_no_array.size(),Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class multiple extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String message="";
        int SMS_SendCount=0;
        int Total_Count=0;
        public multiple(String sms) {
            message=sms;
        }

        protected Void doInBackground(Void... voids) {
            try {
                Total_Count=phone_no_list.size();
                HttpURLConnection uc = null;
                String requestUrl = "";
                for (int i = 0; i < phone_no_list.size(); i++) {

                    requestUrl  = ("http://alerts.justnsms.com/api/v3/?method=sms&api_key=Ad68553890184f28bf0a8c8951f3a665f&to=" + URLEncoder.encode(phone_no_list.get(i), "UTF-8")+ "&sender=EFFICA&message=" + URLEncoder.encode(message, "UTF-8")+"&format=json&custom=1,2&flash=0&unicode=1");
                  //  requestUrl  = ("http://alerts.justnsms.com/api/web2sms.php?workingkey=A2cabcee227fa491ee050155a13485498&sender=CMSBKP&to=" + URLEncoder.encode(phone_no_list.get(i), "UTF-8")+"&message=" + URLEncoder.encode(message, "UTF-8")+"&format=json&custom=1,2&flash=0&unicode=1");
//                    requestUrl = ("http://alerts.justnsms.com/api/v3/?method=sms&api_key=Ad68553890184f28bf0a8c8951f3a665f&to=" + URLEncoder.encode(phone_no_list.get(i), "UTF-8") + "&sender=EFFICA&message=" + URLEncoder.encode(message, "UTF-8") + "&format=json&custom=1,2&flash=0");


                    URL url = new URL(requestUrl);
                    uc = (HttpURLConnection) url.openConnection();
                    String responseMessage=uc.getResponseMessage();
                    if(responseMessage.contentEquals("OK"))
                    {
                        SMS_SendCount=SMS_SendCount+1;
                    }
                    uc.disconnect();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Sending SMS...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            phone_no.setText("");
            sms_box3.setText("");
            dialog.dismiss();
            Toast.makeText(getActivity(),"SMS Send Successfully", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(SMS_SendCount +" SMS Sent Successfully Out of "+Total_Count);
            alert.setPositiveButton("OK",null);
            alert.show();
        }
    }
    private class sendsms extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String message="";
        int SMS_SendCount=0;
        int Total_Count=0;
        public sendsms(String sms) {
            message=sms;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Total_Count=sms_phone_no_array.size();
                HttpURLConnection uc = null;
                String requestUrl="";
                for (int i = 0; i < sms_phone_no_array.size(); i++)
                {
                   // requestUrl  = ("http://alerts.justnsms.com/api/web2sms.php?workingkey=A2cabcee227fa491ee050155a13485498&sender=CMSBKP&to=" + URLEncoder.encode(sms_phone_no_array.get(i), "UTF-8")+"&message=" + URLEncoder.encode(message, "UTF-8")+"&format=json&custom=1,2&flash=0&unicode=1");
                     requestUrl  = ("http://alerts.justnsms.com/api/v3/?method=sms&api_key=Ad68553890184f28bf0a8c8951f3a665f&to=" + URLEncoder.encode(sms_phone_no_array.get(i), "UTF-8")+ "&sender=EFFICA&message=" + URLEncoder.encode(message, "UTF-8")+"&format=json&custom=1,2&flash=0&unicode=1");
                    URL url = new URL(requestUrl);
                    uc = (HttpURLConnection)url.openConnection();
                    System.out.println(uc.getResponseMessage());
                    String response=uc.getResponseMessage();
                    if(response.contentEquals("OK"))
                    {
                        SMS_SendCount=SMS_SendCount+1;
                    }
                    uc.disconnect();
                }


            } catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Sending SMS...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            sms_phone_no_array.clear();
            dialog.dismiss();
            Toast.makeText(getActivity(),"SMS Send Successfully", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(SMS_SendCount +" SMS Sent Successfully Out of "+Total_Count);
            alert.setPositiveButton("OK",null);
            alert.show();
        }
    }
    private class Division_name extends AsyncTask<Void, Void, Void> {

        String Standard_id="";
        public Division_name(String std_id) {
            Standard_id=std_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataList1.clear();
            OPERATION_NAME = "Division";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "GetDivision");
                request.addProperty("standard", Standard_id);
                request.addProperty("intSchool_id", Schooli_id);
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
                                // Standard std =new Standard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
//                                    if(isValidProperty(str2, "intDivision_id"))
//                                    {
//                                        std.setDivision_id(str2.getProperty("intDivision_id").toString().trim());
//                                    }
//
//                                    if(isValidProperty(str2, "vchDivisionName"))
//                                    {
//                                        std.setStandarad_div(str2.getProperty("vchDivisionName").toString().trim());
//                                    }
                                    map.put("div_id", str2.getProperty("intDivision_id").toString().trim());
                                    map.put("div_name", str2.getProperty("vchDivisionName").toString().trim());
                                    dataList1.add(map);


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
            try
            {
                adapter1 = new Division_spinner_adapter(getActivity(), dataList1,"Division_name");
                spinner_section.setAdapter(adapter1);
            }catch (Exception ex)
            {

            }

            progressBar.setVisibility(View.GONE);

        }
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
}

