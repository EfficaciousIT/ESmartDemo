package com.mobi.efficacious.ESmartDemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Division_spinner_adapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {
    Button btnLogin;
    Button btnCancel;
    EditText edtUsername;
    EditText edtPassword;
    Spinner spinner, sp_Academic_year, school_spinner;
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
    String User_id;
    String Student_id;
    String Standard_id, Selected_academic_id, Selected_School_id;
    String Division_id;
    String UserType_id;
    public String User_name;
    public String passWord;
    String Name, Division_name, academic_id;
    int roleid;
    String Session_usertype_id, session_username, session_password, session_fcmtoken, session_emailid;
    String Name2, Standrad_name;
    ConnectionDetector cd;
    private FirebaseAuth auth;
    Division_spinner_adapter adapter1;
    Databasehelper mydb;
    HashMap<Object, Object> map;
    int loginstatus = 0;
    int loginverificationstatus = 0;
    private ArrayList<HashMap<Object, Object>> dataList1;
    private ArrayList<HashMap<Object, Object>> dataList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        btnLogin = (Button) findViewById(R.id.btnSubmit_login);
        btnLogin.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel_login);
        btnCancel.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());
        edtUsername = (EditText) findViewById(R.id.edtUserName_login);
        edtPassword = (EditText) findViewById(R.id.edtPassword_login);
        spinner = (Spinner) findViewById(R.id.spUserType_login);
        school_spinner = (Spinner) findViewById(R.id.school_spinner);
        sp_Academic_year = (Spinner) findViewById(R.id.sp_Academic_year);
        dataList1 = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        spinner.setOnItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        session_emailid = settings.getString("TAG_USEREMAILID", "");
        session_fcmtoken = settings.getString("TAG_USERFIREBASETOKEN", "");
        Session_usertype_id = settings.getString("TAG_USERTYPEID", "");
        session_username = settings.getString("TAG_USERNAME", "");
        session_password = settings.getString("TAG_PASSWORD", "");
        mydb = new Databasehelper(getApplicationContext(), "Notifications", null, 1);
