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
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.AddEventFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.CalendarFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.ToDoListFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule.WeeklyScheduleFragment;

/**
 * Activity class to control 3 different fragments: CalendarFragment, ToDoListFragment, WeeklyScheduleFragment.
 * It connects to the backend database through AsyncTask and GET/POST event data.
 * It contains drawer menu to navigate different fragments.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddEventFragment.AddEventListener, CalendarFragment.GetEventsListener {

    private String mEmail;
    private DrawerLayout mDrawer;
    private JSONObject mAddEventJSON;
    private JSONObject mGetEventsJSON;
    private boolean mAddEventMode;
    private boolean mWeeklyScheduleMode;
    private CalendarFragment mCalendarFragment;
    private WeeklyScheduleFragment mWeeklyScheduleFragment;
    private List<Events> mEventsList = new ArrayList<>();

    /**
     * Called when the activity is starting.
     * It inflates the activity's UI using activity_main.xml file.
     *
     * @param savedInstanceState - If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("HuskyCal");

        // We have set this class' theme to "@style/AppTheme.NoActionBar"
        // so we need a toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieves email string from sharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        mEmail = sharedPreferences.getString(getString(R.string.PASSEMAIL), "");

        // initialize drawer menu
        mDrawer = findViewById(R.id.main_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = (TextView) headerView.findViewById(R.id.tv_display_email);
        emailTextView.setText(mEmail);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mCalendarFragment = new CalendarFragment();
        mWeeklyScheduleFragment = new WeeklyScheduleFragment();

        // Opens up the weekly schedule fragment once this activity is loaded
        mWeeklyScheduleMode = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mWeeklyScheduleFragment)
                .commit();
        navigationView.setCheckedItem(R.id.nav_weekly_schedule);
        getEvents();
    }

    /**
     * Determine what action to do when an item is selected from drawer menu.
     *
     * @param item - item from drawer menu
     * @return - true if item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_weekly_schedule:
                mWeeklyScheduleMode = true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mWeeklyScheduleFragment).commit();
                getEvents();
                break;
            case R.id.nav_calendar:
                mWeeklyScheduleMode = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mCalendarFragment).commit();
                getEvents();
                break;
            case R.id.nav_to_do_list:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ToDoListFragment()).commit();
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
     * Overridden method from AddEventFragment.AddEventListener interface.
     * It adds a new event through
     * posting the event to the web service.
     *
     * @param event - event to be added
     */
    @Override
    public void addEvent(Events event) {
        mAddEventMode = true;
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

            new EventsAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void editEvent(Events event) {
        mAddEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.edit_event));
        mAddEventJSON = new JSONObject();
        try {
            mAddEventJSON.put(Events.EVENT_ID, event.getEventId());
            mAddEventJSON.put(Events.START_DATE, event.getStartDate());
            mAddEventJSON.put(Events.START_TIME, event.getStartTime());
            mAddEventJSON.put(Events.END_DATE, event.getEndDate());
            mAddEventJSON.put(Events.END_TIME, event.getEndTime());
            mAddEventJSON.put(Events.EVENT_NAME, event.getEventName());
            mAddEventJSON.put(Events.NOTE, event.getNote());
            mAddEventJSON.put(Events.EMAIL, mEmail);

            new EventsAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error editing event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from CalendarFragment.GetEventsListener interface.
     * Retrieve events from the web service.
     */
    @Override
    public void getEvents() {
        mAddEventMode = false;
        StringBuilder url = new StringBuilder(getString(R.string.get_events));
        mGetEventsJSON = new JSONObject();
        try {
            mGetEventsJSON.put(Events.EMAIL, mEmail);
            new EventsAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error in getting events: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inner class which can connect to the backend database and GET/POST data to the corresponding web service.
     * It tries to add an event if mAddEventMode is set to true. Otherwise, it tries to retrieve events.
     */
    private class EventsAsyncTask extends AsyncTask<String, Void, String> {
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

                    if (!mAddEventMode) {
                        wr.write(mGetEventsJSON.toString());
                    } else {
                        wr.write(mAddEventJSON.toString());
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
                    if (!mAddEventMode) {
                        response = "Unable to get events, Reason: "
                                + e.getMessage();
                    } else {
                        response = "Unable to add event, Reason: "
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
            if (response.startsWith("Unable to get events") || response.startsWith("Unable to add event")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    if (!mAddEventMode) {
                        mEventsList = Events.parseEventsJson(jsonObject.getString("events"));
                        if(mWeeklyScheduleMode){
                            mWeeklyScheduleFragment.setEventsList(mEventsList);
                        } else {
                            mCalendarFragment.setEventsList(mEventsList);
                        }

                        Toast.makeText(getApplicationContext(), "Events retrieved successfully"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        getEvents();
                        Toast.makeText(getApplicationContext(), "Event added successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                } else {
                    if (!mAddEventMode) {
                        Toast.makeText(getApplicationContext(), "Events couldn't be retrieved: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Event couldn't be added: "
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