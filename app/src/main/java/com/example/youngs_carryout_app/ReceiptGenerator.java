package com.example.youngs_carryout_app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.Collections;
import java.util.Locale;

public class ReceiptGenerator {
    static public String createReceipt(MainActivity mainActivity, String date, String time, boolean isWithPrice) {

        String DEFAULT_RICE = "plain fried rice";
        String DEFAULT_ROLL = "egg roll";
        String DEFAULT_SAUCE = "Normal";
        String DEFAULT_SPICE = "Normal";

        // sorts the order base on the type
        Collections.sort(mainActivity.getOrder_list(), new OrderComparator());

        String subTotal = mainActivity.getSubTotal_textView().getText().toString();
        String tax = mainActivity.getTax_textView().getText().toString();
        String grandTotal = mainActivity.getTotal_textView().getText().toString();

        String name_et = mainActivity.getNameEditText().getText().toString();
        String phone_et = mainActivity.getPhoneEditText().getText().toString();
        String note_et = mainActivity.getNoteEditText().getText().toString();

        String header =        "YOUNG'S CHINESE FOOD CARRY OUT";
        String address =       "    921 E. ELEVEN MILE ROAD   ";
        String city =          "   ROYAL OAK, MICHIGAN 48067  ";
        String telephone =     "     PHONE (248) 543-3131     ";
        String dashes =        "------------------------------";
        String thankYouNote =  "     Thank You Come Again!    ";
        String space = " ";
        String newLine = "\n";
        String name = "NAME: " + name_et;
        String number = "PHONE#: " + phone_et;
        String note = "NOTE: " + note_et;

        StringBuilder textToPrint = new StringBuilder();

        // header
        textToPrint.append(header);
        textToPrint.append(newLine);
        textToPrint.append(address);
        textToPrint.append(newLine);
        textToPrint.append(city);
        textToPrint.append(newLine);
        textToPrint.append(telephone);
        textToPrint.append(newLine);
        textToPrint.append(dashes);
        textToPrint.append(newLine);

        // date and time
        int date_length = date.length();
        int time_length = time.length();
        int difference = 30 - (date_length + time_length);
        textToPrint.append(date);
        for(int i = 0; i < difference; i++) {
            textToPrint.append(space);
        }
        textToPrint.append(time);
        textToPrint.append(newLine);
        textToPrint.append(newLine);
        textToPrint.append(newLine);

        Collections.sort(mainActivity.getOrder_list(), new OrderComparator());
        // Order
        for (Order order : mainActivity.getOrder_list()) {
            StringBuilder chineseName = new StringBuilder();

            if (!order.getFood().getChineseName().equals("null")) {
                for (int i = 0; i < 15; i++) {
                    chineseName.append(" ");
                }
                chineseName.append(order.getFood().getChineseName());
            }

            String quantity = Integer.toString(order.getQuantity());
            String item = order.getFood().getAbbrevName();
            String quantityItem;
            if (order.getQuantity() >= 10) {
                quantityItem = quantity + " " + item;
            }
            else {
                quantityItem = " " + quantity + " " + item;
            }


            String price = String.format(Locale.US, "%.2f", order.getTotal());
            int length = quantityItem.length() + price.length();
            length = 30 - length;

            StringBuilder quantityItemBuilder = new StringBuilder(quantityItem);
            if (isWithPrice) {
                for (int i = 0; i < length; i++) {
                    quantityItemBuilder.append(" ");
                }
                quantityItemBuilder.append(price);
                textToPrint.append(quantityItemBuilder.toString());
                textToPrint.append(newLine);
            }
            else {
                textToPrint.append(quantityItemBuilder.toString());
                textToPrint.append(newLine);
            }

            // Print Chinese Character
            if (!order.getFood().getChineseName().equals("null")) {
                textToPrint.append(chineseName.toString());
                textToPrint.append(newLine);
            }


            // fried rice
            if (!order.getRice().equals(DEFAULT_RICE)) {
                String riceNote = " ** " + order.getRice();
                textToPrint.append(riceNote);
                textToPrint.append(newLine);
            }

            // egg roll / spring roll
            if (!order.getRoll().equals(DEFAULT_ROLL)) {
                String rollNote = " ** " + order.getRoll();
                textToPrint.append(rollNote);
                textToPrint.append(newLine);
            }

            // sauce
            if (!order.getSauce().equals(DEFAULT_SAUCE)) {
                String sauceNote = " ** " + order.getSauce();
                textToPrint.append(sauceNote);
                textToPrint.append(newLine);
            }

            // spice
            if (!order.getSpice().equals(DEFAULT_SPICE)) {
                String spiceNote = " ** " + order.getSpice();
                textToPrint.append(spiceNote);
                textToPrint.append(newLine);
            }

            //no list
            if (!order.getNo_list().isEmpty()) {
                for (int k = 0; k < order.getNo_list().size(); k++) {
                    String noListStr = " ** No " + order.getNo_list().get(k);
                    textToPrint.append(noListStr);
                    textToPrint.append(newLine);
                }
            }

            // Light List
            if (!order.getLight_list().isEmpty()) {
                // for each item in the Light List
                for (int m = 0; m < order.getLight_list().size(); m++) {
                    String lightListStr = " ** Light " +
                            order.getLight_list().get(m);
                    textToPrint.append(lightListStr);
                    textToPrint.append(newLine);
                }
            }

            // Only List
            if (!order.getOnly_list().isEmpty()) {
                StringBuilder onlyListBuilder = new StringBuilder();
                onlyListBuilder.append(" ** Only ");
                // for each item in the Only List
                for (int n = 0; n < order.getOnly_list().size(); n++) {
                    if (n == order.getOnly_list().size() - 1) {
                        onlyListBuilder.append(order.getOnly_list().get(n));
                    }
                    else {
                        onlyListBuilder.append(order.getOnly_list().get(n));
                        onlyListBuilder.append(", ");
                    }
                }
                textToPrint.append(onlyListBuilder.toString());
                textToPrint.append(newLine);
            }

            // Extra List
            if (!order.getAdd_on().isEmpty()) {
                // for each item in the AddOnList
                for (int p = 0; p < order.getAdd_on().size(); p++) {
                    String addon_price = String.format(Locale.US, "%.2f",
                            order.getAdd_on().get(p).getPrice());
                    String str = " ** add " + order.getAdd_on().get(p).getName() + " (" + addon_price + ")";
                    textToPrint.append(str);
                    textToPrint.append(newLine);
                }
            }

            // Special Instruction
            if (!order.getInstruction_list().isEmpty()) {
                for (String str: order.getInstruction_list()) {
                    String instr = " ** " + str;
                    textToPrint.append(instr);
                    textToPrint.append(newLine);
                }
            }
            textToPrint.append(newLine);
        }

        if (isWithPrice) {
            // Total
            int str_length = 30 - subTotal.length();
            for (int i = 0; i < str_length; i++) {
                textToPrint.append(space);
            }
            textToPrint.append(subTotal);
            textToPrint.append(newLine);

            str_length = 30 - tax.length();
            for (int i = 0; i < str_length; i++) {
                textToPrint.append(space);
            }
            textToPrint.append(tax);
            textToPrint.append(newLine);

            str_length = 30 - grandTotal.length();
            for (int i = 0; i < str_length; i++) {
                textToPrint.append(space);
            }
            textToPrint.append(grandTotal);
            textToPrint.append(newLine);
            textToPrint.append(dashes);
            textToPrint.append(newLine);

            // name and phone
            textToPrint.append(newLine);
            textToPrint.append(note);
            textToPrint.append(newLine);
            textToPrint.append(name);
            textToPrint.append(newLine);
            textToPrint.append(number);
            textToPrint.append(newLine);
            textToPrint.append(newLine);

            // Thank you note
            textToPrint.append(thankYouNote);
            textToPrint.append(newLine);
        }
        else {
            // name and phone
            textToPrint.append(note);
            textToPrint.append(newLine);
            textToPrint.append(name);
            textToPrint.append(newLine);
            textToPrint.append(number);
            textToPrint.append(newLine);
        }

        return textToPrint.toString();
    }

    static public Bitmap createBitmapFromText(String printText, int textSize, int printWidth, Typeface typeface) {
        Paint paint = new Paint();
        Bitmap bitmap;
        Canvas canvas;

        paint.setTextSize(textSize);
        paint.setTypeface(typeface);

        paint.getTextBounds(printText, 0, printText.length(), new Rect());

        TextPaint textPaint = new TextPaint(paint);
        android.text.StaticLayout staticLayout = new StaticLayout(printText, textPaint, printWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

        // Create bitmap
        bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create canvas
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.translate(0, 0);
        staticLayout.draw(canvas);

        return bitmap;
    }
}
