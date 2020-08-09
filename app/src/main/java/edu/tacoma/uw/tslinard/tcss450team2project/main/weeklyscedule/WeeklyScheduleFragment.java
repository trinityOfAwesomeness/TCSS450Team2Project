package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Weekly Schedule page.
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class WeeklyScheduleFragment extends Fragment {
    private AlertDialog mAlertDialog;
    private View mView;
    private GridView mGridView;
    private List<WeeklyEvent> mWeeklyEventList;
    private WeeklyScheduleGridAdapter mWeeklyScheduleGridAdapter;

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
                Toast.makeText(getActivity(), "Position: " + position
                        , Toast.LENGTH_SHORT).show();
            }
        });

        return mView;
    }
//
//    /**
//     * Opens up the dialog and display list of events using recycle view.
//     * @param view - the fragment's view
//     * @param selectedDate - the selected date to show events
//     */
//    private void openEventDialog(View view, Date selectedDate){
//        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_events, null);
//
//        // set recycler view
//        RecyclerView recyclerView = dialogView.findViewById(R.id.events_recycle_view);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dialogView.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        String stringDate = mDateFormat.format(selectedDate);
//        builder.setView(dialogView)
//                .setTitle("Events on " + stringDate)
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//        mAlertDialog = builder.create();
//        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(this,
//                mAlertDialog, collectEventsByDate(selectedDate));
//        eventRecyclerAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(eventRecyclerAdapter);
//        mAlertDialog.show();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


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

    private void updateWeeklySchedule() {
        Toast.makeText(getActivity(), "Weekly Events: " + mWeeklyEventList.toString()
                , Toast.LENGTH_SHORT).show();
        mWeeklyScheduleGridAdapter = new WeeklyScheduleGridAdapter(mView.getContext(), mWeeklyEventList);
        mGridView.setAdapter(mWeeklyScheduleGridAdapter);
    }

    public void setWeeklyEventList(List<WeeklyEvent> weeklyEventList) {
        mWeeklyEventList = weeklyEventList;
        updateWeeklySchedule();
    }

    public interface GetWeeklyEventsListener {
        void getWeeklyEvents();
    }
}