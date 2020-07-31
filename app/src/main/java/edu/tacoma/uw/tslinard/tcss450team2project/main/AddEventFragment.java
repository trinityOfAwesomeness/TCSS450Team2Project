package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class AddEventFragment extends Fragment implements View.OnClickListener {

    private AddEventListener mAddEventListener;

    private EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime, txtEventName, txtNote;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mAddEventListener = (AddEventListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        getActivity().setTitle("Add Event");

        txtStartDate = view.findViewById(R.id.et_start_date);
        txtStartTime = view.findViewById(R.id.et_start_time);
        txtEndDate = view.findViewById(R.id.et_end_date);
        txtEndTime = view.findViewById(R.id.et_end_time);
        txtEventName = view.findViewById(R.id.et_event_name);
        txtNote = view.findViewById(R.id.et_note);

        // get current date
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        txtStartDate.setText((mMonth + 1)  + "/" + mDay + "/" + mYear);
        txtStartTime.setText(mHour + ":" + mMinute);
        txtEndDate.setText((mMonth + 1)  + "/" + mDay + "/" + mYear);
        txtEndTime.setText(mHour + ":" + mMinute);

        txtStartDate.setOnClickListener(this);
        txtStartTime.setOnClickListener(this);
        txtEndDate.setOnClickListener(this);
        txtEndTime.setOnClickListener(this);

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        //hide item
        menu.findItem(R.id.add_event_item).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.check_item) {
            String startDate = txtStartDate.getText().toString();
            String startTime = txtStartTime.getText().toString();
            String endDate = txtEndDate.getText().toString();
            String endTime = txtEndTime.getText().toString();
            String eventName = txtEventName.getText().toString();
            String note = txtNote.getText().toString();
            Events event = new Events(startDate, startTime, endDate, endTime, eventName, note);
            if(mAddEventListener != null) {
                mAddEventListener.addEvent(event);
            }
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public interface AddEventListener {
        void addEvent(Events event);
    }

    @Override
    public void onClick(View v) {

        if (v == txtStartDate) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtStartDate.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtStartTime) {

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtStartTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == txtEndDate) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtEndDate.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtEndTime) {

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtEndTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}