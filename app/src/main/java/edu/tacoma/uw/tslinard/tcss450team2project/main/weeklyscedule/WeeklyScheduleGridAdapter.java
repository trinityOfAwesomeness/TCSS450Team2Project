package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

public class WeeklyScheduleGridAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<Date> mCurrentWeekDateList;
    private List<Events> mEventsList;

    public WeeklyScheduleGridAdapter(@NonNull Context context, List<Date> currentWeekDateList, List<Events> eventsList) {
        super(context, R.layout.single_cell_weekly_schedule);
        mInflater = LayoutInflater.from(context);
        mCurrentWeekDateList = currentWeekDateList;
        mEventsList = eventsList;
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

            convertView = mInflater.inflate(R.layout.single_cell_weekly_schedule_dayofweek, parent, false);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwPurple));
            TextView dayOfWeekTextView = convertView.findViewById(R.id.tv_weekly_schedule_dayofweek);
            dayOfWeekTextView.setText(dayOfWeek);
        } else {
            position -= 7;
            convertView = mInflater.inflate(R.layout.single_cell_weekly_schedule, parent, false);

            if(position / 7 % 2 == 0){
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold3));
            } else {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold2));
            }


            for(int i = 0; i < mEventsList.size(); i++) {
                Date eventStartDate = Events.convertStringToDate(mEventsList.get(i).getStartDate());
                Date eventEndDate = Events.convertStringToDate(mEventsList.get(i).getEndDate());
                String eventStartTime = mEventsList.get(i).getStartTime();
                String eventEndTime = mEventsList.get(i).getEndTime();
                int startHour = Integer.parseInt(eventStartTime.split(":")[0]);
                int startMinute = Integer.parseInt(eventStartTime.split(":")[1]);
                int endHour = Integer.parseInt(eventEndTime.split(":")[0]);
                int endMinute = Integer.parseInt(eventEndTime.split(":")[1]);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventStartDate);
                int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 Sun ~ 6 Sat
                calendar.setTime(eventEndDate);
                int endDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                if(position % 7 == endDayOfWeek && position / 7 >= startHour && position / 7 < endHour){
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold));
                }
            }
        }


//        if(mEventsList.isEmpty()){
//                    Toast.makeText(convertView.getContext(), "NNOOOOO"
//                , Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(convertView.getContext(), ""+mEventsList.toString()
//                    , Toast.LENGTH_SHORT).show();
//        }
        return convertView;
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
