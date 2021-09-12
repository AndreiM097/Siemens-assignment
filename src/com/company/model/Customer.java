package com.company.model;

import com.company.util.PropertiesUtil;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Customer {
    private String honorific;
    private String name;
    private int numberOfPeople;
    private LocalTime arrivedAt;
    private LocalTime greetTimeoutMinutes;
    private Boolean hasBeenNotified;
    private LocalTime joinedTableAt;
    private Table table;
    private LocalTime leavingAt;
    private Properties properties;
    private Restaurant restaurant;


    public Customer(String honorific, String name, int numberOfCustomers) throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        properties = propertiesUtil.getPropertiesFile();

        this.honorific = honorific;
        this.name = name;
        this.numberOfPeople = numberOfCustomers;
        this.hasBeenNotified = false;

        restaurant = new Restaurant();
        arrivedAt = LocalTime.now();
        greetTimeoutMinutes = arrivedAt.plusMinutes(parseInt(properties.getProperty("restaurant.greet.timeout.minutes")));
    }

    public Customer() {
    }

    public static Customer arrive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(">>>arrive ");
        String honorific = scanner.nextLine();
        String name = scanner.nextLine();

        int numberOfPeople = scanner.nextInt();

        Customer customer = null;
        try {
            customer = new Customer(honorific, name, numberOfPeople);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public String getHonorific() {
        return honorific;
    }

    public void setHonorific(String honorific) {
        this.honorific = honorific;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public LocalTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(LocalTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public LocalTime getGreetTimeoutMinutes() {
        return greetTimeoutMinutes;
    }

    public void setGreetTimeoutMinutes(LocalTime greetTimeoutMinutes) {
        this.greetTimeoutMinutes = greetTimeoutMinutes;
    }

    public Boolean getHasBeenNotified() {
        return hasBeenNotified;
    }

    public void setHasBeenNotified(Boolean hasBeenNotified) {
        this.hasBeenNotified = hasBeenNotified;
    }

    public LocalTime getJoinedTableAt() {
        return joinedTableAt;
    }

    public void setJoinedTableAt(LocalTime joinedTableAt) {
        this.joinedTableAt = joinedTableAt;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalTime getLeavingAt() {
        return leavingAt;
    }

    public void setLeavingAt(LocalTime leavingAt) {
        this.leavingAt = leavingAt;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
