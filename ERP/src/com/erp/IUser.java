package com.erp;

public interface IUser {
    boolean login(String email, String password);
    void logout();
    String getEmail();
    String getPassword();
}
