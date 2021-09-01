package com.mobi.efficacious.ESmartDemo.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mobi.efficacious.ESmartDemo.R;


public class CustomInfoWindowAdapterMap implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapterMap(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_window_map, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        TextView tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
//        String title=marker.getTitle();

        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());

        return view;
    }
}
