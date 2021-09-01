package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.TeacherApprovalAdapter;
import com.mobi.efficacious.ESmartDemo.entity.LeaveApproval;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class TeacherApprovalFragment extends Fragment {
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<LeaveApproval> leave=new ArrayList<LeaveApproval>();
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String user_id;
    String UserType_id;
    String School_id;
    String Year_id;
    public TeacherApprovalFragment() {
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
        return inflater.inflate(R.layout.fragment_teacherapproval, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        user_id = settings.getString("TAG_USERID", "");
        UserType_id = settings.getString("TAG_USERTYPEID", "");
        Year_id = settings.getString("TAG_ACADEMIC_ID", "");
        School_id = settings.getString("TAG_SCHOOL_ID", "");
        progressBar = (ProgressBar) getActivity().findViewById(R.id.teacherapproval_ProgressBar);
        listView = (ListView) getActivity().findViewById(R.id.teacherapproval_list);
        AdminAsync adm = new AdminAsync();
        adm.execute();
    }

    private class AdminAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "TeacherLeaveDetails";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "SelectTeacher");
                request.addProperty("intSchool_id",School_id);
               // request.addProperty("school_id",School_id);

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
                                LeaveApproval Leave =new LeaveApproval();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intLeaveApplocation_id"))
                                    {
                                        Leave.setLeaveApp_id(str2.getProperty("intLeaveApplocation_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtFrom_date"))
                                    {
                                        Leave.setFrom_date(str2.getProperty("dtFrom_date").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtTo_Date"))
                                    {
                                        Leave.setTo_Date(str2.getProperty("dtTo_Date").toString().trim());
                                    }

                                    if(isValidProperty(str2, "intTotalDays"))
                                    {
                                        Leave.setTotalDays(str2.getProperty("intTotalDays").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchReason"))
                                    {
                                        Leave.setReason(str2.getProperty("vchReason").toString().trim());
                                    }

                                    if(isValidProperty(str2, "bitAdminApproval"))
                                    {
                                        Leave.setAdminApproval(str2.getProperty("bitAdminApproval").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchProfile"))
                                    {
                                        Leave.setTeacherProfile(str2.getProperty("vchProfile").toString().trim());
                                    }
                                    if(isValidProperty(str2, "intTeacher_id"))
                                    {
                                        Leave.setIntTeacher_id(str2.getProperty("intTeacher_id").toString().trim());
                                    }
                                    if(res.contains("Name"))
                                    {
                                        if(isValidProperty(str2, "Name"))
                                        {
                                            Leave.setName(str2.getProperty("Name").toString().trim());
                                        }
                                    }else
                                    {
                                        Leave.setName("NA");
                                    }

                                    leave.add(Leave);
                                }

                            }

                        }

                        adapter = new TeacherApprovalAdapter(getActivity(), leave);

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
            progressBar.setVisibility(View.GONE);
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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

}