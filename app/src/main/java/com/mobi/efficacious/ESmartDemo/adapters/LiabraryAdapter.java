package com.mobi.efficacious.ESmartDemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mobi.efficacious.ESmartDemo.R;
import com.mobi.efficacious.ESmartDemo.entity.Liabrary;

import java.util.ArrayList;


public class LiabraryAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    //private final ArrayList<Liabrary> itemsArrayList;
    ArrayList<Liabrary> menus = new ArrayList<Liabrary>();
    public ArrayList<Liabrary> categories;
    public ArrayList<Liabrary> orig;

    public LiabraryAdapter(Context context, ArrayList<Liabrary> Menus) {
        super();
        this.context = context;
        this.menus = Menus;
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Liabrary> results = new ArrayList<Liabrary>();
                if (orig == null)
                    orig = menus;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Liabrary g : orig) {
                            if (g.getBookDetail().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                menus = (ArrayList<Liabrary>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.liabrary_row, parent, false);
        TextView name=(TextView)row.findViewById(R.id.bookname_liabrary);
        TextView assigneddate = (TextView)row.findViewById(R.id.assignedDate_liabrary);
        TextView returneddate = (TextView)row.findViewById(R.id.returnedDate_liabrary);
        name.setText(menus.get(position).getBookDetail());
        assigneddate.setText(menus.get(position).getAssignedDate());
        returneddate.setText(menus.get(position).getReturnDate());

        return row;
    }
}
