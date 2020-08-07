package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyScedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

public class WeeklyScheduleGridAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<Date> mCurrentWeekDateList;
    private List<Events> mEventsList;

    public WeeklyScheduleGridAdapter(@NonNull Context context, List<Date> currentWeekDateList, List<Events> eventsList) {
        super(context, R.layout.layout_single_cell_weekly_schedule);
        mCurrentWeekDateList = currentWeekDateList;
        mEventsList = eventsList;
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
            convertView = mInflater.inflate(R.layout.layout_single_cell_weekly_schedule_dayofweek, parent, false);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwWhite));
            TextView dayOfWeekTextView = convertView.findViewById(R.id.tv_weekly_schedule_dayofweek);
            dayOfWeekTextView.setText(dayOfWeek);
        } else {
            // Inflates view with layout if it is created for the first time. (Similar to recyclerview)
            convertView = mInflater.inflate(R.layout.layout_single_cell_weekly_schedule, parent, false);
            //  }

//            TextView weeklyScheduleTextView = convertView.findViewById(R.id.tv_weekly_schedule);
//            Calendar eventCalendar = Calendar.getInstance();
//            Calendar weekDateCalendar = Calendar.getInstance();
//            for(Events event: mEventsList) {
//                Date eventEndDate = convertStringToDate(event.getEndDate());
//                eventCalendar.setTime(eventEndDate);
//                for(Date date: mCurrentWeekDateList) {
//                    weekDateCalendar.setTime(date);
//                    // if the event is in the current week
//                    if (weekDateCalendar.get(Calendar.DAY_OF_MONTH) == eventCalendar.get(Calendar.DAY_OF_MONTH)
//                            && weekDateCalendar.get(Calendar.MONTH) + 1 == eventCalendar.get(Calendar.MONTH) + 1
//                            && weekDateCalendar.get(Calendar.YEAR) == eventCalendar.get(Calendar.YEAR)) {
//
//                        weeklyScheduleTextView.setText("hi");
//                    }
//                }
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold));
        }
        return convertView;
    }

    /**
     * Covert string representation of a date into Date object.
     *
     * @param eventDate - string representation of a date
     * @return - Date object
     */
    private Date convertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Get number of items in the grid view.
     *
     * @return - number of items in the grid view
     */
    @Override
    public int getCount() {
        return 175;
    }
}
