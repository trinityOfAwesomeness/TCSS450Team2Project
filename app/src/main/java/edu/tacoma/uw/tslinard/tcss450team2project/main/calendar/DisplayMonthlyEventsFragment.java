package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Display Monthly Events page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class DisplayMonthlyEventsFragment extends Fragment {

    private List<MonthlyEvent> mMonthlyEventList;

    public DisplayMonthlyEventsFragment(List<MonthlyEvent> monthlyEventList) {
        mMonthlyEventList = monthlyEventList;
    }

    /**
     * Called to do initial creation of DisplayMonthlyEventsFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_display_monthly_events.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_monthly_events, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.events_recycle_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        MonthlyEventRecyclerAdapter adapter = new MonthlyEventRecyclerAdapter(this, mMonthlyEventList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // sort monthly events by creation order
        Collections.sort(mMonthlyEventList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                MonthlyEvent event1 = (MonthlyEvent) o1;
                MonthlyEvent event2 = (MonthlyEvent) o2;
                return event1.getEventId().compareTo(event2.getEventId());
            }
        });
        return view;
    }
}