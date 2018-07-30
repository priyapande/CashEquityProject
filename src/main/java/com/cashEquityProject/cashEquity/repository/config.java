package com.cashEquityProject.cashEquity.repository;

public interface config {

    final public static Integer SUCCESS = 200;
    final public static Integer FAILED = 400;

    final public static Integer INVALID_USER = 500;
    final public static Integer INVALID_PASSWORD = 600;

    // Order status Information
    // 0 - Not yet executed
    // 1 - In progress
    // 2 - Order executed

    final public static Integer NOT_EXECUTED = 0;
    final public static Integer IN_PROGRESS = 1;
    final public static Integer EXECUTED = 2;


}
