package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mUserName;
    private String mPassword;

    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";


    public Account(String firstName, String lastName, String email,
                    String username, String password){
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mUserName = username;
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public static List<Account> parseCourseJson(String accountJson) throws JSONException {
        List<Account> accountList = new ArrayList<>();
        if(accountJson != null){

            JSONArray arr = new JSONArray(accountJson);

            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Account account = new Account(obj.getString(Account.FIRST_NAME), obj.getString(Account.LAST_NAME)
                        , obj.getString(Account.EMAIL), obj.getString(Account.USER_NAME), obj.getString(Account.PASSWORD));
                accountList.add(account);
            }
        }
        return accountList;
    }
}
