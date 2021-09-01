package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.StudentAttendance;
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


public class ApplyLeaveFragment extends Fragment {
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    ListView listView;
    ArrayAdapter adapter;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    ArrayList<StudentAttendance> admmin=new ArrayList<StudentAttendance>();
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String UsertypeId;
    String StudId;
    String UserId;
    TextView TvDate1;
    TextView TvDate2;
    EditText days;
    EditText reason;
    ImageView img1;
    ImageView img2;
    Button submit;
    Button cancel;
    String fromdate;
    String todate;
    String noofdays;
    String reasons;
    Date newDate1;
    Date newDate2;
    String Year_id,school_id,NAme;
    String serviceresult;
    ConnectionDetector cd;
    public ApplyLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_applyleave, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cd = new ConnectionDetector(getActivity());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        UsertypeId = settings.getString("TAG_USERTYPEID", "");
        UserId = settings.getString("TAG_USERID", "");
        StudId = settings.getString("TAG_STUDENTID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
       NAme =settings.getString("TAG_NAME", "");
        TvDate1 = (TextView) getActivity().findViewById(R.id.date1_leave);
        TvDate2 = (TextView) getActivity().findViewById(R.id.date2_leave);
        days = (EditText) getActivity().findViewById(R.id.edtdays_leave);
        reason = (EditText) getActivity().findViewById(R.id.edtreason_leave);
        img1 = (ImageView) getActivity().findViewById(R.id.img1);
        img2 = (ImageView) getActivity().findViewById(R.id.img2);
        submit = (Button) getActivity().findViewById(R.id.btnSubmit_leave);
        cancel = (Button) getActivity().findViewById(R.id.btnCancel_leave);

        TvDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate1(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                fromdate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
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
                                    date2 = sdf.parse(fromdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                TvDate1.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        TvDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //showDate2(year, month+1, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                todate=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
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
                                    date2 = sdf.parse(todate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                TvDate2.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

     submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!cd.isConnectingToInternet())
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("No InternetConnection");
                alert.setPositiveButton("OK",null);
                alert.show();

            }
            else {
                if(TvDate1.getText().toString().contentEquals("")||TvDate2.getText().toString().contentEquals("")||days.getText().toString().contentEquals("")||reason.getText().toString().contentEquals(""))
                {

                    if(TextUtils.isEmpty(TvDate1.getText().toString())) {
                        TvDate1.setError("Enter Valid From Date ");
                    }


                    if(TextUtils.isEmpty(TvDate2.getText().toString())) {
                        TvDate2.setError("Enter Valid To Date ");
                    }

                    if(TextUtils.isEmpty(days.getText().toString())) {
                        days.setError("Enter Valid Days ");
                    }

                    if(TextUtils.isEmpty(reason.getText().toString())) {
                        reason.setError("Enter Reason of Leave ");
                    }

                }else
                {
                    AdminAsync adm = new AdminAsync();
                    adm.execute();
                }

            }

        }
     });
    }



    private class AdminAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "LeaveApply";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;

            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "insert");
                request.addProperty("userType_id", UsertypeId);
                request.addProperty("Type_id", "1");
                request.addProperty("User_id", UserId);
                request.addProperty("From_date",fromdate);
                request.addProperty("To_date",todate);
                request.addProperty("Reason", reasons);
                request.addProperty("days", noofdays);
                request.addProperty("adminApproval", 0);
                request.addProperty("school_id", school_id);
                request.addProperty("leavetype_id", "1");
                request.addProperty("year_id", Year_id);
                request.addProperty("UserName",NAme);
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
                                if(res.contains("true"))
                                {
                                    serviceresult="1";
                                    //Toast.makeText(getActivity(), "Leave Applied Sucessfully", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    serviceresult="0";
                                    //Toast.makeText(getActivity(), "Leave Applied UnSucessfully", Toast.LENGTH_LONG).show();
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
            this.dialog.dismiss();
            if(serviceresult.equalsIgnoreCase("1"))
            {
                Toast.makeText(getActivity(), "Leave Applied Sucessfully", Toast.LENGTH_LONG).show();
                TvDate1.setText("");
                TvDate2.setText("");
                reason.setText("");
                days.setText("");
            }else
            {
                Toast.makeText(getActivity(), "Leave Applied Sucessfully", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
            fromdate = TvDate1.getText().toString();
            todate = TvDate2.getText().toString();
            reasons = reason.getText().toString();
            noofdays = days.getText().toString();
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