package com.mobi.efficacious.ESmartDemo.dialogbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
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

public class Result_Dialog_box extends Activity {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    Button btnsave,btnCancel;
    EditText dateet,monthet,yearet,houret,minuteet;
    String DateTv,MonthTv,YearTv,HourTv,MinuteTv;
    String academic_id,role_id,user_id,result_date,Exam_selected_name,Exam_id,school_id;
    Spinner spinnerexamname;
    HashMap<Object, Object> map;
    spinner_std_name adapter;
    private ArrayList<HashMap<Object, Object>> dataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.resultdate);
        btnsave=(Button) findViewById(R.id.btnsave);
        btnCancel=(Button) findViewById(R.id.btnCancel);
        dateet=(EditText)findViewById(R.id.dateet) ;
        monthet=(EditText)findViewById(R.id.monthtv) ;
        yearet=(EditText)findViewById(R.id.yeartv) ;
        houret=(EditText)findViewById(R.id.hourtv) ;
        minuteet=(EditText)findViewById(R.id.minutetv) ;
        spinnerexamname=(Spinner) findViewById(R.id.spinnerexamname) ;
        dataList = new ArrayList<HashMap<Object, Object>>();
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");

        school_id=settings.getString("TAG_SCHOOL_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        user_id=settings.getString("TAG_USERID", "");
        ExamAsync examAsync=new ExamAsync();
        examAsync.execute();
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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTv=dateet.getText().toString();
                MonthTv=monthet.getText().toString();
                YearTv=yearet.getText().toString();
                HourTv=houret.getText().toString();
                MinuteTv=minuteet.getText().toString();
                if(!DateTv.contentEquals("")&&!MonthTv.contentEquals("")&&!YearTv.contentEquals("")&&!HourTv.contentEquals("")&&!MinuteTv.contentEquals("")&&!Exam_selected_name.contentEquals("--Select--"))
                {
                    result_date=(DateTv+"/"+MonthTv+"/"+YearTv+" "+HourTv+":"+MinuteTv);
                    ResiltDateAsync resiltDateAsync=new ResiltDateAsync();
                    resiltDateAsync.execute();
                }else
                {
                    Toast.makeText(Result_Dialog_box.this,"Please Enter Proper Data",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class ResiltDateAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Result_Dialog_box.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            finish();
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "ResultDate";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "Insert");
                request.addProperty("Exam_id", Exam_id.toString());
                request.addProperty("dtResultDate",result_date);
                request.addProperty("intSchool_id",school_id);
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
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }
    private class ExamAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Result_Dialog_box.this);
        @Override
        protected Void doInBackground(Void... params) {

            dataList.clear();

            map = new HashMap<Object, Object>();

            map.put("Exam_id","--Select--");
            map.put("Exam_Name","--Select--");
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
                        Toast.makeText(Result_Dialog_box.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(Result_Dialog_box.this, "Error", Toast.LENGTH_LONG).show();
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
            adapter = new spinner_std_name(Result_Dialog_box.this, dataList,"ResultDate");
            spinnerexamname.setAdapter(adapter);
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading ...");
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}