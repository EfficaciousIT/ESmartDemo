package com.mobi.efficacious.ESmartDemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mobi.efficacious.ESmartDemo.FCMServices.FagmentPageOpen;
import com.mobi.efficacious.ESmartDemo.MapActivity.Fleet_Tracking_Map;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.Tab.AdminApproval_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.Attendence_sliding_tab;
import com.mobi.efficacious.ESmartDemo.Tab.Calender_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.Chating_Sliding_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.DailyDiary_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.Event_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.Leave_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.NoticeBoardTab;
import com.mobi.efficacious.ESmartDemo.Tab.StudentAttendanceActivity;
import com.mobi.efficacious.ESmartDemo.Tab.Syllabus_Tab;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_student;
import com.mobi.efficacious.ESmartDemo.Tab.TimetableActivity_teacher;
import com.mobi.efficacious.ESmartDemo.Tab.Timetable_sliding_tab;
import com.mobi.efficacious.ESmartDemo.common.ConnectionDetector;
import com.mobi.efficacious.ESmartDemo.dialogbox.VehicleOption_DialogboxMap;
import com.mobi.efficacious.ESmartDemo.fragment.Admin_Dashboard;
import com.mobi.efficacious.ESmartDemo.fragment.DailyDiaryListFragment;
import com.mobi.efficacious.ESmartDemo.fragment.Examination_Fragment;
import com.mobi.efficacious.ESmartDemo.fragment.Gallery_fragment;
import com.mobi.efficacious.ESmartDemo.fragment.MessageCenter;
import com.mobi.efficacious.ESmartDemo.fragment.Noticeboard;
import com.mobi.efficacious.ESmartDemo.fragment.Profile_Fragment;
import com.mobi.efficacious.ESmartDemo.fragment.Sms_Fragment;
import com.mobi.efficacious.ESmartDemo.fragment.Standard_nameForSyllabus;
import com.mobi.efficacious.ESmartDemo.fragment.StudentChangePassword;
import com.mobi.efficacious.ESmartDemo.fragment.StudentExamFragment;
import com.mobi.efficacious.ESmartDemo.fragment.StudentResultFragment;
import com.mobi.efficacious.ESmartDemo.fragment.StudentSyllabusFragment;
import com.mobi.efficacious.ESmartDemo.fragment.Student_Std_Fragment;
import com.mobi.efficacious.ESmartDemo.fragment.TeacherDashboard;
import com.mobi.efficacious.ESmartDemo.fragment.Tracking_Status_fragmentMap;
import com.mobi.efficacious.ESmartDemo.webservices.Constants;
import com.mobi.efficacious.ESmartDemo.webservices.Responce;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int k,FabmenuStatus=0;
    String title = "";
    ConnectionDetector cd;
    ImageView chating_imgbtn;
   public static CircleImageView profile_img;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String role_id, name1, user_id,academic_id,school_id, stud_id, stand_id;
    public static FragmentManager fragmentManager;
    FloatingActionButton fab,FabLiveTracking,FabReplayTracking,FabTRackingStatus,FabFleetStatus;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        role_id = settings.getString("TAG_USERTYPEID", "");
        user_id = settings.getString("TAG_USERID", "");
        academic_id = settings.getString("TAG_ACADEMIC_ID", "");
        school_id=settings.getString("TAG_SCHOOL_ID", "");
        chating_imgbtn=(ImageView)findViewById(R.id.chating_imgbtn);
        fragmentManager=getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cd = new ConnectionDetector(getApplicationContext());
        profile_img=(CircleImageView)findViewById(R.id.profile_img);
         fab = (FloatingActionButton) findViewById(R.id.fabMenu);
        FabLiveTracking = (FloatingActionButton) findViewById(R.id.fabLiveTrack);
        FabReplayTracking = (FloatingActionButton) findViewById(R.id.fabReplayStatus);
        FabTRackingStatus = (FloatingActionButton) findViewById(R.id.fabTrackingStatus);
        FabFleetStatus = (FloatingActionButton) findViewById(R.id.fabFleetStatus);
        ProfileAsync profileAsync=new ProfileAsync();
        profileAsync.execute();
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    if (!FagmentPageOpen.isChatActivityOpen()) {
                        getSupportActionBar().setTitle("Profile");
                        fragmentManager.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commit();
                    } else {
                        Toast.makeText(MainActivity.this, "Already Open", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        chating_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    getSupportActionBar().setTitle("Chat");
                    fragmentManager.beginTransaction().replace(R.id.content_main,new Chating_Sliding_Tab() ).commit();
                }
            }
        });
        if(role_id.equalsIgnoreCase("3")||role_id.contentEquals("2")||role_id.contentEquals("1"))
        {
            fab.setVisibility(View.GONE);
            FabLiveTracking.setVisibility(View.GONE);
            FabReplayTracking.setVisibility(View.GONE);
            FabTRackingStatus.setVisibility(View.GONE);
            FabFleetStatus.setVisibility(View.GONE);
        }
        else
        {
            fab.setVisibility(View.VISIBLE);
            FabLiveTracking.setVisibility(View.GONE);
            FabReplayTracking.setVisibility(View.GONE);
            FabTRackingStatus.setVisibility(View.GONE);
            FabFleetStatus.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {

                    if(FabmenuStatus==0)
                    {
                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.show_from_bottom);
                        FabLiveTracking.startAnimation(animFadein);
                        FabReplayTracking.startAnimation(animFadein);
                        FabTRackingStatus.startAnimation(animFadein);
                        FabFleetStatus.startAnimation(animFadein);

                        FabLiveTracking.setVisibility(View.VISIBLE);
                        FabReplayTracking.setVisibility(View.VISIBLE);
                        FabTRackingStatus.setVisibility(View.VISIBLE);
                        FabFleetStatus.setVisibility(View.VISIBLE);
                        FabmenuStatus=1;
                    }else
                    {
                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_to_bottom);
                        FabLiveTracking.startAnimation(animFadein);
                        FabReplayTracking.startAnimation(animFadein);
                        FabTRackingStatus.startAnimation(animFadein);
                        FabFleetStatus.startAnimation(animFadein);

                        FabLiveTracking.setVisibility(View.GONE);
                        FabReplayTracking.setVisibility(View.GONE);
                        FabTRackingStatus.setVisibility(View.GONE);
                        FabFleetStatus.setVisibility(View.GONE);
                        FabmenuStatus=0;

                    }


                }
            }
        });
        FabFleetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_to_bottom);
                    FabLiveTracking.startAnimation(animFadein);
                    FabReplayTracking.startAnimation(animFadein);
                    FabTRackingStatus.startAnimation(animFadein);
                    FabFleetStatus.startAnimation(animFadein);

                    FabLiveTracking.setVisibility(View.GONE);
                    FabReplayTracking.setVisibility(View.GONE);
                    FabTRackingStatus.setVisibility(View.GONE);
                    FabFleetStatus.setVisibility(View.GONE);
                    FabmenuStatus=0;
                    Intent intent=new Intent(MainActivity.this,Fleet_Tracking_Map.class);
                    startActivity(intent);
                }
            }
        });
        FabTRackingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_to_bottom);
                    FabLiveTracking.startAnimation(animFadein);
                    FabReplayTracking.startAnimation(animFadein);
                    FabTRackingStatus.startAnimation(animFadein);
                    FabFleetStatus.startAnimation(animFadein);

                    FabLiveTracking.setVisibility(View.GONE);
                    FabReplayTracking.setVisibility(View.GONE);
                    FabTRackingStatus.setVisibility(View.GONE);
                    FabFleetStatus.setVisibility(View.GONE);
                    FabmenuStatus=0;
                    getSupportActionBar().setTitle("Tracking Status");
                    title="Tracking Status";
                    Tracking_Status_fragmentMap tracking_status_fragment = new Tracking_Status_fragmentMap();
                    Bundle args = new Bundle();
                    args.putString("pagename",title);
                    tracking_status_fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_main,tracking_status_fragment).commit();
                }
            }
        });
        FabLiveTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_to_bottom);
                    FabLiveTracking.startAnimation(animFadein);
                    FabReplayTracking.startAnimation(animFadein);
                    FabTRackingStatus.startAnimation(animFadein);
                    FabFleetStatus.startAnimation(animFadein);

                    FabLiveTracking.setVisibility(View.GONE);
                    FabReplayTracking.setVisibility(View.GONE);
                    FabTRackingStatus.setVisibility(View.GONE);
                    FabFleetStatus.setVisibility(View.GONE);
                    FabmenuStatus=0;
                    VehicleOption_DialogboxMap notifDialog = new VehicleOption_DialogboxMap(MainActivity.this);
                    notifDialog.show();
                }
            }
        });
        FabReplayTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setMessage("No Internet Connection");
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_to_bottom);
                    FabLiveTracking.startAnimation(animFadein);
                    FabReplayTracking.startAnimation(animFadein);
                    FabTRackingStatus.startAnimation(animFadein);
                    FabFleetStatus.startAnimation(animFadein);

                    FabLiveTracking.setVisibility(View.GONE);
                    FabReplayTracking.setVisibility(View.GONE);
                    FabTRackingStatus.setVisibility(View.GONE);
                    FabFleetStatus.setVisibility(View.GONE);
                    FabmenuStatus=0;
                    getSupportActionBar().setTitle("Tracking Status Replay");
                    title="Tracking Status Replay";
                    Tracking_Status_fragmentMap tracking_status_fragment = new Tracking_Status_fragmentMap();
                    Bundle args = new Bundle();
                    args.putString("pagename",title);
                    tracking_status_fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_main,tracking_status_fragment).commit();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FragmentManager mfragment = getSupportFragmentManager();
        //   mfragment.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commit();
