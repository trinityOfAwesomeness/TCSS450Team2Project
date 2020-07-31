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

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.MainActivity;

public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentListener, CreateAccountFragment.CreateAccountListener {

    private SharedPreferences mSharedPreferences;
    private JSONObject mCreateAccountJSON;
    private JSONObject mLoginJSON;
    private boolean mLoginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_layout, new LoginFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void login(String email, String pwd) {
        mLoginMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.login));
        mLoginJSON = new JSONObject();
        try {
            mLoginJSON.put(Account.EMAIL, email);
            mLoginJSON.put(Account.PASSWORD, pwd);
            new SignInAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on creating an account: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void launchMain() {
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        try {
            mSharedPreferences.edit().putString(getString(R.string.PASSEMAIL), mLoginJSON.getString(Account.EMAIL)).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void launchCreateAccountFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sign_in_layout, new CreateAccountFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createAccount(Account account) {
        StringBuilder url = new StringBuilder(getString(R.string.register));

        mCreateAccountJSON = new JSONObject();
        try {
            mCreateAccountJSON.put(Account.FIRST_NAME, account.getFirstName());
            mCreateAccountJSON.put(Account.LAST_NAME, account.getLastName());
            mCreateAccountJSON.put(Account.USER_NAME, account.getUserName());
            mCreateAccountJSON.put(Account.EMAIL, account.getEmail());
            mCreateAccountJSON.put(Account.PASSWORD, account.getPassword());
            new SignInAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on creating an account: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class SignInAsyncTask extends AsyncTask<String, Void, String> {
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

                    if (!mLoginMode) {
                        wr.write(mCreateAccountJSON.toString());
                    } else {
                        wr.write(mLoginJSON.toString());
                    }
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    if (!mLoginMode) {
                        response = "Unable to add a new account, Reason: "
                                + e.getMessage();
                    } else {
                        response = "Unable to log in, Reason: "
                                + e.getMessage();
                    }
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.startsWith("Unable to add a new account")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            } else if (response.startsWith("Unable to log in")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!mLoginMode) {
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "Account created successfully"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Account couldn't be created: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "Logged in successfully"
                                , Toast.LENGTH_SHORT).show();
                        launchMain();
                    } else {
                        Toast.makeText(getApplicationContext(), "Couldn't log in: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error: "
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

}