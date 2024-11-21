package com.erp;

public class DropDeadlinePassedException extends Exception {
    public DropDeadlinePassedException(String courseName) {
        super("Cannot drop " + courseName + ": Drop deadline has passed.");
    }
}