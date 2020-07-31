package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.SignInActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddEventFragment.AddEventListener, CalendarFragment.GetEventsListener {

    private DrawerLayout mDrawer;
    private JSONObject mAddEventJSON;
    private JSONObject mGetEventsJSON;
    private String mEmail;
    private List<Events> mEventsList = new ArrayList<>();
    CalendarFragment calendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        mEmail = sharedPreferences.getString(getString(R.string.PASSEMAIL),"");

        // We have set this class' theme to "@style/AppTheme.NoActionBar"
        // so we need a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.main_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) headerView.findViewById(R.id.display_email);
        tv_email.setText(mEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up calendar fragment once this activity is loaded
        calendarFragment = new CalendarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, calendarFragment)
                .commit();
        navigationView.setCheckedItem(R.id.nav_calendar);
        getEvents();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    // handle button activities
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.btn_add_event) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new AddEventFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_weekly_schedule:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new WeeklyScheduleFragment()).commit();
                break;
            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, calendarFragment).commit();
                break;
            case R.id.nav_to_do_list:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ToDoListFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout:
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false).commit();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Listener for AddEventFragment.AddEventListener
     *
     * @param event
     */
    @Override
    public void addEvent(Events event) {
        StringBuilder url = new StringBuilder(getString(R.string.add_event));

        mAddEventJSON = new JSONObject();
        try {
            mAddEventJSON.put(Events.START_DATE, event.getStartDate());
            mAddEventJSON.put(Events.START_TIME, event.getStartTime());
            mAddEventJSON.put(Events.END_DATE, event.getEndDate());
            mAddEventJSON.put(Events.END_TIME, event.getEndTime());
            mAddEventJSON.put(Events.EVENT_NAME, event.getEventName());
            mAddEventJSON.put(Events.NOTE, event.getNote());
            mAddEventJSON.put(Events.EMAIL, mEmail);

            new AddEventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Listener for CalendarFragment.CollectEventsListener
     */
    @Override
    public void getEvents() {
        StringBuilder url = new StringBuilder(getString(R.string.get_events));
        mGetEventsJSON = new JSONObject();
        try {
            mGetEventsJSON.put(Events.EMAIL, mEmail);
            new GetEventsTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on getting events: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private class GetEventsTask extends AsyncTask<String, Void, String> {
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

                    wr.write(mGetEventsJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to get events, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.startsWith("Unable to get events")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    mEventsList = Events.parseEventsJson(jsonObject.getString("events"));
                    calendarFragment.setEventsList(mEventsList);
                    calendarFragment.setUpCalendar();

                    Toast.makeText(getApplicationContext(), "Events retrieved successfully"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Events couldn't be retrieved: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error: "
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AddEventAsyncTask extends AsyncTask<String, Void, String> {
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

                    wr.write(mAddEventJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add event, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.startsWith("Unable to add event")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    getEvents();
                    Toast.makeText(getApplicationContext(), "Event added successfully"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Event couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error: "
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
        }
    }
}