package com.erp;

public class Feedback<T> {
    private T feedbackData;
    private String studentName;

    public Feedback(String studentName, T feedbackData) {
        this.studentName = studentName;
        this.feedbackData = feedbackData;
    }

    public T getFeedbackData() {
        return feedbackData;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        if (feedbackData instanceof Number) {
            return String.format("Rating by %s: %d", studentName, feedbackData);
        } else {
            return String.format("Feedback by %s: %s", studentName, feedbackData);
        }
    }
}
