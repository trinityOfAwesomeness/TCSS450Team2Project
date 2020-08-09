package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class WeeklyScheduleGridAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<WeeklyEvent> mWeeklyEventList;

    public WeeklyScheduleGridAdapter(@NonNull Context context, List<WeeklyEvent> weeklyEventList) {
        super(context, R.layout.single_cell_weekly_schedule);
        mInflater = LayoutInflater.from(context);
        mWeeklyEventList = weeklyEventList;
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

            if (position / 7 % 2 == 0) {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold3));
            } else {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.uwGold2));
            }

//            Toast.makeText(convertView.getContext(), "" +str
//                    , Toast.LENGTH_SHORT).show();

            TextView displayEventNameTextView = convertView.findViewById(R.id.tv_display_event_name);

            for (int i = 0; i < mWeeklyEventList.size(); i++) {
                WeeklyEvent weeklyEvent = mWeeklyEventList.get(i);
                String startTime = weeklyEvent.getStartTime();
                String endTime = weeklyEvent.getEndTime();
                String color = weeklyEvent.getColor();

                int dayOfWeek = Integer.parseInt(weeklyEvent.getDayOfWeek());
                int startHour = Integer.parseInt(startTime.split(":")[0]);
                int startMinute = Integer.parseInt(startTime.split(":")[1]);
                int endHour = Integer.parseInt(endTime.split(":")[0]);
                int endMinute = Integer.parseInt(endTime.split(":")[1]);

                // highlight the current cell if it is in event time range
                if(position % 7 == dayOfWeek) {
                    if(position / 7 >= startHour && position / 7 < endHour){
                        displayEventNameTextView.setText(weeklyEvent.getEventName());
                        convertView.setBackgroundColor(Color.parseColor(color));
                    }
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
        return 175;
    }
}
