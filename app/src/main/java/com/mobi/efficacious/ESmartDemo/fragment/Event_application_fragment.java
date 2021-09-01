package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Tracking_status_adapterMap;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
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
import java.util.HashMap;

public class Event_application_fragment extends Fragment {
    View myview;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String UsertypeId,flag;
    String UserId,Year_id;
    TextView EventStartDate,RegistrationStartDate;
    TextView EventEndDate,RegistrationEndDate;
    EditText Eventname,EventFees;
    EditText description;
    Spinner Standard_spinner;
    Button submit;
    private Calendar calendar;
    private int year, month, day;
    String fromdate;
    String todate,std_selected;
    Tracking_status_adapterMap adapter;
    HashMap<Object, Object> map;
    ConnectionDetector cd;
    String school_id,RegStartDate,RegEndDate,Event_StartDate,Event_EndDate,EventName,Event_Fees,Event_Description;
    private ArrayList<HashMap<Object, Object>> dataList;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_event_application, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        UsertypeId = settings.getString("TAG_USERTYPEID", "");
        UserId = settings.getString("TAG_USERID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        flag="1";
        EventStartDate = (TextView) myview.findViewById(R.id.date1_leave);
        EventEndDate = (TextView) myview.findViewById(R.id.date2_leave);
        RegistrationStartDate = (TextView) myview.findViewById(R.id.Registrationfrmdate);
        RegistrationEndDate = (TextView) myview.findViewById(R.id.Registrationtodate);
        Eventname = (EditText) myview.findViewById(R.id.edteventname);
        EventFees= (EditText) myview.findViewById(R.id.edteventfee);
        description = (EditText) myview.findViewById(R.id.edtdescription);
        submit = (Button) myview.findViewById(R.id.btnSubmit_leave);
        Standard_spinner=(Spinner)myview.findViewById(R.id.std_spinner);
        dataList = new ArrayList<HashMap<Object, Object>>();
        cd = new ConnectionDetector(getContext().getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            StudenStandardtAsync studenStandardtAsync = new StudenStandardtAsync();
            studenStandardtAsync.execute();
        }
        EventStartDate.setOnClickListener(new View.OnClickListener() {
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
                                EventStartDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        EventEndDate.setOnClickListener(new View.OnClickListener() {
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
                                EventEndDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        RegistrationStartDate.setOnClickListener(new View.OnClickListener() {
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
                                RegistrationStartDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        RegistrationEndDate.setOnClickListener(new View.OnClickListener() {
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
                                RegistrationEndDate.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        Standard_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Std_spinner=position;
                std_selected= String.valueOf(dataList.get(position).get("intstandard_id"));
                if(std_selected.contentEquals("--Select--"))
                {
                    Toast.makeText(getActivity(),"Select Standard", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"Please Select Standard",Toast.LENGTH_SHORT).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             RegStartDate=RegistrationStartDate.getText().toString();
             RegEndDate=RegistrationEndDate.getText().toString();
             Event_StartDate=EventStartDate.getText().toString();
             Event_EndDate=EventEndDate.getText().toString();
             Event_Fees=EventFees.getText().toString();
             if(Event_Fees.contentEquals(""))
             {
                 Event_Fees="0";
             }
             Event_Description=description.getText().toString();
             EventName=Eventname.getText().toString();
                if(!std_selected.contentEquals("--Select--")&&!RegStartDate.contentEquals("")&&!RegEndDate.contentEquals("")&&!Event_StartDate.contentEquals("")&&!Event_EndDate.contentEquals("")&&!EventName.contentEquals("")&&!Event_Description.contentEquals(""))
                {
                    SubmitASYNC sumbitASYNC=new SubmitASYNC(std_selected,RegStartDate,RegEndDate,Event_StartDate,Event_EndDate,Event_Fees,Event_Description,EventName);
                    sumbitASYNC.execute();
                }
                else
                {
                    if (std_selected.contentEquals("--Select--"))
                    {
                        setSpinnerError(Standard_spinner,"Select valid Standard ");
                    }
                    if(TextUtils.isEmpty(RegStartDate)) {
                        RegistrationStartDate.setError("Enter Valid Registration Start Date ");
                    }
                    if(TextUtils.isEmpty(RegEndDate)) {
                        RegistrationEndDate.setError("Enter Valid Registration End Date ");
                    }
                    if(TextUtils.isEmpty(Event_StartDate)) {
                        EventStartDate.setError("Enter Valid Event Start Date ");
                    }
                    if(TextUtils.isEmpty(Event_EndDate)) {
                        EventEndDate.setError("Enter Valid Event End Date ");
                    }
                    if(TextUtils.isEmpty(EventName)) {
                        Eventname.setError("Enter Valid Event Name ");
                    }
                    if(TextUtils.isEmpty(Event_Description)) {
                        description.setError("Enter Event Description ");
                    }

                }
//
            }
        });

        return myview;
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
    private class StudenStandardtAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            map = new HashMap<Object, Object>();
            map.put("intstandard_id", "--Select--");
            map.put("vchStandard_name", "--Select--");
            dataList.add(map);
            map = new HashMap<Object, Object>();
            map.put("intstandard_id", "AllStudent & AllTeacher");
            map.put("vchStandard_name", "AllStudent & AllTeacher");
            dataList.add(map);
            map = new HashMap<Object, Object>();
            map.put("intstandard_id", "AllStudent");
            map.put("vchStandard_name", "AllStudent");
            dataList.add(map);
            map = new HashMap<Object, Object>();
            map.put("intstandard_id", "AllTeacher");
            map.put("vchStandard_name", "AllTeacher");
            dataList.add(map);
            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
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
                                map = new HashMap<Object, Object>();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intstandard_id", str2.getProperty("intStandard_Id").toString().trim());
                                    map.put("vchStandard_name", str2.getProperty("vchStandard_name").toString().trim());
                                    dataList.add(map);
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
                dataList.clear();
                map = new HashMap<Object, Object>();
                map.put("intstandard_id", "--No Data Available--");
                map.put("vchStandard_name", "--No Data Available--");
                dataList.add(map);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new Tracking_status_adapterMap(getActivity(), dataList, "StandradName");
            Standard_spinner.setAdapter(adapter);
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
        private class SubmitASYNC extends AsyncTask<Void, Void, Void> {
            private final ProgressDialog dialog = new ProgressDialog(getActivity());
String vchStandrad,dtRegistrartionStartDate,dtRegistrationEndDate,dtEventStartDate,dtEventEndDate,vchEventFees,vchEventDescription,vchEventName;
            public SubmitASYNC(String std_selected, String regStartDate, String regEndDate, String event_startDate, String event_endDate, String event_fees, String event_description, String eventName) {
                vchStandrad=std_selected;
                dtRegistrartionStartDate=regStartDate;
                dtRegistrationEndDate=regEndDate;
                dtEventStartDate=event_startDate;
                dtEventEndDate=event_endDate;
                vchEventFees=event_fees;
                vchEventDescription=event_description;
                vchEventName=eventName;
            }

            protected Void doInBackground(Void... params) {
                OPERATION_NAME = "Event";
                SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
                final Responce responce = new Responce();
                SoapObject response = null;
                String result = null;
                try {
                    SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                    request.addProperty("command", "Insert");
                    request.addProperty("vchStandard_id",vchStandrad);
                    request.addProperty("intUser_id",UserId);
                    request.addProperty("intAcademic_id",Year_id);
                    request.addProperty("intUserType_id",UsertypeId);
                    request.addProperty("intSchool_id",school_id);
                    request.addProperty("dtRegistrartionStartDate",dtRegistrartionStartDate);
                    request.addProperty("dtRegistrationEndDate",dtRegistrationEndDate );
                    request.addProperty("dtEventStartDate",dtEventStartDate);
                    request.addProperty("dtEventEndDate",dtEventEndDate);
                    request.addProperty("vchEventName",vchEventName);
                    request.addProperty("vchEventFees",vchEventFees);
                    request.addProperty("vchEventDescription",vchEventDescription);
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
                    flag="0";
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.dialog.dismiss();
                if (flag.contentEquals("1") ) {
                    Toast.makeText(getActivity(), "Event Submitted Successfully", Toast.LENGTH_SHORT).show();
                    RegistrationStartDate.setText("");
                    RegistrationEndDate.setText("");
                    EventStartDate.setText("");
                    EventEndDate.setText("");
                    EventFees.setText("");
                    description.setText("");
                    Eventname.setText("");
                } else {
                    Toast.makeText(getActivity(), " UnSucessfull", Toast.LENGTH_LONG).show();

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

}
