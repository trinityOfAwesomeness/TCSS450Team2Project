package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.app.AlertDialog;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

/**
 * Class to create and control the Calendar page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class CalendarFragment extends Fragment {

    private static final int MAX_CALENDAR_DAYS = 42;

    private ImageButton mNextMonthImageButton, mPreviousMonthImageButton;
    private TextView mCurrentDateTextView;
    private GridView mGridView;
    private GridAdapter mGridAdapter;
    private AlertDialog mAlertDialog;
    private View mView;
    private Calendar mMainCalendar = Calendar.getInstance(Locale.ENGLISH);       // calendar set according to today's date
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private List<Date> mPageDates = new ArrayList<>();
    private List<Events> mEventsList = new ArrayList<>();
    /**
     * Called to do initial creation of CalendarFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mGridView = mView.findViewById(R.id.grid_view);

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

        // Display list of clicked day's events
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 7) {
                    Date date = mPageDates.get(position - 7);
                    if (!collectEventsByDate(date).isEmpty()) {
                        // display list of events using AlertDialog which has recyclerView in it.
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setCancelable(true);
                        View displayView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_display_events, null);
                        RecyclerView recyclerView = displayView.findViewById(R.id.events_recycle_view);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(displayView.getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(displayView.getContext()
                                , collectEventsByDate(date));
                        recyclerView.setAdapter(eventRecyclerAdapter);
                        eventRecyclerAdapter.notifyDataSetChanged();
                        builder.setView(displayView);
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
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
        //hide a menu item
        menu.findItem(R.id.add_event_item).setVisible(false);
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
        if (id == R.id.create_event_item) {
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        String selectedDate = simpleDateFormat.format(date);
        ArrayList<Events> selectedDateEvents = new ArrayList<>();
        for (int i = 0; i < mEventsList.size(); i++) {
            String compareDate = mEventsList.get(i).getStartDate();
            if (selectedDate.equals(compareDate)) {
                selectedDateEvents.add(mEventsList.get(i));
            }
        }
        return selectedDateEvents;
    }


    /**
     * Sets up a page of calendar based on the current month.
     * It sets up total number of 42 days of month including all the days of the current month
     * and a few days from previous/next months to make calendar look organized.
     */
    public void updateCalendar() {
        String currentDate = mDateFormat.format(mMainCalendar.getTime());
        mCurrentDateTextView.setText(currentDate);
        mPageDates.clear();

        //setting up the start day of the calendar
        Calendar pageCalendar = (Calendar) mMainCalendar.clone();
        pageCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayofWeekOfCurrentMonth = pageCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        pageCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofWeekOfCurrentMonth);

        while (mPageDates.size() < MAX_CALENDAR_DAYS) {
            mPageDates.add(pageCalendar.getTime());
            pageCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mGridAdapter = new GridAdapter(mView.getContext(), mPageDates, mMainCalendar, mEventsList);
        mGridView.setAdapter(mGridAdapter);
    }

    /**
     * Sets up list of events.
     *
     * @param events - list of events to be set
     */
    public void setEventsList(List<Events> events) {
        mEventsList = events;
    }

    /**
     * Interface for getting events.
     */
    public interface GetEventsListener {
        /**
         * Retrieve events from the web service.
         */
        void getEvents();
    }
}