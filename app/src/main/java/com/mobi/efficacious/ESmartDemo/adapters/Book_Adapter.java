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
import com.mobi.efficacious.ESmartDemo.entity.Book_library;

import java.util.ArrayList;


public class Book_Adapter extends BaseAdapter implements Filterable {

    private final Context context;
    ArrayList<Book_library> menus = new ArrayList<Book_library>();
    public ArrayList<Book_library> categories;
    public ArrayList<Book_library> orig;

    public Book_Adapter(Context context, ArrayList<Book_library> Menus) {
        super();
        this.context = context;
        this.menus = Menus;
    }

    static class ImageHolder
    {
        TextView accsion_no;
        TextView book_name;
        TextView Book_price;
        TextView book_edition;
        TextView book_author;
        TextView book_lang;

    }
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Book_library> results = new ArrayList<Book_library>();
                if (orig == null)
                    orig = menus;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Book_library g : orig) {
                            if (g.getBook_name().toLowerCase()
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
                menus = (ArrayList<Book_library>) results.values;
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
        View row = convertView;
        ImageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.book_detail_library, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }

        holder.accsion_no = (TextView) row.findViewById(R.id.accsion_no);
        holder.book_name = (TextView) row.findViewById(R.id.bookname);
        holder.Book_price = (TextView) row.findViewById(R.id.price);
        holder.book_edition = (TextView) row.findViewById(R.id.book_edition);
        holder.book_author = (TextView) row.findViewById(R.id.book_author);
        holder.book_lang = (TextView) row.findViewById(R.id.book_lang);

        holder.accsion_no.setText("Accsion No:"+menus.get(position).getAccsion_no());
        holder.book_name.setText("Name:"+menus.get(position).getBook_name());
        holder.Book_price.setText("Book Price:"+menus.get(position).getBook_price());
        holder.book_edition.setText("Edition:"+menus.get(position).getBook_edition());
        holder.book_author.setText("Author:"+menus.get(position).getBook_author());
        holder.book_lang.setText("Lang:"+menus.get(position).getBook_lang());

        return row;
    }

}