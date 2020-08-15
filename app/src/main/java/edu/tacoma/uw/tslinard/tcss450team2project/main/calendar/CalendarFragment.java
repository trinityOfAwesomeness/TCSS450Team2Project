package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView mCurrentDateTextView;
    private GridView mGridView;
    private List<Date> mPageDates;
    private List<MonthlyEvent> mMonthlyEventList;
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
        mMonthlyEventList = new ArrayList<>();
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
        mCurrentDateTextView = mView.findViewById(R.id.tv_current_Date);
        mGridView = mView.findViewById(R.id.calendar_grid_view);

        ImageButton nextMonthImageButton = mView.findViewById(R.id.ib_next);
        ImageButton previousMonthImageButton = mView.findViewById(R.id.ib_previous);

        // Update the calendar with current month's dates when this fragment is initialized
        updateCalendar();

        // Update the calendar with last month's dates
        previousMonthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainCalendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // Update the calendar with next month's dates
        nextMonthImageButton.setOnClickListener(new View.OnClickListener() {
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
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new DisplayMonthlyEventsFragment(collectEventsByDate(selectedDate)))
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
        return mView;
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
        // Launches AddMonthlyEventFragment if create_event_item is selected
        if (id == R.id.add_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddMonthlyEventFragment())
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
    private ArrayList<MonthlyEvent> collectEventsByDate(Date date) {
        String selectedDate = mDateFormat.format(date);
        ArrayList<MonthlyEvent> selectedDateEvents = new ArrayList<>();
        for (int i = 0; i < mMonthlyEventList.size(); i++) {
            String compareDate = mMonthlyEventList.get(i).getEndDate();
            if (selectedDate.equals(compareDate)) {
                selectedDateEvents.add(mMonthlyEventList.get(i));
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

        CalendarGridAdapter calendarGridAdapter = new CalendarGridAdapter(mView.getContext(), mPageDates, mMainCalendar, mMonthlyEventList);
        mGridView.setAdapter(calendarGridAdapter);
    }

    /**
     * Sets up monthly event list.
     *
     * @param monthlyEventList - list of monthly events to be set
     */
    public void setMonthlyEventList(List<MonthlyEvent> monthlyEventList) {
        mMonthlyEventList = monthlyEventList;
        updateCalendar();
    }

    /**
     * Interface for getting monthly events.
     */
    public interface GetMonthlyEventsListener {
        /**
         * Retrieve monthly events from the web service.
         */
        void getMonthlyEvents();
    }
}