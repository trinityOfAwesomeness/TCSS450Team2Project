package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class GridAdapter extends ArrayAdapter {

    private List<Date> mPageDates;
    private Calendar mCurrentDate;
    private List<Events> mEventsList;
    private LayoutInflater mInflater;

    public GridAdapter(@NonNull Context context, List<Date> pageDates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_day_layout);

        this.mPageDates = pageDates;
        this.mCurrentDate = currentDate;
        this.mEventsList = events;

        //layout을 뷰로 만들어주는 것
        mInflater = LayoutInflater.from(context);

    }

    //getView는 아이템하나의 view를 만들어주는 메소드다.
    // convertView가 아이템하나의 view이다
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_cell_dayofweek_layout, parent, false);
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                TextView tv_dayOfWeek = convertView.findViewById(R.id.calendar_dayOfWeek);
                tv_dayOfWeek.setText(dayOfWeek);
            }
        } else {
            Date pageDate = mPageDates.get(position - 7);
            Calendar pageCalendar = Calendar.getInstance();
            pageCalendar.setTime(pageDate);
            int displayDay = pageCalendar.get(Calendar.DAY_OF_MONTH);
            int displayMonth = pageCalendar.get(Calendar.MONTH) + 1;
            int displayYear = pageCalendar.get(Calendar.YEAR);
            int currentMonth = mCurrentDate.get(Calendar.MONTH) + 1;
            int currentYear = mCurrentDate.get(Calendar.YEAR);

            // inflater과정을 거쳐야지 화면상의 UI가 만들어진다.
            // layout을가지고 view를 inflate해주는것
            // 한번도 만들어지지않은 item view를 만든다. (만약 화면에 안보여지면 사라지고 추후 재활용을 한다.)
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_cell_day_layout, parent, false);
            }


            TextView tv_calendarDay = convertView.findViewById(R.id.calendar_day);
            TextView tv_numberOfEvents = convertView.findViewById(R.id.number_of_events);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            if (displayMonth == currentMonth && displayYear == currentYear) {
                tv_calendarDay.setTextColor(Color.parseColor("#FF000000"));
                tv_numberOfEvents.setTextColor(Color.parseColor("#BE000000"));
            } else {
                tv_calendarDay.setTextColor(Color.parseColor("#FFDAD8D8"));
                tv_numberOfEvents.setTextColor(Color.parseColor("#FFDAD8D8"));
            }

            tv_calendarDay.setText(String.valueOf(displayDay));

            Calendar eventCalendar = Calendar.getInstance();
            TextView EventNumber = convertView.findViewById(R.id.number_of_events);
            ArrayList<Events> currentDateEvents = new ArrayList<>();
            for (int i = 0; i < mEventsList.size(); i++) {
                Date date = ConvertStringToDate(mEventsList.get(i).getStartDate());
                eventCalendar.setTime(date);
                if (displayDay == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                        && displayYear == eventCalendar.get(Calendar.YEAR)) {
                    currentDateEvents.add(mEventsList.get(i));
                    EventNumber.setText(currentDateEvents.size() + " Events");
                }
            }
        }
        return convertView;
    }

    private Date ConvertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    // 화면상에 뿌려질 data갯수
    @Override
    public int getCount() {
        return mPageDates.size() + 7; //42
    }


    // 무엇을하는가?
    @Override
    public int getPosition(@Nullable Object item) {
        return mPageDates.indexOf(item);
    }


    // 아이템이 무엇을 반환할것인가.
    // 무엇을하는가?
    @Nullable
    @Override
    public Object getItem(int position) {
        return mPageDates.get(position);
    }
}