//        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        startService(new Intent(getBaseContext(), Mapservice.class));
        mydb.query("Create table if not exists MessageCenter(ID INTEGER PRIMARY KEY AUTOINCREMENT,Message varchar,MessageDate varchar)");
        try {
            mydb.query("Create table if not exists EILGroupChat(ID INTEGER PRIMARY KEY AUTOINCREMENT,SenderMessage varchar,SenderName varchar,RecieverName varchar,ReciverMessage varchar,GroupName varchar,MessageDate varchar)");
            mydb.query("Create table if not exists EILChat(ID INTEGER PRIMARY KEY AUTOINCREMENT,SenderMessage varchar,ReciverMessage varchar,ReciverId varchar,MessageDate varchar,ReceiverUserTypeId varchar)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mydb.query("Create table if not exists NoticeBoard(ID INTEGER PRIMARY KEY AUTOINCREMENT,Subject varchar,Notice varchar,IssueDate varchar,LastDate varchar)");
//&&!session_fcmtoken.contentEquals("")
        if (!Session_usertype_id.contentEquals("") && !session_emailid.contentEquals("") && !session_username.contentEquals("") && !session_password.contentEquals("")) {
            loginstatus = 1;
            if (!cd.isConnectingToInternet()) {

                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setMessage("No InternetConnection");
                alert.setPositiveButton("OK", null);
                alert.show();

            } else {
                Intent HomeScreenIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(HomeScreenIntent);
                finish();
            }

        }

        List<String> categories = new ArrayList<String>();
        categories.add("Admin");
        categories.add("Teacher");

//        categories.add("Staff");
//        categories.add("Parent");
        categories.add("Parent");
        // categories.add("Driver");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LoginActivity.this, R.layout.spinner_item, categories);
        spinner.setAdapter(dataAdapter);
        if (!cd.isConnectingToInternet()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setMessage("No InternetConnection");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            if (loginstatus == 0) {
                SchoolASYNC schoolASYNC = new SchoolASYNC();
                schoolASYNC.execute();
                Academic_Year academic_year = new Academic_Year(Selected_School_id);
                academic_year.execute();
            }
        }

        sp_Academic_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Selected_academic_id = String.valueOf(dataList1.get(position).get("intAcademic_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        school_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Selected_School_id = String.valueOf(dataList2.get(position).get("intSchool_id"));
                if (Selected_School_id.contentEquals("Select")) {
                    Toast.makeText(LoginActivity.this, "Please Select School", Toast.LENGTH_SHORT).show();
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getSelectedItem().toString();
        if (item.equalsIgnoreCase("Parent")) {
            roleid = 1;
        }
//        else if(item.equalsIgnoreCase("Parent"))
//        {
//            roleid = 2;
//        }

        else if (item.equalsIgnoreCase("Teacher")) {
            roleid = 3;
        } else if (item.equalsIgnoreCase("Staff")) {
            roleid = 4;
        } else {
            roleid = 5;
        }
    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit_login:
                Animation animFadein2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                btnLogin.startAnimation(animFadein2);
                USERNAME = edtUsername.getText().toString().trim();
                PASSWORD = edtPassword.getText().toString().trim();
                if (USERNAME.contentEquals("")||PASSWORD.contentEquals("")) {
                    if(TextUtils.isEmpty(USERNAME)) {
                        edtUsername.setError("Enter Valid Username ");
                    }
                    if(TextUtils.isEmpty(PASSWORD)) {
                        edtPassword.setError("Enter Valid Password ");
                    }
                }else {
                    if (!cd.isConnectingToInternet()) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                    } else {

                        if (Selected_academic_id.contentEquals("Select") || Selected_School_id.contentEquals("Select")) {
                            if (Selected_academic_id.contentEquals("Select")) {
                                setSpinnerError(sp_Academic_year, "Select valid Academic Year ");
                            }
                            if (Selected_School_id.contentEquals("Select")) {
                                setSpinnerError(school_spinner, "Select valid School Name ");
                            }
                            //Toast.makeText(LoginActivity.this, "Please Select Proper Data", Toast.LENGTH_LONG).show();
                        } else {

                            LoginAsync login = new LoginAsync();
                            login.execute();
                        }
                    }
                }
                break;
            case R.id.btnCancel_login:
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                btnCancel.startAnimation(animFadein);
                finish();
                break;
        }
    }

    private class LoginAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected Void doInBackground(Void... params) {
            loginverificationstatus = 0;
            OPERATION_NAME = "LoginDetails";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;

            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if (roleid == 1) {
                    request.addProperty("command", "student");
                    request.addProperty("userType_id", String.valueOf(roleid));
                    request.addProperty("userName", USERNAME);
                    request.addProperty("password", PASSWORD);
                    request.addProperty("SchoolId", Selected_School_id);
                    request.addProperty("Academic_id", Selected_academic_id.trim());
                } else if (roleid == 2) {
                    request.addProperty("command", "parents");
                    request.addProperty("userType_id", String.valueOf(roleid));
                    request.addProperty("userName", USERNAME);
                    request.addProperty("password", PASSWORD);
                } else if (roleid == 3) {
                    request.addProperty("command", "teacher");
                    request.addProperty("userType_id", String.valueOf(roleid));
                    request.addProperty("userName", USERNAME);
                    request.addProperty("password", PASSWORD);
                    request.addProperty("SchoolId", Selected_School_id);
                    request.addProperty("Academic_id", Selected_academic_id.trim());
                } else if (roleid == 4) {
                    request.addProperty("command", "staff");
                    request.addProperty("userType_id", String.valueOf(roleid));
                    request.addProperty("userName", USERNAME);
                    request.addProperty("password", PASSWORD);
                } else {
                    request.addProperty("command", "admin");
                    request.addProperty("userType_id", String.valueOf(roleid));
                    request.addProperty("userName", USERNAME);
                    request.addProperty("password", PASSWORD);
                    request.addProperty("SchoolId", Selected_School_id);
                    request.addProperty("Academic_id", Selected_academic_id.trim());
                }
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
                        try {
                            result = response.getProperty(0).toString();
                        } catch (Exception ex) {
                            loginverificationstatus = 1;
                        }

                        if (loginverificationstatus == 1) {

                        } else {
                            result = response.getProperty(0).toString();
                            SoapObject root = (SoapObject) response.getProperty(0);
                            SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                            SoapObject column = (SoapObject) table.getProperty("LoginDetails");

                            UserType_id = column.getProperty(0).toString();
                            if (UserType_id.equalsIgnoreCase("5")) {
                                User_id = column.getProperty(1).toString();
                                Name = column.getProperty(4).toString();
                                User_name = column.getProperty(5).toString();
                                passWord = column.getProperty(6).toString();

                                settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                                settings.edit().putString("TAG_NAME", Name).commit();
                                settings.edit().putString("TAG_NAME2", "Administrator").commit();
                                settings.edit().putString("TAG_USERID", User_id).commit();
                                settings.edit().putString("TAG_USERNAME", User_name).commit();
                                settings.edit().putString("TAG_PASSWORD", passWord).commit();
                                settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                                settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id).commit();


                            } else if (UserType_id.equalsIgnoreCase("4")) {
                                User_id = column.getProperty(1).toString();
                                Name = column.getProperty(4).toString();
                                User_name = column.getProperty(5).toString();
                                passWord = column.getProperty(6).toString();

                                settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                                settings.edit().putString("TAG_NAME", Name).commit();
                                settings.edit().putString("TAG_NAME2", "Staff").commit();
                                settings.edit().putString("TAG_USERID", User_id).commit();
                                settings.edit().putString("TAG_USERNAME", User_name).commit();
                                settings.edit().putString("TAG_PASSWORD", passWord).commit();
                                settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id).commit();
                            } else if (UserType_id.equalsIgnoreCase("3")) {
                                User_id = column.getProperty(1).toString();
                                Name = column.getProperty(4).toString();
                                User_name = column.getProperty(5).toString();
                                passWord = column.getProperty(6).toString();
//                                academic_id = column.getProperty(7).toString();
                                settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                                settings.edit().putString("TAG_USERID", User_id).commit();
                                settings.edit().putString("TAG_NAME", Name).commit();
                                settings.edit().putString("TAG_NAME2", "Teacher").commit();
                                settings.edit().putString("TAG_USERNAME", User_name).commit();
                                settings.edit().putString("TAG_PASSWORD", passWord).commit();
//                                settings.edit().putString("TAG_ACADEMIC_ID", academic_id).commit();
                                settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                                settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id).commit();
                            } else if (UserType_id.equalsIgnoreCase("2")) {
                                User_id = column.getProperty(1).toString();
                                Student_id = column.getProperty(2).toString();
                                Standard_id = column.getProperty(3).toString();
                                Division_id = column.getProperty(4).toString();
                                User_name = column.getProperty(5).toString();
                                passWord = column.getProperty(6).toString();

                                settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                                settings.edit().putString("TAG_USERID", User_id).commit();
                                settings.edit().putString("TAG_STUDENTID", Student_id).commit();
                                settings.edit().putString("TAG_STANDERDID", Standard_id).commit();
                                settings.edit().putString("TAG_DIVISIONID", Division_id).commit();
                                settings.edit().putString("TAG_USERNAME", User_name).commit();
                                settings.edit().putString("TAG_PASSWORD", passWord).commit();
                                settings.edit().putString("TAG_USERID", Student_id).commit();
                                settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id).commit();
                            } else {
                                Student_id = column.getProperty(1).toString();
                                Name = column.getProperty(4).toString();
                                Name2 = column.getProperty(5).toString();
                                Standard_id = column.getProperty(6).toString();
                                Division_id = column.getProperty(7).toString();
                                User_name = column.getProperty(10).toString();
                                passWord = column.getProperty(11).toString();
                                Standrad_name = column.getProperty(8).toString();
                                Division_name = column.getProperty(9).toString();
//                                academic_id = column.getProperty(12).toString();
                                settings.edit().putString("TAG_StandardName", Standrad_name).commit();
                                settings.edit().putString("TAG_DivisionName", Division_name).commit();
                                settings.edit().putString("TAG_USERTYPEID", UserType_id).commit();
                                settings.edit().putString("TAG_STUDENTID", Student_id).commit();
                                settings.edit().putString("TAG_STANDERDID", Standard_id).commit();
                                settings.edit().putString("TAG_DIVISIONID", Division_id).commit();
                                settings.edit().putString("TAG_USERNAME", User_name).commit();
                                settings.edit().putString("TAG_PASSWORD", passWord).commit();
                                settings.edit().putString("TAG_NAME", Name).commit();
                                settings.edit().putString("TAG_NAME2", Name2).commit();
                                settings.edit().putString("TAG_USERID", Student_id).commit();
//                                settings.edit().putString("TAG_ACADEMIC_ID", academic_id).commit();
                                settings.edit().putString("TAG_ACADEMIC_ID", Selected_academic_id.trim()).commit();
                                settings.edit().putString("TAG_SCHOOL_ID", Selected_School_id).commit();
                            }

                            Intent HomeScreenIntent = new Intent(LoginActivity.this, Gmail_login.class);
                            startActivity(HomeScreenIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No Response from Server", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if (loginverificationstatus == 1) {
                loginverificationstatus = 0;
                Toast.makeText(LoginActivity.this, "Incorrect Username Or Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
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

    private class Academic_Year extends AsyncTask<Void, Void, Void> {

        String SchoolId;

        public Academic_Year(String selected_school_id) {
            SchoolId = selected_school_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dataList1.clear();
            map = new HashMap<Object, Object>();
            map.put("intAcademic_id", "Select");
            map.put("AcademicYear", "--Select Academic Year--");
            dataList1.add(map);
            OPERATION_NAME = "SchoolName";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "Academic_year");
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
                                map = new HashMap<Object, Object>();
                                // Standard std =new Standard();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intAcademic_id", str2.getProperty("intAcademic_id").toString().trim());
                                    map.put("AcademicYear", str2.getProperty("AcademicYear").toString().trim());
                                    dataList1.add(map);


                                }
                            }
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter1 = new Division_spinner_adapter(LoginActivity.this, dataList1, "AcademicYear");
            sp_Academic_year.setAdapter(adapter1);


        }
    }

    private class SchoolASYNC extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dataList2.clear();
            map = new HashMap<Object, Object>();
            map.put("intSchool_id", "Select");
            map.put("School_name", "-- Select School --");
            dataList2.add(map);
            OPERATION_NAME = "SchoolName";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
//                request.addProperty("School_id", Schoolid);
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
                                map = new HashMap<Object, Object>();
                                // Standard std =new Standard();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intSchool_id", str2.getProperty("intSchool_id").toString().trim());
                                    map.put("School_name", str2.getProperty("vchSchool_name").toString().trim());
                                    dataList2.add(map);


                                }
                            }
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter1 = new Division_spinner_adapter(LoginActivity.this, dataList2, "SchoolName");
            school_spinner.setAdapter(adapter1);


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
    boolean isValidProperty(SoapObject soapObject, String PropertyName) {
        if (soapObject != null) {
            if (soapObject.getProperty(PropertyName) != null) {
                if (!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("") && !soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
                    return true;
                else
                    return false;
            }
            return false;
        } else
            return false;
    }
}
