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
import com.mobi.efficacious.ESmartDemo.entity.AllStudent;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


/**
 * Created by EFF-4 on 3/19/2018.
 */

public class Student_mark_Adapter extends RecyclerView.Adapter<Student_mark_Adapter.ViewHolder> {
    ArrayList<AllStudent> studentList=new ArrayList<AllStudent>();
    private static String SOAP_ACTION = "";
    String Selected_Date,academic_id,status,FCMToken;
    int intStudent_id,intStandard_id,intDivision_id;
    String school_id;
    private static String OPERATION_NAME = "GetAboutADSXML";
    Context context;
    View itemLayoutView;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    public Student_mark_Adapter(ArrayList<AllStudent> studentList, String Selected_Date, String academic_id, Context context) {
        this.studentList=studentList;
        this.Selected_Date=Selected_Date;
        this.academic_id=academic_id;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemLayoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adpater_student_attendence_mark,null);

        ViewHolder viewHolder=new ViewHolder(itemLayoutView);
//        Animation animFadein6 = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.slidedown);
//        itemLayoutView.startAnimation(animFadein6);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        final  int pos=position;

        holder.studentname.setText(studentList.get(position).getName());

        holder.radio_Absent.setChecked(studentList.get(position).isSelected());
        holder.radio_Absent.setTag(studentList.get(position));
        holder.radio_Present.setChecked(studentList.get(position).isP_selected());
        holder.radio_Present.setTag(studentList.get(position));

        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id=settings.getString("TAG_SCHOOL_ID", "");

        holder.radio_Absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton cb=(RadioButton) v;
                AllStudent contact=(AllStudent)cb.getTag();
                contact.setSelected(cb.isChecked());
                studentList.get(pos).setSelected(cb.isChecked());
                studentList.get(pos).setP_selected(false);
                holder.radio_Present.setChecked(studentList.get(pos).isP_selected());
                holder.radio_Present.setTag(studentList.get(pos));
                intStudent_id=studentList.get(pos).getStudent_id();
                intStandard_id=studentList.get(pos).getStandard_id();
              //  intDivision_id=studentList.get(pos).getDivision_id();
                FCMToken=studentList.get(pos).getFCMToken();
                status="Absent";
//                Attendence_mark attendence_mark=new Attendence_mark();
//                attendence_mark.execute();
            }
        });

        holder.radio_Present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton cb1=(RadioButton) v;
                AllStudent contact1=(AllStudent)cb1.getTag();
                contact1.setP_selected(cb1.isChecked());
                studentList.get(pos).setP_selected(cb1.isChecked());
                studentList.get(pos).setSelected(false);
                holder.radio_Absent.setChecked(studentList.get(pos).isSelected());
                holder.radio_Absent.setTag(studentList.get(pos));
                intStudent_id=studentList.get(pos).getStudent_id();
                intStandard_id=studentList.get(pos).getStandard_id();
              //  intDivision_id=studentList.get(pos).getDivision_id();
                FCMToken=studentList.get(pos).getFCMToken();
                status="Present";
//                Attendence_mark attendence_mark=new Attendence_mark();
//                attendence_mark.execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        RadioButton radio_Absent;
        RadioButton radio_Present;

        public ViewHolder(View itemView) {
            super(itemView);
            studentname=(TextView)itemView.findViewById(R.id.textView13);
            radio_Absent=(RadioButton)itemView.findViewById(R.id.radio_absent);
            radio_Present=(RadioButton)itemView.findViewById(R.id.radio_present);
        }
    }
    public ArrayList<AllStudent> getStudentList()
    {
        return studentList;
    }
    private class Attendence_mark extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        //
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Student_Mark_activity.progressbar.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Student_Mark_activity.progressbar.setVisibility(View.GONE);
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

                request.addProperty("command", "Insert");
                request.addProperty("intUserType_id", "1");
                request.addProperty("intUser_id",intStudent_id);
                request.addProperty("dtDate",Selected_Date);
                request.addProperty("intschool_id", school_id);
                request.addProperty("intstanderd_id", intStandard_id);
              //  request.addProperty("intdivision_id", intDivision_id);
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

