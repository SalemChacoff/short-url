package com.java.admin.constant;

import java.util.HashMap;

public class ApiUrlEndpoints {

    public static final String API_VERSION = "/api/v1";
    public static final String BASE_NAME = "/urls";
    public static final String BASE_PATH = API_VERSION + BASE_NAME;

    private ApiUrlEndpoints() {
        // Private constructor to prevent instantiation
    }

    public static Integer getUrlCodeFieldsValidation(String fieldName) {
        HashMap<String, Integer> urlCodeFieldsValidation = new HashMap<>();
        urlCodeFieldsValidation.put("customAlias", 10000);
        urlCodeFieldsValidation.put("url", 10010);
        urlCodeFieldsValidation.put("description", 10020);
        urlCodeFieldsValidation.put("validSince", 10030);
        urlCodeFieldsValidation.put("validUntil", 10040);
        urlCodeFieldsValidation.put("isActive", 10050);
        urlCodeFieldsValidation.put("page", 10060);
        urlCodeFieldsValidation.put("size", 10070);
        urlCodeFieldsValidation.put("sortBy", 10080);
        urlCodeFieldsValidation.put("ascending", 10090);
        urlCodeFieldsValidation.put("filterBy", 10100);
        urlCodeFieldsValidation.put("filterValue", 10110);

        return urlCodeFieldsValidation.getOrDefault(fieldName, 0);
    }

}
