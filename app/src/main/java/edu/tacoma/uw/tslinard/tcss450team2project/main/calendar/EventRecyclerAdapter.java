package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.app.AlertDialog;
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
 * Class to set and display list of events into recycler view.
 * It is an adapter class which connects the data and the view.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder> {

    private DeleteMonthlyEventListener mDeleteMonthlyEventListener;
    private Fragment mFragment;
    private AlertDialog mAlertDialog;
    private List<Events> mEventList;

    public EventRecyclerAdapter(Fragment fragment, AlertDialog alertDialog, List<Events> eventsList) {
        mFragment = fragment;
        mAlertDialog = alertDialog;
        mEventList = eventsList;
        mDeleteMonthlyEventListener = (DeleteMonthlyEventListener) mFragment.getActivity();
    }

    /**
     * Creates view holder for an event in recycler view.
     *
     * @param parent   - The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return - ViewHolder used to display events of the adapter
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_events_row, parent, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    /**
     * Called by RecyclerView to display an event at the specified position.
     *
     * @param holder   - The ViewHolder which should be updated to represent
     *                 the events at the given position in the data set.
     * @param position - The position of the event within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        final Events currentEvent = mEventList.get(position);
        holder.mEventNameTextView.setText(currentEvent.getEventName());
        holder.mNoteTextView.setText(currentEvent.getNote());
        holder.mStartDateTextView.setText("Start Date: " + currentEvent.getStartDate() + " " + currentEvent.getStartTime());
        holder.mEndDateTextView.setText("End Date: " + currentEvent.getEndDate() + " " + currentEvent.getEndTime());
        holder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteMonthlyEventListener != null) {
                    mDeleteMonthlyEventListener.deleteMonthlyEvent(currentEvent.getEventId());
                }
            }
        });
        holder.mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                mFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EditEventFragment(currentEvent))
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.itemView.setTag(position);

        // Placeholder for action to be done when an item in RecyclerView is selected
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String eventDate = holder.mEventNameTextView.getText().toString();
//                Toast.makeText(view.getContext(), currentEvent.getEventId() + "", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * Returns the total number of events in the data set held by the adapter.
     *
     * @return - number of events
     */
    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public interface DeleteMonthlyEventListener {
        void deleteMonthlyEvent(String eventId);
    }

    /**
     * Class to describes an item view and metadata about its place within the RecyclerView.
     */
    public class EventViewHolder extends RecyclerView.ViewHolder {

        protected TextView mEventNameTextView, mStartDateTextView, mEndDateTextView, mNoteTextView;
        protected ImageView mDeleteImageView, mEditImageView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteImageView = itemView.findViewById(R.id.iv_delete);
            mEditImageView = itemView.findViewById(R.id.iv_edit);
            mEventNameTextView = itemView.findViewById(R.id.tv_display_event_name);
            mStartDateTextView = itemView.findViewById(R.id.tv_display_event_start_date);
            mEndDateTextView = itemView.findViewById(R.id.tv_display_event_end_date);
            mNoteTextView = itemView.findViewById(R.id.tv_display_note);
        }
    }

}