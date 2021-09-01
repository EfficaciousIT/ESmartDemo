package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.MapActivity.Tracking_Status_Map;
import com.mobi.efficacious.ESmartDemo.MapActivity.Tracking_Status_Replay_Map;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Tracking_Status_fragmentMap extends Fragment {
    String[] Month_name;
    String[] nodata;
    Spinner Month_spinner,Date_spinner,Vehicle_spinner;
    int Month_selected,staus;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    Tracking_status_adapterMap adapter;
    Button View_Map;
    HashMap<Object, Object> map;
    String vehicle_selectedId,Date_selected;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList2;
View myView;
String pageName;

    ConnectionDetector cd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      myView=inflater.inflate(R.layout.tracking_status_activitymap,null);
        Month_spinner=(Spinner)myView.findViewById(R.id.spinner_month);
        Date_spinner=(Spinner)myView.findViewById(R.id.spinner_date);
        Vehicle_spinner=(Spinner)myView.findViewById(R.id.spinner_vehicle);
        View_Map=(Button)myView.findViewById(R.id.button_viewmap);
        Month_name= getResources().getStringArray(R.array.month_name);
        cd = new ConnectionDetector(getContext().getApplicationContext());

        pageName=getArguments().getString("pagename");
        if(pageName.contentEquals("Tracking Status Replay")||pageName.contentEquals("Tracking Status"))
        {
            View_Map.setText("View Map");
        }
        else
        {
            View_Map.setText("Detail");
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,Month_name);
        Month_spinner.setAdapter(ad);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Month_selected=i;

                if(Month_selected==0)
                {
                    Toast.makeText(getActivity(),"Please select Month", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                        VehicleAsync vehicleAsync = new VehicleAsync();
                        vehicleAsync.execute();
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Vehicle_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int Vehiclespinner=i;
                vehicle_selectedId= String.valueOf(dataList.get(i).get("VehicleId"));
                if(vehicle_selectedId.contentEquals("--No Data Available--"))
                {
                    Toast.makeText(getActivity(),"NO Data available for this Month", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                        DateAsync dateAsync = new DateAsync(vehicle_selectedId);
                        dateAsync.execute();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int date_selection=i;
                Date_selected= String.valueOf(dataList2.get(i).get("date"));
                if(Date_selected.contentEquals("--No Data Available--"))
                {
                    Toast.makeText(getActivity(),"NO Data available for this Month", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        View_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (vehicle_selectedId.contentEquals("--No Data Available--") || Date_selected.contentEquals("--No Data Available--")) {
                        Toast.makeText(getActivity(), "Please Select Proper Data", Toast.LENGTH_LONG).show();
                    } else {
                        String string = Date_selected;
                        String[] parts = string.split("/");
                        String date = parts[0]; // 004
                        String month = parts[1];
                        String year = parts[2];
                        String date_selected = (date + "/" + month + "/" + year);
                        if(pageName.contentEquals("Tracking Status Replay"))
                        {
                            Intent intent = new Intent(getActivity(), Tracking_Status_Replay_Map.class);
                            intent.putExtra("DateSelected", date_selected);
                            intent.putExtra("vehicle_selectedId",vehicle_selectedId);
                            startActivity(intent);
                        }else if(pageName.contentEquals("Tracking Status"))
                        {
                            Intent intent = new Intent(getActivity(), Tracking_Status_Map.class);
                            intent.putExtra("DateSelected", date_selected);
                            intent.putExtra("vehicle_selectedId",vehicle_selectedId);
                            startActivity(intent);
                        }
                       else
                        {

                        }

                    }
                }catch (Exception ex)
                {
                    Toast.makeText(getActivity(), "Please Select Proper Data", Toast.LENGTH_LONG).show();
                }
            }
        });
        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        return myView;
    }

    private class VehicleAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            staus=0;
            OPERATION_NAME = "LiveTrackingStatus";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "VehicleOfMonth");
                request.addProperty("Monthno", Month_selected);

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
                                map = new HashMap<Object, Object>();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map.put("VehicleNo", str2.getProperty("vchVehicleNo").toString().trim());
                                    map.put("VehicleId", str2.getProperty("intVehicle_id").toString().trim());
                                    dataList.add(map);

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
                staus=1;
                map = new HashMap<Object, Object>();
                map.put("VehicleNo","--No Data Available--");
                map.put("VehicleId","--No Data Available--" );
                dataList.add(map);

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(staus==1)
            {
                dataList2.clear();
                map = new HashMap<Object, Object>();
                map.put("date","--No Data Available--");
                dataList2.add(map);
                adapter = new Tracking_status_adapterMap(getActivity(), dataList2,"DAte");
                Date_spinner.setAdapter(adapter);

            }
            adapter = new Tracking_status_adapterMap(getActivity(), dataList,"VehicleName");
            Vehicle_spinner.setAdapter(adapter);



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

    private class DateAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String vehicle_id;
        public DateAsync(String vehicle_selectedId) {
            vehicle_id=vehicle_selectedId;
        }

        @Override
        protected Void doInBackground(Void... params) {

            dataList2.clear();
            OPERATION_NAME = "LiveTrackingStatus";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "VehicleDateofMaonth");
                request.addProperty("vehicle_id", vehicle_id);
                request.addProperty("Monthno", Month_selected);

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
                                map = new HashMap<Object, Object>();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {

                                    map.put("date", str2.getProperty("dtdatetime").toString().trim());
                                    dataList2.add(map);

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
            adapter = new Tracking_status_adapterMap(getActivity(), dataList2,"DAte");
            Date_spinner.setAdapter(adapter);
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
    public void viewmap()
    {

    }
}
