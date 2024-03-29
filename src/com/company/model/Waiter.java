package com.company.model;

import com.company.util.PropertiesUtil;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Waiter implements Runnable {

    private HashMap<Integer, Table> tableHashMap = new HashMap<>();
    private Properties properties;
    private List<Customer> waitingCustomers = new ArrayList<>();
    private List<Customer> customersInside = new ArrayList<>();

    public Waiter() throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        properties = propertiesUtil.getPropertiesFile();

        for (int i = 1 ; i <= parseInt(properties.getProperty("restaurant.tables")); i++){
            tableHashMap.put(i, new Table(i, parseInt(properties.getProperty("restaurant.table" + i))));
        }
    }

    public void greetCustomer(Customer customer, String restaurantName) {
        System.out.println("Welcome " + customer.getHonorific() + " " + customer.getName() + " to " + restaurantName
                + ". I am going to find you a table as soon as possible.\n");


        if (getAvailableTable(customer) != null) {

            System.out.println(customer.getHonorific() + " " + customer.getName() + ", I'm pleased to have found a free table for you and your guests."
                    + "The waiter will take you to the table number " + customer.getTable().getTableNumber() + ".\n");

            customersInside.add(customer);

        } else {
            waitingCustomers.add(customer);
            System.out.println("Customers waiting: " + waitingCustomers.size());
        }
    }


    public Table getAvailableTable(Customer customer) {
        for (int i = 1; i <= tableHashMap.size(); i++) {
            if (tableHashMap.get(i).getTableSeats() >= customer.getNumberOfPeople()
                    && tableHashMap.get(i).getAvailable()
                    && (tableHashMap.get(i).getTableSeats() - customer.getNumberOfPeople()) < 3) {

                customer.setTable(tableHashMap.get(i));
                customer.getTable().setAvailable(false);
                customer.setJoinedTableAt(LocalTime.now());
                customer.setLeavingAt(LocalTime.now().plusMinutes(parseInt(properties.getProperty("restaurant.greet.timeout.minutes")) / 2));

                return tableHashMap.get(i);
            }
        }
        return null;
    }

    //The thread is greeting the customers, checking for available tables and cleaning them and so on.This is done at every 5 seconds.
    @Override
    public void run() {
        for (; ; ) {
            if (customersInside.size() > 0) {
                Iterator<Customer> iterator = customersInside.iterator();
                while (iterator.hasNext()) {
                    Customer currentCustomer = iterator.next();
                    if (currentCustomer.getLeavingAt().isBefore(LocalTime.now())) {
                        currentCustomer.getTable().setTimeUntilCleaned(LocalTime.now().plusMinutes(parseInt(properties.getProperty("restaurant.clean.timeout.minutes"))));
                        currentCustomer.getTable().setAvailable(true);
                        System.out.println("Goodbye " + currentCustomer.getHonorific() + " " + currentCustomer.getName() + ". I hope you enjoyed our restaurant and we will be happy" +
                                " to serve you anytime.\n");
                        iterator.remove();
                    }
                }
            }

            if (waitingCustomers.size() > 0) {
                Iterator<Customer> iterator = waitingCustomers.iterator();
                while (iterator.hasNext()) {
                    Customer currentCustomer = iterator.next();
                    //If a table has become available and has been cleaned then the waiter will take the customer to it, else the waiter will notify the customer that his table will be ready soon.
                    if(getAvailableTable(currentCustomer) != null || currentCustomer.getTable() != null){
                        if(currentCustomer.getTable().getTimeUntilCleaned().isBefore(LocalTime.now())) {
                            System.out.println(currentCustomer.getHonorific() + " " + currentCustomer.getName() + ",Sorry for the wait. I'm pleased to have found a free table for you and your guests."
                                    + "The waiter will take you to the table number " + currentCustomer.getTable().getTableNumber() + ".\n");

                            currentCustomer.getTable().setAvailable(false);
                            currentCustomer.setJoinedTableAt(LocalTime.now());
                            currentCustomer.setLeavingAt(LocalTime.now().plusMinutes(parseInt(properties.getProperty("restaurant.greet.timeout.minutes")) / 2));

                            customersInside.add(currentCustomer);

                            iterator.remove();
                        }
                    }
                        
                        if(!currentCustomer.getHasBeenNotified() && currentCustomer.getGreetTimeoutMinutes().minusMinutes(parseInt(properties.getProperty("restaurant.reminder.timeout.minutes"))).isBefore(LocalTime.now())){
    
                        System.out.println(currentCustomer.getHonorific() + " " + currentCustomer.getName() + ", a table will be ready for you in a few minutes."
                                + "Thank you for your patience.\n");
                            currentCustomer.setHasBeenNotified(true);
                        }



                    //Asking the customer to wait some more time
                    if (currentCustomer.getGreetTimeoutMinutes().isBefore(LocalTime.now()) && !currentCustomer.getHasBeenNotified()) {
                        System.out.println(currentCustomer.getHonorific() + " " + currentCustomer.getName() + " I'm sorry that I could not find you a seat.Please wait some more\n");
                        currentCustomer.setGreetTimeoutMinutes(currentCustomer.getGreetTimeoutMinutes().plusMinutes(parseInt(properties.getProperty("restaurant.greet.timeout.minutes"))));
                    }


                    //If the customer has waited for too long and we don't have a large enough table for him and his company then they are leaving.
                    if (currentCustomer.getGreetTimeoutMinutes().isBefore(LocalTime.now()) && currentCustomer.getNumberOfPeople() <= 12 && !currentCustomer.getHasBeenNotified()) {
                        System.out.println("Goodbye " + currentCustomer.getHonorific() + " " + currentCustomer.getName() + ". I'm sorry that our restaurant is now full. Please come back another time.\n");
                        iterator.remove();
                    }

                    //If we don't have a large enough table for him and his company then they are leaving.
                    if (currentCustomer.getGreetTimeoutMinutes().isAfter(LocalTime.now()) && currentCustomer.getNumberOfPeople() > 12) {
                        System.out.println("We are sorry " + currentCustomer.getHonorific() + " " + currentCustomer.getName() + " but our restaurant does not have enough room for so many people." +
                                ". Our biggest table can only have a maximum of 12 people seating at it. I hope you come back another time. Goodbye");

                        iterator.remove();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Customer> getWaitingCustomers() {
        return waitingCustomers;
    }
}