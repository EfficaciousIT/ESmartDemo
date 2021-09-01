package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.GalleryAdapter;
import com.mobi.efficacious.ESmartDemo.dialogbox.Gallery_dialogBox;
import com.mobi.efficacious.ESmartDemo.entity.Gallery;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Gallery_fragment extends Fragment {
    View myview;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    String attachmentFile,school_id;
    Long imageSize;
    String path,role_id;
    ImageView profileimg;
    Uri URI = null;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    int uploadstatus=0;
    GridView imagesgrid;
    GalleryAdapter adapter;
    ArrayList<Gallery> allimages=new ArrayList<Gallery>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.gallery_fragment,null);
        imagesgrid=(GridView)myview.findViewById(R.id.imagesgrid);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        FloatingActionButton fab = (FloatingActionButton)myview. findViewById(R.id.fab1);
        if(role_id.contentEquals("1")||role_id.contentEquals("2")||role_id.contentEquals("3"))
        {
            fab.setVisibility(View.GONE);
        }else
        {
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

               Intent intent=new Intent(getActivity(),Gallery_dialogBox.class);
               startActivity(intent);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
            }
        });
        GalleryAsync galleryAsync=new GalleryAsync();
        galleryAsync.execute();
        return myview;
    }
    private class GalleryAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            allimages.clear();
            OPERATION_NAME = "SchoolName";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectGallery");
                request.addProperty("School_id", school_id);
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
                                Gallery images =new Gallery();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "Name"))
                                    {
                                        images.setImageName(str2.getProperty("Name").toString().trim());

                                    }
                                    if(isValidProperty(str2, "EventDescription"))
                                    {
                                        images.setImageDescription(str2.getProperty("EventDescription").toString().trim());

                                    }
                                    if(isValidProperty(str2, "Uploadedfrom"))
                                    {
                                        images.setUplaoedFrom(str2.getProperty("Uploadedfrom").toString().trim());

                                    }
//                                    if(str2.getProperty("Uploadedfrom").toString().trim().contentEquals("web"))
//                                    {
//                                        if(isValidProperty(str2, "image"))
//                                        {
//                                            images.setImageBase64(str2.getProperty("image").toString().trim());
//
//                                        }
//
//                                    }
                                    allimages.add(images);
                                }

                            }

                        }
                        adapter = new GalleryAdapter(getActivity(),allimages);

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
            imagesgrid.setAdapter(adapter);
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading Gallery...");
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
