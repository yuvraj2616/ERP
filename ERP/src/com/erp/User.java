package com.erp;

class User implements IUser {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }


    public void logout() {
        System.out.println(email + " logged out.");
    }
}
