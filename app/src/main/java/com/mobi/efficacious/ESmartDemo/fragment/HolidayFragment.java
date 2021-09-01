package com.mobi.efficacious.ESmartDemo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.adapters.HolidayAdapter;
import com.mobi.efficacious.ESmartDemo.entity.Holiday;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HolidayFragment  extends Fragment {
    ProgressBar progressBar;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    ArrayAdapter adapter;
    TextView view_selected;
    ArrayList<Holiday> holiday=new ArrayList<Holiday>();
    ArrayList<Holiday> holiday1=new ArrayList<Holiday>();
FrameLayout calenderview;
    Date holidayDay;
ListView holidaylist;
    ArrayList<String> dates = new ArrayList<String>();
    CaldroidFragment mCaldroidFragment = new CaldroidFragment();
    String a,academic_id;
    String status="";
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String date_selected;
    String festival="";
    int dayscount;
    String newDate,fesival,school_id;
    public HolidayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holiday, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY );
view_selected=(TextView)getActivity().findViewById(R.id.textView15);
holidaylist=(ListView)getActivity().findViewById(R.id.holidaylist);
calenderview=(FrameLayout)getActivity().findViewById(R.id.cal_container);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        mCaldroidFragment.setArguments( args );
        HolidayAsync holiday = new HolidayAsync();
        holiday.execute();
        view_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected=view_selected.getText().toString();
                if(selected.contentEquals("Show In List"))
                {
                    if(adapter==null)
                    {

                    }else
                    {
                        adapter.notifyDataSetChanged();
                    }
                    view_selected.setText("Show In Calender");
                    holidaylist.setVisibility(View.VISIBLE);
                    calenderview.setVisibility(View.GONE);
                    status="list";
                    HolidayAsync holiday = new HolidayAsync();
                    holiday.execute();
                }
                else
                {
                    view_selected.setText("Show In List");
                    holidaylist.setVisibility(View.GONE);
                    calenderview.setVisibility(View.VISIBLE);
                    status="";
                    HolidayAsync holiday = new HolidayAsync();
                    holiday.execute();
                }

            }
        });
        mCaldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.get(Calendar.YEAR);

               int day1=date.getDate();
               int month1=((date.getMonth())+1);
                NumberFormat f = new DecimalFormat("00");
                date_selected=((f.format(day1))+"/"+(f.format(month1))+"/"+String.valueOf(calendar.get(Calendar.YEAR)) );
//                 date_selected= String.valueOf(day1)+("/")+ String.valueOf(month1)+("/")+ String.valueOf(calendar.get(Calendar.YEAR));

                boolean status=dates.contains(date_selected);
                if(status==true)
                {
                    festivalnmae(date_selected);
                }else
                {
                    Toast.makeText(getActivity()," "+date_selected, Toast.LENGTH_LONG).show();
                }
            }
        });


        }
        private class HolidayAsync extends AsyncTask<Void, Void, Void> {
            private final ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected Void doInBackground(Void... params) {
            holiday1.clear();
            OPERATION_NAME = "HolidayList";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "AllHolidays");
                request.addProperty("academic_id", academic_id);
                request.addProperty("intSchool_id",school_id);
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
                                Holiday hol =new Holiday();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "dtFromDate"))
                                    {
                                        a=str2.getProperty("dtFromDate").toString().trim();
                                        hol.setFromDate(str2.getProperty("dtFromDate").toString().trim());
                                    }
                                    if (isValidProperty(str2, "intNoOfDay"))
                                    {
                                        dayscount= Integer.parseInt(str2.getProperty("intNoOfDay").toString().trim());
                                    }
                                    if(isValidProperty(str2, "vchHoliday_name"))
                                    {
                                        fesival=str2.getProperty("vchHoliday_name").toString().trim();
                                        hol.setHoliday_name(str2.getProperty("vchHoliday_name").toString().trim());
                                    }


                                    if(status.contentEquals("list")) {
                                        holiday.add(hol);

                                    }
                                    else
                                    {
                                        for (int i = 0; i < dayscount - 1; i++) {
                                            Holiday vac1 = new Holiday();
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                            Calendar c = Calendar.getInstance();
                                            try {
                                                //Setting the date to the given date
                                                if (i == 0) {
                                                    c.setTime(sdf.parse(a));
                                                } else {
                                                    c.setTime(sdf.parse(newDate));
                                                }

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //Number of Days to add
                                            c.add(Calendar.DAY_OF_MONTH, 1);
                                            //Date after adding the days to the given date
                                            newDate = sdf.format(c.getTime());
                                            vac1.setFromDate(newDate);
                                            vac1.setHoliday_name(fesival);
                                            holiday1.add(vac1);
                                            dates.add(newDate);

                                        }
                                        dates.add(a);
                                        holiday1.add(hol);
                                    }


                                }

                            }

                        }

                        if(status.contentEquals("list"))
                        {
                            adapter = new HolidayAdapter(getActivity(), holiday);
                        }
                        else
                        {
                            holiday_list();

                        }

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
            if(status.contentEquals("list"))
            {
                holidaylist.setAdapter(adapter);
            }
            else
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , mCaldroidFragment ).commit();
            }

            status="";

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
    public  void holiday_list() {
        int day = 0;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        for (int i = 0; i < dates.size(); i++) {
            String inputString2 = dates.get(i);
            String inputString1 = myFormat.format(date);

            try {
                //Converting String format to date format
                Date date1 = null;
                try {
                    date1 = myFormat.parse(inputString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = myFormat.parse(inputString2);
                //Calculating number of days from two dates
                long diff = date2.getTime() - date1.getTime();
                long datee = diff / (1000 * 60 * 60 * 24);
                //Converting long type to int type
                day = (int) datee;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, day);
            holidayDay = cal.getTime();
            ColorDrawable bgToday = new ColorDrawable(Color.RED);
            mCaldroidFragment.setBackgroundDrawableForDate(bgToday,holidayDay);
            //  colors();

        }
    }
    public void festivalnmae(String date_selected)
    {
        String date_selected1=date_selected;
        int sizee=holiday1.size();
        for(int i=0;i< holiday1.size();i++)
        {
            String holidaydate=holiday1.get(i).getFromDate();

            if(date_selected1.contentEquals(holidaydate))
            {
                String holidayname=holiday1.get(i).getHoliday_name();
                Toast.makeText(getActivity(),holidayname,Toast.LENGTH_SHORT).show();
             break;
            }


        }

    }


}
