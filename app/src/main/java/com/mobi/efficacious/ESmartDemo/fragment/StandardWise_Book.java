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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.Book_Adapter;
import com.mobi.efficacious.ESmartDemo.adapters.StandardAdapter;
import com.mobi.efficacious.ESmartDemo.entity.Book_library;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class StandardWise_Book extends Fragment implements SearchView.OnQueryTextListener {
    View myview;
    ProgressBar progressBar;
    ListView listView;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    Context mContext;
    String standard_id;
    String academic_id, role_id,userid,school_id;
    Book_Adapter adapter;
    ArrayList<Book_library> allbook=new ArrayList<Book_library>();
    SearchView searchView;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.activity_liabrary,null);
        searchView = (SearchView)myview.findViewById(R.id.search_view_lib);
        progressBar = (ProgressBar) myview.findViewById(R.id.liabrary_ProgressBar);
        listView  = (ListView) myview.findViewById(R.id.liabrary_list);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        userid = settings.getString("TAG_USERID", "");
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            standard_id=settings.getString("TAG_STANDERDID", "");
        }else if(role_id.contentEquals("3"))
        {
            standard_id= StandardAdapter.std_id;
        }else
        {

        }
//        mContext =StandardWise_Book.this;
//        Intent intent = getActivity().getIntent();
//        standard_id = intent.getStringExtra("std_id");
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
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "BookDetailsLibrary";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "StandardwiseBooks");
                request.addProperty("School_d", school_id);
                request.addProperty("standard_id", standard_id);
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
                                Book_library std =new Book_library();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "vchAccessionNo"))
                                    {
                                        std.setAccsion_no(str2.getProperty("vchAccessionNo").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchBookDetails_bookName"))
                                    {
                                        std.setBook_name(str2.getProperty("vchBookDetails_bookName").toString().trim());
                                    }
                                    if(isValidProperty(str2, "intBookPrice"))
                                    {
                                        std.setBook_price(str2.getProperty("intBookPrice").toString().trim());
                                    }
                                    if(isValidProperty(str2, "intBookEdition_id"))
                                    {
                                        std.setBook_edition(str2.getProperty("intBookEdition_id").toString().trim());
                                    }
                                    if(isValidProperty(str2, "intBook_Author_id"))
                                    {
                                        std.setBook_author(str2.getProperty("intBook_Author_id").toString().trim());
                                    }
                                    if(isValidProperty(str2, "intBookLanguage_id"))
                                    {
                                        std.setBook_lang(str2.getProperty("intBookLanguage_id").toString().trim());
                                    }



                                    allbook.add(std);


                                }

                            }

                        }

                        adapter = new Book_Adapter(getActivity(), allbook);

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
            listView.setTextFilterEnabled(true);
            this.dialog.dismiss();
            setupSearchView();
//            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
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
