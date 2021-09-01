package com.mobi.efficacious.ESmartDemo.fragment;

import android.Manifest;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.database.Databasehelper;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile_Fragment extends Fragment {

    View myview;
    Toolbar toolbar;
    EditText StudentId;
    EditText StudentName,studentLastname;
    String FileName;
    String base64String="";
    EditText Address;
    EditText Std;
    int uploadstatus=0;
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String vchFirst_name, vchEmail, intMobileNo, vchAddress;
    String Stud_id;
    String path;
    CircleImageView profileimg;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    String attachmentFile,LastName;
    Long imageSize;
    Uri URI = null;
    String role_id;
    Button Update;
    String image;
    String user_id,academic_id,school_id;
    Databasehelper mydb;
    ConnectionDetector cd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.activity_profile,null);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Stud_id = settings.getString("TAG_STUDENTID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        StudentId = (EditText) myview.findViewById(R.id.studentId);
        StudentName = (EditText) myview.findViewById(R.id.StudentName);
        studentLastname = (EditText) myview.findViewById(R.id.studentLastname);
        Address = (EditText) myview.findViewById(R.id.address);
        Update = (Button) myview.findViewById(R.id.Update);
        Std = (EditText) myview.findViewById(R.id.Standerd);
        profileimg = (CircleImageView) myview.findViewById(R.id.imageView1);
        mydb=new Databasehelper(getActivity(),"Teacher_record",null,1);
        progressBar = (ProgressBar) myview.findViewById(R.id.ProfileProgressBar);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");

        role_id = settings.getString("TAG_USERTYPEID", "");
        if(role_id.contentEquals("1")||role_id.contentEquals("2"))
        {
            StudentId.setFocusable(false);
            StudentName.setFocusable(false);
            studentLastname.setFocusable(false);
            Address.setFocusable(false);

            user_id = settings.getString("TAG_STUDENTID", "");
        }else
        {
            user_id = settings.getString("TAG_USERID", "");
        }

        ProfileAsync profile = new ProfileAsync();
        profile.execute();
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();

                }
                else {
                    StudentId.getText().toString();
                    StudentName.getText().toString();
                    Std.getText().toString();
                    Address.getText().toString();
                    studentLastname.getText().toString();
                    if(StudentId.getText().toString().contentEquals("")|| StudentName.getText().toString().contentEquals("")|| Std.getText().toString().contentEquals("")||  Address.getText().toString().contentEquals("")|| studentLastname.getText().toString().contentEquals(""))
                    {
                        if(TextUtils.isEmpty(StudentId.getText().toString())) {
                            StudentId.setError("Enter Valid First Name");

                        }
                        if(TextUtils.isEmpty(StudentName.getText().toString())) {
                            StudentName.setError("Enter Valid Email Address");

                        }
                        if(TextUtils.isEmpty(Std.getText().toString())) {
                            StudentName.setError("Enter Valid Mobile No.");

                        }
                        if(TextUtils.isEmpty(Address.getText().toString())) {
                            Address.setError("Enter Valid Address ");

                        }
                        if(TextUtils.isEmpty(studentLastname.getText().toString())) {
                            studentLastname.setError("Enter Valid Last Name ");

                        }

                    }else
                    {
                        ProfileupdateASync profileupdateASync = new ProfileupdateASync();
                        profileupdateASync.execute();
                    }

                }

            }
        });
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        return myview;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
            try {

            } catch (Exception e) {
                String msg = e.getMessage();

            }

        }

    }



    private class ProfileAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("loading...");
            dialog.show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+image+"";
            Picasso.with(getActivity()).load(url)
                    .fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(profileimg);
            StudentId.setText(vchFirst_name);
            StudentName.setText(vchEmail);
            Std.setText(intMobileNo);
            Address.setText(vchAddress);
            studentLastname.setText(LastName);
            progressBar.setVisibility(View.GONE);
            this.dialog.dismiss();
        }


        protected Void doInBackground(Void... params) {
            String get_profile;
            OPERATION_NAME = "Profiler";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                if (role_id.contentEquals("3")) {
                    get_profile = "GetTeacherProfile";

                } else if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                    get_profile = "GetStudentProfile";
                } else if (role_id.contentEquals("4")) {
                    get_profile = "InsertSTaffProfile";
                } else {
                    get_profile = "GetAdminProfile";
                }
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", get_profile);
                request.addProperty("user_id", user_id);
                request.addProperty("academic_id", academic_id);
                request.addProperty("intSchool_id",school_id);
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
                                if (res.contains("No record found")) {

                                } else {
                                    LastName=str2.getProperty("Last_name").toString().trim();
                                    image = str2.getProperty("vchImageURL").toString().trim();
                                    vchEmail = str2.getProperty("vchEmail").toString().trim();
                                    if (vchEmail.equals("") || vchEmail.equals(null) || vchEmail.equals("anyType{}")) {
                                        vchEmail = "-";
                                    }
                                    vchFirst_name = str2.getProperty("vchFirst_name").toString().trim();
                                    intMobileNo = str2.getProperty("intMobileNo").toString().trim();
                                    if (intMobileNo.equals("") || intMobileNo.equals(null) || intMobileNo.equals("anyType{}")) {
                                        intMobileNo = "-";
                                    }
                                    vchAddress = str2.getProperty("vchAddress").toString().trim();
                                    if (vchAddress.equals("") || vchAddress.equals(null) || vchAddress.equals("anyType{}")) {
                                        vchAddress = "-";
                                    }

                                }

                            }

                        }


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
    }

    private class Profileupdate extends AsyncTask<Void, Void, Void> {
        String get_profile_user;
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Uplaoding...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+FileName+".jpeg";
            Picasso.with(getActivity()).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(MainActivity.profile_img);

            Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            this.dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            Bitmap bitmap = ((BitmapDrawable) profileimg.getDrawable()).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//            byte[] byteArray = stream.toByteArray();
//            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            if (role_id.contentEquals("3")) {
                get_profile_user = "InsertTeacherProfile";
                OPERATION_NAME = "ProfileForTeacher";
            } else if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
                get_profile_user = "InsertStudentProfile";
                OPERATION_NAME = "ProfileForStudents";
            } else if (role_id.contentEquals("4")) {
                get_profile_user = "InsertSTaffProfile";
                OPERATION_NAME = "ProfileForStaff";
            } else {
                get_profile_user = "InsertAdminProfile";
                OPERATION_NAME = "ProfileForAdmin";
            }

            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", get_profile_user);
                request.addProperty("User_id", user_id);
                // request.addProperty("year_id", "1");
                request.addProperty("name", StudentId.getText().toString().trim());
                request.addProperty("email", StudentName.getText().toString().trim());
                request.addProperty("address", Address.getText().toString().trim());
                request.addProperty("mobile", Std.getText().toString().trim());
                request.addProperty("image", FileName+".jpeg");
                request.addProperty("intSchool_id",school_id);
                request.addProperty("vchLastName", studentLastname.getText().toString().trim());
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
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class ProfileupdateASync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());


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
                Toast.makeText(getActivity(), "Error While Uploading Image ", Toast.LENGTH_LONG).show();

            }else
            {
                Profileupdate profile = new Profileupdate();
                profile.execute();
                //   Toast.makeText(getActivity(), " Uploaded Profile ", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                uploadstatus=1;
                e.printStackTrace();
            }
            return null;
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
