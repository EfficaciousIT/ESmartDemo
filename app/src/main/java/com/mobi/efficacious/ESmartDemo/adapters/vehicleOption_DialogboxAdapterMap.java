package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.ESmartDemo.MapActivity.Live_Tracking_Map_old;
import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.dialog_boxMap;

import java.util.ArrayList;


public class vehicleOption_DialogboxAdapterMap extends ArrayAdapter<dialog_boxMap> {
    private ArrayList<dialog_boxMap> objects;
    private Context context;

    public vehicleOption_DialogboxAdapterMap(Context context, ArrayList<dialog_boxMap> objects) {
        super(context, R.layout.custom_dialog_adaptermap,objects);
        this.objects = objects;
        this.context = context;
    }

    static class ImageHolder
    {
        TextView textView;


    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_dialog_adaptermap, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        holder.textView = (TextView) row.findViewById(R.id.textView38);
        holder.textView.setText(objects.get(position).getVehicle_no());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicle_id_selected=objects.get(position).getVehicle_id();
                if(vehicle_id_selected.contentEquals("No Data Available"))
                {
                    Toast.makeText(context,"No data available",Toast.LENGTH_SHORT).show();
                }else
                {
                    Intent intent=new Intent(context,Live_Tracking_Map_old.class);
//                Intent intent=new Intent(context,Live_Tracking_Map.class);
                    intent.putExtra("vehicle_id_selected",vehicle_id_selected);
                    context.startActivity(intent);


                }

            }
        });

        return row;
    }

}
