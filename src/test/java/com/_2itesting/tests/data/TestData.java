package com._2itesting.tests.data;
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
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getProductName() { return productName; }
    public String getCoupon() { return coupon; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getAddress2() { return address2; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostcode() { return postcode; }
    public String getPhone() { return phone; }
    public int getExpectedDiscountPercent() { return expectedDiscountPercent; }

    @Override
    public String toString() {
        return String.format("TestData{username='%s', product='%s', coupon='%s', discount=%d%%}",
                username, productName, coupon, expectedDiscountPercent);
    }
}
