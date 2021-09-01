package com.mobi.efficacious.ESmartDemo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.AdminApproval_Tab;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.LeaveApproval;
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

public class StudentApprovalAdapter extends ArrayAdapter<LeaveApproval> {

    private final Context context;
    private final ArrayList<LeaveApproval> itemsArrayList;
    String LeaveApplication_id,intStudent_id;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String flag, int_Approval,school_id;
    public StudentApprovalAdapter(Context context, ArrayList<LeaveApproval> itemsArrayList) {
        super(context, R.layout.fragment_studentleaveapproval, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_studentleaveapproval, parent, false);

        TextView fromDate = (TextView)rowView.findViewById(R.id.fromdate_StudentLeave);
        TextView toDate=(TextView)rowView.findViewById(R.id.ToDate_StudentLeave);
        TextView Days=(TextView)rowView.findViewById(R.id.Days_StudentLeave);
        TextView Reason = (TextView)rowView.findViewById(R.id.Reason_StudentLeave);
        TextView Name = (TextView)rowView.findViewById(R.id.Name_StudentLeave);
        CheckBox approval_checkbx = (CheckBox)rowView.findViewById(R.id.approval_checkbx);
        TextView Approvedtv=(TextView)rowView.findViewById(R.id.Approvedtv);
        CircleImageView imageteacher=(CircleImageView)rowView.findViewById(R.id.imageteacher);
        CheckBox rejected_checkbx = (CheckBox)rowView.findViewById(R.id.reject_checkbx1);
        TextView rejectedtv=(TextView)rowView.findViewById(R.id.rejecttv);
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id = settings.getString("TAG_SCHOOL_ID", "");
        LeaveApplication_id = itemsArrayList.get(position).getLeaveApp_id();
        fromDate.setText("From Date: "+itemsArrayList.get(position).getFrom_date());
        toDate.setText("To Date: "+itemsArrayList.get(position).getTo_Date());
        Days.setText("Days: "+itemsArrayList.get(position).getTotalDays());
        Reason.setText("Reason: "+itemsArrayList.get(position).getReason());
        Name.setText("Name: "+itemsArrayList.get(position).getName());

        String url="https://eserveshiksha.co.in/ESmartDemo/ESmartDemoDemoServices/UploadImages/"+itemsArrayList.get(position).getStudentProfile()+"";
        Picasso.with(context).load(url).resize(100,100)
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(imageteacher);
        approval_checkbx.setVisibility(View.VISIBLE);
        rejected_checkbx.setVisibility(View.VISIBLE);
        approval_checkbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intStudent_id=itemsArrayList.get(pos).getIntStudent_id();
                int_Approval="1";
                AdminAsync adm = new AdminAsync(int_Approval);
                adm.execute();
            }
        });
        rejected_checkbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intStudent_id=itemsArrayList.get(pos).getIntStudent_id();
                int_Approval="2";
                AdminAsync adm = new AdminAsync(int_Approval);
                adm.execute();
            }
        });
        return rowView;
    }

    private class AdminAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        String int_Approval_leave;
        public AdminAsync(String int_approval) {
            int_Approval_leave=int_approval;
        }
        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "LeaveDetails";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "UpdateStatus");
                request.addProperty("leaveApplication_id", LeaveApplication_id);
                request.addProperty("school_id",school_id);
                request.addProperty("intUser_id", intStudent_id);
                request.addProperty("Usertype", "Student");
                request.addProperty("bitAdminApproval",int_Approval_leave);
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
                                if(res.contains("true"))
                                {
                                    flag="1";
                                }
                                else
                                {
                                    flag="0";
                                }

                            }

                        }

                    }
                    else
                    {
                        Toast.makeText(context, "Null Response", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
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
            this.dialog.dismiss();
            if(flag=="1")
            {
                Toast.makeText(context, "Leave Approval Sucessful", Toast.LENGTH_LONG).show();
                AdminApproval_Tab adminApproval_tab = new AdminApproval_Tab();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main,adminApproval_tab).commit();
            }
            else
            {
                Toast.makeText(context, "Leave Approval UnSucessful", Toast.LENGTH_LONG).show();
            }

            //progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
            //progressBar.setVisibility(View.VISIBLE);
        }
    }


}