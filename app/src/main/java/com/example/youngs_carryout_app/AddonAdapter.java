package com.example.youngs_carryout_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class AddonAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Food> list;

    private static LayoutInflater inflater = null;

    public AddonAdapter(Activity context, ArrayList<Food> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {return list.size();}

    @Override
    public Food getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.addon_layout, null): itemView;

        TextView name_textView = itemView.findViewById(R.id.name_text);

        TextView price_textView = itemView.findViewById(R.id.price_text);

        Food selectedFood = list.get(position);

        name_textView.setText(selectedFood.getName());

        String price = String.format(Locale.US,"%.2f", selectedFood.getPrice());

        price_textView.setText(price);

        return itemView;
    }
}
