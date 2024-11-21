package com.erp;

public class CourseFullException extends Exception {
    public CourseFullException(String courseName) {
        super("Cannot register for " + courseName + ": Course is full.");
    }
}
