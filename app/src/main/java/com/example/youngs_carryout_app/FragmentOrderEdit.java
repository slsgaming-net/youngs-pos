package com.example.youngs_carryout_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class FragmentOrderEdit extends Fragment {

    private Order order;
    private int counter;

    // substitute fried rice charge
    private static double EGG_FR_CHARGE = 2.00;
    private static double CH_FR_CHARGE = 4.00;
    private static double PORK_FR_CHARGE = 4.00;
    private static double VEG_FR_CHARGE = 4.00;
    private static double BEEF_FR_CHARGE = 5.00;
    private static double SHR_FR_CHARGE = 5.00;
    private static double YNGS_FR_CHARGE = 5.50;

    // substitute price for sauce on side
    private static double SOS_CHARGE = 0.35;

    // string values of rice for order note
    private final static String WHITE_RICE = "white rice";
    private final static String PLAIN_FR = "plain fried rice";
    private final static String EGG_FR = "egg fried rice ("
            + String.format(Locale.US, "%.2f", EGG_FR_CHARGE) + ")";
    private final static String CHICKEN_FR = "chicken fried rice ("
            + String.format(Locale.US, "%.2f", CH_FR_CHARGE) + ")";
    private final static String SHRIMP_FR = "shrimp fried rice ("
            + String.format(Locale.US, "%.2f", SHR_FR_CHARGE) + ")";
    private final static String PORK_FR = "pork fried rice ("
            + String.format(Locale.US, "%.2f", PORK_FR_CHARGE) + ")";
    private final static String BEEF_FR = "beef fried rice (" +
            String.format(Locale.US, "%.2f", BEEF_FR_CHARGE) + ")";
    private final static String VEG_FR = "veg fried rice ("
            + String.format(Locale.US, "%.2f", VEG_FR_CHARGE) + ")";
    private final static String YNGS_FR = "Young's fried rice ("
            + String.format(Locale.US, "%.2f", YNGS_FR_CHARGE) + ")";

    // string values for rolls and sauce on side
    private final static String SOS_STR = "sauce on side ("
            + String.format(Locale.US, "%.2f", SOS_CHARGE) + ")";
    private final static String EGG_ROLL = "egg roll";
    private final static String SPRING_ROLL = "spring roll";

    private String[] extra_list;

    private String rice;
    private String roll;
    private String sauce;
    private String spice;

    private String[] ItemList;
    private String[] onlyItemList;
    private String[] instructionList;

    private boolean[] noListCheckedItems;
    private boolean[] lightListCheckedItems;
    private boolean[] onlyListCheckedItems;
    private boolean[] instructionChecked;

    private ArrayList<Integer> noItemListSelected;
    private ArrayList<Integer> lightItemListSelected;
    private ArrayList<Integer> onlyItemListSelected;
    private ArrayList<Integer> instructionSelected;

    final int MAX_COUNT = 99;

    // this is used to keep track the change in price for side upgrades
    private double prePriceCharge = 0.00;

    Button negativeButton;
    Button positiveButton;

    AddonAdapter addonAdapter;

    TextView tv_quantity;

    TextView riceTextView;
    TextView rollTextView;
    TextView sauceTextView;
    TextView spiceTextView;

    Spinner riceSpinner;
    Spinner rollSpinner;
    Spinner sauceSpinner;
    Spinner spiceSpinner;

    ListView extra_listView;

    EditText addon_price_et;
    TextView addon_tv;


    public void setOrder(Order order_in) {
        order = order_in;
    }

    public FragmentOrderEdit() {
        // Required empty public constructor
    }

    private double getPrevPrice(String rice) {

        if (order.getRice().equals(EGG_FR)) {
            return EGG_FR_CHARGE;
        }

        if (order.getRice().equals(CHICKEN_FR)) {
            return CH_FR_CHARGE;
        }
        if (order.getRice().equals(PORK_FR)) {
            return PORK_FR_CHARGE;
        }

        if (order.getRice().equals(VEG_FR)) {
            return VEG_FR_CHARGE;
        }
        if (order.getRice().equals(BEEF_FR)) {
            return BEEF_FR_CHARGE;
        }
        if (order.getRice().equals(SHRIMP_FR)) {
            return SHR_FR_CHARGE;
        }
        if (order.getRice().equals(YNGS_FR)) {
            return YNGS_FR_CHARGE;
        }
        return 0.00;
    }

    public void ShowExtraBox(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customLayout = View.inflate(getContext(), R.layout.fragment_dialog, null);

        String[] addOnsCategory = getResources().getStringArray(R.array.AddOnCategories);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.alert_layout, addOnsCategory);

        final GridView gridView = customLayout.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        builder.setView(customLayout);
        builder.setTitle("EXTRA ADDONS");
        final AlertDialog dialog = builder.create();

        dialog.show();

        // selecting the category
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder category_builder = new AlertDialog.Builder(view.getContext());

                View customLayout = View.inflate(getContext(), R.layout.fragment_fragment1, null);
                // default initialization
                extra_list = getResources().getStringArray(R.array.AddOnCategories);
                ArrayAdapter<String> extraAdapter = new ArrayAdapter<>(
                        getActivity(), R.layout.listview_layout, extra_list);

                final GridView categoryGridView = customLayout.findViewById(R.id.fragment1_gv);
                category_builder.setView(customLayout);
                final AlertDialog categoryDialog = category_builder.create();

                if (position == 0) { // meat
                    extra_list = getResources().getStringArray(R.array.AddOnMeat);
                    extraAdapter = new ArrayAdapter<>(
                            getActivity(), R.layout.listview_layout, extra_list);
                } else if (position == 1) { // vegetable
                    extra_list = getResources().getStringArray(R.array.AddOnVegetable);
                    extraAdapter = new ArrayAdapter<>(
                            getActivity(), R.layout.listview_layout, extra_list);
                } else if (position == 2) {
                    extra_list = getResources().getStringArray(R.array.AddOnMisc);
                    extraAdapter = new ArrayAdapter<>(
                            getActivity(), R.layout.listview_layout, extra_list);
                }
                categoryGridView.setAdapter(extraAdapter);
                categoryDialog.show();


                categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();

                        // alert dialog builder
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                        final View customLayout2 = View.inflate(getContext(), R.layout.dialog_price, null);

                        String item = extra_list[position];
                        addon_tv = customLayout2.findViewById(R.id.dialog_title);
                        addon_price_et = customLayout2.findViewById(R.id.price_input);
                        addon_tv.setText(item);
                        builder2.setView(customLayout2);
                        final AlertDialog priceDialog = builder2.create();
                        priceDialog.setCancelable(false);
                        priceDialog.show();

                        positiveButton = customLayout2.findViewById(R.id.dialog_pos_btn);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Double.parseDouble(addon_price_et.getText().toString());
                                    double price = Double.valueOf(addon_price_et.getText().toString());
                                    Food food = new Food(addon_tv.getText().toString(), "null", "null", "null", price);
                                    order.addAddOns(food);
                                    order.setAdd_on_price(order.getAdd_on_price() + price);
                                    addonAdapter.notifyDataSetChanged();
                                    ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                                    ((MainActivity) getActivity()).UpdateTotal();
                                    priceDialog.dismiss();

                                } catch (NumberFormatException ex) {
                                    ((MainActivity) getActivity()).showToast("Please Enter a price in the correct format.");
                                }

                            }
                        });

                        negativeButton = customLayout2.findViewById(R.id.dialog_negative_btn);

                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                priceDialog.dismiss();
                            }
                        });

                        // closes the category dialog
                        categoryDialog.dismiss();
                    }
                });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_order_edit, container, false);

        extra_listView = view.findViewById(R.id.extra_list);
        addonAdapter = new AddonAdapter(getActivity(), order.getAdd_on());
        extra_listView.setAdapter(addonAdapter);

        extra_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double price = order.getAdd_on().get(position).getPrice();
                order.getAdd_on().remove(position);
                order.setAdd_on_price(order.getAdd_on_price() - price);
                addonAdapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                ((MainActivity) getActivity()).UpdateTotal();
            }
        });

        //--------------BUTTON DECLARATIONS----------------------//
        Button noButton = view.findViewById(R.id.noButton);
        Button lightButton = view.findViewById(R.id.lightButton);
        Button onlyButton = view.findViewById(R.id.onlyButton);
        Button extraButton = view.findViewById(R.id.extraButton);
        Button specialButton = view.findViewById(R.id.specialButton);
        Button plusButton = view.findViewById(R.id.plusButton);
        Button minusButton = view.findViewById(R.id.minusButton);
        Button removeButton = view.findViewById(R.id.removeButton);
        Button clearExtraButton = view.findViewById(R.id.clearExtraButton);
        Button newOrderButton = view.findViewById(R.id.newOrderButton);

        //-----------SPINNERS DECLARATIONS----------------------------//
        riceSpinner = view.findViewById(R.id.rice_sp);
        ArrayList<String> rice_options = new ArrayList<>();
        rice_options.add("Plain");
        rice_options.add("White");
        rice_options.add("Egg");
        rice_options.add("Chicken");
        rice_options.add("Pork");
        rice_options.add("Vegetable");
        rice_options.add("Shrimp");
        rice_options.add("Beef");
        rice_options.add("Young's");
        ArrayAdapter<String> rice_options_adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_list, rice_options);
        riceSpinner.setAdapter(rice_options_adapter);

        rollSpinner = view.findViewById(R.id.roll_sp);
        ArrayList<String> roll_options = new ArrayList<>();
        roll_options.add("Egg Roll");
        roll_options.add("Spring Roll");
        ArrayAdapter<String> roll_options_adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_list, roll_options);
        rollSpinner.setAdapter(roll_options_adapter);


        sauceSpinner = view.findViewById(R.id.sauce_sp);
        ArrayList<String> sauce_options = new ArrayList<>();
        sauce_options.add("Normal");
        sauce_options.add("S.o.S");
        sauce_options.add("None");
        ArrayAdapter<String> sauce_options_adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_list, sauce_options);
        sauceSpinner.setAdapter(sauce_options_adapter);

        spiceSpinner = view.findViewById(R.id.spice_sp);
        ArrayList<String> spice_options = new ArrayList<>();
        spice_options.add("Normal");
        spice_options.add("Mild");
        spice_options.add("Medium");
        spice_options.add("Hot");
        spice_options.add("Extra Hot");
        spice_options.add("No Spice");
        ArrayAdapter<String> spice_options_adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_list, spice_options);
        spiceSpinner.setAdapter(spice_options_adapter);


        HashMap<String, String> map = new HashMap<>();
        map.put(CHICKEN_FR, "Chicken");
        map.put(WHITE_RICE, "White");
        map.put(BEEF_FR, "Beef");
        map.put(SHRIMP_FR, "Shrimp");
        map.put(PORK_FR, "Pork");
        map.put(VEG_FR, "Vegetable");
        map.put(YNGS_FR, "Young's");
        map.put(PLAIN_FR, "Plain");
        map.put(EGG_FR, "Egg");

        map.put("Normal", "Normal");
        map.put(SOS_STR, "S.o.S");
        map.put("no sauce", "None");

        map.put(EGG_ROLL, "Egg Roll");
        map.put(SPRING_ROLL, "Spring Roll");

        map.put("spice: mild", "Mild");
        map.put("spice: medium", "Medium");
        map.put("spice: hot", "Hot");
        map.put("spice: extra hot", "Extra Hot");
        map.put("no spice", "No Spice");

        int rice_position = rice_options_adapter.getPosition(map.get(order.getRice()));
        riceSpinner.setSelection(rice_position);
        int roll_position = roll_options_adapter.getPosition(map.get(order.getRoll()));
        rollSpinner.setSelection(roll_position);
        int sauce_position = sauce_options_adapter.getPosition(map.get(order.getSauce()));
        sauceSpinner.setSelection(sauce_position);
        int spice_position = spice_options_adapter.getPosition(map.get(order.getSpice()));
        spiceSpinner.setSelection(spice_position);

        //----------------------END OF SPINNERS---------------------------------//

        riceTextView = view.findViewById(R.id.rice_note);
        rollTextView = view.findViewById(R.id.roll_note);
        sauceTextView = view.findViewById(R.id.sauce_note);
        spiceTextView = view.findViewById(R.id.spice_text);

        // this spinner sets the sides up charge and upgrades the total price
        riceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                prePriceCharge = getPrevPrice(order.getRice());

                switch (position) {
                    case (0): // Plain Fried Rice
                        rice = PLAIN_FR;
                        order.setAdd_on_price(order.getAdd_on_price() - prePriceCharge);
                        prePriceCharge = 0.00;
                        break;

                    case (1): // White Rice
                        rice = WHITE_RICE;
                        order.setAdd_on_price(order.getAdd_on_price() - prePriceCharge);
                        prePriceCharge = 0.00;
                        break;

                    case (2): // Egg Fried Rice
                        rice = EGG_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + EGG_FR_CHARGE
                        );
                        prePriceCharge = EGG_FR_CHARGE;
                        break;

                    case (3): // Chicken Fried Rice
                        rice = CHICKEN_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + CH_FR_CHARGE
                        );
                        prePriceCharge = CH_FR_CHARGE;
                        break;

                    case (4): // Pork Fried Rice
                        rice = PORK_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + PORK_FR_CHARGE
                        );
                        prePriceCharge = PORK_FR_CHARGE;
                        break;

                    case (5): // Vegetable Fried Rice
                        rice = VEG_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + VEG_FR_CHARGE
                        );
                        prePriceCharge = VEG_FR_CHARGE;
                        break;

                    case (6): // Shrimp Fried Rice
                        rice = SHRIMP_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + SHR_FR_CHARGE
                        );
                        prePriceCharge = SHR_FR_CHARGE;
                        break;

                    case (7): // Beef Fried Rice
                        rice = BEEF_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + BEEF_FR_CHARGE
                        );
                        prePriceCharge = BEEF_FR_CHARGE;
                        break;

                    case (8): // Young's Fried Rice
                        rice = YNGS_FR;
                        order.setAdd_on_price(
                                order.getAdd_on_price() - prePriceCharge + YNGS_FR_CHARGE
                        );
                        prePriceCharge = YNGS_FR_CHARGE;
                        break;

                }

                order.setRice(rice);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                ((MainActivity) getActivity()).UpdateTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rollSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    roll = EGG_ROLL;
                }
                if (position == 1) {
                    roll = SPRING_ROLL;
                }
                order.setRoll(roll);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sauceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sauce = "Normal";
                    if (order.getSauce().equals(SOS_STR)) {
                        order.setAdd_on_price(order.getAdd_on_price() - SOS_CHARGE);
                        order.setSauce(sauce);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                        ((MainActivity) getActivity()).UpdateTotal();
                    }
                    else {
                        order.setSauce(sauce);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                }
                else if (position == 1) {
                    sauce = SOS_STR;
                    if (!order.getSauce().equals(SOS_STR)) {
                        order.setAdd_on_price(order.getAdd_on_price() + SOS_CHARGE);
                        order.setSauce(sauce);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                        ((MainActivity) getActivity()).UpdateTotal();
                    }
                }

                else if (position == 2) {
                    sauce = "no sauce";
                    if (order.getSauce().equals(SOS_STR)) {
                        order.setAdd_on_price(order.getAdd_on_price() - SOS_CHARGE);
                        order.setSauce(sauce);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                        ((MainActivity) getActivity()).UpdateTotal();
                    }
                    else {
                        order.setSauce(sauce);
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spice = "Normal";
                }
                if (position == 1) {
                    spice = "spice: mild";
                }
                if (position == 2) {
                    spice = "spice: medium";
                }
                if (position == 3) {
                    spice = "spice: hot";
                }
                if (position == 4) {
                    spice = "spice: extra hot";
                }
                if (position == 5) {
                    spice = "no spice";
                }
                order.setSpice(spice);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //----------------SPECIAL INSTRUCTION SECTION------------------------//

        ItemList = getResources().getStringArray(R.array.items);
        onlyItemList = getResources().getStringArray(R.array.onlyItems);
        instructionList = getResources().getStringArray(R.array.special_instructions);

        noListCheckedItems = new boolean[ItemList.length];
        noItemListSelected = new ArrayList<>();

        lightListCheckedItems = new boolean[ItemList.length];
        lightItemListSelected = new ArrayList<>();

        onlyListCheckedItems = new boolean[onlyItemList.length];
        onlyItemListSelected = new ArrayList<>();

        instructionChecked = new boolean[instructionList.length];
        instructionSelected = new ArrayList<>();

        // initialize the no Item check list
        for (int i = 0; i < order.getNo_list().size(); i++) {
            for (int j = 0; j < ItemList.length; j++) {
                if (order.getNo_list().get(i).equals(ItemList[j])) {
                    noListCheckedItems[j] = true;
                    noItemListSelected.add(j);
                    break;
                }
            }
        }

        // initialize the light item check list
        for (int i = 0; i < order.getLight_list().size(); i++) {
            for (int j = 0; j < ItemList.length; j++) {
                if (order.getLight_list().get(i).equals(ItemList[j])) {
                    lightListCheckedItems[j] = true;
                    lightItemListSelected.add(j);
                    break;
                }
            }
        }

        // initialize the only item check list
        for (int i = 0; i < order.getOnly_list().size(); i++) {
            for (int j = 0; j < onlyItemList.length; j++) {
                if (order.getOnly_list().get(i).equals(onlyItemList[j])) {
                    onlyListCheckedItems[j] = true;
                    onlyItemListSelected.add(j);
                    break;
                }
            }
        }

        // initializes the instruction check list
        for (int i = 0; i < order.getInstruction_list().size(); i++) {
            for (int j = 0; j < instructionList.length; j++) {
                if (order.getInstruction_list().get(i).equals(instructionList[j])) {
                    instructionChecked[j] = true;
                    instructionSelected.add(j);
                    break;
                }
            }
        }

        // No Button
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder noListBuilder = new AlertDialog.Builder(getActivity());

                // add custom layout for AlertDialog

                noListBuilder.setTitle("No List");
                noListBuilder.setMultiChoiceItems(ItemList, noListCheckedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked) {
                                    if (!noItemListSelected.contains(position)) {
                                        noItemListSelected.add(position);
                                    }
                                    else {
                                        noItemListSelected.remove(position);
                                    }
                                }
                                else { // not checked
                                    if (noItemListSelected.contains(position)) {

                                        for(int i = 0; i < noItemListSelected.size(); i++) {
                                            if (position == noItemListSelected.get(i)) {
                                                noItemListSelected.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });

                noListBuilder.setCancelable(false);
                // Ok
                noListBuilder.setPositiveButton(getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        order.getNo_list().clear();
                        for (int i = 0; i < noItemListSelected.size(); i++) {
                            order.add2NoList(ItemList[noItemListSelected.get(i)]);
                        }

                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                // Dismiss
                noListBuilder.setNeutralButton(getString(R.string.dismiss_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });

                // Clear All
                noListBuilder.setNegativeButton(getString(R.string.clear_all_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for (int i = 0; i < noListCheckedItems.length; i++) {
                            noListCheckedItems[i] = false;
                        }
                        noItemListSelected.clear();
                        order.getNo_list().clear();
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                AlertDialog mDialog = noListBuilder.create();
                mDialog.show();
            }
        });

        //Extra Button
        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowExtraBox(v);
            }
        });


        // Only Button
        onlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder onlyListBuilder = new AlertDialog.Builder(getActivity());
                onlyListBuilder.setTitle("Only List");
                onlyListBuilder.setMultiChoiceItems(onlyItemList, onlyListCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!onlyItemListSelected.contains(position)) {
                                onlyItemListSelected.add(position);
                            }
                            else {
                                onlyItemListSelected.remove(position);
                            }
                        }
                        else { // not checked
                            if (onlyItemListSelected.contains(position)) {
                                for (int i = 0; i < onlyItemListSelected.size(); i++) {
                                    if (position == onlyItemListSelected.get(i)) {
                                        onlyItemListSelected.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });

                onlyListBuilder.setCancelable(false);

                // Positive Button
                onlyListBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        order.getOnly_list().clear();
                        for (int i = 0; i < onlyItemListSelected.size(); i++) {
                            order.add2OnlyList(onlyItemList[onlyItemListSelected.get(i)]);
                        }

                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                // Neutral
                onlyListBuilder.setNeutralButton(getString(R.string.dismiss_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Negative Button
                onlyListBuilder.setNegativeButton(getString(R.string.clear_all_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < onlyListCheckedItems.length; i++) {
                            onlyListCheckedItems[i] = false;
                        }
                        onlyItemListSelected.clear();
                        order.getOnly_list().clear();
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = onlyListBuilder.create();
                dialog.show();
            }
        });

        // Light Button
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder lightListBuilder = new AlertDialog.Builder(getActivity());
                lightListBuilder.setTitle("Light List");
                lightListBuilder.setMultiChoiceItems(ItemList,
                        lightListCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked) {
                                    if (!lightItemListSelected.contains(position)) {
                                        lightItemListSelected.add(position);
                                    }
                                    else {
                                        lightItemListSelected.remove(position);
                                    }
                                }
                                else { // not checked
                                    if (lightItemListSelected.contains(position)) {

                                        for(int i = 0; i < lightItemListSelected.size(); i++) {
                                            if (position == lightItemListSelected.get(i)) {
                                                lightItemListSelected.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });

                lightListBuilder.setCancelable(false);

                // Positive Button
                lightListBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        order.getLight_list().clear();
                        for (int i = 0; i < lightItemListSelected.size(); i++) {
                            order.add2LightList(ItemList[lightItemListSelected.get(i)]);
                        }

                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                // Neutral
                lightListBuilder.setNeutralButton(getString(R.string.dismiss_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });

                // Negative
                lightListBuilder.setNegativeButton(getString(R.string.clear_all_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for (int i = 0; i < lightListCheckedItems.length; i++) {
                            lightListCheckedItems[i] = false;
                        }
                        lightItemListSelected.clear();
                        order.getLight_list().clear();
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });



                AlertDialog mDialog = lightListBuilder.create();
                mDialog.show();

            }
        });

        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instructionBuilder = new AlertDialog.Builder(getActivity());
                instructionBuilder.setTitle("Special Instructions");
                instructionBuilder.setMultiChoiceItems(instructionList, instructionChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!instructionSelected.contains(position)) {
                                instructionSelected.add(position);
                            }
                            else {
                                instructionSelected.remove(position);
                            }
                        }
                        else { // not checked
                            if (instructionSelected.contains(position)) {

                                for(int i = 0; i < instructionSelected.size(); i++) {
                                    if (position == instructionSelected.get(i)) {
                                        instructionSelected.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
                instructionBuilder.setCancelable(false);

                // Positive Button
                instructionBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        order.getInstruction_list().clear();
                        for (int i = 0; i < instructionSelected.size(); i++) {
                            order.add2Instruction_list(instructionList[instructionSelected.get(i)]);
                        }
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });

                // Neutral
                instructionBuilder.setNeutralButton(getString(R.string.dismiss_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });

                // Negative
                instructionBuilder.setNegativeButton(getString(R.string.clear_all_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for (int i = 0; i < instructionChecked.length; i++) {
                            instructionChecked[i] = false;
                        }
                        instructionSelected.clear();
                        order.getInstruction_list().clear();
                        ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    }
                });



                AlertDialog mDialog = instructionBuilder.create();
                mDialog.show();
            }
        });

        clearExtraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price = 0;
                for (Food food: order.getAdd_on()) {
                    price += food.getPrice();
                }
                order.getAdd_on().clear();
                order.setAdd_on_price(order.getAdd_on_price() - price);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                addonAdapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).UpdateTotal();
            }
        });

        tv_quantity = view.findViewById(R.id.quantity_textView);
        counter = order.getQuantity();
        String c = Integer.toString(order.getQuantity());
        tv_quantity.setText(c);

        //base_price = order.getFood().getPrice();

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter >= 0 && counter < MAX_COUNT) {
                    counter += 1;
                    String string_counter = Integer.toString(counter);
                    tv_quantity.setText(string_counter);

                    order.setQuantity(counter);
                    ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    ((MainActivity) getActivity()).UpdateTotal();
                }

            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 1) {
                    counter -= 1;
                    String string_counter = Integer.toString(counter);
                    tv_quantity.setText(string_counter);

                    order.setQuantity(counter);
                    ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                    ((MainActivity) getActivity()).UpdateTotal();
                }
            }
        });

        // New Order Button
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order newOrder = new Order();
                Food newFood = new Food();

                newFood.setPrice(order.getBase_price());
                newFood.setName(order.getFood().getName());
                newFood.setChineseName(order.getFood().getChineseName());
                newFood.setAbbrevName(order.getFood().getAbbrevName());
                newFood.setType(order.getFood().getType());

                newOrder.setQuantity(1);
                newOrder.setFood(newFood);
                newOrder.setCategory(order.getCategory());
                newOrder.setBase_price(order.getBase_price());

                ((MainActivity) getActivity()).addItem(newOrder);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                ((MainActivity) getActivity()).UpdateTotal();
            }
        });

        // Remove Button
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).removeItem(order);
                ((MainActivity) getActivity()).getOrderAdapter().notifyDataSetChanged();
                ((MainActivity) getActivity()).UpdateTotal();

                // go back to main fragment
                MainPageFragment main_fragment = new MainPageFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, main_fragment).commit();

            }
        });
        return view;
    }

}

