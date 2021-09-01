package com.mobi.efficacious.ESmartDemo.dialogbox;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.vehicleOption_DialogboxAdapterMap;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.entity.dialog_boxMap;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class VehicleOption_DialogboxMap extends Dialog {


    public VehicleOption_DialogboxMap(@NonNull Context context) {
        super(context);
    }
    ArrayAdapter adapter;
    ArrayList<dialog_boxMap> all_vehicle=new ArrayList<dialog_boxMap>();
    ListView listView;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_boxmap);
        listView = (ListView) findViewById(R.id.notificationlistView);
        cd = new ConnectionDetector(getContext().getApplicationContext());
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            AllVehicle holidayAsync = new AllVehicle();
            holidayAsync.execute();
        }
    }

    private class AllVehicle extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            OPERATION_NAME = "LiveTrackingStatus";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "NoOfVehicle");
//                request.addProperty("standard", Standard_id);
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
                                dialog_boxMap std =new dialog_boxMap();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "intVehicle_id"))
                                    {
                                        std.setVehicle_id(str2.getProperty("intVehicle_id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "vchVehicleNo"))
                                    {
                                        std.setVehicle_no(str2.getProperty("vchVehicleNo").toString().trim());
                                    }

                                    all_vehicle.add(std);
                                }
                            }
                        }
                        adapter=new vehicleOption_DialogboxAdapterMap(getContext(),all_vehicle);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                dialog_boxMap distance =new dialog_boxMap();
                distance.setVehicle_no("No Data Available");
                distance.setVehicle_id("No Data Available");
                all_vehicle.add(distance);
                adapter=new vehicleOption_DialogboxAdapterMap(getContext(),all_vehicle);
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
