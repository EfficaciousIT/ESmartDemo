package com.mobi.efficacious.ESmartDemo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.DailyDiary_Tab;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.DailyDiary;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DailyDiaryAdapter extends ArrayAdapter<DailyDiary> {

    private final Context context;
    private final ArrayList<DailyDiary> itemsArrayList;
    String Pagename,url,standard_id,divion_id,subjectName;
    String Usertype,intMy_id,flag="1";
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String FileName1="",school_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String int_Approval;
    public DailyDiaryAdapter(Context context, ArrayList<DailyDiary> itemsArrayList,String pagename,String usertype) {
        super(context, R.layout.leaveapply_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.Pagename=pagename;
        this.Usertype=usertype;
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.leaveapply_row, parent, false);

        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");

        TextView standard = (TextView)rowView.findViewById(R.id.fromdate_Leave);
        TextView division=(TextView)rowView.findViewById(R.id.ToDate_Leave);
        TextView datetime=(TextView)rowView.findViewById(R.id.Days_Leave);
        TextView comment = (TextView)rowView.findViewById(R.id.Reason_Leave);
        TextView subject = (TextView)rowView.findViewById(R.id.Approval_Leave);
        TextView Teachername = (TextView)rowView.findViewById(R.id.Name_Leave);
        LinearLayout attachmntrelative=(LinearLayout)rowView.findViewById(R.id.attachmntrelative);
//        RelativeLayout attachmntrelative2=(RelativeLayout)rowView.findViewById(R.id.attachmntrelative2);
//        RelativeLayout attachmntrelative3=(RelativeLayout)rowView.findViewById(R.id.attachmntrelative3);
        RelativeLayout namerelative=(RelativeLayout)rowView.findViewById(R.id.Name_relLeave);
//        TextView Attachmntfile=(TextView)rowView.findViewById(R.id.attachemnt);
//        TextView Attachmntfile2=(TextView)rowView.findViewById(R.id.attachemnt2);
//        TextView Attachmntfile3=(TextView)rowView.findViewById(R.id.attachemnt3);



        CircleImageView dowload_img = (CircleImageView)rowView.findViewById(R.id.downloadimg);
        CircleImageView dowload_img2 = (CircleImageView)rowView.findViewById(R.id.downloadimg2);
        CircleImageView dowload_img3 = (CircleImageView)rowView.findViewById(R.id.downloadimg3);
        CheckBox approval_checkbx = (CheckBox)rowView.findViewById(R.id.approval_checkbx);
        TextView Approvedtv=(TextView)rowView.findViewById(R.id.Approvedtv);
        CircleImageView imageteacher=(CircleImageView)rowView.findViewById(R.id.imageteacher);
        CheckBox rejected_checkbx = (CheckBox)rowView.findViewById(R.id.reject_checkbx1);
        TextView rejectedtv=(TextView)rowView.findViewById(R.id.rejecttv);

        attachmntrelative.setVisibility(View.VISIBLE);
        approval_checkbx.setVisibility(View.GONE);


        if(itemsArrayList.get(position).getVchFilePath2().contentEquals("0"))
        {
            dowload_img2.setVisibility(View.GONE);

        }else
        {
            dowload_img2.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath2()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img2);
            //  Attachmntfile2.setText(itemsArrayList.get(position).getVchFilePath2());
        }
        if(itemsArrayList.get(position).getVchFilePath3().contentEquals("0"))
        {
            dowload_img3.setVisibility(View.GONE);
        }else
        {
            dowload_img3.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath3()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img3);
            //Attachmntfile3.setText(itemsArrayList.get(position).getVchFilePath3());
        }


        if(itemsArrayList.get(position).getVchFileName().contentEquals("0"))
        {
//            attachmntrelative.setVisibility(View.GONE);
            dowload_img.setVisibility(View.GONE);

        }else
        {
            dowload_img.setVisibility(View.VISIBLE);
            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(dowload_img);
            // Attachmntfile.setText(itemsArrayList.get(position).getVchFileName());
        }
        if(Usertype.contentEquals("student"))
        {
            standard.setText("Teacher: "+itemsArrayList.get(position).getVchStandard());
            division.setVisibility(View.GONE);
            datetime.setText("Date: "+itemsArrayList.get(position).getDtDatetime());
            if(Pagename.contentEquals("HomeWork"))
            {
                comment.setText("HomeWork: "+itemsArrayList.get(position).getVchComment());
            }
            else {
                comment.setText("Remark: "+itemsArrayList.get(position).getVchComment());
            }
            subject.setTextColor(ContextCompat.getColor(context, R.color.black));
            subject.setText("Subject: "+itemsArrayList.get(position).getVchSubject());

        }else if(Usertype.contentEquals("Admin"))
        {
            namerelative.setVisibility(View.VISIBLE);

            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getTeacherProfileImage()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(imageteacher);
            Teachername.setText("Name: "+itemsArrayList.get(position).getTeacherName());
            standard.setText("Standard: "+itemsArrayList.get(position).getVchStandard());
            division.setText("Division: "+itemsArrayList.get(position).getVchDivision());
            datetime.setText("Date: "+itemsArrayList.get(position).getDtDatetime());
            if(Pagename.contentEquals("HomeWork"))
            {
                rejected_checkbx.setVisibility(View.VISIBLE);
                approval_checkbx.setVisibility(View.VISIBLE);
                Approvedtv.setVisibility(View.GONE);
                rejectedtv.setVisibility(View.GONE);
                String Aprroval_status=itemsArrayList.get(position).getIntApproval();
                if(Aprroval_status.contentEquals("1"))
                {
                    Approvedtv.setVisibility(View.VISIBLE);
                    approval_checkbx.setVisibility(View.GONE);
                    rejected_checkbx.setVisibility(View.GONE);
                    rejectedtv.setVisibility(View.GONE);
                }else if(Aprroval_status.contentEquals("2"))
                {
                    Approvedtv.setVisibility(View.GONE);
                    approval_checkbx.setVisibility(View.GONE);
                    rejected_checkbx.setVisibility(View.GONE);
                    rejectedtv.setVisibility(View.VISIBLE);
                }else
                {
                    rejected_checkbx.setVisibility(View.VISIBLE);
                    approval_checkbx.setVisibility(View.VISIBLE);
                    rejectedtv.setVisibility(View.GONE);
                    Approvedtv.setVisibility(View.GONE);
                    approval_checkbx.setChecked(false);
                    rejected_checkbx.setChecked(false);
                }
                comment.setText("HomeWork: "+itemsArrayList.get(position).getVchComment());
            }
            else {
                approval_checkbx.setVisibility(View.GONE);
                comment.setText("Remark: "+itemsArrayList.get(position).getVchComment());
            }
            subject.setTextColor(ContextCompat.getColor(context, R.color.black));
            subject.setText("Subject: "+itemsArrayList.get(position).getVchSubject());
        }
        else
        {
            standard.setText("Standard: "+itemsArrayList.get(position).getVchStandard());
            division.setText("Division: "+itemsArrayList.get(position).getVchDivision());
            datetime.setText("Date: "+itemsArrayList.get(position).getDtDatetime());
            if(Pagename.contentEquals("HomeWork"))
            {
                comment.setText("HomeWork: "+itemsArrayList.get(position).getVchComment());
                String Aprroval_status=itemsArrayList.get(position).getIntApproval();
                Approvedtv.setVisibility(View.GONE);
                approval_checkbx.setVisibility(View.GONE);
                rejected_checkbx.setVisibility(View.GONE);
                rejectedtv.setVisibility(View.GONE);

                if(Aprroval_status.contentEquals("1")) {
                    Approvedtv.setVisibility(View.VISIBLE);
                    approval_checkbx.setVisibility(View.GONE);
                    rejected_checkbx.setVisibility(View.GONE);
                    rejectedtv.setVisibility(View.GONE);
                }else if(Aprroval_status.contentEquals("2"))
                {
                    Approvedtv.setVisibility(View.GONE);
                    approval_checkbx.setVisibility(View.GONE);
                    rejected_checkbx.setVisibility(View.GONE);
                    rejectedtv.setVisibility(View.VISIBLE);

                }else
                {
                    Approvedtv.setVisibility(View.GONE);
                    approval_checkbx.setVisibility(View.GONE);
                    rejected_checkbx.setVisibility(View.GONE);
                    rejectedtv.setVisibility(View.GONE);
                }

            }
            else {
                comment.setText("Remark: "+itemsArrayList.get(position).getVchComment());
            }
            subject.setTextColor(ContextCompat.getColor(context, R.color.black));
            subject.setText("Subject: "+itemsArrayList.get(position).getVchSubject());
        }

        dowload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        dowload_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath2()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        dowload_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getVchFilePath3()+"";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        approval_checkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    subjectName=itemsArrayList.get(position).getVchSubject();
                    divion_id=itemsArrayList.get(position).getIntDivision_id();
                    standard_id=itemsArrayList.get(position).getIntstandard_id();
                    intMy_id=itemsArrayList.get(position).getIntMy_id();
                    int_Approval="1";
                    SumbitASYNC sumbitASYNC=new SumbitASYNC(int_Approval);
                    sumbitASYNC.execute();

                }
                else
                {

                }
            }
        });
        rejected_checkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    subjectName=itemsArrayList.get(position).getVchSubject();
                    divion_id=itemsArrayList.get(position).getIntDivision_id();
                    standard_id=itemsArrayList.get(position).getIntstandard_id();
                    intMy_id=itemsArrayList.get(position).getIntMy_id();
                    int_Approval="2";
                    SumbitASYNC sumbitASYNC=new SumbitASYNC(int_Approval);
                    sumbitASYNC.execute();

                }
                else
                {

                }
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = itemsArrayList.get(position).getVchComment();
                if(textToCopy.length() != 0)
                {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) // check sdk version
                    {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(textToCopy);
                        Toast.makeText(context.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clipData = android.content.ClipData.newPlainText("Clip",textToCopy);
                        Toast.makeText(context.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                        clipboard.setPrimaryClip(clipData);
                    }
                }
                else
                {
                    Toast.makeText(context.getApplicationContext(), "Empty Selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rowView;
    }

    private class SumbitASYNC extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        String Approval;
        public SumbitASYNC(String int_approval) {
            Approval=int_approval;
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

                request.addProperty("command", "Update");
                request.addProperty("intMy_id",intMy_id);
                request.addProperty("intStandard_id",standard_id);
                request.addProperty("intDivision_id",divion_id);
                request.addProperty("SubjectName",subjectName);
                request.addProperty("int_Approval",Approval);
                request.addProperty("intSchool_id",school_id);
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
                    result = response.getProperty(0).toString();
                    if(result.contentEquals("false"))
                    {
                        flag="0";
                    }
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
                if(Pagename.contentEquals("HomeWork")) {

                    Toast.makeText(context, "HomeWork Approved Successfully", Toast.LENGTH_SHORT).show();
                    DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                    Bundle args = new Bundle();
                    args.putString("value", "HomeWork");
                    dailyDiary_tab.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();

                }

            }
            else
            {
                if(Pagename.contentEquals("HomeWork")) {
                    // Toast.makeText(context, " UnSucessfull", Toast.LENGTH_LONG).show();
                    DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                    Bundle args = new Bundle();
                    args.putString("value", "HomeWork");
                    dailyDiary_tab.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();
                }
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

}