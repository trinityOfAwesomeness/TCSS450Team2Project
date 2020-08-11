package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Add Event page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class EditEventFragment extends Fragment implements View.OnClickListener {

    private Events mEvent;
    private EditMonthlyEventListener mEditMonthlyEventListener;

    private EditText mStartDateEditText, mStartTimeEditText, mEndDateEditText, mEndTimeEditText,
            mEventNameEditText, mNoteEditText;
    private Button mSaveEventButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar mCalendar;

    public EditEventFragment(Events event){
        mEvent = event;
    }
    /**
     * Called to do initial creation of AddEventFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditMonthlyEventListener = (EditMonthlyEventListener) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_add_event.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        getActivity().setTitle("Edit Event");

        // get today's date
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mStartDateEditText = view.findViewById(R.id.et_start_date);
        mStartTimeEditText = view.findViewById(R.id.et_start_time);
        mEndDateEditText = view.findViewById(R.id.et_end_date);
        mEndTimeEditText = view.findViewById(R.id.et_end_time);
        mEventNameEditText = view.findViewById(R.id.et_event_name);
        mNoteEditText = view.findViewById(R.id.et_note);
        mSaveEventButton = view.findViewById(R.id.btn_save_event);

        mStartDateEditText.setText(mEvent.getStartDate());
        mStartTimeEditText.setText(mEvent.getStartTime());
        mEndDateEditText.setText(mEvent.getEndDate());
        mEndTimeEditText.setText(mEvent.getEndTime());
        mEventNameEditText.setText(mEvent.getEventName());
        mNoteEditText.setText(mEvent.getNote());

        mStartDateEditText.setOnClickListener(this);
        mStartTimeEditText.setOnClickListener(this);
        mEndDateEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mSaveEventButton.setOnClickListener(this);
        return view;
    }

    /**
     * Actions to be done when an view is clicked
     *
     * @param view - the clicked view
     */
    @Override
    public void onClick(View view) {
        if (view == mStartDateEditText) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mStartDateEditText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == mStartTimeEditText) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mStartTimeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (view == mEndDateEditText) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mEndDateEditText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == mEndTimeEditText) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mEndTimeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (view == mSaveEventButton) {
            String startDate = mStartDateEditText.getText().toString();
            String startTime = mStartTimeEditText.getText().toString();
            String endDate = mEndDateEditText.getText().toString();
            String endTime = mEndTimeEditText.getText().toString();
            String eventName = mEventNameEditText.getText().toString();
            String note = mNoteEditText.getText().toString();

            mEvent.setStartDate(startDate);
            mEvent.setStartTime(startTime);
            mEvent.setEndDate(endDate);
            mEvent.setEndTime(endTime);
            mEvent.setEventName(eventName);
            mEvent.setNote(note);

            if (mEditMonthlyEventListener != null) {
                mEditMonthlyEventListener.editMonthlyEvent(mEvent);
            }
        }
    }


    public interface EditMonthlyEventListener {
        void editMonthlyEvent(Events event);
    }
}