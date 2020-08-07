package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.main.Events;

/**
 * Class to set and display list of events into recycler view.
 * It is an adapter class which connects the data and the view.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Events> mEventList;

    public EventRecyclerAdapter(Context context, List<Events> eventsList) {
        this.mContext = context;
        this.mEventList = eventsList;
    }

    /**
     * Creates view holder for an event in recycler view.
     *
     * @param parent - The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return - ViewHolder used to display events of the adapter
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dialog_events_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    /**
     * Called by RecyclerView to display an event at the specified position.
     *
     * @param holder - The ViewHolder which should be updated to represent
     *               the events at the given position in the data set.
     * @param position - The position of the event within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Events events = mEventList.get(position);
        holder.mEventNameTextView.setText(events.getEventName());
        holder.mNoteTextView.setText(events.getNote());
        holder.mStartDateTextView.setText("Start Date: " + events.getStartDate() + " "+ events.getStartTime());
        holder.mEndDateTextView.setText("End Date: " + events.getEndDate() + " "+ events.getEndTime());
        holder.itemView.setTag(position);
        // Placeholder for action to be done when an item in RecyclerView is selected
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventDate = holder.mEventNameTextView.getText().toString();
                Toast.makeText(view.getContext(), eventDate, Toast.LENGTH_SHORT).show();
            }
        });
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

    /**
     * Class to describes an item view and metadata about its place within the RecyclerView.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView mEventNameTextView, mStartDateTextView, mEndDateTextView, mNoteTextView;
        protected Button mDeleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mEventNameTextView = itemView.findViewById(R.id.tv_display_event_name);
            mStartDateTextView = itemView.findViewById(R.id.tv_display_event_start_date);
            mEndDateTextView = itemView.findViewById(R.id.tv_display_event_end_date);
            mNoteTextView = itemView.findViewById(R.id.tv_display_note);
            mDeleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}