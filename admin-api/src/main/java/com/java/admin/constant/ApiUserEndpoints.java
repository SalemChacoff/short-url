package com.java.admin.constant;

import java.util.HashMap;

public class ApiUserEndpoints {

    public static final String API_VERSION = "/api/v1";
    public static final String BASE_NAME = "/users";
    public static final String BASE_PATH = API_VERSION + BASE_NAME;
    public static final String PROFILE_PATH = "/profile";

    private ApiUserEndpoints() {
        // Private constructor to prevent instantiation
    }

    public static Integer getUserCodeFieldsValidation(String fieldName) {
        HashMap<String, Integer> userCodeFieldsValidation = new HashMap<>();
        userCodeFieldsValidation.put("email", 10000);
        userCodeFieldsValidation.put("password", 10010);
        userCodeFieldsValidation.put("firstName", 10020);
        userCodeFieldsValidation.put("lastName", 10030);
        userCodeFieldsValidation.put("phoneNumber", 10040);
        userCodeFieldsValidation.put("address", 10050);
        userCodeFieldsValidation.put("username", 10060);
        return userCodeFieldsValidation.getOrDefault(fieldName, 0);
    }

}
