package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Events> eventsList;


    public EventRecyclerAdapter(Context context, ArrayList<Events> eventsList) {
        this.context = context;
        this.eventsList = eventsList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_row_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Events events = eventsList.get(position);
        holder.eventName.setText(events.getEventName());
        holder.note.setText(events.getNote());
        holder.startDate.setText("Start Date: " + events.getStartDate() + " "+ events.getStartTime());
        holder.endDate.setText("End Date: " + events.getEndDate() + " "+ events.getEndTime());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventDate = holder.startDate.getText().toString();
                Toast.makeText(view.getContext(), eventDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView eventName, startDate, endDate, note;
        protected Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.display_eventname);
            startDate = itemView.findViewById(R.id.display_event_start_date);
            endDate = itemView.findViewById(R.id.display_event_end_date);
            note = itemView.findViewById(R.id.display_note);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}