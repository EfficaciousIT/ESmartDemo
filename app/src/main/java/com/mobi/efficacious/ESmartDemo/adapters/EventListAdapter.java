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
import com.mobi.efficacious.ESmartDemo.Tab.Event_Tab;
import com.mobi.efficacious.ESmartDemo.activity.MainActivity;
import com.mobi.efficacious.ESmartDemo.entity.Event;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    private final Context context;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String school_id;
    String role_id, intstandard_id, userid, Year_id, intDivision_id;
    private final ArrayList<Event> itemsArrayList;
    String LeaveApplication_id;
    String pagename;

    public EventListAdapter(Context context, ArrayList<Event> itemsArrayList, String Pagenmae) {
        super(context, R.layout.event_list, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.pagename = Pagenmae;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.event_list, parent, false);
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        TextView registrationStartDate = (TextView) rowView.findViewById(R.id.dtregistrationfrm);
        TextView registrationEndDate = (TextView) rowView.findViewById(R.id.dtregistrationto);
        TextView eventStartDate = (TextView) rowView.findViewById(R.id.eventfrm);
        TextView eventEndDate = (TextView) rowView.findViewById(R.id.eventto);
        TextView eventName = (TextView) rowView.findViewById(R.id.eventname);
        TextView eventFees = (TextView) rowView.findViewById(R.id.eventfees);
        TextView eventDescription = (TextView) rowView.findViewById(R.id.eventdescription);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        school_id = settings.getString("TAG_SCHOOL_ID", "");
        registrationStartDate.setText("From Date: " + itemsArrayList.get(position).getDtRegistrartionStartDate());
        registrationEndDate.setText("To Date: " + itemsArrayList.get(position).getDtRegistrationEndDate());
        eventStartDate.setText("From Date: " + itemsArrayList.get(position).getDtEventStartDate());
        eventEndDate.setText("To Date: " + itemsArrayList.get(position).getDtEventEndDate());
        eventName.setText("Name: " + itemsArrayList.get(position).getVchEventName());
        eventFees.setText("Fees: " + itemsArrayList.get(position).getVchEventFees());
        eventDescription.setText("Description: " + itemsArrayList.get(position).getVchEventDescription());
        if (pagename.contentEquals("EventParticipated")) {
            checkBox.setVisibility(View.GONE);
        } else if(role_id.contentEquals("5")){
            checkBox.setVisibility(View.GONE);
        }else
        {
            checkBox.setVisibility(View.VISIBLE);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
                String EventId = itemsArrayList.get(pos).getEvent_id();
                role_id = settings.getString("TAG_USERTYPEID", "");
                intstandard_id = settings.getString("TAG_STANDERDID", "");
                userid = settings.getString("TAG_USERID", "");
                Year_id = settings.getString("TAG_ACADEMIC_ID", "");
                intDivision_id = settings.getString("TAG_DIVISIONID", "");
                EventAsync adm = new EventAsync(EventId, role_id, intstandard_id, userid, Year_id, intDivision_id);
                adm.execute();


            }
        });
        return rowView;
    }

    private class EventAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        String intEvent_id, intStandard_id, intdivision_id, intUser_id, intUserType_id, intAcademic_id;

        public EventAsync(String eventId, String role_id, String intstandard_id, String userid, String year_id, String intDivision_id) {
            intEvent_id = eventId;
            intStandard_id = intstandard_id;
            intdivision_id = intDivision_id;
            intUser_id = userid;
            intUserType_id = role_id;
            intAcademic_id = year_id;
        }

        //
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            Toast.makeText(context, "Participation Done", Toast.LENGTH_SHORT).show();
            Event_Tab event_tab = new Event_Tab();
            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, event_tab).commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "EventParticipation";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "Insert");
                request.addProperty("intEvent_id", intEvent_id);
                request.addProperty("intStandard_id", intStandard_id);
                request.addProperty("intDivision_id", intdivision_id);
                request.addProperty("intUser_id", intUser_id);
                request.addProperty("intUserType_id", intUserType_id);
                request.addProperty("intAcademic_id", intAcademic_id);
                request.addProperty("intSchool_id", school_id);

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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
