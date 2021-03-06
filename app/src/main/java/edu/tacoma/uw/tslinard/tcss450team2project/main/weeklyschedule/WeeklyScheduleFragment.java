package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyschedule;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Weekly Schedule page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class WeeklyScheduleFragment extends Fragment {

    private DeleteWeeklyEventListener mDeleteWeeklyEventListener;
    private AlertDialog mAlertDialog;
    private View mView;
    private GridView mGridView;
    private List<WeeklyEvent> mWeeklyEventList;

    /**
     * Called to do initial creation of WeeklyScheduleFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeeklyEventList = new ArrayList<>();
        setHasOptionsMenu(true);
        mDeleteWeeklyEventListener = (DeleteWeeklyEventListener) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_weekly_schedule.xml file.
     *
     * @param inflater - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
        getActivity().setTitle("Weekly Schedule");
        mGridView = mView.findViewById(R.id.weekly_schedule_grid_view_am);

        updateWeeklySchedule();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 7;

                for (int i = 0; i < mWeeklyEventList.size(); i++) {
                    WeeklyEvent weeklyEvent = mWeeklyEventList.get(i);
                    String startTime = weeklyEvent.getStartTime();
                    String endTime = weeklyEvent.getEndTime();

                    int dayOfWeek = Integer.parseInt(weeklyEvent.getDayOfWeek());
                    int startHour = Integer.parseInt(startTime.split(":")[0]);
                    int startMinute = Integer.parseInt(startTime.split(":")[1]);
                    int endHour = Integer.parseInt(endTime.split(":")[0]);
                    int endMinute = Integer.parseInt(endTime.split(":")[1]);

                    // highlight the current cell if it is in the event's time range
                    if(position % 7 == dayOfWeek) {
                        if(position / 7 >= startHour && position / 7 < endHour){
                            openEventDialog(view, weeklyEvent);
                        }
                    }

                }
            }
        });

        return mView;
    }

    /**
     * Create the options menu when the user opens the menu for the first time.
     *
     * @param menu     - menu container
     * @param inflater - The MenuInflater object that can be used to inflate views in the menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * When the user selects an item from the options menu, the system calls this method.
     *
     * @param item - the MenuItem selected.
     * @return - the boolean for checking if an item is selected or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddWeeklyEventFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens up the dialog and display list of weekly events using recycle view.
     * @param view - the fragment's view
     */
    private void openEventDialog(View view, final WeeklyEvent weeklyEvent){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_weekly_event, null);
        dialogView.setBackgroundColor(Color.parseColor(weeklyEvent.getColor()));

        TextView eventNameTextView = dialogView.findViewById(R.id.tv_display_event_name);
        TextView noteTextView = dialogView.findViewById(R.id.tv_display_note);
        TextView timeTextView = dialogView.findViewById(R.id.tv_display_time);
        ImageView deleteImageView = dialogView.findViewById(R.id.iv_delete);
        ImageView editImageView = dialogView.findViewById(R.id.iv_edit);

        eventNameTextView.setText(weeklyEvent.getEventName());
        noteTextView.setText(weeklyEvent.getNote());
        timeTextView.setText(weeklyEvent.getStartTime() + " ~ " + weeklyEvent.getEndTime());

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteWeeklyEventListener != null) {
                    mDeleteWeeklyEventListener.deleteWeeklyEvent(weeklyEvent.getEventId());
                    mAlertDialog.dismiss();
                }
            }
        });
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EditWeeklyEventFragment(weeklyEvent))
                        .addToBackStack(null)
                        .commit();
            }
        });
        builder.setView(dialogView);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    /**
     * Sets up a page of weekly schedule.
     */
    private void updateWeeklySchedule() {
        WeeklyScheduleGridAdapter weeklyScheduleGridAdapter = new WeeklyScheduleGridAdapter(mView.getContext(), mWeeklyEventList);
        mGridView.setAdapter(weeklyScheduleGridAdapter);
    }

    /**
     * Sets up weekly event list.
     *
     * @param weeklyEventList - list of weekly events to be set
     */
    public void setWeeklyEventList(List<WeeklyEvent> weeklyEventList) {
        mWeeklyEventList = weeklyEventList;
        updateWeeklySchedule();
    }

    /**
     * Interface for getting weekly events.
     */
    public interface GetWeeklyEventsListener {
        /**
         * Retrieve weekly events from the web service.
         */
        void getWeeklyEvents();
    }

    /**
     * Interface for a deleting weekly event.
     */
    public interface DeleteWeeklyEventListener {
        /**
         * Deleting a weekly event through
         * posting the event id to the web service.
         *
         * @param eventId - weekly event's id
         */
        void deleteWeeklyEvent(String eventId);
    }
}