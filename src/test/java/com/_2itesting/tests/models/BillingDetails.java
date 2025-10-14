package com._2itesting.tests.models;

/**
 * Simple immutable model representing billing details.
 * Acts as a data container for the checkout process.
 */
public record BillingDetails(
        String firstName,
        String lastName,
        String address1,
        String address2,
        String city,
        String county,
        String postcode,
        String phone
) {}
