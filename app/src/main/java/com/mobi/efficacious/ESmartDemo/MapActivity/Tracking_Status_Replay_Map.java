package com.mobi.efficacious.ESmartDemo.MapActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class Tracking_Status_Replay_Map extends FragmentActivity implements OnMapReadyCallback {
    String dateSelected,vehicle_selectedId,CurrDateTime;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList1;
    TextView vehiclenao,distance1,average;
    int status,handlerstatus=0;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyLine;
    private Handler handler;
    private Marker marker;
    private int index, next;
    private LatLng startPosition, endPosition;
    private float v;
    private double lat, lng;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackin_status_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        CurrDateTime = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        vehiclenao=(TextView)findViewById(R.id.vehicle_no1) ;
        distance1=(TextView)findViewById(R.id.vehicle_speed1) ;
        average=(TextView)findViewById(R.id.vehicle_average1) ;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList1 = new ArrayList<HashMap<Object, Object>>();
        status=1;
        cd = new ConnectionDetector(getApplicationContext());
        Intent intent=getIntent();
        dateSelected=intent.getStringExtra("DateSelected");
        vehicle_selectedId=intent.getStringExtra("vehicle_selectedId");
        if (!cd.isConnectingToInternet())
        {
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK",null);
            alert.show();
        }else {
            TrackingData trackingData = new TrackingData();
            trackingData.execute();

//            Vehicledata trackingData1 = new Vehicledata();
//            trackingData1.execute();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
        if(dataList.size()==0)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Tracking_Status_Replay_Map.this);
            alert.setMessage("No Data Available");
            alert.setPositiveButton("OK",null);
            alert.show();
            LatLng jibreellatlong = new LatLng(22.601460,  88.383739);
            mMap.addMarker(new MarkerOptions()
                    .position(jibreellatlong)
                    .title("Kolkata")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapsource))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jibreellatlong, 15));
        }
        else if(dataList.size()==1)
        {
            Double latitude_startpoint = Double.parseDouble(dataList.get(0).get("latitude").toString());
            Double longitude_startpoint = Double.parseDouble(dataList.get(0).get("longitude").toString());
            LatLng startpoint = new LatLng(latitude_startpoint, longitude_startpoint);
            mMap.addMarker(new MarkerOptions()
                    .position(startpoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2))
            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_startpoint,longitude_startpoint), 15.5f), 4000, null);
        }
        else {
            mMap.clear();
            final ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions polyLineOptions = new PolylineOptions();
            Double latitude_startpoint = Double.parseDouble(dataList.get(0).get("latitude").toString());
            Double longitude_startpoint = Double.parseDouble(dataList.get(0).get("longitude").toString());
            LatLng startpoint = new LatLng(latitude_startpoint, longitude_startpoint);
            mMap.addMarker(new MarkerOptions()
                    .position(startpoint)
                    .title("Start Point")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapsource))
            );
            int last_point = (dataList.size() - 1);
            Double latitude_endpoint = Double.parseDouble(dataList.get(last_point).get("latitude").toString());
            Double longitude_endpoint = Double.parseDouble(dataList.get(last_point).get("longitude").toString());
            LatLng endpoint = new LatLng(latitude_endpoint, longitude_endpoint);
            mMap.addMarker(new MarkerOptions()
                    .position(endpoint)
                    .title("End Point")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapdestination))
            );
            for (int i = 0; i < dataList.size(); i++) {
                Double latitude = Double.parseDouble(dataList.get(i).get("latitude").toString());
                Double longitude = Double.parseDouble(dataList.get(i).get("longitude").toString());
                String duration=dataList.get(i).get("Duration_stopage").toString();
                String FromDateTime=dataList.get(i).get("fromDt").toString();
                String ToDateTime=dataList.get(i).get("ToDt").toString();
                String titile=("From :"+FromDateTime+"\nTo :"+ToDateTime+"\nDuration :"+duration);
                if(duration.contentEquals("0")||duration.contentEquals("0:0"))
                {

                }
                else {

                    LatLng stopagepoint = new LatLng(latitude, longitude);
                    MarkerOptions markerOpt = new MarkerOptions();
                    markerOpt.position(new LatLng(latitude,longitude))
                            .title(titile)
                            .snippet("")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker));

                    //Set Custom InfoWindow Adapter
                    CustomInfoWindowAdapterMap adapter = new CustomInfoWindowAdapterMap(Tracking_Status_Replay_Map.this);
                    mMap.setInfoWindowAdapter(adapter);

                    mMap.addMarker(markerOpt);
                }
                points.add(new LatLng(latitude, longitude));
            }

//            for (int i = 0; i < dataList.size()-1; i++) {
//                Double latitude = Double.parseDouble(dataList.get(i).get("latitude").toString());
//                Double longitude = Double.parseDouble(dataList.get(i).get("longitude").toString());
//                Double latitude1 = Double.parseDouble(dataList.get(i+1).get("latitude").toString());
//                Double longitude1 = Double.parseDouble(dataList.get(i+1).get("longitude").toString());
//                points.add(new LatLng(latitude, longitude));
//                LatLng endpoint1 = new LatLng(latitude1, longitude1);
//                Marker marker = mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(latitude, longitude))
//                        .title("San Francisco")
//                        .snippet("Population: 776733"));
//                animateMarker(endpoint1,marker);
//            }

