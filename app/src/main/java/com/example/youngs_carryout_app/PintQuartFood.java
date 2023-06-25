package com.example.youngs_carryout_app;

public class PintQuartFood extends Food {

    private double pintPrice;
    private double quartPrice;

    public void setPintPrice(double pintPrice_in) {
        pintPrice = pintPrice_in;
    }

    public void setQuartPrice(double quartPrice_in) {
        quartPrice = quartPrice_in;
    }

    public double getPintPrice() {
        return pintPrice;
    }

    public double getQuartPrice() {
        return quartPrice;
    }
}
