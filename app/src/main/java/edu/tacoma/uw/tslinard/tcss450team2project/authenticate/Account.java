package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import java.io.Serializable;

/**
 * User's account class.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class Account implements Serializable {

    // Properties of JSON object. Used to GET/POST an account.
    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private String mFirstName;
    private String mLastName;
    private String mUserName;
    private String mEmail;
    private String mPassword;

    public Account(String firstName, String lastName, String username, String email, String password) {
        mFirstName = firstName;
        mLastName = lastName;
        mUserName = username;
        mEmail = email;
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

}
