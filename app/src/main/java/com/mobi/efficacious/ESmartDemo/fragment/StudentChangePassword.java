package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class StudentChangePassword extends Fragment implements View.OnClickListener{
    View myview;
    Button btnupdate;
    Button btnCancel;
    EditText edtUsername;
    EditText edtPassword;
    Toolbar toolbar;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String USERNAME;
    String PASSWORD;
    String Student_id;
    public String User_name,academic_id;
    public String school_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_studentchangepassword,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Student_id = settings.getString("TAG_STUDENTID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        USERNAME = settings.getString("TAG_USERNAME", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        btnupdate = (Button) myview.findViewById(R.id.btnUpdate_studentchangepassword);
        progressBar = (ProgressBar) myview.findViewById(R.id.ProgressBar_studentchangepassword);
        btnupdate.setOnClickListener(this);
        btnCancel = (Button) myview.findViewById(R.id.btnCancel_studentchangepassword);
        btnCancel.setOnClickListener(this);
        edtUsername = (EditText) myview.findViewById(R.id.edtUserName_studentchangepassword);
        edtPassword = (EditText) myview.findViewById(R.id.edtPassword_studentchangepassword);
        edtUsername.setText(USERNAME);
        edtUsername.clearFocus();
        edtUsername.setFocusable(false);
        edtUsername.setEnabled(false);
        return myview;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate_studentchangepassword:
                Animation animFadein2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
                btnupdate.startAnimation(animFadein2);
                USERNAME = edtUsername.getText().toString().trim();
                PASSWORD =  edtPassword.getText().toString().trim();
                LoginAsync login = new LoginAsync();
                login.execute();
                break;
            case R.id.btnCancel_studentchangepassword:
                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
                btnCancel.startAnimation(animFadein);

                break;
        }
    }

    private class LoginAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "StudentChangePassword";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;

            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "update");
                request.addProperty("userName",  USERNAME);
                request.addProperty("password", PASSWORD);
                request.addProperty("stud_id", Student_id);
                request.addProperty("academic_id", academic_id);
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
                        if(result.contains("updated sucessfully"))
                        {
                            Log.d("Success", "updated sucessfully");

                           // Toast.makeText(StudentChangePasswordActivity.this, "Password updated succesfully", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("Success", "not updated");
                           // Toast.makeText(StudentChangePasswordActivity.this, "Password Not updated", Toast.LENGTH_LONG).show();
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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Password updated succesfully", Toast.LENGTH_LONG).show();

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

}
