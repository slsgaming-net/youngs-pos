package com.example.youngs_carryout_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PintQuartFragment extends Fragment {

    private String category;

    private GridView gridView;

    private ArrayList<PintQuartFood> foodList = new ArrayList<>();

    private PintQuartFoodAdapter pintQuartFoodAdapter;

    public void addItem(PintQuartFood food) {
        foodList.add(food);
    }

    public void setCategory(String str) {
        category = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        gridView = view.findViewById(R.id.fragment1_gv);

        pintQuartFoodAdapter = new PintQuartFoodAdapter(getActivity(), foodList);

        gridView.setAdapter(pintQuartFoodAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PintQuartFood selectedFood = foodList.get(position);
                final Order order = new Order();
                boolean hasDuplicate = false;

                if (selectedFood.getPintPrice() == 0.00) { // there is no pint size for the food

                    selectedFood.setPrice(selectedFood.getQuartPrice());

                    order.setCategory(category);
                    order.setFood(selectedFood);
                    order.setBase_price(selectedFood.getPrice());

                    // check to see if order is already in the order list
                    for (int i = 0; i < ((MainActivity) getActivity()).getOrder_list().size(); i++) {
                        if (order.isEqual(((MainActivity) getActivity()).getOrder_list().get(i))) {
                            Order o = ((MainActivity) getActivity()).getOrder_list().get(i);
                            if (((MainActivity) getActivity()).getOrder_list().get(i).getQuantity() < 99) {
                                ((MainActivity) getActivity()).getOrder_list().get(i).setQuantity(o.getQuantity() + 1);
                                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                ((MainActivity) getActivity()).UpdateTotal();
                            }
                            hasDuplicate = true;
                            break;
                        }
                    }
                    if (!hasDuplicate) {
                        order.setQuantity(1);
                        order.setBase_price(selectedFood.getPrice());
                        ((MainActivity) getActivity()).addItem(order);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                        ((MainActivity) getActivity()).UpdateTotal();
                    }
                } else {
                    // create an alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View customLayout = View.inflate(getContext(), R.layout.fragment_pint_quart, null);

                    TextView food_name = customLayout.findViewById(R.id.name);
                    food_name.setText(selectedFood.getName());

                    final Button pint_button = customLayout.findViewById(R.id.pint_button);

                    Button quart_button = customLayout.findViewById(R.id.quart_button);

                    builder.setView(customLayout);

                    final AlertDialog pintQuartDialog = builder.create();
                    pintQuartDialog.setCancelable(true);
                    pintQuartDialog.show();

                    pint_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean hasDuplicate = false;

                            PintQuartFood selectedFoodTemp = new PintQuartFood();
                            selectedFoodTemp.setName("Pt " + selectedFood.getName());
                            selectedFoodTemp.setAbbrevName("PT " + selectedFood.getAbbrevName());
                            if (!selectedFood.getChineseName().equals("null")) {
                                selectedFoodTemp.setChineseName("半个" + selectedFood.getChineseName());
                            }
                            selectedFoodTemp.setType(selectedFood.getType());
                            selectedFoodTemp.setPrice(selectedFood.getPintPrice());


                            //selectedFood.setName("Pt " + selectedFood.getName());
                            //selectedFood.setAbbrevName("PT " + selectedFood.getAbbrevName());

                            // initialize the food
                            order.setFood(selectedFoodTemp);
                            //order.getFood().setName("Pt " + selectedFood.getName());
                            //order.getFood().setAbbrevName("PT " + selectedFood.getAbbrevName());
                            order.setCategory(category);
                            order.setBase_price(selectedFoodTemp.getPintPrice());

                            // check to see if order is already in the order list
                            for (int i = 0; i < ((MainActivity) getActivity()).getOrder_list().size(); i++) {
                                if (order.isEqual(((MainActivity) getActivity()).getOrder_list().get(i))) {
                                    Order o = ((MainActivity) getActivity()).getOrder_list().get(i);
                                    if (((MainActivity) getActivity()).getOrder_list().get(i).getQuantity() < 99) {
                                        ((MainActivity) getActivity()).getOrder_list().get(i).setQuantity(o.getQuantity() + 1);
                                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                        ((MainActivity) getActivity()).UpdateTotal();
                                    }
                                    hasDuplicate = true;
                                    break;
                                }
                            }
                            if (!hasDuplicate) {
                                order.setQuantity(1);
                                order.setBase_price(selectedFoodTemp.getPrice());
                                ((MainActivity) getActivity()).addItem(order);
                                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                ((MainActivity) getActivity()).UpdateTotal();

                            }
                            pintQuartDialog.dismiss();
                        }
                    });

                    quart_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean hasDuplicate = false;

                            PintQuartFood selectedFoodTemp = new PintQuartFood();
                            selectedFoodTemp.setName("Qt " + selectedFood.getName());
                            selectedFoodTemp.setAbbrevName("QT " + selectedFood.getAbbrevName());
                            if (!selectedFood.getChineseName().equals("null")) {
                                selectedFoodTemp.setChineseName("大" + selectedFood.getChineseName());
                            }
                            selectedFoodTemp.setType(selectedFood.getType());
                            selectedFoodTemp.setPrice(selectedFood.getQuartPrice());

                            // initialize the food
                            order.setFood(selectedFoodTemp);

                            order.setCategory(category);
                            order.setBase_price(selectedFoodTemp.getQuartPrice());

                            // check to see if order is already in the order list
                            for (int i = 0; i < ((MainActivity) getActivity()).getOrder_list().size(); i++) {
                                if (order.isEqual(((MainActivity) getActivity()).getOrder_list().get(i))) {
                                    Order o = ((MainActivity) getActivity()).getOrder_list().get(i);
                                    if (((MainActivity) getActivity()).getOrder_list().get(i).getQuantity() < 99) {
                                        ((MainActivity) getActivity()).getOrder_list().get(i).setQuantity(o.getQuantity() + 1);
                                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                        ((MainActivity) getActivity()).UpdateTotal();
                                    }
                                    hasDuplicate = true;
                                    break;
                                }
                            }
                            if (!hasDuplicate) {
                                order.setQuantity(1);
                                order.setBase_price(selectedFoodTemp.getPrice());
                                ((MainActivity) getActivity()).addItem(order);
                                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                ((MainActivity) getActivity()).UpdateTotal();
                            }
                            pintQuartDialog.dismiss();
                        }
                    });
                }
            }
        });
        return view;
    }
}