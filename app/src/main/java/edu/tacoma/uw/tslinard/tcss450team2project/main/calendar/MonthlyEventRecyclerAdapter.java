package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to set and display list of monthly events into recycler view.
 * It is an adapter class which connects the data with the view.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class MonthlyEventRecyclerAdapter extends RecyclerView.Adapter<MonthlyEventRecyclerAdapter.EventViewHolder> {

    private DeleteMonthlyEventListener mDeleteMonthlyEventListener;
    private Fragment mFragment;
    private List<MonthlyEvent> mMonthlyEventList;

    public MonthlyEventRecyclerAdapter(Fragment fragment, List<MonthlyEvent> monthlyEventList) {
        mFragment = fragment;
        mMonthlyEventList = monthlyEventList;
        mDeleteMonthlyEventListener = (DeleteMonthlyEventListener) mFragment.getActivity();
    }

    /**
     * Creates view holder for an event in recycler view.
     *
     * @param parent   - The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return - ViewHolder used to display monthly events of the adapter
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    /**
     * Called by RecyclerView to display a monthly event at the specified position.
     *
     * @param holder   - The ViewHolder which should be updated to represent
     *                 the monthly event at the given position in the data set.
     * @param position - The position of the monthly event within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        final MonthlyEvent currentMonthlyEvent = mMonthlyEventList.get(position);
        holder.mEventNameTextView.setText(currentMonthlyEvent.getEventName());
        holder.mNoteTextView.setText(currentMonthlyEvent.getNote());
        holder.mDateTextView.setText(currentMonthlyEvent.getStartDate() + " " + currentMonthlyEvent.getStartTime() + " ~ " +
                currentMonthlyEvent.getEndDate() + " " + currentMonthlyEvent.getEndTime());
        holder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteMonthlyEventListener != null) {
                    mDeleteMonthlyEventListener.deleteMonthlyEvent(currentMonthlyEvent.getEventId());
                    mMonthlyEventList.remove(position);
                    mFragment.getActivity().getSupportFragmentManager().beginTransaction().remove(mFragment)
                            .replace(R.id.fragment_container, new DisplayMonthlyEventsFragment(mMonthlyEventList)).commit();
                }
            }
        });
        holder.mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EditMonthlyEventFragment(currentMonthlyEvent))
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.itemView.setTag(position);
    }

    /**
     * Returns the total number of monthly events in the data set held by the adapter.
     *
     * @return - number of monthly events
     */
    @Override
    public int getItemCount() {
        return mMonthlyEventList.size();
    }

    /**
     * Interface for deleting a monthly event.
     */
    public interface DeleteMonthlyEventListener {
        /**
         * Deleting a monthly event through
         * posting the event id to the web service.
         *
         * @param eventId - monthly event's id
         */
        void deleteMonthlyEvent(String eventId);
    }

    /**
     * Class to describe an item view and metadata about its place within the RecyclerView.
     */
    public class EventViewHolder extends RecyclerView.ViewHolder {

        protected TextView mEventNameTextView, mNoteTextView, mDateTextView;
        protected ImageView mDeleteImageView, mEditImageView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteImageView = itemView.findViewById(R.id.iv_delete);
            mEditImageView = itemView.findViewById(R.id.iv_edit);
            mEventNameTextView = itemView.findViewById(R.id.tv_display_event_name);
            mNoteTextView = itemView.findViewById(R.id.tv_display_note);
            mDateTextView = itemView.findViewById(R.id.tv_display_date);
        }
    }

}