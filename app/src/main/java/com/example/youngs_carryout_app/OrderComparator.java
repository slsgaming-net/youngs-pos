package com.example.youngs_carryout_app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        Order order1 = (Order) o1;
        Order order2 = (Order) o2;

        return order1.getFood().getType().compareTo(order2.getFood().getType());
    }
}
