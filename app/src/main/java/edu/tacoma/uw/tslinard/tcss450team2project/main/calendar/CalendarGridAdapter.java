package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

/**
 * Class to display single cell of the calendar.
 * It is an adapter class which connects the data and the view.
 */
public class CalendarGridAdapter extends ArrayAdapter {

    private List<Date> mPageDates;
    private Calendar mCurrentDate;
    private List<Events> mEventsList;
    private LayoutInflater mInflater;

    public CalendarGridAdapter(@NonNull Context context, List<Date> pageDates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_calendar);
        mPageDates = pageDates;
        mCurrentDate = currentDate;
        mEventsList = events;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Get a View that displays the day of month at the specified position in the Calendar.
     *
     * @param position    - the position of the grid cell
     * @param convertView - view that inflate single cell layout
     * @param parent      - parent view that apply default layout parameters
     * @return - view of the single cell
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // the first row displays the day of week
        if (position < 7) {
            String dayOfWeek = "";
            switch (position) {
                case 0:
                    dayOfWeek = "Sun";
                    break;
                case 1:
                    dayOfWeek = "Mon";
                    break;
                case 2:
                    dayOfWeek = "Tue";
                    break;
                case 3:
                    dayOfWeek = "Wed";
                    break;
                case 4:
                    dayOfWeek = "Thu";
                    break;
                case 5:
                    dayOfWeek = "Fri";
                    break;
                case 6:
                    dayOfWeek = "Sat";
                    break;
            }

            // Inflates view with layout if it is created for the first time. (Similar to recyclerview)
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_cell_calendar_dayofweek, parent, false);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwWhite));
                TextView dayOfWeekTextView = convertView.findViewById(R.id.tv_calendar_dayofweek);
                dayOfWeekTextView.setText(dayOfWeek);
            }
        }
        // the rest of the rows display days of month
        else {
            Date pageDate = mPageDates.get(position - 7);
            Calendar pageCalendar = Calendar.getInstance();
            pageCalendar.setTime(pageDate);
            int displayDay = pageCalendar.get(Calendar.DAY_OF_MONTH);
            int displayMonth = pageCalendar.get(Calendar.MONTH) + 1;
            int displayYear = pageCalendar.get(Calendar.YEAR);
            int currentMonth = mCurrentDate.get(Calendar.MONTH) + 1;
            int currentYear = mCurrentDate.get(Calendar.YEAR);

            // Inflates view with layout if it is created for the first time. (Similar to recyclerview)
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_cell_calendar, parent, false);
            }


            TextView calendarDayTextView = convertView.findViewById(R.id.tv_calendar_day);
            TextView numberOfEventsTextView = convertView.findViewById(R.id.tv_number_of_events);
            // Set the background color of a cell to white
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwWhite));
            // Deepen color of the day's text view if the day is in the current month
            if (displayMonth == currentMonth && displayYear == currentYear) {
                calendarDayTextView.setTextColor(Color.parseColor("#FF000000"));
                numberOfEventsTextView.setTextColor(Color.parseColor("#BE000000"));
            } else {
                calendarDayTextView.setTextColor(Color.parseColor("#FFDAD8D8"));
                numberOfEventsTextView.setTextColor(Color.parseColor("#FFDAD8D8"));
            }

            // display day in the calendar
            calendarDayTextView.setText(String.valueOf(displayDay));

            // display number of events of the day in the calendar
            Calendar eventCalendar = Calendar.getInstance();
            ArrayList<Events> currentDateEvents = new ArrayList<>();
            for (int i = 0; i < mEventsList.size(); i++) {
                Date date = Events.convertStringToDate(mEventsList.get(i).getEndDate());
                eventCalendar.setTime(date);
                if (displayDay == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                        && displayYear == eventCalendar.get(Calendar.YEAR)) {
                    currentDateEvents.add(mEventsList.get(i));
                    numberOfEventsTextView.setText(currentDateEvents.size() + " Events");
                }
            }
        }
        return convertView;
    }

    /**
     * Get number of items in the grid view.
     *
     * @return - number of items in the grid view
     */
    @Override
    public int getCount() {
        return mPageDates.size() + 7; //49
    }


}