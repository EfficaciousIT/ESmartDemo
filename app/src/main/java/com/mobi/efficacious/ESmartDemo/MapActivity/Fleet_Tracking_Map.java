package com.mobi.efficacious.ESmartDemo.MapActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.CustomInfoWindowAdapterMap;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Fleet_Tracking_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    int staus,handlerstatus=0;
    Double latitude_startpoint,longitude_startpoint;
    SupportMapFragment mapFragment;
    Geocoder geocoder;
    List<Address> addresses;
    String address_vehicle,vehicleno,markertitle;
    ConnectionDetector cd;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fleet_status_map);
        dataList = new ArrayList<HashMap<Object, Object>>();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cd = new ConnectionDetector(getApplicationContext());

        staus = 0;
        if (!cd.isConnectingToInternet())
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            TrackingData trackingData = new TrackingData();
            trackingData.execute();
        }

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (dataList.size() == 0) {
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Fleet_Tracking_Map.this);
            alert.setMessage("No Data Available");
            alert.setPositiveButton("OK", null);
            alert.show();

        } else {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions polyLineOptions = new PolylineOptions();
            for (int i = 0; i <dataList.size() ; i++)
            {

                latitude_startpoint = Double.parseDouble(dataList.get(i).get("latitude").toString());
                longitude_startpoint = Double.parseDouble(dataList.get(i).get("longitude").toString());
                geocoder = new Geocoder(Fleet_Tracking_Map.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude_startpoint, longitude_startpoint, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address_vehicle = addresses.get(0).getAddressLine(0);
                vehicleno=dataList.get(i).get("vehicle_no").toString();
                markertitle=(addresses.get(0).getAddressLine(0));
//                LatLng startpoint = new LatLng(latitude_startpoint, longitude_startpoint);
                MarkerOptions markerOpt = new MarkerOptions();
                markerOpt.position(new LatLng(latitude_startpoint,longitude_startpoint))
                        .title(markertitle)
                        .snippet("Vehicle No:"+vehicleno)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2));

                CustomInfoWindowAdapterMap adapter = new CustomInfoWindowAdapterMap(Fleet_Tracking_Map.this);
                mMap.setInfoWindowAdapter(adapter);

                mMap.addMarker(markerOpt);
            }

            staus=1;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_startpoint,longitude_startpoint), 10.5f), 4000, null);
//            geocoder = new Geocoder(Fleet_Tracking_Map.this, Locale.getDefault());

//            try {
//                addresses = geocoder.getFromLocation(latitude_startpoint, longitude_startpoint, 1);
//                address_vehicle = addresses.get(0).getAddressLine(0);
//                VehicleNotv.setText(dataList.get(0).get("vehicle_no").toString());
//                Addresstv.setText(address_vehicle.toString());
//                if(dataList.get(0).get("Speed").toString().contentEquals("0"))
//                {
//                    Speedtv.setText("Stop(0)");
//                }else
//                {
//                    Speedtv.setText("Running (" +dataList.get(0).get("Speed").toString()+")");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }



            mMap.setMyLocationEnabled(true);

        }
    }


    private class TrackingData extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Fleet_Tracking_Map.this);

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            OPERATION_NAME = "LiveTrackingStatus";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "FleetStatus");
//                request.addProperty("Vehicle_id", vehicle_selectedId);
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
                                    map.put("latitude", str2.getProperty("lat").toString().trim());
                                    map.put("longitude", str2.getProperty("lng").toString().trim());
                                    map.put("Speed", str2.getProperty("Speed").toString().trim());
                                    map.put("vehicle_no", str2.getProperty("vehicle_no").toString().trim());
                                    dataList.add(map);
                                }

                            }

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                if(handlerstatus==1)
                {
                    handlerstatus=1;
                }else
                {
                    handlerstatus=0;
                }

                e.printStackTrace();
            }

            return null;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            if(staus==0)
            {
                if(dataList.size()>0)
                {
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("Processing...");
                    dialog.show();
                }

            }


        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (staus == 0) {

                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(Fleet_Tracking_Map.this);
                if(dataList.size()>0) {
                    handlerstatus=1;
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!cd.isConnectingToInternet()) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK", null);
                                alert.show();
                            } else {
                                TrackingData trackingData = new TrackingData();
                                trackingData.execute();
                            }
                        }
                    }, 15000);
                }
                if(dataList.size()>0) {
                    dialog.dismiss();
                }
            }
            else {
                livetrack();

            }

        }
        public void livetrack() {
            mMap.clear();
            if (dataList.size() == 0) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Fleet_Tracking_Map.this);
                alert.setMessage("No Data Available");
                alert.setPositiveButton("OK", null);
                alert.show();

            } else {
                ArrayList<LatLng> points = new ArrayList<LatLng>();
                PolylineOptions polyLineOptions = new PolylineOptions();
                for (int i = 0; i <dataList.size() ; i++)
                {
                    latitude_startpoint = Double.parseDouble(dataList.get(i).get("latitude").toString());
                    longitude_startpoint = Double.parseDouble(dataList.get(i).get("longitude").toString());
//                    LatLng startpoint = new LatLng(latitude_startpoint, longitude_startpoint);
                    geocoder = new Geocoder(Fleet_Tracking_Map.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(latitude_startpoint, longitude_startpoint, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address_vehicle = addresses.get(0).getAddressLine(0);
                    vehicleno=dataList.get(i).get("vehicle_no").toString();
                    markertitle=(addresses.get(0).getAddressLine(0));
                    MarkerOptions markerOpt = new MarkerOptions();
                    markerOpt.position(new LatLng(latitude_startpoint,longitude_startpoint))
                            .title(markertitle)
                            .snippet("Vehicle No:"+vehicleno)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2));

                    CustomInfoWindowAdapterMap adapter = new CustomInfoWindowAdapterMap(Fleet_Tracking_Map.this);
                    mMap.setInfoWindowAdapter(adapter);

                    mMap.addMarker(markerOpt);
                }
//                Double latitude_startpoint = Double.parseDouble(dataList.get(0).get("latitude").toString());
//                Double longitude_startpoint = Double.parseDouble(dataList.get(0).get("longitude").toString());
//                LatLng startpoint = new LatLng(latitude_startpoint, longitude_startpoint);
//                mMap.addMarker(new MarkerOptions()
//                        .position(startpoint)
//                        .title("Current Location")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2))
//                );
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startpoint, 16));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_startpoint,longitude_startpoint), 10.5f), 4000, null);
//                geocoder = new Geocoder(Fleet_Tracking_Map.this, Locale.getDefault());

//                try {
//                    addresses = geocoder.getFromLocation(latitude_startpoint, longitude_startpoint, 1);
//                    address_vehicle = addresses.get(0).getAddressLine(0);
//                    VehicleNotv.setText(dataList.get(0).get("vehicle_no").toString());
//                    Addresstv.setText(address_vehicle.toString());
//                    if(dataList.get(0).get("Speed").toString().contentEquals("0"))
//                    {
//                        Speedtv.setText("Stop(0)");
//                    }else
//                    {
//                        Speedtv.setText("Running (" +dataList.get(0).get("Speed").toString()+")");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
            if(dataList.size()>0) {
                handler = new Handler();
                handlerstatus=1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!cd.isConnectingToInternet()) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                            alert.setMessage("No Internet Connection");
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        } else {
                            TrackingData trackingData = new TrackingData();
                            trackingData.execute();
                        }
                    }
                }, 15000);
            }
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        if(handlerstatus==1)
        {
            handler.removeCallbacksAndMessages(null);
        }
        this.finish();
        this.finish();
    }

}
