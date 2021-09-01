package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.EventListAdapter;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.Event;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Event_participate_fragment extends Fragment {
    View myview;
    ListView Listview;
    ArrayAdapter adapter;
    ArrayList<Event> diary = new ArrayList<Event>();
    ConnectionDetector cd;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String  Year_id;
    String userid,school_id, role_id, value,vchstandard_id,vchDivisionId;
String pagename;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_leavelist, null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        Listview = (ListView) myview.findViewById(R.id.leavelist_list);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        pagename="EventParticipated";
        role_id = settings.getString("TAG_USERTYPEID", "");
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {
            vchstandard_id = settings.getString("TAG_STANDERDID", "");
            vchDivisionId = settings.getString("TAG_DIVISIONID", "");
            userid = settings.getString("TAG_USERID", "");
            Year_id = settings.getString("TAG_ACADEMIC_ID", "");
            value = "Event";
            EventAsync adm = new EventAsync();
            adm.execute();
        }

        return myview;
    }

    private class EventAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "EventList";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                if(role_id.contentEquals("1")||role_id.contentEquals("2"))
                {
                    request.addProperty("command", "SelectEventParticipatedByStudent");
                    request.addProperty("vchstandard_id", vchstandard_id);
                    request.addProperty("intDivision_id", vchDivisionId);
                    request.addProperty("intUser_id", userid);
                    request.addProperty("intUserType_id",role_id);
                    request.addProperty("school_id",school_id);
                    request.addProperty("academic_id",Year_id);

                }else if(role_id.contentEquals("3"))
                {
                    request.addProperty("command", "SelectEventParticipatedByTeacher");
                    request.addProperty("school_id",school_id);
                    request.addProperty("intUser_id", userid);
                    request.addProperty("intUserType_id",role_id);
                    request.addProperty("academic_id",Year_id);
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
                                Event event = new Event();
                                if (res.contains("No record found")) {

                                } else {
                                    if (isValidProperty(str2, "dtRegistrartionStartDate")) {
                                        event.setDtRegistrartionStartDate(str2.getProperty("dtRegistrartionStartDate").toString().trim());
                                    }

                                    if (isValidProperty(str2, "dtRegistrationEndDate")) {
                                        event.setDtRegistrationEndDate(str2.getProperty("dtRegistrationEndDate").toString().trim());
                                    }

                                    if (isValidProperty(str2, "dtEventStartDate")) {
                                        event.setDtEventStartDate(str2.getProperty("dtEventStartDate").toString().trim());
                                    }

                                    if (isValidProperty(str2, "dtEventEndDate")) {
                                        event.setDtEventEndDate(str2.getProperty("dtEventEndDate").toString().trim());
                                    }

                                    if (isValidProperty(str2, "vchEventName")) {
                                        event.setVchEventName(str2.getProperty("vchEventName").toString().trim());
                                    }
                                    if (isValidProperty(str2, "vchEventFees")) {
                                        event.setVchEventFees(str2.getProperty("vchEventFees").toString().trim());
                                    }
                                    if (isValidProperty(str2, "vchEventDescription")) {
                                        event.setVchEventDescription(str2.getProperty("vchEventDescription").toString().trim());
                                    }
                                    if (isValidProperty(str2, "intEvent_id")) {
                                        event.setEvent_id(str2.getProperty("intEvent_id").toString().trim());
                                    }
                                    diary.add(event);
                                }

                            }

                        }

                        adapter = new EventListAdapter(getActivity(),diary,pagename);

                    } else {
                        Toast.makeText(getActivity(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Listview.setAdapter(adapter);
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