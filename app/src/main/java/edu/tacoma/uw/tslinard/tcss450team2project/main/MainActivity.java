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
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.Account;
import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.SignInActivity;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.AddMonthlyEventFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.CalendarFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.EditMonthlyEventFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.MonthlyEvent;
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.MonthlyEventRecyclerAdapter;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.AddTaskFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.EditTaskFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.Task;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.TaskRecyclerAdapter;
import edu.tacoma.uw.tslinard.tcss450team2project.main.todolist.ToDoListFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule.AddWeeklyEventFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule.EditWeeklyEventFragment;
import edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule.WeeklyEvent;
import edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule.WeeklyScheduleFragment;

/**
 * Activity class that control 3 different main fragments: CalendarFragment, ToDoListFragment, WeeklyScheduleFragment.
 * It connects to the backend database through AsyncTask and GET/POST data.
 * It contains drawer menu to navigate different fragments.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CalendarFragment.GetMonthlyEventsListener, AddMonthlyEventFragment.AddMonthlyEventListener,
        MonthlyEventRecyclerAdapter.DeleteMonthlyEventListener, EditMonthlyEventFragment.EditMonthlyEventListener,
        WeeklyScheduleFragment.GetWeeklyEventsListener, AddWeeklyEventFragment.AddWeeklyEventListener,
        WeeklyScheduleFragment.DeleteWeeklyEventListener, EditWeeklyEventFragment.EditWeeklyEventListener,
        ToDoListFragment.GetTasksListener, AddTaskFragment.AddTaskListener,
        TaskRecyclerAdapter.DeleteTaskListener, EditTaskFragment.EditTaskListener,
        ResetPasswordFragment.ResetPasswordListener {

    private String mEmail;
    private DrawerLayout mDrawer;

    private boolean mResetPasswordMode;
    private JSONObject mResetPasswordJSON;

    // calendar
    private boolean mGetMonthlyEventsMode;
    private boolean mAddMonthlyEventMode;
    private boolean mDeleteMonthlyEventMode;
    private boolean mEditMonthlyEventMode;
    private JSONObject mGetMonthlyEventsJSON;
    private JSONObject mAddMonthlyEventJSON;
    private JSONObject mDeleteMonthlyEventJSON;
    private JSONObject mEditMonthlyEventJSON;
    private CalendarFragment mCalendarFragment;

    // weekly schedule
    private boolean mGetWeeklyEventsMode;
    private boolean mAddWeeklyEventMode;
    private boolean mDeleteWeeklyEventMode;
    private boolean mEditWeeklyEventMode;
    private JSONObject mGetWeeklyEventsJSON;
    private JSONObject mAddWeeklyEventJSON;
    private JSONObject mDeleteWeeklyEventJSON;
    private JSONObject mEditWeeklyEventJSON;
    private WeeklyScheduleFragment mWeeklyScheduleFragment;

    // to do list
    private boolean mGetTasksMode;
    private boolean mAddTaskMode;
    private boolean mDeleteTaskMode;
    private boolean mEditTaskMode;
    private JSONObject mGetTasksJSON;
    private JSONObject mAddTaskJSON;
    private JSONObject mDeleteTaskJSON;
    private JSONObject mEditTaskJSON;
    private ToDoListFragment mToDoListFragment;

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
        mToDoListFragment = new ToDoListFragment();

        // Opens up the weekly schedule fragment once this activity is loaded
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mWeeklyScheduleFragment)
                .commit();
        navigationView.setCheckedItem(R.id.nav_weekly_schedule);
        getWeeklyEvents();
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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mWeeklyScheduleFragment).commit();
                getWeeklyEvents();
                break;
            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mCalendarFragment).commit();
                getMonthlyEvents();
                break;
            case R.id.nav_to_do_list:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mToDoListFragment).commit();
                getTasks();
                break;
            case R.id.nav_rest_password:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ResetPasswordFragment())
                        .addToBackStack(null).commit();
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
     * Overridden method from CalendarFragment.GetMonthlyEventsListener interface.
     * Retrieve monthly events from the web service.
     */
    @Override
    public void getMonthlyEvents() {
        mGetMonthlyEventsMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.get_events));
        mGetMonthlyEventsJSON = new JSONObject();
        try {
            mGetMonthlyEventsJSON.put(MonthlyEvent.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error in getting events: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from AddMonthlyEventFragment.AddMonthlyEventListener interface.
     * It adds a new monthly event through
     * posting the monthly event to the web service.
     *
     * @param monthlyEvent - monthlyEvent to be added
     */
    @Override
    public void addMonthlyEvent(MonthlyEvent monthlyEvent) {
        mAddMonthlyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.add_event));
        mAddMonthlyEventJSON = new JSONObject();
        try {
            mAddMonthlyEventJSON.put(MonthlyEvent.START_DATE, monthlyEvent.getStartDate());
            mAddMonthlyEventJSON.put(MonthlyEvent.START_TIME, monthlyEvent.getStartTime());
            mAddMonthlyEventJSON.put(MonthlyEvent.END_DATE, monthlyEvent.getEndDate());
            mAddMonthlyEventJSON.put(MonthlyEvent.END_TIME, monthlyEvent.getEndTime());
            mAddMonthlyEventJSON.put(MonthlyEvent.EVENT_NAME, monthlyEvent.getEventName());
            mAddMonthlyEventJSON.put(MonthlyEvent.NOTE, monthlyEvent.getNote());
            mAddMonthlyEventJSON.put(MonthlyEvent.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding monthlyEvent: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from MonthlyEventRecyclerAdapter.DeleteMonthlyEventListener interface.
     * It deletes a monthly event through
     * posting the event id to the web service.
     *
     * @param eventId - monthly event's id
     */
    @Override
    public void deleteMonthlyEvent(String eventId) {
        mDeleteMonthlyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.delete_event));
        mDeleteMonthlyEventJSON = new JSONObject();
        try {
            mDeleteMonthlyEventJSON.put(MonthlyEvent.EVENT_ID, eventId);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on deleting event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from EditMonthlyEventFragment.EditMonthlyEventListener interface.
     * It edits a monthly event through
     * posting the monthly event to the web service.
     *
     * @param monthlyEvent - monthly event to be edited
     */
    @Override
    public void editMonthlyEvent(MonthlyEvent monthlyEvent) {
        mEditMonthlyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.edit_event));
        mEditMonthlyEventJSON = new JSONObject();
        try {
            mEditMonthlyEventJSON.put(MonthlyEvent.EVENT_ID, monthlyEvent.getEventId());
            mEditMonthlyEventJSON.put(MonthlyEvent.START_DATE, monthlyEvent.getStartDate());
            mEditMonthlyEventJSON.put(MonthlyEvent.START_TIME, monthlyEvent.getStartTime());
            mEditMonthlyEventJSON.put(MonthlyEvent.END_DATE, monthlyEvent.getEndDate());
            mEditMonthlyEventJSON.put(MonthlyEvent.END_TIME, monthlyEvent.getEndTime());
            mEditMonthlyEventJSON.put(MonthlyEvent.EVENT_NAME, monthlyEvent.getEventName());
            mEditMonthlyEventJSON.put(MonthlyEvent.NOTE, monthlyEvent.getNote());
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on editing monthlyEvent: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from WeeklyScheduleFragment.GetWeeklyEventsListener interface.
     * Retrieve weekly events from the web service.
     */
    @Override
    public void getWeeklyEvents() {
        mGetWeeklyEventsMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.get_weekly_events));
        mGetWeeklyEventsJSON = new JSONObject();
        try {
            mGetWeeklyEventsJSON.put(MonthlyEvent.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error in getting weekly events: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from AddWeeklyEventFragment.AddWeeklyEventListener interface.
     * It adds a new weekly event through
     * posting the weekly event to the web service.
     *
     * @param weeklyEvent - weeklyEvent to be added
     */
    @Override
    public void addWeeklyEvent(WeeklyEvent weeklyEvent) {
        mAddWeeklyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.add_weekly_event));
        mAddWeeklyEventJSON = new JSONObject();
        try {
            mAddWeeklyEventJSON.put(WeeklyEvent.DAY_OF_WEEK, weeklyEvent.getDayOfWeek());
            mAddWeeklyEventJSON.put(WeeklyEvent.START_TIME, weeklyEvent.getStartTime());
            mAddWeeklyEventJSON.put(WeeklyEvent.END_TIME, weeklyEvent.getEndTime());
            mAddWeeklyEventJSON.put(WeeklyEvent.EVENT_NAME, weeklyEvent.getEventName());
            mAddWeeklyEventJSON.put(WeeklyEvent.COLOR, weeklyEvent.getColor());
            mAddWeeklyEventJSON.put(WeeklyEvent.NOTE, weeklyEvent.getNote());
            mAddWeeklyEventJSON.put(WeeklyEvent.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding weekly event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from WeeklyScheduleFragment.DeleteWeeklyEventListener interface.
     * It deletes a weekly event through
     * posting the event id to the web service.
     *
     * @param eventId - weekly event's id
     */
    @Override
    public void deleteWeeklyEvent(String eventId) {
        mDeleteWeeklyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.delete_weekly_event));
        mDeleteWeeklyEventJSON = new JSONObject();
        try {
            mDeleteWeeklyEventJSON.put(WeeklyEvent.EVENT_ID, eventId);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on deleting weekly event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from EditWeeklyEventFragment.EditWeeklyEventListener interface.
     * It edits a weekly event through
     * posting the weekly event to the web service.
     *
     * @param weeklyEvent - weekly event to be edited
     */
    @Override
    public void editWeeklyEvent(WeeklyEvent weeklyEvent) {
        mEditWeeklyEventMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.edit_weekly_event));
        mEditWeeklyEventJSON = new JSONObject();
        try {
            mEditWeeklyEventJSON.put(WeeklyEvent.EVENT_ID, weeklyEvent.getEventId());
            mEditWeeklyEventJSON.put(WeeklyEvent.DAY_OF_WEEK, weeklyEvent.getDayOfWeek());
            mEditWeeklyEventJSON.put(WeeklyEvent.START_TIME, weeklyEvent.getStartTime());
            mEditWeeklyEventJSON.put(WeeklyEvent.END_TIME, weeklyEvent.getEndTime());
            mEditWeeklyEventJSON.put(WeeklyEvent.EVENT_NAME, weeklyEvent.getEventName());
            mEditWeeklyEventJSON.put(WeeklyEvent.COLOR, weeklyEvent.getColor());
            mEditWeeklyEventJSON.put(WeeklyEvent.NOTE, weeklyEvent.getNote());
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on editing weekly event: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from ToDoListFragment.GetTasksListener interface.
     * Retrieve tasks from the web service.
     */
    @Override
    public void getTasks() {
        mGetTasksMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.get_tasks));
        mGetTasksJSON = new JSONObject();
        try {
            mGetTasksJSON.put(Task.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error in getting tasks: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from AddTaskFragment.AddTaskListener interface.
     * It adds a new task through
     * posting the task to the web service.
     *
     * @param task - task to be added
     */
    @Override
    public void addTask(Task task) {
        mAddTaskMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.add_task));
        mAddTaskJSON = new JSONObject();
        try {
            mAddTaskJSON.put(Task.TASK, task.getTask());
            mAddTaskJSON.put(Task.STATUS, task.getStatus());
            mAddTaskJSON.put(Task.EMAIL, mEmail);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding task: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from TaskRecyclerAdapter.DeleteTaskListener interface.
     * It deletes a task through
     * posting the task id to the web service.
     *
     * @param toDoId - task's id
     */
    @Override
    public void deleteTask(String toDoId) {
        mDeleteTaskMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.delete_task));
        mDeleteTaskJSON = new JSONObject();
        try {
            mDeleteTaskJSON.put(Task.TO_DO_ID, toDoId);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on deleting task: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from EditTaskFragment.EditTaskListener interface.
     * It edits a task through
     * posting the task to the web service.
     *
     * @param task - task to be edited
     */
    @Override
    public void editTask(Task task) {
        mEditTaskMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.edit_task));
        mEditTaskJSON = new JSONObject();
        try {
            mEditTaskJSON.put(Task.TO_DO_ID, task.getToDoId());
            mEditTaskJSON.put(Task.TASK, task.getTask());
            mEditTaskJSON.put(Task.STATUS, task.getStatus());
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on editing task: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden method from ResetPasswordFragment.ResetPasswordListener interface.
     * It resets the password through
     * posting the new password to the web service.
     *
     * @param newPassword - new password
     */
    @Override
    public void resetPassword(String newPassword) {
        mResetPasswordMode = true;
        StringBuilder url = new StringBuilder(getString(R.string.reset_password));
        mResetPasswordJSON = new JSONObject();
        try {
            mResetPasswordJSON.put(Account.EMAIL, mEmail);
            mResetPasswordJSON.put(Account.PASSWORD, newPassword);
            new EventAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error in resetting password: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Inner class which can connect to the backend database and GET/POST data to the corresponding web service.
     */
    private class EventAsyncTask extends AsyncTask<String, Void, String> {
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

                    if (mResetPasswordMode) {
                        wr.write(mResetPasswordJSON.toString());
                    } else if (mGetMonthlyEventsMode) {
                        wr.write(mGetMonthlyEventsJSON.toString());
                    } else if (mAddMonthlyEventMode) {
                        wr.write(mAddMonthlyEventJSON.toString());
                    } else if (mDeleteMonthlyEventMode) {
                        wr.write(mDeleteMonthlyEventJSON.toString());
                    } else if (mEditMonthlyEventMode) {
                        wr.write(mEditMonthlyEventJSON.toString());
                    } else if (mGetWeeklyEventsMode) {
                        wr.write(mGetWeeklyEventsJSON.toString());
                    } else if (mAddWeeklyEventMode) {
                        wr.write(mAddWeeklyEventJSON.toString());
                    } else if (mDeleteWeeklyEventMode) {
                        wr.write(mDeleteWeeklyEventJSON.toString());
                    } else if (mEditWeeklyEventMode) {
                        wr.write(mEditWeeklyEventJSON.toString());
                    } else if (mGetTasksMode) {
                        wr.write(mGetTasksJSON.toString());
                    } else if (mAddTaskMode) {
                        wr.write(mAddTaskJSON.toString());
                    } else if (mDeleteTaskMode) {
                        wr.write(mDeleteTaskJSON.toString());
                    } else if (mEditTaskMode) {
                        wr.write(mEditTaskJSON.toString());
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

                    if (mResetPasswordMode) {
                        response = "Unable to reset password, Reason: "
                                + e.getMessage();
                    } else if (mGetMonthlyEventsMode) {
                        response = "Unable to get monthly events, Reason: "
                                + e.getMessage();
                    } else if (mAddMonthlyEventMode) {
                        response = "Unable to add monthly event, Reason: "
                                + e.getMessage();
                    } else if (mDeleteMonthlyEventMode) {
                        response = "Unable to delete monthly event, Reason: "
                                + e.getMessage();
                    } else if (mEditMonthlyEventMode) {
                        response = "Unable to edit monthly event, Reason: "
                                + e.getMessage();
                    } else if (mGetWeeklyEventsMode) {
                        response = "Unable to get weekly events, Reason: "
                                + e.getMessage();
                    } else if (mAddWeeklyEventMode) {
                        response = "Unable to add weekly event, Reason: "
                                + e.getMessage();
                    } else if (mDeleteWeeklyEventMode) {
                        response = "Unable to delete weekly event, Reason: "
                                + e.getMessage();
                    } else if (mEditWeeklyEventMode) {
                        response = "Unable to edit weekly event, Reason: "
                                + e.getMessage();
                    } else if (mGetTasksMode) {
                        response = "Unable to get tasks, Reason: "
                                + e.getMessage();
                    } else if (mAddTaskMode) {
                        response = "Unable to add task, Reason: "
                                + e.getMessage();
                    } else if (mDeleteTaskMode) {
                        response = "Unable to delete task, Reason: "
                                + e.getMessage();
                    } else if (mEditTaskMode) {
                        response = "Unable to edit task, Reason: "
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
            if (response.startsWith("Unable to get monthly events") || response.startsWith("Unable to add monthly event") ||
                    response.startsWith("Unable to delete monthly event") || response.startsWith("Unable to edit monthly event") ||
                    response.startsWith("Unable to get weekly events") || response.startsWith("Unable to add weekly event") ||
                    response.startsWith("Unable to delete weekly event") || response.startsWith("Unable to edit weekly event") ||
                    response.startsWith("Unable to get tasks") || response.startsWith("Unable to add task") ||
                    response.startsWith("Unable to delete task") || response.startsWith("Unable to edit task") ||
                    response.startsWith("Unable to reset password")) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {

                    if(mResetPasswordMode) {
                        Toast.makeText(getApplicationContext(), "Password reset successfully"
                                , Toast.LENGTH_SHORT).show();
                        mResetPasswordMode = false;
                    } else if (mGetMonthlyEventsMode) {
                        List<MonthlyEvent> monthlyEventList = MonthlyEvent.parseMonthlyEventsJson(jsonObject.getString("events"));
                        mCalendarFragment.setMonthlyEventList(monthlyEventList);
                        Toast.makeText(getApplicationContext(), "Monthly events retrieved successfully"
                                , Toast.LENGTH_SHORT).show();
                        mGetMonthlyEventsMode = false;
                    } else if (mAddMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event added successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mAddMonthlyEventMode = false;

                        mGetMonthlyEventsMode = true;
                        getMonthlyEvents();
                    } else if (mDeleteMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event deleted successfully"
                                , Toast.LENGTH_SHORT).show();
                        mDeleteMonthlyEventMode = false;

                        mGetMonthlyEventsMode = true;
                        getMonthlyEvents();
                    } else if (mEditMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event edited successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mEditMonthlyEventMode = false;

                        mGetMonthlyEventsMode = true;
                        getMonthlyEvents();
                    } else if (mGetWeeklyEventsMode) {
                        List<WeeklyEvent> weeklyEventList = WeeklyEvent.parseWeeklyEventJson(jsonObject.getString("weeklyevents"));
                        mWeeklyScheduleFragment.setWeeklyEventList(weeklyEventList);
                        Toast.makeText(getApplicationContext(), "Weekly events retrieved successfully"
                                , Toast.LENGTH_SHORT).show();
                        mGetWeeklyEventsMode = false;
                    } else if (mAddWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event added successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mAddWeeklyEventMode = false;

                        mGetWeeklyEventsMode = true;
                        getWeeklyEvents();
                    } else if (mDeleteWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event deleted successfully"
                                , Toast.LENGTH_SHORT).show();
                        mDeleteWeeklyEventMode = false;

                        mGetWeeklyEventsMode = true;
                        getWeeklyEvents();
                    } else if (mEditWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event edited successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mEditWeeklyEventMode = false;

                        mGetWeeklyEventsMode = true;
                        getWeeklyEvents();
                    } else if (mGetTasksMode) {
                        List<Task> taskList = Task.parseTasksJson(jsonObject.getString("todolist"));
                        mToDoListFragment.setTaskList(taskList);
                        Toast.makeText(getApplicationContext(), "Tasks retrieved successfully"
                                , Toast.LENGTH_SHORT).show();
                        mGetTasksMode = false;
                    } else if (mAddTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task added successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mAddTaskMode = false;

                        mGetTasksMode = true;
                        getTasks();
                    } else if (mDeleteTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task deleted successfully"
                                , Toast.LENGTH_SHORT).show();
                        mDeleteTaskMode = false;

                        mGetTasksMode = true;
                        getTasks();
                    } else if (mEditTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task edited successfully"
                                , Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStackImmediate();
                        mEditTaskMode = false;

                        mGetTasksMode = true;
                        getTasks();
                    }
                } else {
                    if (mGetMonthlyEventsMode) {
                        Toast.makeText(getApplicationContext(), "Monthly events couldn't be retrieved: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mAddMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event couldn't be added: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mDeleteMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event couldn't be deleted: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mEditMonthlyEventMode) {
                        Toast.makeText(getApplicationContext(), "Monthly event couldn't be edited: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mGetWeeklyEventsMode) {
                        Toast.makeText(getApplicationContext(), "Weekly events couldn't be retrieved: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mAddWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event couldn't be added: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mDeleteWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event couldn't be deleted: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mEditWeeklyEventMode) {
                        Toast.makeText(getApplicationContext(), "Weekly event couldn't be edited: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mGetTasksMode) {
                        Toast.makeText(getApplicationContext(), "Tasks couldn't be retrieved: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mAddTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task couldn't be added: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mDeleteTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task couldn't be deleted: "
                                        + jsonObject.getString("error")
                                , Toast.LENGTH_LONG).show();
                    } else if (mEditTaskMode) {
                        Toast.makeText(getApplicationContext(), "Task couldn't be edited: "
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