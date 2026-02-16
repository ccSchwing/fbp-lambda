package com.fbp;

public class FBPUser {
    public String firstName;
    public String lastName;
    public String email;
    public String displayName;

    public FBPUser() {

    }

    public FBPUser(String firstName, String lastName, String email, String displayName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.displayName = displayName;
    }
}
