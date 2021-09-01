package com.mobi.efficacious.ESmartDemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mobi.efficacious.ESmartDemo.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by EFF-4 on 3/29/2018.
 */

public class Zoom_fragment extends AppCompatActivity
{

   ImageView imagezoom;
String url="",UploadFrom;
    Bitmap decodeimage=null;
    @Nullable

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_fragment);
        imagezoom=(ImageView)findViewById(R.id.imageView14);
        Intent intent=getIntent();
       // UploadFrom=intent.getStringExtra("UploadFrom");
        url=intent.getStringExtra("image_url");

//        Picasso.with(getApplicationContext()).load(url).resize(100,100)
//                .onlyScaleDown()
//                .placeholder(R.drawable.dummy)
//                .error(R.drawable.dummy)
//                .into(imagezoom);

            Picasso.with(getApplicationContext()).load(url).fit().into(imagezoom);

            PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imagezoom);
            pAttacher.update();



    }
        @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
