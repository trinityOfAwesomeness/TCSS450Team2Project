package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Calendar page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class CalendarFragment extends Fragment {

    private static final int MAX_CALENDAR_DAYS = 42;

    private View mView;
    private ImageButton mNextMonthImageButton;
    private ImageButton mPreviousMonthImageButton;
    private TextView mCurrentDateTextView;
    private GridView mGridView;
    private CalendarGridAdapter mCalendarGridAdapter;
    private AlertDialog mAlertDialog;
    private List<Date> mPageDates;
    private List<Events> mEventsList;
    private Calendar mMainCalendar;
    private SimpleDateFormat mMonthYearFormat;
    private SimpleDateFormat mDateFormat;

    /**
     * Called to do initial creation of CalendarFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainCalendar = Calendar.getInstance(Locale.ENGLISH);       // set Calendar according to today's date
        mMonthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        mDateFormat = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        mPageDates = new ArrayList<>();
        mEventsList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_calendar.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle("Calendar");
        mNextMonthImageButton = mView.findViewById(R.id.ib_next);
        mPreviousMonthImageButton = mView.findViewById(R.id.ib_previous);
        mCurrentDateTextView = mView.findViewById(R.id.tv_current_Date);
        mGridView = mView.findViewById(R.id.calendar_grid_view);

        // Update the calendar with current month's dates when this fragment is initialized
        updateCalendar();

        // Update the calendar with last month's dates
        mPreviousMonthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainCalendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // Update the calendar with next month's dates
        mNextMonthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainCalendar.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // Display list of selected day's events
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 7) {
                    Date selectedDate = mPageDates.get(position - 7);
                    if (!collectEventsByDate(selectedDate).isEmpty()) {
                        openEventDialog(view, selectedDate);
                    }
                }
            }
        });

        return mView;
    }

    /**
     * Opens up the dialog and display list of events using recycle view.
     * @param view - the fragment's view
     * @param selectedDate - the selected date to show events
     */
    private void openEventDialog(View view, Date selectedDate){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_events, null);

        // set recycler view
        RecyclerView recyclerView = dialogView.findViewById(R.id.events_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dialogView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        String stringDate = mDateFormat.format(selectedDate);
        builder.setView(dialogView)
                .setTitle("Events on " + stringDate)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        mAlertDialog = builder.create();
        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(this,
                mAlertDialog, collectEventsByDate(selectedDate));
        eventRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(eventRecyclerAdapter);
        mAlertDialog.show();
    }

    /**
     * Create the options menu when the user opens the menu for the first time.
     *
     * @param menu     - menu container
     * @param inflater - The MenuInflater object that can be used to inflate views in the menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * When the user selects an item from the options menu, the system calls this method.
     *
     * @param item - the MenuItem selected.
     * @return - the boolean for checking if an item is selected or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Launches AddEventFragment if create_event_item is selected
        if (id == R.id.add_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddEventFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Collect events corresponding to the input date.
     *
     * @param date - the specific date to collect events
     * @return - list of events corresponding to the input date.
     */
    private ArrayList<Events> collectEventsByDate(Date date) {
        String selectedDate = mDateFormat.format(date);
        ArrayList<Events> selectedDateEvents = new ArrayList<>();
        for (int i = 0; i < mEventsList.size(); i++) {
            String compareDate = mEventsList.get(i).getEndDate();
            if (selectedDate.equals(compareDate)) {
                selectedDateEvents.add(mEventsList.get(i));
            }
        }
        return selectedDateEvents;
    }


    /**
     * Sets up a page of calendar based on the current month.
     * It sets up total number of 42 days for each month including all the days of the current month
     * and a few days from previous/next months to make calendar look organized.
     */
    private void updateCalendar() {
        String currentDate = mMonthYearFormat.format(mMainCalendar.getTime());
        mCurrentDateTextView.setText(currentDate);
        mPageDates.clear();

        //setting up the start day of the calendar
        Calendar pageCalendar = (Calendar) mMainCalendar.clone();
        pageCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayofWeekOfCurrentMonth = pageCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        pageCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofWeekOfCurrentMonth);
        //add total number of 42 days to mPageDates list
        while (mPageDates.size() < MAX_CALENDAR_DAYS) {
            mPageDates.add(pageCalendar.getTime());
            pageCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mCalendarGridAdapter = new CalendarGridAdapter(mView.getContext(), mPageDates, mMainCalendar, mEventsList);
        mGridView.setAdapter(mCalendarGridAdapter);

        // sort monthly events by creation order
        Collections.sort(mEventsList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Events event1 = (Events) o1;
                Events event2 = (Events) o2;
                return event1.getEventId().compareTo(event2.getEventId());
            }
        });
    }

    /**
     * Sets up list of events.
     *
     * @param events - list of events to be set
     */
    public void setEventsList(List<Events> events) {
        mEventsList = events;
        updateCalendar();
    }

    /**
     * Interface for getting events.
     */
    public interface GetMonthlyEventsListener {
        /**
         * Retrieve events from the web service.
         */
        void getMonthlyEvents();
    }
}