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

public class PintQuartFoodAdapter extends BaseAdapter {

    Activity context;
    // array containing a list of food
    ArrayList<PintQuartFood> food_list;
    private static LayoutInflater inflater = null;

    public PintQuartFoodAdapter(Activity context, ArrayList<PintQuartFood> list) {
        this.context = context;
        this.food_list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return food_list.size();
    }

    @Override
    public Food getItem(int position) {
        return food_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.name_pint_quart_price, null): itemView;

        TextView textName = itemView.findViewById(R.id.name);
        TextView textPintPrice = itemView.findViewById(R.id.pint_price);
        TextView textQuartPrice = itemView.findViewById(R.id.quart_price);

        PintQuartFood selectedFood = food_list.get(position);
        textName.setText(selectedFood.getName());

        // format the price into 2 decimal places
        String pint_price = String.format(Locale.US,"%.2f", selectedFood.getPintPrice());
        pint_price = '$' + pint_price;
        textPintPrice.setText(pint_price);
        String quart_price = String.format(Locale.US,"%.2f", selectedFood.getQuartPrice());
        quart_price = '$' + quart_price;
        textQuartPrice.setText(quart_price);

        if (selectedFood.getPintPrice() == 0.00) {
            textPintPrice.setVisibility(View.INVISIBLE);
        }

        return itemView;
    }

}

