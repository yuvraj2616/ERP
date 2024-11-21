package com.erp;

public class Complaint {
    private final String description;
    private String status;

    public Complaint(String description) {
        this.description = description;
        this.status = "Pending"; // Default status when created
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Complaint: " + description + ", Status: " + status;
    }

}