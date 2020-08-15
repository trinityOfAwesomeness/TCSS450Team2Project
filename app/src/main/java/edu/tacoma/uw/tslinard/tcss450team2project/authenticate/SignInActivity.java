package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
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

/**
 * Class to initialize sign in activity.
 * It implements login and sing up functionalities.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginListener, SignUpFragment.SignUpListener {

    private SharedPreferences mSharedPreferences;
    private JSONObject mSignUpJSON;
    private JSONObject mLoginJSON;
    private boolean mLoginMode;

    /**
     * Called when the activity is starting.
     * It inflates the activity's UI using activity_sign_in.xml file.
     *
     * @param savedInstanceState - If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Checks if login has been done. If so, go to MainActivity class.
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

    /**
     * Overridden method from LoginFragment.LoginListener interface.
     * It logs in with user's entered email and password through
     * retrieving corresponding account from the web service.
     *
     * @param email    - user'e email
     * @param password - user's password
     */
    @Override
    public void login(String email, String password) {
        mLoginMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.login));
        mLoginJSON = new JSONObject();
        try {
            mLoginJSON.put(Account.EMAIL, email);
            mLoginJSON.put(Account.PASSWORD, password);
            new SignInAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on creating an account: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from LoginFragment.LoginListener interface.
     * It launches signUpFragment.
     */
    @Override
    public void launchSignUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sign_in_layout, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }


    /**
     * Overridden method from SignUpFragment.SignUpListener interface.
     * It signs up an input account through
     * posting the account to the web service.
     *
     * @param account - The account to be signed up.
     */
    @Override
    public void singUp(Account account) {
        mLoginMode = false;
        StringBuilder url = new StringBuilder(getString(R.string.register));
        mSignUpJSON = new JSONObject();
        try {
            mSignUpJSON.put(Account.FIRST_NAME, account.getFirstName());
            mSignUpJSON.put(Account.LAST_NAME, account.getLastName());
            mSignUpJSON.put(Account.USER_NAME, account.getUserName());
            mSignUpJSON.put(Account.EMAIL, account.getEmail());
            mSignUpJSON.put(Account.PASSWORD, account.getPassword());
            new SignInAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on creating an account: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launch MainActivity class with data stored in mSharedPreferences.
     */
    private void launchMain() {
        try {
            mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true)
                    .putString(getString(R.string.PASSEMAIL), mLoginJSON.getString(Account.EMAIL))
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Inner class which can connect to the backend database and GET/POST data to the corresponding web service.
     * It tries to login if mLoginMode is set to true. Otherwise, it tries to sign up an account.
     */
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
                        wr.write(mSignUpJSON.toString());
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
            if (response.startsWith("Unable to add a new account") || response.startsWith("Unable to log in")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!mLoginMode) {
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "Account created successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                    } else {
                        Toast.makeText(getApplicationContext(), "Account couldn't be created: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (jsonObject.getBoolean("success")) {
                        //Initialize Progress Dialog
                        ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.dialog_progress);
                        //Set Transparent Background
                        progressDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
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
                final TextView errorTextView = findViewById(R.id.tv_error);
                errorTextView.setText("That account doesn't exist. Enter a different account.");
                Toast.makeText(getApplicationContext(), "JSON Parsing error: "
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

}