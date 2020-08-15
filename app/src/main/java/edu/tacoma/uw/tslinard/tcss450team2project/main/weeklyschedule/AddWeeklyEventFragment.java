package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule;

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

/**
 * Class to create and control the Add Weekly Event page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class AddWeeklyEventFragment extends Fragment implements View.OnClickListener {

    private AddWeeklyEventListener mAddWeeklyEventListener;
    private int mHour, mMinute;
    private String mHexColor;
    private String mDayOfWeek;
    private EditText mStartTimeEditText, mEndTimeEditText, mEventNameEditText, mNoteEditText;
    private Button mSaveEventButton;

    /**
     * Called to do initial creation of AddWeeklyEventFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddWeeklyEventListener = (AddWeeklyEventListener) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_add_weekly_event.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
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

        // handling color palette radio group
        RadioGroup palette_color_RadioGroup = view.findViewById(R.id.rg_palette_color);
        palette_color_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.color_pink:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.pink) & 0x00ffffff);
                        break;
                    case R.id.color_blue:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.blue) & 0x00ffffff);
                        break;
                    case R.id.color_green:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.green) & 0x00ffffff);
                        break;
                    case R.id.color_white:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.white) & 0x00ffffff);
                        break;
                    case R.id.color_yellow:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.yellow) & 0x00ffffff);
                        break;
                    case R.id.color_orange:
                        mHexColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.orange) & 0x00ffffff);
                        break;
                }
            }
        });
        mStartTimeEditText = view.findViewById(R.id.et_start_time);
        mEndTimeEditText = view.findViewById(R.id.et_end_time);
        mEventNameEditText = view.findViewById(R.id.et_event_name);
        mNoteEditText = view.findViewById(R.id.et_note);
        mSaveEventButton = view.findViewById(R.id.btn_save_event);

        // get today's date
        Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        mStartTimeEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mSaveEventButton.setOnClickListener(this);
        return view;
    }

    /**
     * Actions to be done when a view is clicked
     *
     * @param view - the clicked view
     */
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
            } else if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
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

    /**
     * Interface for adding a weekly event.
     */
    public interface AddWeeklyEventListener {
        /**
         * Adding a new weekly event through
         * posting the weekly event to the web service.
         *
         * @param weeklyEvent - weekly event to be added
         */
        void addWeeklyEvent(WeeklyEvent weeklyEvent);
    }
}