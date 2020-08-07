package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

/**
 * Class to create and control the Weekly Schedule page.
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class WeeklyScheduleFragment extends Fragment {
    private View mView;
    private GridView mGridView;
    private List<Events> mEventsList;
    private List<Date> mCurrentWeekDateList;
    private WeeklyScheduleGridAdapter mWeeklyScheduleGridAdapter;

    /**
     * Called to do initial creation of WeeklyScheduleFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventsList = new ArrayList<>();
        mCurrentWeekDateList = getCurrentWeekDates();
        setHasOptionsMenu(true);
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_weekly_schedule.xml file.
     *
     * @param inflater - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
        getActivity().setTitle("Weekly Schedule");
        mGridView = mView.findViewById(R.id.weekly_schedule_grid_view_am);

        updateWeeklySchedule();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 7;
                Toast.makeText(getActivity(), "Position: " + position
                        , Toast.LENGTH_SHORT).show();
            }
        });

        return mView;
    }

    private void updateWeeklySchedule() {
//        Toast.makeText(getActivity(), extractEventsInCurrentWeek(mEventsList).toString() + "KK"
//                , Toast.LENGTH_SHORT).show();
        mWeeklyScheduleGridAdapter = new WeeklyScheduleGridAdapter(mView.getContext(), mCurrentWeekDateList,
                extractEventsInCurrentWeek(mEventsList));
        mGridView.setAdapter(mWeeklyScheduleGridAdapter);
    }

    private List<Events> extractEventsInCurrentWeek(List<Events> allEvents){
        List<Events> eventsInCurrentWeek = new ArrayList<>();
        Calendar eventCalendar = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        for(Events event: allEvents) {
            Date eventEndDate = Events.convertStringToDate(event.getEndDate());
            eventCalendar.setTime(eventEndDate);
            for(Date date: mCurrentWeekDateList) {
                dateCalendar.setTime(date);
                if (dateCalendar.get(Calendar.DAY_OF_MONTH) == eventCalendar.get(Calendar.DAY_OF_MONTH)
                        && dateCalendar.get(Calendar.MONTH) + 1 == eventCalendar.get(Calendar.MONTH) + 1
                        && dateCalendar.get(Calendar.YEAR) == eventCalendar.get(Calendar.YEAR)) {
                    eventsInCurrentWeek.add(event);
                }
            }
        }
        return eventsInCurrentWeek;
    }

    // Get dates of the current week
    private List<Date> getCurrentWeekDates(){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        List<Date> currentWeekDateList = new ArrayList<>();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -currentDayOfWeek);
        while (currentWeekDateList.size() < 7) {
            currentWeekDateList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return currentWeekDateList;
    }


    /**
     * Sets up list of events.
     *
     * @param events - list of events to be set
     */
    public void setEventsList(List<Events> events) {
        mEventsList = events;
        updateWeeklySchedule();
    }
}