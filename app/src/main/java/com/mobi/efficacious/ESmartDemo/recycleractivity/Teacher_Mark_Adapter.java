package com.mobi.efficacious.ESmartDemo.recycleractivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Teacher_attendence;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/16/2018.
 */

public class Teacher_Mark_Adapter extends RecyclerView.Adapter<Teacher_Mark_Adapter.ViewHolder> {
    ArrayList<Teacher_attendence> tchrList = new ArrayList<Teacher_attendence>();
    Context context;
    private static String SOAP_ACTION = "";
    String Selected_Date,academic_id,status,FCMToken,school_id;
    int intTeacherid;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String OPERATION_NAME = "GetAboutADSXML";
    public Teacher_Mark_Adapter(ArrayList<Teacher_attendence> teachersList, String Selected_Date, String academic_id, Context context) {
        this.tchrList=teachersList;
        this.Selected_Date=Selected_Date;
        this.academic_id=academic_id;
        this.context=context;
    }

    @Override
    public Teacher_Mark_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_teacher_attendence_mark,null);
        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
//        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.slidedown);
//        itemLayoutView.startAnimation(animFadein6);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final  int pos=position;
        holder.tchrname.setText(tchrList.get(position).getTeacherName());
//      holder.tchrdesignation.setText(tchrList.get(position).getTeacherDesignation());
        holder.radio_Absent.setChecked(tchrList.get(position).isSelected());
        holder.radio_Absent.setTag(tchrList.get(position));
        holder.radio_Present.setChecked(tchrList.get(position).isP_selected());
        holder.radio_Present.setTag(tchrList.get(position));
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        holder.radio_Absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton cb=(RadioButton) v;
                Teacher_attendence contact=(Teacher_attendence)cb.getTag();
                contact.setSelected(cb.isChecked());
                tchrList.get(pos).setSelected(cb.isChecked());
                tchrList.get(pos).setP_selected(false);
                holder.radio_Present.setChecked(tchrList.get(pos).isP_selected());
                holder.radio_Present.setTag(tchrList.get(pos));
                intTeacherid=tchrList.get(pos).getTeacherId();
                FCMToken=tchrList.get(pos).getFCMToken();
                status="Absent";
//                Attendence_mark attendence_mark=new Attendence_mark();
//                attendence_mark.execute();
            }
        });

        holder.radio_Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton cb1=(RadioButton) v;
                Teacher_attendence contact1=(Teacher_attendence)cb1.getTag();
                contact1.setP_selected(cb1.isChecked());
                tchrList.get(pos).setP_selected(cb1.isChecked());
                tchrList.get(pos).setSelected(false);
                holder.radio_Absent.setChecked(tchrList.get(pos).isSelected());
                holder.radio_Absent.setTag(tchrList.get(pos));
                intTeacherid=tchrList.get(pos).getTeacherId();
                FCMToken=tchrList.get(pos).getFCMToken();
                status="Present";
//                Attendence_mark attendence_mark=new Attendence_mark();
//                attendence_mark.execute();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tchrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tchrname;
      //  TextView tchrdesignation;
        RadioButton radio_Absent;
        RadioButton radio_Present;
        public Teacher_attendence singleteacher;
        public ViewHolder(View itemView) {
            super(itemView);
            tchrname=(TextView)itemView.findViewById(R.id.teachername);
            //tchrdesignation=(TextView)itemView.findViewById(R.id.Dept_allteacher1);
            radio_Absent=(RadioButton)itemView.findViewById(R.id.radio_absent);
            radio_Present=(RadioButton)itemView.findViewById(R.id.radio_present);
        }
    }
    public ArrayList<Teacher_attendence> getTchrList()
    {
        return tchrList;
    }


    private class Attendence_mark extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        //
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Teacher_Mark_activity.progressbar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Teacher_Mark_activity.progressbar.setVisibility(View.GONE);
            this.dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "MarkAttendence";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "InsertTeacher");
                request.addProperty("intUserType_id", "3");
                request.addProperty("intUser_id",intTeacherid);
                request.addProperty("dtDate",Selected_Date);
                request.addProperty("intschool_id", school_id);
                request.addProperty("intAcademic_id", academic_id);
                request.addProperty("status",status);
                request.addProperty("FCMToken",FCMToken);
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
                e.printStackTrace();
            }


            return null;
        }
    }
}
