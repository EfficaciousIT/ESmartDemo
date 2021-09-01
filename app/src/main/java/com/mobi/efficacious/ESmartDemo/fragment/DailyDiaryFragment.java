package com.mobi.efficacious.ESmartDemo.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.DailyDiary_Tab;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.adapters.Tracking_status_adapterMap;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class DailyDiaryFragment extends Fragment {
    View myview;
    Toolbar toolbar;
    Tracking_status_adapterMap adapter;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private GoogleApiClient client;
    String Standard_id;
    HashMap<Object, Object> map;
    private ArrayList<HashMap<Object, Object>> dataList;
    private ArrayList<HashMap<Object, Object>> dataList2;
    private ArrayList<HashMap<Object, Object>> dataList3;
    String value,role_id,academic_id,userid;
    ConnectionDetector cd;
    String std_selected,div_selected,sub_selected;
    Spinner Std_spinner,Div_spinner,Sub_spinner;
    Button calenderBtn,SubmitBtn;
    private int mYear, mMonth, mDay;
    String Selected_Date,currentdate,dtDatetime;
    TextView tv_dateSelection,File_name,File_name2,File_Name3;
    EditText remarktv;
    String flag;
    ImageView Attachmntbtn,Attachmntbtn2,Attachmntbtn3;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMAGE = 1;
    Uri URI = null;
    int AttachmntStatus=0;
    String FileName="",FileName_2="",FileName_3="",fileextension2,fileextension3;
    String base64String="";
    String attachmentFile;
    Long imageSize;
    String path="";
    File file;
    int uploadstatus=0,syllabusuploadstatus=0;
    String  fileextension,TeacherName,StandardName,SubjectName,DivisionName,school_id;
    ImageView image,image2,image3;
    int Attachment=0,Attachment2=0,Attachment3=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fragment_daily_diary,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        Standard_id = settings.getString("TAG_STANDERDID", "");
        role_id = settings.getString("TAG_USERTYPEID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        flag="1";
        File_name=(TextView) myview.findViewById(R.id.attachemnttv);
        File_name2=(TextView) myview.findViewById(R.id.attachemnttv2);
        File_Name3=(TextView) myview.findViewById(R.id.attachemnttv3);
        Attachmntbtn2=(ImageView)myview.findViewById(R.id.calenderbtn2);
        Attachmntbtn3=(ImageView)myview.findViewById(R.id.calenderbtn3);
        Attachmntbtn=(ImageView)myview.findViewById(R.id.calenderbtn1);
        Std_spinner=(Spinner)myview.findViewById(R.id.std_spinner);
        Div_spinner=(Spinner)myview.findViewById(R.id.div_spinner);
        Sub_spinner=(Spinner)myview.findViewById(R.id.sub_spinner);
        calenderBtn=(Button)myview.findViewById(R.id.calenderbtn);
        tv_dateSelection=(TextView) myview.findViewById(R.id.datetextvw);
        SubmitBtn=(Button)myview.findViewById(R.id.btnSubmit);
        remarktv=(EditText) myview.findViewById(R.id.remarktv);
        image=(ImageView) myview.findViewById(R.id.image);
        image2=(ImageView) myview.findViewById(R.id.image2);
        image3=(ImageView) myview.findViewById(R.id.image3);
        currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tv_dateSelection.setText(currentdate);
        value = DailyDiary_Tab.value;
        if(value.contentEquals("HomeWork"))
        {
            remarktv.setHint("Write Home work here");
        }else
        {
            remarktv.setHint("Write Remark here");
        }
        cd = new ConnectionDetector(getContext().getApplicationContext());
        dataList = new ArrayList<HashMap<Object, Object>>();
        dataList2 = new ArrayList<HashMap<Object, Object>>();
        dataList3 = new ArrayList<HashMap<Object, Object>>();
        if(role_id.contentEquals("3"))
        {
            if (!cd.isConnectingToInternet())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK",null);
                alert.show();
            }else {
                userid = settings.getString("TAG_USERID", "");
                TeacherName = settings.getString("TAG_NAME", "");
                StudenStandardtAsync studenStandardtAsync = new StudenStandardtAsync();
                studenStandardtAsync.execute();
            }
        }
        Std_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Std_spinner=position;
                StandardName=String.valueOf(dataList.get(position).get("vchStandard_name"));
                std_selected= String.valueOf(dataList.get(position).get("intstandard_id"));
                if(std_selected.contentEquals("--No Data Available--"))
                {
                    Toast.makeText(getActivity(),"No Data Available", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                        StudentDivisionAsync studentDivisionAsync = new StudentDivisionAsync(std_selected);
                        studentDivisionAsync.execute();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"Please Select Standard",Toast.LENGTH_SHORT).show();
            }
        });
        Div_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Div_spinner=position;
                DivisionName= String.valueOf(dataList2.get(position).get("vchDivisionName"));
                div_selected= String.valueOf(dataList2.get(position).get("intDivision_id"));
                if(div_selected.contentEquals("--No Data Available--"))
                {
                    Toast.makeText(getActivity(),"No Data Available", Toast.LENGTH_LONG).show();
                }else
                {
                    if (!cd.isConnectingToInternet())
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("No Internet Connection");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                    }else {
                        StudentSubjectAsync studentSubjectAsync = new StudentSubjectAsync(div_selected);
                        studentSubjectAsync.execute();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"Please Select Division",Toast.LENGTH_SHORT).show();
            }
        });
        Sub_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Sub_spinner=position;
                SubjectName= String.valueOf(dataList3.get(position).get("vchSubjectName"));
                sub_selected= String.valueOf(dataList3.get(position).get("intSubject_id"));
                if(sub_selected.contentEquals("--No Data Available--"))
                {
                    Toast.makeText(getActivity(),"No Data Available", Toast.LENGTH_LONG).show();
                }else
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"Please Select Subject",Toast.LENGTH_SHORT).show();
            }
        });
        Attachmntbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmntStatus=3;
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });
        Attachmntbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmntStatus=2;
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });
        Attachmntbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmntStatus=1;
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                NumberFormat f = new DecimalFormat("00");
                                Selected_Date=((f.format(monthOfYear +1))+"/"+(f.format(dayOfMonth))+"/"+year );
