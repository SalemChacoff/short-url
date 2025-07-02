package com.java.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static final String LOGGER_PATTERN = "[Class: {}] [Method: {}] [Line: {}] - {}";

    private CustomLogger() {
        // Private constructor to prevent instantiation
    }

    public static void logInfo(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        logger.info(LOGGER_PATTERN,
                clazz.getSimpleName(),
                caller.getMethodName(),
                caller.getLineNumber(),
                message);
    }

    public static void logWarning(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        logger.warn(LOGGER_PATTERN,
                clazz.getSimpleName(),
                caller.getMethodName(),
                caller.getLineNumber(),
                message);
    }

    public static void logError(Class<?> clazz, String message, Throwable e) {
        Logger logger = LoggerFactory.getLogger(clazz);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        logger.error(LOGGER_PATTERN,
                clazz.getSimpleName(),
                caller.getMethodName(),
                caller.getLineNumber(),
                message, e);
    }
}