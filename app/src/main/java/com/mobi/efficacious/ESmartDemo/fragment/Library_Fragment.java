package com.mobi.efficacious.ESmartDemo.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.LiabraryAdapter;
import com.mobi.efficacious.ESmartDemo.entity.Liabrary;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


/**
 * Created by EFF on 2/23/2017.
 */

public class Library_Fragment extends Fragment implements SearchView.OnQueryTextListener{
    View myview;
    Toolbar toolbar;
    ListView listView;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String student_id,school_id;
    Context mContext;
    //ArrayAdapter adapter;
    LiabraryAdapter adapter;
    ArrayList<Liabrary> liabrary=new ArrayList<Liabrary>();
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_liabrary,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        student_id = settings.getString("TAG_USERID", "");
        searchView = (SearchView)myview.findViewById(R.id.search_view_lib);
        progressBar = (ProgressBar)myview.findViewById(R.id.liabrary_ProgressBar);
        listView  = (ListView) myview.findViewById(R.id.liabrary_list);
        LoginAsync login = new LoginAsync();
        login.execute();
        return myview;
    }


    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Book Name Here");
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

            OPERATION_NAME = "StudentBookAssigned";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("stud_id", student_id);
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
                                Liabrary lib =new Liabrary();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intBook_assign_id"))
                                    {
                                        lib.setBookassignId(str2.getProperty("intBook_assign_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "intstandard_id"))
                                    {
                                        lib.setStandardId(str2.getProperty("intstandard_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "intDivision_id"))
                                    {
                                        lib.setDivisionId(str2.getProperty("intDivision_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "intStudent_id"))
                                    {
                                        lib.setStudentId(str2.getProperty("intStudent_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "intBookDetails_id"))
                                    {
                                        lib.setBookDetail(str2.getProperty("intBookDetails_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtAssigned_Date"))
                                    {
                                        lib.setAssignedDate(str2.getProperty("dtAssigned_Date").toString().trim());
                                    }

                                    if(isValidProperty(str2, "dtReturn_date"))
                                    {
                                        lib.setReturnDate(str2.getProperty("dtReturn_date").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchStatus"))
                                    {
                                        lib.setStatus(str2.getProperty("vchStatus").toString().trim());
                                    }

                                    liabrary.add(lib);
                                }

                            }

                        }

                        adapter = new LiabraryAdapter(mContext, liabrary);

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
            listView.setTextFilterEnabled(true);
            setupSearchView();
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

