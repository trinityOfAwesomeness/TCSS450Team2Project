package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import android.graphics.Color;
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
 * Class to set and display list of tasks into recycler view.
 * It is an adapter class which connects the data with the view.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private DeleteTaskListener mDeleteTaskListener;
    private Fragment mFragment;
    private List<Task> mTaskList;

    public TaskRecyclerAdapter(Fragment fragment, List<Task> taskList) {
        mFragment = fragment;
        mTaskList = taskList;
        mDeleteTaskListener = (DeleteTaskListener) mFragment.getActivity();
    }

    /**
     * Creates view holder for a task in recycler view.
     *
     * @param parent   - The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType - The view type of the new View.
     * @return - ViewHolder used to display tasks of the adapter
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viw = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list, parent, false);
        TaskViewHolder holder = new TaskViewHolder(viw);
        return holder;
    }

    /**
     * Called by RecyclerView to display a task at the specified position.
     *
     * @param holder   - The ViewHolder which should be updated to represent
     *                 the task at the given position in the data set.
     * @param position - The position of the task within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final Task currentTask = mTaskList.get(position);
        holder.mTaskTextView.setText(currentTask.getTask());
        if(currentTask.getStatus().equals("false")){
            holder.mStatusTextView.setText("STARTED");
        } else {
            holder.mStatusTextView.setTextColor(Color.parseColor("#34c759"));
            holder.mStatusTextView.setText("COMPLETED");
        }
        holder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteTaskListener != null) {
                    mDeleteTaskListener.deleteTask(currentTask.getToDoId());
                }
            }
        });
        holder.mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EditTaskFragment(currentTask))
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.itemView.setTag(position);

    }

    /**
     * Returns the total number of tasks in the data set held by the adapter.
     *
     * @return - number of tasks
     */
    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    /**
     * Interface for deleting a task.
     */
    public interface DeleteTaskListener {
        /**
         * Deleting a task through posting the task id to the web service.
         *
         * @param toDoId - task's id
         */
        void deleteTask(String toDoId);
    }

    /**
     * Class to describe an item view and metadata about its place within the RecyclerView.
     */
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTaskTextView, mStatusTextView;
        protected ImageView mDeleteImageView, mEditImageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteImageView = itemView.findViewById(R.id.iv_delete);
            mEditImageView = itemView.findViewById(R.id.iv_edit);
            mTaskTextView = itemView.findViewById(R.id.tv_task);
            mStatusTextView = itemView.findViewById(R.id.tv_status);
        }
    }
}
