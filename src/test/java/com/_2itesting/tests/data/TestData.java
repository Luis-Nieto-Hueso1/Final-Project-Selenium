package com._2itesting.tests.data;

import com._2itesting.tests.models.ProductData;

// Data class to hold test parameters
public class TestData {
    private String username;
    private String password;
    private String productName;
    private String coupon;
    private String firstName;
    private String lastName;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String postcode;
    private String phone;
    private int expectedDiscountPercent;

    // Constructor
    public TestData(String username, String password, String productName, String coupon,
                    String firstName, String lastName, String address, String address2,
                    String city, String state, String postcode, String phone, int expectedDiscountPercent) {
        this.username = username;
        this.password = password;
        this.productName = productName;
        this.coupon = coupon;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.phone = phone;
        this.expectedDiscountPercent = expectedDiscountPercent;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    // New getter for password
    public String getPassword() {
        return password;
    }

    // New getter for productName
    public String getProductName() {
        return productName;
    }

    // New getter for coupon
    public String getCoupon() {
        return coupon;
    }

    // New getter for firstName
    public String getFirstName() {
        return firstName;
    }

    // New getter for lastName
    public String getLastName() {
        return lastName;
    }

    // New getter for address
    public String getAddress() {
        return address;
    }

    // New getter for address2
    public String getAddress2() {
        return address2;
    }

    // New getter for city
    public String getCity() {
        return city;
    }

    // New getter for state
    public String getState() {
        return state;
    }

    // New getter for postcode
    public String getPostcode() {
        return postcode;
    }

    // New getter for phone
    public String getPhone() {
        return phone;
    }

    // New getter for expected discount percentage
    public int getExpectedDiscountPercent() {
        return expectedDiscountPercent;
    }

    // For easier logging and debugging
    @Override
    public String toString() {
        return String.format("TestData{username='%s', product='%s', coupon='%s', discount=%d%%}",
                username, productName, coupon, expectedDiscountPercent);
    }


}