//                            tv_dateSelection.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
                                Date date1 = null;
                                try {
                                    date1 = sdf.parse(((f.format(monthOfYear +1))+"-"+dayOfMonth+"-"+year ));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date date2 = null;
                                try {
                                    date2 = sdf.parse(currentdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (date1.compareTo(date2) > 0) {
//                               Toast.makeText(getActivity(),"Please select proper Date \n Selected date must be less than or equal to current Date",Toast.LENGTH_LONG).show();
                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));

                                } else if (date1.compareTo(date2) < 0) {

                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));

                                } else if (date1.compareTo(date2) == 0) {
                                    tv_dateSelection.setText(((f.format(dayOfMonth))+"/"+(f.format(monthOfYear +1))+"/"+year ));

                                } else {

                                }

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!std_selected.contentEquals("--No Data Available--")&&!div_selected.contentEquals("--No Data Available--")&&!sub_selected.contentEquals("--No Data Available--"))
                {
                    dtDatetime=tv_dateSelection.getText().toString();
                    String Remark= remarktv.getText().toString();
                    String Filename=File_name.getText().toString();
                    if(!Remark.contentEquals(""))
                    {
                        if(Filename.contentEquals("")&& File_name2.getText().toString().contentEquals("")&& File_Name3.getText().toString().contentEquals("")){
                            SumbitASYNC sumbitASYNC=new SumbitASYNC(Remark,dtDatetime,std_selected,div_selected,sub_selected);
                            sumbitASYNC.execute();
                        }else
                        {
                            try
                            {
                                SumbitASYNC sumbitASYNC=new SumbitASYNC(Remark,dtDatetime,std_selected,div_selected,sub_selected);
                                sumbitASYNC.execute();
                            } catch (Exception e) {

                            }
                        }


                    }else
                    {
                        if(TextUtils.isEmpty(Remark)) {
                            remarktv.setError("Enter Remark");
                        }
                    }

                }else
                {
                    if(std_selected.contentEquals("--No Data Available--"))
                    {
                        setSpinnerError(Std_spinner,"Select valid Standard ");
                    }
                    if(div_selected.contentEquals("--No Data Available--"))
                    {
                        setSpinnerError(Div_spinner,"Select valid Division ");
                    }
                    if(sub_selected.contentEquals("--No Data Available--"))
                    {
                        setSpinnerError(Sub_spinner,"Select valid Subject ");
                    }

                }
            }
        });
        return myview;
    }
    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(error); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == RESULT_LOAD_IMAGE && data != null) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                File file = new File(attachmentFile);
                long length = file.length();
                length = length / 1024;
                imageSize = length;
                path = file.getName();
                URI = Uri.parse("file://" + attachmentFile);

                if(AttachmntStatus==1)
                {
                    image.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));
                    fileextension = path.substring(path.lastIndexOf("."));
                    File_name.setText(path);
                    FileUploadASYNC login = new FileUploadASYNC(file, fileextension);
                    login.execute();
                }
                if(AttachmntStatus==2)
                {
                    image2.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));
                    fileextension2 = path.substring(path.lastIndexOf("."));
                    File_name2.setText(path);
                    FileUpload2ASYNC login = new FileUpload2ASYNC(file, fileextension2);
                    login.execute();
                }
                if(AttachmntStatus==3)
                {
                    image3.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));
                    fileextension3 = path.substring(path.lastIndexOf("."));
                    File_Name3.setText(path);
                    FileUpload3ASYNC login = new FileUpload3ASYNC(file, fileextension3);
                    login.execute();
                }



            }
    }
    private class StudenStandardtAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected Void doInBackground(Void... params) {
            dataList.clear();
            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String  result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectStandradByLectures");
                request.addProperty("intTeacher_id", userid);
                request.addProperty("intAcademic_id",academic_id);
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
                                map = new HashMap<Object, Object>();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intstandard_id", str2.getProperty("intstandard_id").toString().trim());
                                    map.put("vchStandard_name", str2.getProperty("vchStandard_name").toString().trim());
                                    dataList.add(map);
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

                map = new HashMap<Object, Object>();
                map.put("intstandard_id","--No Data Available--");
                map.put("vchStandard_name","--No Data Available--" );
                dataList.add(map);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new Tracking_status_adapterMap(getActivity(), dataList,"StandradName");
            Std_spinner.setAdapter(adapter);
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
    private class StudentDivisionAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String StandardId;
        public StudentDivisionAsync(String std_selected) {
            StandardId=std_selected;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataList2.clear();
            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String  result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "selectDivisionByLectures");
                request.addProperty("intTeacher_id", userid);
                request.addProperty("intAcademic_id",academic_id);
                request.addProperty("intStandard_id",StandardId);
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
                                map = new HashMap<Object, Object>();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intDivision_id", str2.getProperty("intDivision_id").toString().trim());
                                    map.put("vchDivisionName", str2.getProperty("vchDivisionName").toString().trim());
                                    dataList2.add(map);
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
                map = new HashMap<Object, Object>();
                map.put("intDivision_id","--No Data Available--");
                map.put("vchDivisionName","--No Data Available--" );
                dataList2.add(map);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new Tracking_status_adapterMap(getActivity(), dataList2,"DivisionName");
            Div_spinner.setAdapter(adapter);
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

    private class StudentSubjectAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String DivisionId;
        public StudentSubjectAsync(String div_selected) {
            DivisionId=div_selected;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataList3.clear();
            OPERATION_NAME = "Standard";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String  result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "SelectSubjectByLectures");
                request.addProperty("intTeacher_id", userid);
                request.addProperty("intAcademic_id",academic_id);
                request.addProperty("intStandard_id",std_selected);
                request.addProperty("intDivision_id",DivisionId);
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
                                map = new HashMap<Object, Object>();
                                if (res.contains("No record found")) {

                                } else {
                                    map.put("intSubject_id", str2.getProperty("intSubject_id").toString().trim());
                                    map.put("vchSubjectName", str2.getProperty("vchSubjectName").toString().trim());
                                    dataList3.add(map);
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
                map = new HashMap<Object, Object>();
                map.put("intSubject_id","--No Data Available--");
                map.put("vchSubjectName","--No Data Available--" );
                dataList3.add(map);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new Tracking_status_adapterMap(getActivity(), dataList3,"SubjectName");
            Sub_spinner.setAdapter(adapter);
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
    private class FileUploadASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        File AttachmentFile;
        String File_extension;
        public FileUploadASYNC(File attachmentFile, String fileextension) {
            //AttachmentFile=attachmentFile;
            File_extension=fileextension;
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
                Toast.makeText(getActivity(), "Error While Uploading Image ", Toast.LENGTH_LONG).show();
            }else
            {
                String Remark= remarktv.getText().toString();

            }


        }

        @Override
        protected Void doInBackground(Void... voids) {


            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String  currentdate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date());
            FileName=currentdate;
            OPERATION_NAME = "SyllabusAttachemnt";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("base64String",base64String);
                request.addProperty("FileName",currentdate);
                request.addProperty("FileExtension",File_extension);
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
    private class FileUpload2ASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        File AttachmentFile;
        String File_extension;
        public FileUpload2ASYNC(File attachmentFile, String fileextension) {
            AttachmentFile=attachmentFile;
            File_extension=fileextension;
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
                Toast.makeText(getActivity(), "Error While Uploading Image ", Toast.LENGTH_LONG).show();
            }else
            {
                String Remark= remarktv.getText().toString();

            }


        }

        @Override
        protected Void doInBackground(Void... voids) {

            Bitmap bitmap = ((BitmapDrawable) image2.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String  currentdate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date());
            FileName_2=currentdate;
            OPERATION_NAME = "SyllabusAttachemnt";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("base64String",base64String);
                request.addProperty("FileName",currentdate);
                request.addProperty("FileExtension",File_extension);
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
    private class FileUpload3ASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        File AttachmentFile;
        String File_extension;
        public FileUpload3ASYNC(File attachmentFile, String fileextension) {
            AttachmentFile=attachmentFile;
            File_extension=fileextension;
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
                Toast.makeText(getActivity(), "Error While Uploading Image ", Toast.LENGTH_LONG).show();
            }else
            {
                String Remark= remarktv.getText().toString();

            }


        }

        @Override
        protected Void doInBackground(Void... voids) {


            Bitmap bitmap = ((BitmapDrawable) image3.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            String  currentdate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date());
            FileName_3=currentdate;
            OPERATION_NAME = "SyllabusAttachemnt";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {

                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("base64String",base64String);
                request.addProperty("FileName",currentdate);
                request.addProperty("FileExtension",File_extension);
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
    private class SumbitASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String vchComment,dtDateTime,intSubject_id,intStandard_id,intDivision_id;
        String filepath,filepath2,filepath3;
        public SumbitASYNC(String remark, String dtDatetime, String std_selected, String div_selected, String sub_selected) {
            vchComment=remark;
            dtDateTime=dtDatetime;
            intSubject_id=sub_selected;
            intStandard_id=std_selected;
            intDivision_id=div_selected;

            if(FileName_2.contentEquals(""))
            {
                filepath2="0";
            }else
            {
                filepath2=FileName_2+fileextension2;
            }
            if(FileName_3.contentEquals(""))
            {
                filepath3="0";
            }else
            {
                filepath3=FileName_3+fileextension3;
            }
            if(!FileName.contentEquals(""))
            {
                filepath=FileName+fileextension;
            }else
            {
                path="0";
                filepath="0";
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "DailyDiary";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "Insert");
                request.addProperty("vchComment", vchComment.trim());
                request.addProperty("intTeacher_id",userid);
                request.addProperty("intSubject_id",intSubject_id);
                request.addProperty("intSchool_id",school_id);
                request.addProperty("intStandard_id",intStandard_id);
                request.addProperty("intDivision_id",intDivision_id);
                request.addProperty("dtDatetime",dtDateTime);
                request.addProperty("intSchool_id", "1");
                request.addProperty("vchType",value);
                request.addProperty("vchFileName",path);
                request.addProperty("vchFilePath",filepath);
                request.addProperty("vchFilePath2",filepath2);
                request.addProperty("vchFilePath3",filepath3);
                request.addProperty("int_Approval","0");
                if(role_id.contentEquals("3"))
                {
                    request.addProperty("TeacherName",TeacherName);
                    request.addProperty("StandardName",StandardName);
                    request.addProperty("DivisionName",DivisionName);
                    request.addProperty("SubjectName",SubjectName);
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
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
                }
            } catch (Exception e) {
                flag="0";
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if(flag.contentEquals("1"))
            {
                if(value.contentEquals("HomeWork")) {
                    Toast.makeText(getActivity(), "HomeWork Submitted Successfully", Toast.LENGTH_SHORT).show();
                    DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                    Bundle args = new Bundle();
                    args.putString("value", "HomeWork");
                    dailyDiary_tab.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();
                }else
                {
                    Toast.makeText(getActivity(), "Dairy Submitted Successfully", Toast.LENGTH_SHORT).show();
                    DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                    Bundle args = new Bundle();
                    args.putString("value", "DailyDiary");
                    dailyDiary_tab.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();
                }

            }
            else
            {
                if(value.contentEquals("HomeWork")) {
                    Toast.makeText(getActivity(), " UnSucessfull", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), " UnSucessfully", Toast.LENGTH_SHORT).show();
                }
            }

            remarktv.setText("");
            if(value.contentEquals("HomeWork"))
            {
                remarktv.setHint("Write Home work here");
            }else
            {
                remarktv.setHint("Write Remark here");
            }
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

}