package com.example.youngs_carryout_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Typeface;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.math.BigDecimal;
import android.widget.Toast;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.starioextension.StarIoExt;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class MainActivity extends AppCompatActivity
        implements MainPageFragment.LoadMainPageFragment {

    // an array list for storing all the items
    private ArrayList<Order> order_list;

    private String[] categories;

    // an adapter for displaying the orders
    private OrderAdapter orderAdapter;

    private ProgressDialog progressDialog;

    private Switch toggle;

    private static final double SALES_TAX = 0.06;

    // StarIOPort for communicating with printer
    StarIOPort port = null;

    // List views
    ListView order_listView;

    private TextView total_textView;
    private TextView tax_textView;
    private TextView subTotal_textView;

    // Edit views
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText noteEditText;
    private EditText entreeNameEditText;
    private EditText entreePriceEditText;

    public TextView getTotal_textView() {
        total_textView = findViewById(R.id.total_textView);
        return total_textView;
    }

    public TextView getTax_textView() {
        tax_textView = findViewById(R.id.tax_textView);
        return tax_textView;
    }

    public TextView getSubTotal_textView() {
        subTotal_textView = findViewById(R.id.sub_total);
        return subTotal_textView;
    }

    public EditText getNameEditText() {
        nameEditText = findViewById(R.id.nameEditText);
        return nameEditText;
    }

    public EditText getPhoneEditText() {
        phoneEditText = findViewById(R.id.phoneEditText);
        return phoneEditText;
    }

    public EditText getNoteEditText() {
        noteEditText = findViewById(R.id.noteEditText);
        return noteEditText;
    }

    // Buttons
    Button home_button;
    Button print_button;
    Button clear_button;
    Button custom_button;
    Button negative_button;
    Button positive_button;

    public ArrayList<Order> getOrder_list() {
        return order_list;
    }

    // add an item to the order
    public void addItem(Order foodDetail) {
        order_list.add(foodDetail);
    }

    // remove the given food from the order
    public void removeItem(Order order) {
        order_list.remove(order);
    }

    // function that updates the total price of the order
    public void UpdateTotal() {
        BigDecimal tax_percent = new BigDecimal(SALES_TAX);
        BigDecimal sum = new BigDecimal(0);

        for (Order order: order_list) {
            BigDecimal base_price = new BigDecimal(order.getBase_price());
            BigDecimal addon_price = new BigDecimal(order.getAdd_on_price());
            BigDecimal quantity = new BigDecimal(order.getQuantity());
            BigDecimal food_total = base_price.add(addon_price).multiply(quantity);
            sum = sum.add(food_total);
            order.setTotal(food_total.doubleValue());
        }

        String string_subTotal = "Sub Total: " + String.format(Locale.US, "%.2f", sum);
        subTotal_textView.setText(string_subTotal);

        BigDecimal total_tax = sum.multiply(tax_percent);
        total_tax = total_tax.setScale(3, RoundingMode.HALF_EVEN);

        String string_tax = String.format(Locale.US, "%.2f", total_tax);
        string_tax = "Tax:  " + string_tax;
        tax_textView.setText(string_tax);

        BigDecimal grandTotal = sum.add(total_tax);
        String string_sum = String.format(Locale.US, "%.2f", grandTotal);
        string_sum = "Total: " + string_sum;
        total_textView.setText(string_sum);
    }

    // return the order adapter
    public OrderAdapter getOrderAdapter() {
        return orderAdapter;
    }

    // toast message function
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void ClearAll() {
        nameEditText.setText("");
        phoneEditText.setText("");
        noteEditText.setText("");
        order_list.clear();
        orderAdapter.notifyDataSetChanged();
        subTotal_textView.setText("Sub Total: 0");
        tax_textView.setText("Tax: 0");
        total_textView.setText("Total: 0");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = getResources().getStringArray(R.array.carryOutMain);

        order_list = new ArrayList<>();

        toggle = findViewById(R.id.toggleButton);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggle.setText("Dinner");

                }
                else {
                    toggle.setText("Lunch");
                }
                MainPageFragment mainPageFragment = new MainPageFragment();
                mainPageFragment.initCategories(categories);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, mainPageFragment);
                fragmentTransaction.commit();
            }
        });

        // initialize text views
        tax_textView = findViewById(R.id.tax_textView);
        total_textView = findViewById(R.id.total_textView);
        subTotal_textView = findViewById(R.id.sub_total);

        // initialize edit texts
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        noteEditText = findViewById(R.id.noteEditText);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        // initialize buttons
        home_button = findViewById(R.id.home_button);
        print_button = findViewById(R.id.print_button);
        clear_button = findViewById(R.id.clear_order);
        custom_button = findViewById(R.id.custom_order_btn);


        // load main fragment
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            MainPageFragment mainPageFragment = new MainPageFragment();
            mainPageFragment.initCategories(categories);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mainPageFragment);
            fragmentTransaction.commit();
        }

        // order list
        order_listView = findViewById(R.id.order);
        orderAdapter = new OrderAdapter(this, order_list);
        order_listView.setAdapter(orderAdapter);

        // Home Button
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageFragment mainPageFragment = new MainPageFragment();
                mainPageFragment.initCategories(categories);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.fragment_container, mainPageFragment);
                fragmentTransaction.commit();
            }
        });

        // Clear All Button
        clear_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to empty the Order?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClearAll();
                        MainPageFragment fragment = new MainPageFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .commit();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Custom Button
        custom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dialog builder

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View customLayout = View.inflate(MainActivity.this, R.layout.dialog_entree, null);

                entreeNameEditText = customLayout.findViewById(R.id.entree_name_et);

                entreePriceEditText = customLayout.findViewById(R.id.price_input);

                positive_button = customLayout.findViewById(R.id.dialog_pos_btn);

                negative_button = customLayout.findViewById(R.id.dialog_negative_btn);

                builder.setView(customLayout);

                final AlertDialog customEntreeDialog = builder.create();
                customEntreeDialog.setCancelable(false);
                customEntreeDialog.show();

                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = entreeNameEditText.getText().toString();
                        if (!name.equals("")) {
                            try {
                                Double.parseDouble(entreePriceEditText.getText().toString());
                                double price = Double.valueOf(entreePriceEditText.getText().toString());
                                Food food = new Food(name, name, "null", "A", price);
                                Order order = new Order();
                                order.setFood(food);
                                order.setBase_price(price);
                                order.setQuantity(1);
                                order.setCategory("Custom");

                                // Client sometimes use custom order for special instruction for the order above.
                                // Check to see if there exists a previous order and change its category to the
                                // as the above

                                if (!order_list.isEmpty()) {
                                    order.getFood().setType(order_list.get(order_list.size() - 1).getFood().getType());
                                }
                                order_list.add(order);
                                orderAdapter.notifyDataSetChanged();
                                UpdateTotal();
                                customEntreeDialog.dismiss();

                            } catch (NumberFormatException ex) {
                                showToast("Please enter a price in the correct format.");
                            }
                        }
                        else {
                            showToast("Name cannot be empty.");
                        }
                    }
                });

                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customEntreeDialog.dismiss();
                    }
                });

            }
        });

        // Print Button
        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!order_list.isEmpty()) {

                    // --------------------------for print test ----------------------------------------------------//
                    // Comment out out when not using print test
/*                   String date = new SimpleDateFormat("MMMM-dd-yyyy", Locale.getDefault()).format(new Date());
                    String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

                    String text = ReceiptGenerator.createReceipt(MainActivity.this, date, time, false);

                    int textSize = 32;

                    Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

                    Bitmap receipt = ReceiptGenerator.createBitmapFromText(text,
                            textSize, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, typeface);*/

                    //----------------------------------------------------------------------------------------------------

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Communicating...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            initPrinter();
                        }
                    }, 1500);

                }
                else { // Order is empty
                    showToast("Order is empty!");
                }
            }
        });


        // User will be able to edit or remove food from order
        order_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentOrderEdit fragmentOrderEdit = new FragmentOrderEdit();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.fragment_container,
                        fragmentOrderEdit)
                        .commit();
                Order order = order_list.get(position);
                fragmentOrderEdit.setOrder(order);
            }
        });
    }

    private void initPrinter() {
        // initiate the portName to the first printer it finds
        String portName = PrinterFunctions.getFirstPrinter("BT:");

        try{
            // open port
            port = StarIOPort.getPort(portName, "",10000, MainActivity.this);

            // date and time
            String date = new SimpleDateFormat("MMMM-dd-yyyy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            String text_with_price = ReceiptGenerator.createReceipt(MainActivity.this, date, time, true);

            String text_without_price = ReceiptGenerator.createReceipt(MainActivity.this, date, time, false);

            int textSize = 32;

            Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

            // bitmap of receipt with price and total
            Bitmap receipt_1 = ReceiptGenerator.createBitmapFromText(text_with_price,
                    textSize, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, typeface);

            // bitmap of receipt without price and total
            Bitmap receipt_2 = ReceiptGenerator.createBitmapFromText(text_without_price,
                    textSize, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, typeface);

            byte[] command_1 = PrinterFunctions.createRasterData(StarIoExt.Emulation.StarGraphic,
                    receipt_1, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, true);

            byte[] command_2 = PrinterFunctions.createRasterData(StarIoExt.Emulation.StarGraphic,
                    receipt_2, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, true);

            Communication.Result firstPrint = Communication.sendCommands(
                    command_1, port.getPortName(), "", 10000
            );

            Communication.Result secondPrint = null;

            switch(firstPrint) {
                case Success:
                    secondPrint = Communication.sendCommands(
                            command_2, port.getPortName(), "", 10000
                    );
                    break;
                default:
                    AlertDialog.Builder errorBuilder = new AlertDialog.Builder(MainActivity.this);
                    errorBuilder.setTitle("Communicating Error");
                    errorBuilder.setMessage("Please check the printer to resolve the issue");
                    errorBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    errorBuilder.create();
                    errorBuilder.show();
                    break;
            }

            if (secondPrint != null) {
                switch (secondPrint) {
                    case Success:
                        ClearAll();
                        showToast("Communication Successful!");
                        break;
                    default:
                        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(MainActivity.this);
                        errorBuilder.setTitle("Communicating Error");
                        errorBuilder.setMessage("Please check the printer to resolve the issue");
                        errorBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        errorBuilder.create();
                        errorBuilder.show();
                        break;
                }
            }
        }
        catch (StarIOPortException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Communication Error");
            builder.setMessage("Please check the printer to resolve the issue");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
        finally {
            try {
                StarIOPort.releasePort(port);
            }
            catch (StarIOPortException ex) {
                showToast("Unable to close port.");
            }
        }

    }

    private void ParseXML(String fileName, FoodFragment foodFragment) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open(fileName);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            processParsing(parser, foodFragment);

        }
        catch (XmlPullParserException e) {
            Log.e("PrintActivity", "Exe ", e);
        }
        catch (IOException e) {
            Log.e("PrintActivity", "Exe ", e);
        }
    }

    private void ParsePintQuartXML(String fileName, PintQuartFragment pintQuartFragment) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open(fileName);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            processParsingPintQuart(parser, pintQuartFragment);
        }
        catch (XmlPullParserException e) {
            Log.e("PrintActivity", "Exe ", e);
        }
        catch (IOException e) {
            Log.e("PrintActivity", "Exe ", e);
        }
    }

    private void processParsing(
            XmlPullParser parser,
            FoodFragment fragment1) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        Food currentFood = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch(eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    // name
                    if (eltName.equals("item")) {
                        currentFood = new Food();
                        fragment1.addItem(currentFood);
                    }

                    else if (currentFood != null) {
                        // name
                        if (eltName.equals("name")) {
                            currentFood.setName(parser.nextText());
                        }
                        // abbrev
                        else if (eltName.equals("abbrev")) {
                            currentFood.setAbbrevName(parser.nextText());
                        }
                        else if (eltName.equals("chinese")) {
                            currentFood.setChineseName(parser.nextText());
                        }
                        else if (eltName.equals("type")) {
                            currentFood.setType(parser.nextText());
                        }
                        else if (eltName.equals("price")) {
                            // converts string to type double
                            double price = Double.parseDouble(parser.nextText());
                            currentFood.setPrice(price);
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
    }

    private void processParsingPintQuart(
            XmlPullParser parser,
            PintQuartFragment pintQuartFragment) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        PintQuartFood currentFood = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch(eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    // name
                    if (eltName.equals("item")) {
                        currentFood = new PintQuartFood();
                        pintQuartFragment.addItem(currentFood);
                    }

                    else if (currentFood != null) {
                        // name
                        if (eltName.equals("name")) {
                            currentFood.setName(parser.nextText());
                        }
                        // abbrev
                        else if (eltName.equals("abbrev")) {
                            currentFood.setAbbrevName(parser.nextText());
                        }
                        else if (eltName.equals("chinese")) {
                            currentFood.setChineseName(parser.nextText());
                        }
                        else if (eltName.equals("type")) {
                            currentFood.setType(parser.nextText());
                        }
                        else if (eltName.equals("pint_price")) {
                            // converts string to type double
                            double pint_price = Double.parseDouble(parser.nextText());
                            currentFood.setPintPrice(pint_price);
                        }
                        else if (eltName.equals("quart_price")) {
                            // converts string to type double
                            double quart_price = Double.parseDouble(parser.nextText());
                            currentFood.setQuartPrice(quart_price);
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
    }

    @Override
    public void loadCategory(int position) {

        switch (position) {

            case 0: { // Combinations
                FoodFragment foodFragment = new FoodFragment();
                if (toggle.getText().equals("Lunch")) {
                    foodFragment.setCategory("Lunch Combinations");
                    ParseXML("CombinationsLunch.xml", foodFragment);
                } else { // Dinner
                    foodFragment.setCategory("Dinner Combinations");
                    ParseXML("CombinationsDinner.xml", foodFragment);
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 1: { // Soup
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Soup");
                ParsePintQuartXML("Soup.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 2: {// Appetizers
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Appetizers");
                ParseXML("Appetizers.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 3: { // Chop Suey
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Chop Suey");
                ParsePintQuartXML("ChopSuey.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 4: { // Chow Mein
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Chow Mein");
                ParsePintQuartXML("ChowMein.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 5: { // Subgum
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Subgum");
                ParsePintQuartXML("Subgum.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 6: { // Fried Rice
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Fried Rice");
                ParsePintQuartXML("FriedRice.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 7: { // Egg Foo Young
                PintQuartFragment pintQuartFragment = new PintQuartFragment();
                pintQuartFragment.setCategory("Egg Foo Young");
                ParsePintQuartXML("EggFooYoung.xml", pintQuartFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pintQuartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 8: { // Poultry
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Poultry");
                ParseXML("Poultry.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 9: { // Beef
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Beef");
                ParseXML("Beef.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 10: { // Shrimp
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Shrimp");
                ParseXML("Shrimp.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 11: { // Lo Mein
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Lo Mein");
                ParseXML("LoMein.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 12: { // Sweet & Sour
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Sweet & Sour");
                ParseXML("SweetSour.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 13: { // Vegetarian
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Vegetarian");
                ParseXML("Vegetarian.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 14: { // Thai's Selection
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Thai's Selection");
                ParseXML("Thai.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 15: { // Misc
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Misc");
                ParseXML("Misc.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 16: { // Drinks
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Drinks");
                ParseXML("Drinks.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case 17: { // Party Trays
                FoodFragment foodFragment = new FoodFragment();
                foodFragment.setCategory("Party Tray");
                ParseXML("PartyTray.xml", foodFragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, foodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
