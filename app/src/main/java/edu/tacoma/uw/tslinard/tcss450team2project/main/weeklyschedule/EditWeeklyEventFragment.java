package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Edit Weekly Event page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class EditWeeklyEventFragment extends Fragment implements View.OnClickListener {

    private EditWeeklyEventListener mEditWeeklyEventListener;
    private int mHour, mMinute;
    private String mDayOfWeek, mHexColor;
    private WeeklyEvent mWeeklyEvent;
    private EditText mStartTimeEditText, mEndTimeEditText, mEventNameEditText, mNoteEditText;
    private Button mSaveEventButton;

    public EditWeeklyEventFragment(WeeklyEvent weeklyEvent) {
        mWeeklyEvent = weeklyEvent;
    }

    /**
     * Called to do initial creation of EditWeeklyEventFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditWeeklyEventListener = (EditWeeklyEventListener) getActivity();
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
        getActivity().setTitle("Edit Weekly Event");

        mStartTimeEditText = view.findViewById(R.id.et_start_time);
        mEndTimeEditText = view.findViewById(R.id.et_end_time);
        mEventNameEditText = view.findViewById(R.id.et_event_name);
        mNoteEditText = view.findViewById(R.id.et_note);
        mSaveEventButton = view.findViewById(R.id.btn_save_event);

        mStartTimeEditText.setText(mWeeklyEvent.getStartTime());
        mEndTimeEditText.setText(mWeeklyEvent.getEndTime());
        mEventNameEditText.setText(mWeeklyEvent.getEventName());
        mNoteEditText.setText(mWeeklyEvent.getNote());
        mHexColor = mWeeklyEvent.getColor();

        initializeDayOfWeekRadioButton(view);
        initializeColorPaletteRadioButton(view);

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
     * Initialize day of week radio button with user's original selected day of week.
     * @param view - View for the fragment's UI
     */
    private void initializeDayOfWeekRadioButton(View view) {
        RadioButton sundayRadioButton = view.findViewById(R.id.radio_sunday);
        RadioButton mondayRadioButton = view.findViewById(R.id.radio_monday);
        RadioButton tuesdayRadioButton = view.findViewById(R.id.radio_tuesday);
        RadioButton wednesdayRadioButton = view.findViewById(R.id.radio_wednesday);
        RadioButton thursdayRadioButton = view.findViewById(R.id.radio_thursday);
        RadioButton fridayRadioButton = view.findViewById(R.id.radio_friday);
        RadioButton saturdayRadioButton = view.findViewById(R.id.radio_saturday);

        switch (Integer.parseInt(mWeeklyEvent.getDayOfWeek())) {
            case 0:
                mDayOfWeek = "0";
                sundayRadioButton.setChecked(true);
                break;
            case 1:
                mDayOfWeek = "1";
                mondayRadioButton.setChecked(true);
                break;
            case 2:
                mDayOfWeek = "2";
                tuesdayRadioButton.setChecked(true);
                break;
            case 3:
                mDayOfWeek = "3";
                wednesdayRadioButton.setChecked(true);
                break;
            case 4:
                mDayOfWeek = "4";
                thursdayRadioButton.setChecked(true);
                break;
            case 5:
                mDayOfWeek = "5";
                fridayRadioButton.setChecked(true);
                break;
            case 6:
                mDayOfWeek = "6";
                saturdayRadioButton.setChecked(true);
                break;
        }

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
    }

    /**
     * Initialize color palette radio button with user's original selected color.
     * @param view - View for the fragment's UI
     */
    private void initializeColorPaletteRadioButton(View view) {

        RadioButton pinkRadioButton = view.findViewById(R.id.color_pink);
        RadioButton blueRadioButton = view.findViewById(R.id.color_blue);
        RadioButton greenRadioButton = view.findViewById(R.id.color_green);
        RadioButton whiteRadioButton = view.findViewById(R.id.color_white);
        RadioButton yellowRadioButton = view.findViewById(R.id.color_yellow);
        RadioButton orangeRadioButton = view.findViewById(R.id.color_orange);

        final String pink = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.pink) & 0x00ffffff);
        final String blue = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.blue) & 0x00ffffff);
        final String green = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.green) & 0x00ffffff);
        final String white = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.white) & 0x00ffffff);
        final String yellow = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.yellow) & 0x00ffffff);
        final String orange = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.orange) & 0x00ffffff);

        switch (mHexColor) {
            case "#ffdeed":
                mHexColor = pink;
                pinkRadioButton.setChecked(true);
                break;
            case "#c8eeff":
                mHexColor = blue;
                blueRadioButton.setChecked(true);
                break;
            case "#2aed76":
                mHexColor = green;
                greenRadioButton.setChecked(true);
                break;
            case "#dde6ec":
                mHexColor = white;
                whiteRadioButton.setChecked(true);
                break;
            case "#ffeb3b":
                mHexColor = yellow;
                yellowRadioButton.setChecked(true);
                break;
            case "#ffc107":
                mHexColor = orange;
                orangeRadioButton.setChecked(true);
                break;
        }

        // handling color palette radio group
        RadioGroup palette_color_RadioGroup = view.findViewById(R.id.rg_palette_color);
        palette_color_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.color_pink:
                        mHexColor = pink;
                        break;
                    case R.id.color_blue:
                        mHexColor = blue;
                        break;
                    case R.id.color_green:
                        mHexColor = green;
                        break;
                    case R.id.color_white:
                        mHexColor = white;
                        break;
                    case R.id.color_yellow:
                        mHexColor = yellow;
                        break;
                    case R.id.color_orange:
                        mHexColor = orange;
                        break;
                }
            }
        });
    }

    /**
     * Actions to be done when an view is clicked
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
                mWeeklyEvent.setDayOfWeek(dayOfWeek);
                mWeeklyEvent.setStartTime(startTime);
                mWeeklyEvent.setEndTime(endTime);
                mWeeklyEvent.setEventName(eventName);
                mWeeklyEvent.setColor(color);
                mWeeklyEvent.setNote(note);
                if (mEditWeeklyEventListener != null) {
                    mEditWeeklyEventListener.editWeeklyEvent(mWeeklyEvent);
                }
            }
        }
    }

    /**
     * Interface for editing a weekly event.
     */
    public interface EditWeeklyEventListener {
        /**
         * Editing a weekly event through
         * posting the weekly event to the web service.
         *
         * @param weeklyEvent - weekly event to be edited
         */
        void editWeeklyEvent(WeeklyEvent weeklyEvent);
    }
}