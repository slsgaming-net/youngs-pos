package com.example.youngs_carryout_app;

public class Food {
    private String name;
    private String abbrevName;
    private String chineseName;
    private String type;
    private double price;

    Food() {

    }

    Food(String name_in, String abbrevName_in, String chineseName_in, String type_in, double price_in) {
        name = name_in;
        abbrevName = abbrevName_in;
        chineseName = chineseName_in;
        type = type_in;
        price = price_in;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAbbrevName(String abbrevName) {
        this.abbrevName = abbrevName;
    }

    public String getAbbrevName() {
        return abbrevName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return this.chineseName;
    }

    public void setType(String type) {this.type = type;}

    public String getType() {return this.type;}

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
