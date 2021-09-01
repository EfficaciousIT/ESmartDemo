package com.mobi.efficacious.ESmartDemo.dialogbox;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.StandardAdapter;
import com.mobi.efficacious.ESmartDemo.adapters.Student_division_adapter;
import com.mobi.efficacious.ESmartDemo.entity.Standard;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class Standard_division_dialog extends Activity {
    ArrayAdapter adapter;
    ArrayList<Standard> all_option=new ArrayList<Standard>();
    TextView heading;
    ListView listView;
    String value;

    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String role_id,academic_id;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String Standard_id,userid,school_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_box);
        settings =getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        userid = settings.getString("TAG_USERID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        heading = (TextView) findViewById(R.id.title);
        heading.setText(" Division");

        listView = (ListView) findViewById(R.id.notificationlistView);
        value= StandardAdapter.page_name;
        Standard_id= StandardAdapter.Std_id_division;

        HolidayAsync holidayAsync = new HolidayAsync();
        holidayAsync.execute();

    }

    private class HolidayAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            OPERATION_NAME = "Division";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if(role_id.contentEquals("3"))
                {
                    request.addProperty("command", "selectDivisionByLectures");
                    request.addProperty("standard", Standard_id);
                    request.addProperty("intTeacher_id", userid);
                    request.addProperty("intAcademic_id", academic_id);
                    request.addProperty("intSchool_id",school_id);
                }else
                {
                    request.addProperty("command", "GetDivision");
                    request.addProperty("standard", Standard_id);
                    request.addProperty("intSchool_id",school_id);
                }

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
                                Standard std =new Standard();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intDivision_id"))
                                    {
                                        std.setDivision_id(str2.getProperty("intDivision_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchDivisionName"))
                                    {
                                        std.setStandarad_div(str2.getProperty("vchDivisionName").toString().trim());
                                    }

                                    all_option.add(std);
                                }
                            }
                        }
                        adapter=new Student_division_adapter(Standard_division_dialog.this,all_option,value);
                    }
                    else
                    {
                        Toast.makeText(Standard_division_dialog.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(Standard_division_dialog.this, "Error", Toast.LENGTH_LONG).show();
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

