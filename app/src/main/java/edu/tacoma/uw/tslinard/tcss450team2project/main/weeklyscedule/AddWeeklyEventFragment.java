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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AddWeeklyEventFragment extends Fragment implements View.OnClickListener {

    private AddWeeklyEventListener mAddWeeklyEventListener;

    private String mDayOfWeek;
    private EditText mStartTimeEditText, mEndTimeEditText,
            mEventNameEditText, mNoteEditText;
    private Button mColorButton, mSaveEventButton;

    private int mHour, mMinute;
    private Calendar mCalendar;

    private int mDefaultColor;
    private String mHexColor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddWeeklyEventListener = (AddWeeklyEventListener) getActivity();
        mDefaultColor = ContextCompat.getColor(getActivity(), R.color.uwPurple);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_weekly_event, container, false);
        getActivity().setTitle("Add Weekly Event");

        // handling day of week radio group
        RadioGroup dayOfWeekRadioGroup = view.findViewById(R.id.rg_day_of_week);
        dayOfWeekRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId){
                    case R.id.radio_sunday:
                        mDayOfWeek = "0";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_monday:
                        mDayOfWeek = "1";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_tuesday:
                        mDayOfWeek = "2";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_wednesday:
                        mDayOfWeek = "3";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_thursday:
                        mDayOfWeek = "4";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_friday:
                        mDayOfWeek = "5";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_saturday:
                        mDayOfWeek = "6";
                        Toast.makeText(getActivity(), "Selected Radio Button: " + mDayOfWeek
                                , Toast.LENGTH_SHORT).show();
                        break;
                }

//                RadioButton dayOfWeekRadioButton = getView().findViewById(checkedId);
//                mDayOfWeek = dayOfWeekRadioButton.getText().toString();
            }
        });

        mStartTimeEditText = view.findViewById(R.id.et_start_time);
        mEndTimeEditText = view.findViewById(R.id.et_end_time);
        mEventNameEditText = view.findViewById(R.id.et_event_name);
        mColorButton = view.findViewById(R.id.btn_color);
        mNoteEditText = view.findViewById(R.id.et_note);
        mSaveEventButton = view.findViewById(R.id.btn_save_event);

        // get today's date
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mStartTimeEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mColorButton.setOnClickListener(this);
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
        if (view == mColorButton){
            openColorPicker();
        }
        if (view == mSaveEventButton) {
            String dayOfWeek = mDayOfWeek;
            String startTime = mStartTimeEditText.getText().toString();
            String endTime = mEndTimeEditText.getText().toString();
            String eventName = mEventNameEditText.getText().toString();
            String color = mHexColor;
            String note = mNoteEditText.getText().toString();
            WeeklyEvent weeklyEvent = new WeeklyEvent(null, dayOfWeek, startTime, endTime, eventName, color, note);
            if (mAddWeeklyEventListener != null) {
                mAddWeeklyEventListener.addWeeklyEvent(weeklyEvent);
            }
        }
    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mHexColor = String.format("#%06X", (0xFFFFFF & mDefaultColor));
                mColorButton.setBackgroundColor(mDefaultColor);
//                Toast.makeText(getActivity(), "" + mHexColor
//                        , Toast.LENGTH_SHORT).show();
            }
        });
        colorPicker.show();
    }

    public interface AddWeeklyEventListener {
        void addWeeklyEvent(WeeklyEvent weeklyEvent);
    }
}