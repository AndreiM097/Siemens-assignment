package com.company.model;

import com.company.util.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

public class Restaurant {

    Thread waitingCustomersSolver;
    private Properties properties;
    private String restaurantName;
    private Waiter waiter;

    public Restaurant() throws IOException {
        waiter = new Waiter();
        waitingCustomersSolver = new Thread(waiter);
        waitingCustomersSolver.start();

        PropertiesUtil propertiesUtil = new PropertiesUtil();
        properties = propertiesUtil.getPropertiesFile();
        restaurantName = properties.getProperty("restaurant.name");

    }

    public void openRestaurant() {

        System.out.println("Restaurant" + restaurantName + " is ready to serve.Please add your order below:\n");

        for (; ; ) {
            try {
                Customer customer = Customer.arrive();
                waiter.greetCustomer(customer, restaurantName);
                waiter.getAvailableTable(customer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
