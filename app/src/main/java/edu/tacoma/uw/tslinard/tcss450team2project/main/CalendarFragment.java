package edu.tacoma.uw.tslinard.tcss450team2project.main;

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


public class CalendarFragment extends Fragment {

    private static final int MAX_CALENDAR_DAYS = 42;
    private ImageButton mNextButton, mPreviousButton;
    private TextView mCurrentDate;
    private GridView mGridView;
    private View mView;

    private Calendar mMainCalendar = Calendar.getInstance(Locale.ENGLISH);       // contains today's date
    private GridAdapter mGridAdapter;
    private AlertDialog alertDialog;
    private List<Date> mPageDates = new ArrayList<>();
    private List<Events> mEventsList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Calendar");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mNextButton = mView.findViewById(R.id.nextBtn);
        mPreviousButton = mView.findViewById(R.id.previousBtn);
        mCurrentDate = mView.findViewById(R.id.current_Date);
        mGridView = mView.findViewById(R.id.gridview);

        setUpCalendar();

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainCalendar.add(Calendar.MONTH, -1);
                setUpCalendar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainCalendar.add(Calendar.MONTH, 1);
                setUpCalendar();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 7) {
                    Date date = mPageDates.get(position - 7);
                    if (!CollectEventsByDate(date).isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setCancelable(true);
                        View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                        RecyclerView recyclerView = showView.findViewById(R.id.events_recycle_view);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext()
                                , CollectEventsByDate(date));
                        recyclerView.setAdapter(eventRecyclerAdapter);
                        eventRecyclerAdapter.notifyDataSetChanged();
                        builder.setView(showView);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        //hide item
        menu.findItem(R.id.check_item).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_event_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddEventFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    // added when click calendar
    private ArrayList<Events> CollectEventsByDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        String clickedDate = simpleDateFormat.format(date);
        ArrayList<Events> clickedDateEvents = new ArrayList<>();
        for (int i = 0; i < mEventsList.size(); i++) {
            String compareDate = mEventsList.get(i).getStartDate();
            if (clickedDate.equals(compareDate)) {
                clickedDateEvents.add(mEventsList.get(i));
            }
        }

        return clickedDateEvents;
    }


    public void setUpCalendar() {
        String currentDate = dateFormat.format(mMainCalendar.getTime());
        mCurrentDate.setText(currentDate);
        mPageDates.clear();

        //setting up the start day of the calendar
        Calendar pageCalendar = (Calendar) mMainCalendar.clone();
        pageCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = pageCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        pageCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (mPageDates.size() < MAX_CALENDAR_DAYS) {
            mPageDates.add(pageCalendar.getTime());
            pageCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mGridAdapter = new GridAdapter(mView.getContext(), mPageDates, mMainCalendar, mEventsList);
        mGridView.setAdapter(mGridAdapter);
    }

    public void setEventsList(List<Events> events) {
        mEventsList = events;
    }

    public interface GetEventsListener {
        void getEvents();
    }
}