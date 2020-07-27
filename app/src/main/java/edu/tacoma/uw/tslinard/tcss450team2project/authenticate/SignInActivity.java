package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.MainMenuActivity;

public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentListener, CreateAccountFragment.CreateAccountListener {

    private SharedPreferences mSharedPreferences;
    private List<Account> mAccountList;
    private JSONObject mRegisterJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_id, new LoginFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void login(String email, String pwd) {
        Toast.makeText(getApplicationContext(),email + ": " + pwd, Toast.LENGTH_SHORT).show();
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void createAccount(Account account) {
        StringBuilder url = new StringBuilder(getString(R.string.register));

        mRegisterJSON = new JSONObject();
        try{
            mRegisterJSON.put(Account.FIRST_NAME, account.getFirstName());
            mRegisterJSON.put(Account.LAST_NAME, account.getLastName());
            mRegisterJSON.put(Account.USER_NAME, account.getUserName());
            mRegisterJSON.put(Account.EMAIL, account.getEmail());
            mRegisterJSON.put(Account.PASSWORD, account.getPassword());
            new CreateAccountAsyncTask().execute(url.toString());
        } catch (JSONException e){
            Toast.makeText(this, "Error with JSON creation on creating an account: "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class CreateAccountAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                  //  Log.i(ADD_COURSE, mCourseJSON.toString());
                    wr.write(mRegisterJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new account, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new course")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Account Added successfully"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Account couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    //Log.e(ADD_COURSE, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Creating account"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
               // Log.e(ADD_COURSE, e.getMessage());
            }
        }
    }

}