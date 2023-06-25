package com.example.youngs_carryout_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;

public class FoodFragment extends Fragment {

    private String category;

    private GridView gridView;

    private ArrayList<Food> foodList = new ArrayList<>();

    private FoodAdapter foodAdapter;

    public void addItem(Food food) {
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

        foodAdapter = new FoodAdapter(getActivity(), foodList);

        gridView.setAdapter(foodAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // add the item to the order
                Order order = new Order();
                order.setCategory(category);
                Food selectedFood = foodList.get(position);

                // initialize the food
                order.setFood(selectedFood);
                order.setBase_price(selectedFood.getPrice());

                //order.setFood(foodList.get(position));
                boolean hasDuplicate = false;

                // check to see if order is already in the order list
                for (int i = 0; i < ((MainActivity) getActivity()).getOrder_list().size(); i++) {
                    if (order.isEqual(((MainActivity) getActivity()).getOrder_list().get(i))) {
                        Order o = ((MainActivity) getActivity()).getOrder_list().get(i);
                        ((MainActivity) getActivity()).getOrder_list().get(i).setQuantity(o.getQuantity() + 1);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                        ((MainActivity) getActivity()).UpdateTotal();
                        hasDuplicate = true;
                        break;
                    }
                }

                if (!hasDuplicate) {
                    if (category.equals("Lunch Combinations")) {
                        order.getFood().setName("Lunch " + foodList.get(position).getName());
                    }
                    order.setQuantity(1);
                    order.setBase_price(foodList.get(position).getPrice());
                    ((MainActivity) getActivity()).addItem(order);
                    ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    ((MainActivity) getActivity()).UpdateTotal();
                }
            }
        });
        return view;
    }
}
