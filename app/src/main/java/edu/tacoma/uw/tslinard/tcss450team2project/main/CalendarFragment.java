package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;


public class CalendarFragment extends Fragment {

    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    View view;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar mainCalendar = Calendar.getInstance(Locale.ENGLISH);       // contains today's date
    GridAdapter myGridAdapter;
    List<Date> pageDates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);

        SetUpCalendar();

        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(view.getContext(), "You clicked " + position , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(mainCalendar.getTime());
        CurrentDate.setText(currentDate);
        pageDates.clear();

        //setting up the start day of the calendar
        Calendar pageCalendar = (Calendar) mainCalendar.clone();
        pageCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = pageCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        pageCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (pageDates.size() < MAX_CALENDAR_DAYS) {
            pageDates.add(pageCalendar.getTime());
            pageCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new GridAdapter(view.getContext(),pageDates,mainCalendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }
}