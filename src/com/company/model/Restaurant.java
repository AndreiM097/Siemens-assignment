package com.company.model;

import com.company.util.PropertiesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Restaurant {

    private Thread waitingCustomersSolver;
    private Properties properties;
    private String restaurantName;
    private Waiter waiter;
    private ArrayList<String> listOfNames;

    public Restaurant() throws IOException {
        waiter = new Waiter();
        waitingCustomersSolver = new Thread(waiter);
        waitingCustomersSolver.start();

        listOfNames = new ArrayList<>();
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        properties = propertiesUtil.getPropertiesFile();
        restaurantName = properties.getProperty("restaurant.name");

    }

    public void openRestaurant() {

        System.out.println("Restaurant " + restaurantName + " is ready to serve.Please add your order below:\n");

        for (; ; ) {
            try {
                Customer customer = Customer.arrive();

                if(listOfNames.size() < 1){
                    listOfNames.add(customer.getName());
                } else{
                    for(int i = 0; i < listOfNames.size(); i++){
                        while(customer.getName().equals(listOfNames.get(i))){
                            System.out.println("Sorry but we already have this name in our list.Could you provide another name?");
                            Scanner scanner = new Scanner(System.in);
                            customer.setName(scanner.nextLine());
                        }
                    }
                        listOfNames.add(customer.getName());
                }
                waiter.greetCustomer(customer, restaurantName);
                waiter.getAvailableTable(customer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
