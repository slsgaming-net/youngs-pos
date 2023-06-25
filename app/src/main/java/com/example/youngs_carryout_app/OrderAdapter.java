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

public class OrderAdapter extends BaseAdapter {

    private Activity context;

    private ArrayList<Order> orders;

    private final static String defaultRice = "plain fried rice";

    private final static String defaultRoll = "egg roll";


    private static LayoutInflater inflater = null;

    public OrderAdapter(Activity context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.order_layout, null): itemView;

        TextView textName = itemView.findViewById(R.id.item);
        TextView textPrice = itemView.findViewById(R.id.price);
        TextView textQuantity = itemView.findViewById(R.id.quantity);

        TextView riceNote = itemView.findViewById(R.id.rice_note);
        TextView rollNote = itemView.findViewById(R.id.roll_note);
        TextView sauceNote = itemView.findViewById(R.id.sauce_note);
        TextView spiceNote = itemView.findViewById(R.id.spice_text);
        TextView addOnsNote = itemView.findViewById(R.id.addOnsTextView);
        TextView noItemsNote = itemView.findViewById(R.id.noItemsTextView);
        TextView lightItemsNote = itemView.findViewById(R.id.lightItemsTextView);
        TextView onlyItemsNote = itemView.findViewById(R.id.onlyItemsTextView);
        TextView instructionNote = itemView.findViewById(R.id.instruction_note);

        Order selectedOrder = orders.get(position);

        // Combination
        riceNote.setText(selectedOrder.getRice());
        rollNote.setText(selectedOrder.getRoll());
        sauceNote.setText(selectedOrder.getSauce());
        spiceNote.setText(selectedOrder.getSpice());


        StringBuilder addOns_str = new StringBuilder();
        //addOnsNote.setText(addOns_str);
        //addOns_str.append("extra ");
        // addOns list
        for (int i = 0; i < selectedOrder.getAdd_on().size(); i++) {
            if (i == selectedOrder.getAdd_on().size() - 1) {
                String name = selectedOrder.getAdd_on().get(i).getName();
                String price = String.format(Locale.US, "%.2f",
                        selectedOrder.getAdd_on().get(i).getPrice());
                String str = "add " + name + " (" + price + ")";
                addOns_str.append(str);
            }
            else {
                String name = selectedOrder.getAdd_on().get(i).getName();
                String price = String.format(Locale.US, "%.2f",
                        selectedOrder.getAdd_on().get(i).getPrice());
                String str = "add " + name + " (" + price + ")";
                addOns_str.append(str);
                addOns_str.append("\n");
            }
        }
        addOnsNote.setText(addOns_str);

        // No list
        StringBuilder noItems_str = new StringBuilder();
        for (int i = 0; i < selectedOrder.getNo_list().size(); i++) {
            if (i == selectedOrder.getNo_list().size() - 1) {
                noItems_str.append("No ");
                noItems_str.append(selectedOrder.getNo_list().get(i));
            }
            else {
                noItems_str.append("No ");
                noItems_str.append(selectedOrder.getNo_list().get(i));
                noItems_str.append("\n");
            }
        }
        noItemsNote.setText(noItems_str);

        // Light List
        StringBuilder lightItems_str = new StringBuilder();
        for (int i = 0; i < selectedOrder.getLight_list().size(); i++) {
            if (i == selectedOrder.getLight_list().size() - 1) {
                lightItems_str.append("Light ");
                lightItems_str.append(selectedOrder.getLight_list().get(i));
            }
            else {
                lightItems_str.append("Light ");
                lightItems_str.append(selectedOrder.getLight_list().get(i));
                lightItems_str.append("\n");
            }
        }
        lightItemsNote.setText(lightItems_str);

        // Only List
        StringBuilder onlyItems_str = new StringBuilder();
        onlyItems_str.append("Only ");
        for (int i = 0; i < selectedOrder.getOnly_list().size(); i++) {
            if (i == selectedOrder.getOnly_list().size() - 1) {
                onlyItems_str.append(selectedOrder.getOnly_list().get(i));
            }
            else {
                onlyItems_str.append(selectedOrder.getOnly_list().get(i));
                onlyItems_str.append(", ");
            }
        }
        onlyItemsNote.setText(onlyItems_str);

        // Instruction note
        StringBuilder instruction_str = new StringBuilder();
        for (String instr: selectedOrder.getInstruction_list()) {
            instruction_str.append(instr + "\n");
        }
        instructionNote.setText(instruction_str);

        // Visibility of TextViews
        if (riceNote.getText().equals(defaultRice)) {
            riceNote.setVisibility(View.GONE);
        }
        else {
            riceNote.setVisibility(View.VISIBLE);
        }

        if (rollNote.getText().equals(defaultRoll)) {
            rollNote.setVisibility(View.GONE);
        }
        else {
            rollNote.setVisibility(View.VISIBLE);
        }

        if (sauceNote.getText().equals("Normal")) {
            sauceNote.setVisibility(View.GONE);
        }
        else {
            sauceNote.setVisibility(View.VISIBLE);
        }

        if (spiceNote.getText().equals("Normal")) {
            spiceNote.setVisibility(View.GONE);
        }
        else {
            spiceNote.setVisibility(View.VISIBLE);
        }
        if (addOnsNote.getText().equals("")) {
            addOnsNote.setVisibility(View.GONE);
        }
        else {
            addOnsNote.setVisibility(View.VISIBLE);
        }
        if (noItemsNote.getText().equals("")) {
            noItemsNote.setVisibility(View.GONE);
        }
        else {
            noItemsNote.setVisibility(View.VISIBLE);
        }
        if (lightItemsNote.getText().equals("")) {
            lightItemsNote.setVisibility(View.GONE);
        }
        else {
            lightItemsNote.setVisibility(View.VISIBLE);
        }
        if (onlyItemsNote.getText().equals("Only ")) {
            onlyItemsNote.setVisibility(View.GONE);
        }
        else {
            onlyItemsNote.setVisibility(View.VISIBLE);
        }
        if (!instructionNote.getText().equals("")) {
            instructionNote.setVisibility(View.VISIBLE);
        }
        else {
            instructionNote.setVisibility(View.GONE);
        }

        String quantity_item = Integer.toString(selectedOrder.getQuantity());

        textQuantity.setText(quantity_item);

        textName.setText(selectedOrder.getFood().getName());

        double total = (selectedOrder.getBase_price() + selectedOrder.getAdd_on_price()) *
                selectedOrder.getQuantity();

        // format the price into 2 decimal places
        String price = String.format(Locale.US, "%.2f", total);
        textPrice.setText(price);

        return itemView;
    }
}
