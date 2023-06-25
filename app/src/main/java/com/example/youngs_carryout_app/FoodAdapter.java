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

public class FoodAdapter extends BaseAdapter {

    Activity context;
    // array containing a list of food
    ArrayList<Food> food_list;
    private static LayoutInflater inflater = null;

    public FoodAdapter(Activity context, ArrayList<Food> list) {
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
        itemView = (itemView == null) ? inflater.inflate(R.layout.name_price, null): itemView;

        TextView textName = itemView.findViewById(R.id.text1);
        TextView textPrice = itemView.findViewById(R.id.text2);

        Food selectedFood = food_list.get(position);
        textName.setText(selectedFood.getName());

        // format the price into 2 decimal places
        String price = String.format(Locale.US,"%.2f", selectedFood.getPrice());
        price = '$' + price;
        textPrice.setText(price);
        return itemView;
    }
}