//        getSupportActionBar().setTitle("Profile");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();

        if (role_id.contentEquals("1") || role_id.contentEquals("2")) {
            MenuItem message = menu.findItem(R.id.nav_message);
            message.setVisible(false);
            MenuItem target = menu.findItem(R.id.nav_dashboard);
            target.setVisible(false);
            MenuItem teacher = menu.findItem(R.id.nav_teacherAttendence);
            teacher.setVisible(false);
            MenuItem student = menu.findItem(R.id.nav_studentAttendence);
            student.setTitle("Self Attendence");
            MenuItem diary = menu.findItem(R.id.nav_Diary);
            diary.setVisible(false);
            if (!cd.isConnectingToInternet())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK",null);
                alert.show();
            }else {
                mfragment.beginTransaction().replace(R.id.content_main, new Profile_Fragment()).commit();
                getSupportActionBar().setTitle("Profile");
            }
        } else if (role_id.contentEquals("3")) {
            MenuItem message = menu.findItem(R.id.nav_message);
            message.setVisible(false);
            MenuItem student = menu.findItem(R.id.nav_teacherAttendence);
            student.setTitle("Self Attendence");
            MenuItem payment = menu.findItem(R.id.nav_payment);
            payment.setVisible(false);
            if (!cd.isConnectingToInternet())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK",null);
                alert.show();
            }else {
                mfragment.beginTransaction().replace(R.id.content_main, new TeacherDashboard()).commit();
                getSupportActionBar().setTitle("Dashboard");
            }
        }else
        {
//            MenuItem message = menu.findItem(R.id.nav_Homework);
//            message.setVisible(false);
            if (!cd.isConnectingToInternet())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("No Internet Connection");
                alert.setPositiveButton("OK",null);
                alert.show();
            }else {
                mfragment.beginTransaction().replace(R.id.content_main, new Admin_Dashboard()).commit();
                getSupportActionBar().setTitle("Dashboard");
            }

        }
    }

    @Override
    public void onBackPressed() {
        k++;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
//            super.onBackPressed();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k = 0;
            }
        }, 1000);
        if (k == 1) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure want to Exit?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ChangePassword) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new StudentChangePassword()).commit();
            return true;
        }
        if (id == R.id.action_Logout) {
            SharedPreferences.Editor editor_delete = settings.edit();
            editor_delete.clear().commit();
            FirebaseAuth.getInstance().signOut();
            this.deleteDatabase("Notifications");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager mfragment = getSupportFragmentManager();
        cd = new ConnectionDetector(getApplicationContext());
        if(role_id.contentEquals("5"))
        {
            fab.setVisibility(View.VISIBLE);
        }else
        {
            fab.setVisibility(View.GONE);
        }

        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
        else
            {
                if (role_id.equalsIgnoreCase("5"))
                {
                        if (id == R.id.nav_dashboard) {
                        title = "Dashboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new Admin_Dashboard()).commit();

                    } else if (id == R.id.nav_syllabus) {
                        title="Syllabus";
                            mfragment.beginTransaction().replace(R.id.content_main, new Standard_nameForSyllabus()).commit();
                    } else if (id == R.id.nav_timetable) {
                        title = "TimeTable";
                            mfragment.beginTransaction().replace(R.id.content_main, new Timetable_sliding_tab()).commit();

                    } else if (id == R.id.nav_examination) {
                        title = "Examination";
                            mfragment.beginTransaction().replace(R.id.content_main, new Examination_Fragment()).commit();
                    } else if (id == R.id.nav_calender) {
                        title = "Calender";
                        mfragment.beginTransaction().replace(R.id.content_main, new Calender_Tab()).commit();

                    } else if (id == R.id.nav_leave) {
                        title="Leave";
                        mfragment.beginTransaction().replace(R.id.content_main,new AdminApproval_Tab() ).commit();
                    }
                    else if (id == R.id.nav_Event) {
                        title="Event";
                        mfragment.beginTransaction().replace(R.id.content_main,new Event_Tab() ).commit();
                    }else if (id == R.id.nav_Gallery) {
                        title="Gallery";
                        fab.setVisibility(View.GONE);
                       mfragment.beginTransaction().replace(R.id.content_main,new Gallery_fragment() ).commit();
//                        new AlertDialog.Builder(this)
//                                .setMessage("Coming Soon...")
//                                .setNegativeButton("ok", null)
//                                .show();
                    } else if (id == R.id.nav_payment) {
                        new AlertDialog.Builder(this)
                                .setMessage("Coming Soon...")
                                .setNegativeButton("ok", null)
                                .show();
                    }
                    else if (id == R.id.nav_Diary) {
                        title = "Daily Diary";
                        DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                        Bundle args = new Bundle();
                        args.putString("value", "DailyDiary");
                        dailyDiaryListFragment.setArguments(args);
                        mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commit();
                    }
                    else if (id == R.id.nav_Homework)
                    {
                        title = "Home Work";
                        DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                        Bundle args = new Bundle();
                        args.putString("value","HomeWork");
                        dailyDiary_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();
                    }

                    else if (id == R.id.nav_Result) {
                        title = "Result";
                        fab.setVisibility(View.GONE);
                            mfragment.beginTransaction().replace(R.id.content_main, new Student_Std_Fragment()).commit();
                    } else if (id == R.id.nav_studentAttendence) {
                        fab.setVisibility(View.GONE);
                        title = "Attendence";
                            Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                            Bundle args = new Bundle();
                            args.putString("pagename", "Std");
                            student_std_activity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commit();

                    }else if(id==R.id.nav_teacherAttendence)
                    {
                        fab.setVisibility(View.GONE);
                        title = "Attendence";
                        StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                        Bundle args = new Bundle();
                        args.putString("selected_layout","Teacher_linearlayout");
                        studentAttendanceActivity.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commit();

                    } else if (id == R.id.nav_message) {
                        title = "Messaging";
                        mfragment.beginTransaction().replace(R.id.content_main, new Sms_Fragment()).commit();

                    } else if (id == R.id.nav_noticeboard) {
                        title = "Noticeboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new NoticeBoardTab()).commit();
                    } else if (id == R.id.nav_messageCenter) {
                        title = "Notification";
                        mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commit();
                    }

                }
                else if (role_id.contentEquals("3")) {

                     if (id == R.id.nav_dashboard)
                    {
                        title = "Dashboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new TeacherDashboard()).commit();

                    }
                    else if (id == R.id.nav_syllabus)
                    {
                        title = "Syllabus";
                        mfragment.beginTransaction().replace(R.id.content_main, new Syllabus_Tab()).commit();
                    }
                    else if (id == R.id.nav_timetable)
                    {
                        title = "TimeTable";
                        mfragment.beginTransaction().replace(R.id.content_main, new TimetableActivity_teacher()).commit();
                    }
                    else if (id == R.id.nav_examination)
                    {
                        title = "Examination";
                        mfragment.beginTransaction().replace(R.id.content_main, new Examination_Fragment()).commit();
                    }
                    else if (id == R.id.nav_calender)
                    {
                        title = "Calender";
                        mfragment.beginTransaction().replace(R.id.content_main, new Calender_Tab()).commit();
                    }
                    else if (id == R.id.nav_leave)
                    {
                        title = "Leave";
                        mfragment.beginTransaction().replace(R.id.content_main, new Leave_Tab()).commit();
                    }
                    else if (id == R.id.nav_Event)
                    {
                        title = "Event";
                        mfragment.beginTransaction().replace(R.id.content_main, new Event_Tab()).commit();
                    }
                    else if (id == R.id.nav_Gallery)
                    {
                        mfragment.beginTransaction().replace(R.id.content_main,new Gallery_fragment() ).commit();
//                        new AlertDialog.Builder(this)
//                                .setMessage("Coming Soon...")
//                                .setNegativeButton("ok", null)
//                                .show();
                    }
                    else if (id == R.id.nav_Homework)
                    {
                        title = "Home Work";
                        DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                        Bundle args = new Bundle();
                        args.putString("value","HomeWork");
                        dailyDiary_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();
//                        stand_id= settings.getString("TAG_STANDERDID", "");
//                        mfragment.beginTransaction().replace(R.id.content_main, new DailyDiary_Tab()).commit();
                       // mfragment.beginTransaction().replace(R.id.content_main, new StudentHomeworkFragment()).commit();
                    }
                    else if (id == R.id.nav_Diary)
                    {
                        title = "Daily Diary";
                        DailyDiary_Tab dailyDiary_tab = new DailyDiary_Tab();
                        Bundle args = new Bundle();
                        args.putString("value", "DailyDiary");
                        dailyDiary_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, dailyDiary_tab).commit();

                      //  mfragment.beginTransaction().replace(R.id.content_main, new DailyDiary_Tab()).commit();

                    }
                    else if (id == R.id.nav_Result)
                    {
                        title = "Result";
                        mfragment.beginTransaction().replace(R.id.content_main, new Student_Std_Fragment()).commit();
                    }
                    else if (id == R.id.nav_studentAttendence)
                    {
                        fab.setVisibility(View.GONE);
                        title = "Attendence";
                        Student_Std_Fragment student_std_activity = new Student_Std_Fragment();
                        Bundle args = new Bundle();
                        args.putString("pagename", "Std");
                        student_std_activity.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, student_std_activity).commit();
                    }
                    else if (id == R.id.nav_teacherAttendence)
                    {
                        title = "Attendence";
                        Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                        Bundle args = new Bundle();
                        args.putString("attendence", "teacher_self_attendence");
                        args.putString("designation", "Teacher");
                        attendence_sliding_tab.setArguments(args);
                        MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commit();
                    }
                    else if (id == R.id.nav_noticeboard)
                    {
                        title = "Noticeboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commit();
                    }
                    else if (id == R.id.nav_messageCenter)
                    {
                        title = "Notification";
                        mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commit();
                    }

                }
                else if (role_id.contentEquals("2"))
                {

                    if (id == R.id.nav_syllabus)
                    {
                        title="Syllabus";
                            stand_id= settings.getString("TAG_STANDERDID", "");
                        StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            subjectFragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, subjectFragment).commit();
                    }
                    else if (id == R.id.nav_timetable)
                    {
                        title = "TimeTable";
                            stand_id = settings.getString("TAG_STANDERDID", "");
                            TimetableActivity_student timetableActivity_student = new TimetableActivity_student();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            timetableActivity_student.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, timetableActivity_student).commit();
                    }
                    else if (id == R.id.nav_examination)
                    {
                        title = "Examination";
                            stand_id = settings.getString("TAG_STANDERDID", "");
                            StudentExamFragment studentExamActivity = new StudentExamFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            studentExamActivity.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, studentExamActivity).commit();

                    } else if (id == R.id.nav_calender)
                    {
                        title = "Calender";
                        mfragment.beginTransaction().replace(R.id.content_main, new Calender_Tab()).commit();

                    } else if (id == R.id.nav_leave)
                    {
                        title="Leave";
                        mfragment.beginTransaction().replace(R.id.content_main,new Leave_Tab() ).commit();
                    } else if (id == R.id.nav_Event)
                    {
                        title="Event";
                        mfragment.beginTransaction().replace(R.id.content_main,new Event_Tab() ).commit();
                    }else if (id == R.id.nav_Gallery)
                    {
                        mfragment.beginTransaction().replace(R.id.content_main,new Gallery_fragment() ).commit();
//                        new AlertDialog.Builder(this)
//                                .setMessage("Coming Soon...")
//                                .setNegativeButton("ok", null)
//                                .show();
                    } else if (id == R.id.nav_payment)
                    {
                        new AlertDialog.Builder(this)
                                .setMessage("Coming Soon...")
                                .setNegativeButton("ok", null)
                                .show();
                    } else if (id == R.id.nav_Homework)
                    {
                        title = "Home Work";
                        DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                        Bundle args = new Bundle();
                        args.putString("value","HomeWork");
                        dailyDiaryListFragment.setArguments(args);
                        mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commit();
                       // mfragment.beginTransaction().replace(R.id.content_main, new StudentHomeworkFragment()).commit();
                    } else if (id == R.id.nav_Result)
                    {
                        title = "Result";
                        mfragment.beginTransaction().replace(R.id.content_main, new StudentResultFragment()).commit();
                    } else if (id == R.id.nav_studentAttendence)
                    {
                        title = "Attendence";
                            stud_id = settings.getString("TAG_STUDENTID", "");
                            Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                            Bundle args = new Bundle();
                            args.putString("Stud_name", name1);
                            args.putString("stud_id12", stud_id);
                            args.putString("attendence", "student_self_attendence");
                            attendence_sliding_tab.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commit();

                    }else if(id==R.id.nav_teacherAttendence)
                    {
                        title = "Attendence";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                            Bundle args = new Bundle();
                            args.putString("selected_layout","Teacher_linearlayout");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commit();

                    }
                    else if (id == R.id.nav_messageCenter)
                    {
                        title = "Notification";
                        mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commit();
                    }
                    else if (id == R.id.nav_noticeboard)
                    {
                        title = "Noticeboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commit();
                    }

                }
                else
                {

                        if (id == R.id.nav_syllabus)
                    {
                        title="Syllabus";
                        stand_id= settings.getString("TAG_STANDERDID", "");
                        StudentSyllabusFragment subjectFragment = new StudentSyllabusFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            subjectFragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, subjectFragment).commit();

                    } else if (id == R.id.nav_timetable)
                    {
                        title = "TimeTable";
                            stand_id = settings.getString("TAG_STANDERDID", "");
                            TimetableActivity_student timetableActivity_student = new TimetableActivity_student();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            timetableActivity_student.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, timetableActivity_student).commit();
                    } else if (id == R.id.nav_examination)
                    {
                        title = "Examination";
                        stand_id = settings.getString("TAG_STANDERDID", "");
                            StudentExamFragment studentExamActivity = new StudentExamFragment();
                            Bundle args = new Bundle();
                            args.putString("std_id", stand_id);
                            studentExamActivity.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, studentExamActivity).commit();

                    } else if (id == R.id.nav_calender)
                    {
                        title = "Calender";
                        mfragment.beginTransaction().replace(R.id.content_main, new Calender_Tab()).commit();

                    } else if (id == R.id.nav_leave)
                    {
                        title="Leave";
                        mfragment.beginTransaction().replace(R.id.content_main,new Leave_Tab() ).commit();
                    }
                    else if (id == R.id.nav_Event)
                    {
                        title="Event";
                        mfragment.beginTransaction().replace(R.id.content_main,new Event_Tab() ).commit();
                    }else if (id == R.id.nav_Gallery)
                    {
                        mfragment.beginTransaction().replace(R.id.content_main,new Gallery_fragment() ).commit();
//                        new AlertDialog.Builder(this)
//                                .setMessage("Coming Soon...")
//                                .setNegativeButton("ok", null)
//                                .show();
                    } else if (id == R.id.nav_payment)
                    {
                        new AlertDialog.Builder(this)
                                .setMessage("Coming Soon...")
                                .setNegativeButton("ok", null)
                                .show();
                    } else if (id == R.id.nav_Homework)
                    {
                        title = "Home Work";
                        DailyDiaryListFragment dailyDiaryListFragment = new DailyDiaryListFragment();
                        Bundle args = new Bundle();
                        args.putString("value","HomeWork");
                        dailyDiaryListFragment.setArguments(args);
                        mfragment.beginTransaction().replace(R.id.content_main,dailyDiaryListFragment).commit();
                       // mfragment.beginTransaction().replace(R.id.content_main, new StudentHomeworkFragment()).commit();

                    } else if (id == R.id.nav_Result)
                    {
                        title = "Result";
                        mfragment.beginTransaction().replace(R.id.content_main, new StudentResultFragment()).commit();
                    } else if (id == R.id.nav_studentAttendence)
                    {
                        title = "Attendence";
                            stud_id = settings.getString("TAG_STUDENTID", "");
                            Attendence_sliding_tab attendence_sliding_tab = new Attendence_sliding_tab();
                            Bundle args = new Bundle();
                            args.putString("Stud_name", name1);
                            args.putString("stud_id12", stud_id);
                            args.putString("attendence", "student_self_attendence");
                            attendence_sliding_tab.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, attendence_sliding_tab).commit();
                    }else if(id==R.id.nav_teacherAttendence)
                    {
                        title = "Attendence";
                            StudentAttendanceActivity studentAttendanceActivity = new StudentAttendanceActivity ();
                            Bundle args = new Bundle();
                            args.putString("selected_layout","Teacher_linearlayout");
                            studentAttendanceActivity.setArguments(args);
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, studentAttendanceActivity).commit();
                    }  else if (id == R.id.nav_noticeboard)
                    {
                        title = "Noticeboard";
                        mfragment.beginTransaction().replace(R.id.content_main, new Noticeboard()).commit();
                    }
                    else if (id == R.id.nav_messageCenter)
                    {
                        title = "Notification";
                        mfragment.beginTransaction().replace(R.id.content_main, new MessageCenter()).commit();
                    }
                }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(title);
        }
        return true;
    }
    private class ProfileAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        String image;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("loading...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+image+"";
            Picasso.with(MainActivity.this).load(url).fit()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(profile_img);
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
                                    image = str2.getProperty("vchImageURL").toString().trim();
                                }

                            }

                        }


                    } else {
                        Toast.makeText(MainActivity.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
