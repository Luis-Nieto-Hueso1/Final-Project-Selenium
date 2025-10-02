package com._2itesting.tests.data;

import java.io.*;
import java.util.*;

// Utility class to load test data from a CSV file
public class CSVTestDataProvider {
    // Data class to hold test parameters
    public static List<TestData> loadTestDataFromCSV(String filePath) {
        List<TestData> testDataList = new ArrayList<>();
// Read the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
// Split the line by commas and create TestData objects
                String[] values = line.split(",");
                if (values.length >= 13) {
                    TestData testData = new TestData(values[0].trim(), // username
                            values[1].trim(), // password
                            values[2].trim(), // productName
                            values[3].trim(), // coupon
                            values[4].trim(), // firstName
                            values[5].trim(), // lastName
                            values[6].trim(), // address
                            values[7].trim(), // address2
                            values[8].trim(), // city
                            values[9].trim(), // state
                            values[10].trim(), // postcode
                            values[11].trim(), // phone
                            Integer.parseInt(values[12].trim()) // expectedDiscountPercent
                    );
                    testDataList.add(testData);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return testDataList;
    }
}
