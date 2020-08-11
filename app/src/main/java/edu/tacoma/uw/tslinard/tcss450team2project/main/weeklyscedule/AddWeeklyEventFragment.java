package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Calendar;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class AddWeeklyEventFragment extends Fragment implements View.OnClickListener {

    private AddWeeklyEventListener mAddWeeklyEventListener;

    private String mDayOfWeek;
    private EditText mStartTimeEditText, mEndTimeEditText,
            mEventNameEditText, mNoteEditText;
    private Button mSaveEventButton;
    private SpectrumPalette mColorPalette;

    private int mHour, mMinute;
    private Calendar mCalendar;

    private String mHexColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddWeeklyEventListener = (AddWeeklyEventListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_weekly_event, container, false);
        getActivity().setTitle("Add Weekly Event");

        // handling day of week radio group
        RadioGroup dayOfWeekRadioGroup = view.findViewById(R.id.rg_day_of_week);
        dayOfWeekRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_sunday:
                        mDayOfWeek = "0";
                        break;
                    case R.id.radio_monday:
                        mDayOfWeek = "1";
                        break;
                    case R.id.radio_tuesday:
                        mDayOfWeek = "2";
                        break;
                    case R.id.radio_wednesday:
                        mDayOfWeek = "3";
                        break;
                    case R.id.radio_thursday:
                        mDayOfWeek = "4";
                        break;
                    case R.id.radio_friday:
                        mDayOfWeek = "5";
                        break;
                    case R.id.radio_saturday:
                        mDayOfWeek = "6";
                        break;
                }
            }
        });

        mStartTimeEditText = view.findViewById(R.id.et_start_time);
        mEndTimeEditText = view.findViewById(R.id.et_end_time);
        mEventNameEditText = view.findViewById(R.id.et_event_name);
        mColorPalette = view.findViewById(R.id.palette_color);
        mNoteEditText = view.findViewById(R.id.et_note);
        mSaveEventButton = view.findViewById(R.id.btn_save_event);


        mColorPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mHexColor = String.format("#%06X", (0xFFFFFF & color));
            }
        });

        // get today's date
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mStartTimeEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mSaveEventButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
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
            String dayOfWeek = mDayOfWeek;
            String startTime = mStartTimeEditText.getText().toString();
            String endTime = mEndTimeEditText.getText().toString();
            String eventName = mEventNameEditText.getText().toString();
            String color = mHexColor;
            String note = mNoteEditText.getText().toString();
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int startMinute = Integer.parseInt(startTime.split(":")[1]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);
            int endMinute = Integer.parseInt(endTime.split(":")[1]);
            if (mHexColor == null) {
                Toast.makeText(getActivity(), "Please choose a color"
                        , Toast.LENGTH_SHORT).show();
            } else if(startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
                Toast.makeText(getActivity(), "End Time must be greater than the Start Time!"
                        , Toast.LENGTH_SHORT).show();
            } else {
                WeeklyEvent weeklyEvent = new WeeklyEvent(null, dayOfWeek, startTime, endTime, eventName, color, note);
                if (mAddWeeklyEventListener != null) {
                    mAddWeeklyEventListener.addWeeklyEvent(weeklyEvent);
                }
            }
        }
    }

    public interface AddWeeklyEventListener {
        void addWeeklyEvent(WeeklyEvent weeklyEvent);
    }
}