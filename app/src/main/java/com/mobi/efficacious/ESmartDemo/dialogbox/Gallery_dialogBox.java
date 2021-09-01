package com.mobi.efficacious.ESmartDemo.dialogbox;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.Gallery;
import com.mobi.efficacious.ESmartDemo.fragment.Gallery_fragment;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Gallery_dialogBox  extends Activity {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    EditText EventDescriptin_et;
    Button btnsave,btnCancel;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    String attachmentFile;
    Long imageSize;
    String path,role_id;
    ImageView profileimg;
    Uri URI = null;
    int uploadstatus=0;
    String FileName,academic_id,school_id;
    String base64String="";
    ArrayList<Gallery> allimages=new ArrayList<Gallery>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gallery_dialogbox);
        EventDescriptin_et=(EditText)findViewById(R.id.editText288);
        btnsave=(Button) findViewById(R.id.btnsave);
        btnCancel=(Button) findViewById(R.id.btnCancel);
        profileimg=(ImageView)findViewById(R.id.image);
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id=settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!EventDescriptin_et.getText().toString().contentEquals(""))
                {
                    ActivityCompat.requestPermissions(Gallery_dialogBox.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                    ActivityCompat.requestPermissions(Gallery_dialogBox.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                }else
                {
                    Toast.makeText(Gallery_dialogBox.this,"Please Enter Event Description",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == RESULT_LOAD_IMAGE && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = Gallery_dialogBox.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                File file = new File(attachmentFile);
                long length = file.length();
                length = length / 1024;
                imageSize = length;
                path = file.getName();
                URI = Uri.parse("file://" + attachmentFile);
                profileimg.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));

                cursor.close();
                try
                {
                    {


                        Profileupdate login = new Profileupdate();
                        login.execute();

                    }

                } catch (Exception e) {
                    String msg = e.getMessage();

                }

            }


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String attachmentFile;
            attachmentFile = photo.toString();
            File file = new File(attachmentFile);
            path = file.getName();
            profileimg.setImageBitmap(photo);

            try {

                {

                    Profileupdate login = new Profileupdate();
                    login.execute();
                }

            } catch (Exception e) {
                String msg = e.getMessage();
            }
        }
    }
    private class Profileupdate extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Gallery_dialogBox.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Uplaoding...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if(uploadstatus==1)
            {
                Toast.makeText(Gallery_dialogBox.this, "Error While Uploading Image ", Toast.LENGTH_LONG).show();
            }else
            {
                String EventDescription=EventDescriptin_et.getText().toString();
                GalleryData galleryData=new GalleryData(EventDescription);
                galleryData.execute();
            }


        }

        @Override
        protected Void doInBackground(Void... voids) {
            String  currentdate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date());
            FileName=currentdate;
            Bitmap bitmap = ((BitmapDrawable) profileimg.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

            OPERATION_NAME = "gallery";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("base64String",base64String);
                request.addProperty("FileName",currentdate);
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
                    result = response.getProperty(0).toString();
                    if(result.contentEquals("false"))
                    {
                        uploadstatus=1;
                    }
                } else {
                    Toast.makeText(Gallery_dialogBox.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                uploadstatus=1;
                e.printStackTrace();
            }
            return null;
        }
    }
    private class GalleryData extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Gallery_dialogBox.this);
        String Eventdescription;

        public GalleryData(String eventDescription) {
            Eventdescription=eventDescription;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Uplaoding...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if(uploadstatus==1)
            {
                Toast.makeText(Gallery_dialogBox.this, "Error While Uploading Image ", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(Gallery_dialogBox.this, "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                finish();
                Gallery_fragment gallery_fragment = new Gallery_fragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, gallery_fragment).commit();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String ImageName=FileName+".jpeg";
            OPERATION_NAME = "galleryData";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command","Insert");
                request.addProperty("FileName",FileName+".jpeg");
                request.addProperty("EventDescription",Eventdescription);
                request.addProperty("Uploadedfrom","Mobile");
                request.addProperty("Filetype","Gallery");
                request.addProperty("image",base64String);
                request.addProperty("intSchool_id",school_id);
                request.addProperty("intAcademic_id",academic_id);
                request.addProperty("btActiveFlg","1");
                request.addProperty("Path","~/ESmartDemoDemoServices/UploadImages/"+ImageName);


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
                    result = response.getProperty(0).toString();
                    if(result.contentEquals("false"))
                    {
                        uploadstatus=1;
                    }
                } else {
                    Toast.makeText(Gallery_dialogBox.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                uploadstatus=1;
                e.printStackTrace();
            }
            return null;
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

