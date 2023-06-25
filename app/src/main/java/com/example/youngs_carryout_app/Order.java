package com.example.youngs_carryout_app;

import java.util.ArrayList;
import java.util.List;


/* Details of each individual food object
*/
public class Order {

    private Food food;
    private int quantity;
    private double add_on_price;
    private double base_price;
    private double total;

    private String category;
    private String rice;
    private String roll;
    private String sauce;
    private String spice;

    private ArrayList<Food> add_on_list = new ArrayList<>();
    private List<String> light_list = new ArrayList<>();
    private List<String> no_list = new ArrayList<>();
    private List<String> only_list = new ArrayList<>();
    private List<String> instruction_list = new ArrayList<>();

    Order() {
        add_on_price = 0;
        rice = "plain fried rice";
        roll = "egg roll";
        sauce = "Normal";
        spice = "Normal";
    }

    public boolean isEqual(Order rhs) {

        return (
                this.food.getAbbrevName().equals(rhs.food.getAbbrevName()) &&
                        this.add_on_price == rhs.add_on_price &&
                        this.rice.equals(rhs.rice) &&
                        this.roll.equals(rhs.roll) &&
                        this.sauce.equals(rhs.sauce) &&
                        this.spice.equals(rhs.spice) &&
                        rhs.getInstruction_list().size() == 0 &&
                        rhs.getLight_list().size() == 0 &&
                        rhs.getNo_list().size() == 0 &&
                        rhs.getOnly_list().size() == 0

        );
    }

    public String getRice() {
        return rice;
    }

    public void setRice(String string) {
        rice = string;
    }

    String getRoll() {
        return roll;
    }

    void setRoll(String string) {
        roll = string;
    }

    void setSauce(String string) {
        sauce = string;
    }

    String getSauce() {
        return sauce;
    }

    void setSpice(String string) {
        spice = string;
    }

    String getSpice() {
        return spice;
    }

    double getBase_price() {
        return base_price;
    }

    void setBase_price(double bp) {
        base_price = bp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public void setQuantity(int q) {
        quantity = q;
    }

    void setCategory(String str) {
        category = str;
    }

    String getCategory() {
        return category;
    }

    void setAdd_on_price(double amount) {
        add_on_price = amount;
    }

    double getAdd_on_price() {
        return add_on_price;
    }

    void addAddOns(Food food) {
        add_on_list.add(food);
    }

    public ArrayList<Food> getAdd_on() {
        return add_on_list;
    }

    void add2NoList(String item) {
        no_list.add(item);
    }

    List<String> getNo_list() {
        return no_list;
    }

    void add2OnlyList(String item) {only_list.add(item);}

    public List<String> getOnly_list() {
        return only_list;
    }

    public List<String> getLight_list() {
        return light_list;
    }

    public void add2LightList(String item) {
        light_list.add(item);
    }

    public List<String> getInstruction_list() {return instruction_list;}

    public void add2Instruction_list(String item) {
        instruction_list.add(item);
    }

    // Total
    public void setTotal(double amt) {
        total = amt;
    }

    public double getTotal() {
        return total;
    }
}
