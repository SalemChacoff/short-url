package com.java.admin.constant;

import java.util.HashMap;

public class ApiAccountEndpoints {

    public static final String API_VERSION = "/api/v1";
    public static final String BASE_NAME = "/accounts";
    public static final String BASE_PATH = API_VERSION + BASE_NAME;
    public static final String CREATE_ACCOUNT = "/signup";
    public static final String VERIFY_ACCOUNT = "/verification/{token}";
    public static final String RESEND_VERIFICATION_CODE = "/resend-verification";
    public static final String VALIDATE_CODE = "/validate-code";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String VALIDATE_RESET_PASSWORD = "/reset-password/{token}";
    public static final String CHANGE_PASSWORD = "/change-password";

    private ApiAccountEndpoints() {
    }

    public static Integer getAccountCodeFieldsValidation(String fieldName) {
        HashMap<String, Integer> accountFieldsValidation = new HashMap<>();
        accountFieldsValidation.put("email", 10000);
        accountFieldsValidation.put("password", 10010);
        accountFieldsValidation.put("firstName", 10020);
        accountFieldsValidation.put("lastName", 10030);
        accountFieldsValidation.put("phoneNumber", 10040);
        accountFieldsValidation.put("address", 10050);
        accountFieldsValidation.put("username", 10060);
        return accountFieldsValidation.getOrDefault(fieldName, 0);
    }

}