//            polyLineOptions.width(7 * 1);
//            polyLineOptions.geodesic(true);
//            polyLineOptions.color(getApplicationContext().getResources().getColor(R.color.darkblue));
//            polyLineOptions.addAll(points);
//            Polyline polyline = mMap.addPolyline(polyLineOptions);
//            polyline.setGeodesic(true);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_endpoint,longitude_endpoint), 15.5f), 4000, null);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startpoint, 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_startpoint,longitude_startpoint), 15.5f), 4000, null);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.GRAY);
            polylineOptions.width(5);
            polylineOptions.startCap(new SquareCap());
            polylineOptions.endCap(new SquareCap());
            polylineOptions.jointType(ROUND);
            polylineOptions.addAll(points);
            greyPolyLine = mMap.addPolyline(polylineOptions);

            blackPolylineOptions = new PolylineOptions();
            blackPolylineOptions.width(5);
            blackPolylineOptions.color(Color.BLACK);
            blackPolylineOptions.startCap(new SquareCap());
            blackPolylineOptions.endCap(new SquareCap());
            blackPolylineOptions.jointType(ROUND);
            blackPolyline = mMap.addPolyline(blackPolylineOptions);

            mMap.addMarker(new MarkerOptions()
                    .position(points.get(points.size() - 1)));

            ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
            polylineAnimator.setDuration(2000);
            polylineAnimator.setInterpolator(new LinearInterpolator());
            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    List<LatLng> points = greyPolyLine.getPoints();
                    int percentValue = (int) valueAnimator.getAnimatedValue();
                    int size = points.size();
                    int newPoints = (int) (size * (percentValue / 100.0f));
                    List<LatLng> p = points.subList(0, newPoints);
                    blackPolyline.setPoints(p);
                }
            });
            polylineAnimator.start();
            marker = mMap.addMarker(new MarkerOptions().position(startpoint)
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2)));
            handler = new Handler();
            index = -1;
            next = 1;
            handlerstatus=1;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index < points.size() - 1) {
                        index++;
                        next = index + 1;
                    }
                    if (index < points.size() - 1) {
                        startPosition = points.get(index);
                        endPosition = points.get(next);
                    }
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                    valueAnimator.setDuration(3000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            v = valueAnimator.getAnimatedFraction();
                            lng = v * endPosition.longitude + (1 - v)
                                    * startPosition.longitude;
                            lat = v * endPosition.latitude + (1 - v)
                                    * startPosition.latitude;
                            LatLng newPos = new LatLng(lat, lng);
                            marker.setPosition(newPos);
                            marker.setAnchor(0.5f, 0.5f);
                            marker.setRotation(getBearing(startPosition, newPos));
                            mMap.moveCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(newPos)
                                                    .zoom(15.5f)
                                                    .build()));
                        }
                    });
                    valueAnimator.start();
                    handler.postDelayed(this, 3000);
                }
            }, 3000);

        }
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
//        if(status==0 && dateSelected.contentEquals(CurrDateTime)) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    TrackingData trackingData = new TrackingData();
//                    trackingData.execute();
//                }
//            }, 15000);
//        }
        mMap.setMyLocationEnabled(true);

    }
    private class TrackingData extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Tracking_Status_Replay_Map.this);

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            OPERATION_NAME = "TrackingStatusMap";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "select");
                request.addProperty("intVehicle_id", vehicle_selectedId);
                request.addProperty("dateselected", dateSelected);
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
                                    map.put("fromDt", str2.getProperty("fromDt").toString().trim());
                                    map.put("ToDt", str2.getProperty("ToDt").toString().trim());
                                    map.put("Duration_stopage", str2.getProperty("Dur").toString().trim());
                                    map.put("latitude", str2.getProperty("lat").toString().trim());
                                    map.put("longitude", str2.getProperty("lng").toString().trim());
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(Tracking_Status_Replay_Map.this);
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

    private class Vehicledata extends AsyncTask<Void, Void, Void> {
        //        private final ProgressDialog dialog = new ProgressDialog(Tracking_Status_Replay_Map.this);
        @Override
        protected Void doInBackground(Void... params) {
            dataList1.clear();
            OPERATION_NAME = "TrackingStatusVehicledetail";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectDist");
                request.addProperty("Vehicle_id", vehicle_selectedId);
                request.addProperty("date", dateSelected);
                //request.addProperty("endate", dateSelected);
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

                                    map.put("Vehicle No.", str2.getProperty("Vehicle_x0020_No.").toString().trim());
                                    map.put("Distance (KM)", str2.getProperty("Distance_x0020__x0028_KM_x0029_").toString().trim());
                                    map.put("Average Speed", str2.getProperty("Average_x0020_Speed").toString().trim());
                                    dataList1.add(map);

                                }

                            }

                        }


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
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
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if(status==1)
//            {
//                this.dialog.dismiss();
//            }
            try
            {
                vehiclenao.setText(dataList1.get(0).get("Vehicle No.").toString());
                distance1.setText(dataList1.get(0).get("Distance (KM)").toString());
                average.setText(dataList1.get(0).get("Average Speed").toString());
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
////            if(status==1)
////            {
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setMessage("Processing...");
//                dialog.show();
////            }

        }
    }
//    private void animateMarker(LatLng latlng, final Marker marker){
//        final LatLng target = latlng;
//
//        final long duration = 800;
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = mMap.getProjection();
//
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//
//        final Interpolator interpolator = new LinearInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
//                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//                if (t < 1.0) {
//                    // Post again 10ms later.
//                    handler.postDelayed(this, 10);
//                } else {
//                    // animation ended
//                }
//            }
//        });
//    }

    public void onBackPressed() {
        super.onBackPressed();
        if(handlerstatus==1)
        {
            handler.removeCallbacksAndMessages(null);
        }
        this.finish();
    }
//    private List<LatLng> decodePoly(String encoded) {
//        List<LatLng> poly = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}



