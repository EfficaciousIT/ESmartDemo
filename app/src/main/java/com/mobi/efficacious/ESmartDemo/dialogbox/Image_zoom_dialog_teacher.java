package com.mobi.efficacious.ESmartDemo.dialogbox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.fragment.Teacher_Calender_attendence;
import com.squareup.picasso.Picasso;


public class Image_zoom_dialog_teacher extends Dialog {
    ImageView imageView;
    ImageView callimage, messageimage, videcallimage;

    String mobile_no;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public Image_zoom_dialog_teacher(@NonNull Context context) {
        super(context);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_image);
        imageView = (ImageView) findViewById(R.id.imageView6);
        callimage = (ImageView) findViewById(R.id.imageView12);
        messageimage = (ImageView) findViewById(R.id.imageView16);
        videcallimage = (ImageView) findViewById(R.id.imageView18);


        mobile_no = Teacher_Calender_attendence.intMobileNo;
        String url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+ Teacher_Calender_attendence.image+"";
        Picasso.with(getContext()).load(url).fit()
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(imageView);
        callimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(mobile_no.contentEquals("-")||mobile_no.contentEquals(""))
                {
                    Toast.makeText(getContext(),"Mobile No. not available",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +mobile_no.trim()));
                    getContext().startActivity(intent);
                }

            }
        });
        messageimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobile_no.contentEquals("-")||mobile_no.contentEquals(""))
                {
                    Toast.makeText(getContext(),"Mobile No. not available",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Uri SMS_URI = Uri.parse("smsto:"+mobile_no.trim()); //Replace the phone number
                    Intent sms = new Intent(Intent.ACTION_VIEW,SMS_URI);
                    sms.putExtra("sms_body",""); //Replace the message witha a vairable
                    getContext().startActivity(sms);
                }

            }
        });


    }

    }



