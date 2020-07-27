package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import java.io.Serializable;

public class Account implements Serializable {

    private String mFirstName;
    private String mLastName;
    private String mUserName;
    private String mEmail;
    private String mPassword;

    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";


    public Account(String firstName, String lastName, String username, String email, String password){
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

    // Use below method when parsing JSON is needed
//    public static List<Account> parseCourseJson(String accountJson) throws JSONException {
//        List<Account> accountList = new ArrayList<>();
//        if(accountJson != null){
//
//            JSONArray arr = new JSONArray(accountJson);
//
//            for(int i = 0; i < arr.length(); i++) {
//                JSONObject obj = arr.getJSONObject(i);
//                Account account = new Account(obj.getString(Account.FIRST_NAME), obj.getString(Account.LAST_NAME)
//                        , obj.getString(Account.EMAIL), obj.getString(Account.USER_NAME), obj.getString(Account.PASSWORD));
//                accountList.add(account);
//            }
//        }
//        return accountList;
//    }
}
