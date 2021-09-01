package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Gallery;
import com.mobi.efficacious.ESmartDemo.fragment.Zoom_fragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter  extends ArrayAdapter<Gallery> {

    private final Context context;
    private final ArrayList<Gallery> itemsArrayList;
String url;
    byte[] data1=null;
   public static Bitmap decodedImage;
    public GalleryAdapter(Context context, ArrayList<Gallery> itemsArrayList) {
        super(context, R.layout.holiday_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemsArrayList.clear();
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos=position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.gallery_adapter, parent, false);

        ImageView image=(ImageView)rowView.findViewById(R.id.imageView5);
        TextView imagedescription=(TextView)rowView.findViewById(R.id.eventDescriptiontv);
//        if(itemsArrayList.get(position).getUplaoedFrom().contentEquals("web"))
//        {
//            data1 = Base64.decode(itemsArrayList.get(position).getImageBase64(), Base64.DEFAULT);
//            try {
//                String text = new String(data1, "UTF-8");
//                int len=  data1.length;
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            decodedImage = BitmapFactory.decodeByteArray(data1, 0, data1.length);
//            image.setImageBitmap(decodedImage);
//            imagedescription.setText(itemsArrayList.get(position).getImageDescription());
//
//        }else
        {
            url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(position).getImageName()+"";
            Picasso.with(context).load(url).fit()
                    .placeholder(R.drawable.dummy)
                    .error(R.drawable.dummy)
                    .into(image);
            imagedescription.setText(itemsArrayList.get(position).getImageDescription());
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="";
//                if(itemsArrayList.get(pos).getUplaoedFrom().contentEquals("web"))
//                {
//                    url="https://eserveshiksha.co.in/ESmartDemo/ESmartDemoDemoServices/UploadImages/"+itemsArrayList.get(pos).getImageName()+"";
//                    Intent intent = new Intent(context, Zoom_fragment.class);
//                    intent.putExtra("image_url",url);
//                    intent.putExtra("UploadFrom","web");
//                    context.startActivity(intent);
//
//                }else
                {
                    url="https://e-smarts.net/Demo/DEMOServices/UploadImages/"+itemsArrayList.get(pos).getImageName()+"";
                    Intent intent = new Intent(context, Zoom_fragment.class);
                    intent.putExtra("image_url",url);
                   // intent.putExtra("UploadFrom","Mobile");
                    context.startActivity(intent);
                }

            }
        });
        return rowView;
    }
}