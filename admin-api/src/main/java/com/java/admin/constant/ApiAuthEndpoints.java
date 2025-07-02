package com.java.admin.constant;

import java.util.HashMap;

public class ApiAuthEndpoints {

    public static final String API_VERSION = "/api/v1";
    public static final String BASE_NAME = "/auth";
    public static final String BASE_PATH = API_VERSION + BASE_NAME;

    private ApiAuthEndpoints() {
        // Private constructor to prevent instantiation
    }

    public static Integer getAuthCodeFieldsValidation(String fieldName) {
        HashMap<String, Integer> authCodeFieldsValidation = new HashMap<>();
        authCodeFieldsValidation.put("email", 10000);
        authCodeFieldsValidation.put("password", 10010);
        authCodeFieldsValidation.put("firstName", 10020);
        authCodeFieldsValidation.put("lastName", 10030);
        authCodeFieldsValidation.put("phoneNumber", 10040);
        authCodeFieldsValidation.put("address", 10050);
        authCodeFieldsValidation.put("username", 10060);
        return authCodeFieldsValidation.getOrDefault(fieldName, 0);
    }

}
