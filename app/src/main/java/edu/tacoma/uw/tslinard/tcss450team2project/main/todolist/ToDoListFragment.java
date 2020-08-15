package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the To Do List page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class ToDoListFragment extends Fragment {

    private List<Task> mTaskList;
    private RecyclerView mRecyclerView;

    /**
     * Called to do initial creation of ToDoListFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    /**
     * Create the options menu when the user opens the menu for the first time.
     *
     * @param menu     - menu container
     * @param inflater - The MenuInflater object that can be used to inflate views in the menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.to_do_list_menu, menu);
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
        // Launches AddTaskFragment if create_event_item is selected
        if (id == R.id.add_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddTaskFragment())
                    .addToBackStack(null)
                    .commit();
        } else if(id == R.id.share_item) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareSubject = "TO DO LIST";
            StringBuilder shareBody = new StringBuilder();
            int i = 1;
            for(Task task: mTaskList) {
                shareBody.append("Task " + i + ": " + task.getTask() + " & ");
                if(task.getStatus().equals("false")){
                    shareBody.append("Status: STARTED");
                } else {
                    shareBody.append("Status: COMPLETED");
                }
                shareBody.append("\n");
                i++;
            }
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
            startActivity(Intent.createChooser(myIntent, "Share using"));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_to_do_list.xml file.
     *
     * @param inflater - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        getActivity().setTitle("To Do List");
        mRecyclerView = view.findViewById(R.id.rv_to_do_list);
        updateToDoList();
        return view;
    }

    /**
     * Sets up to do list page with the tasks.
     */
    private void updateToDoList() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        TaskRecyclerAdapter adapter = new TaskRecyclerAdapter(this, mTaskList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        // sort tasks by creation order
        Collections.sort(mTaskList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Task task1 = (Task) o1;
                Task task2 = (Task) o2;
                return task1.getToDoId().compareTo(task2.getToDoId());
            }
        });
    }

    /**
     * Sets up task list.
     *
     * @param taskList - list of tasks to be set
     */
    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
        updateToDoList();
    }

    /**
     * Interface for getting tasks.
     */
    public interface GetTasksListener {
        /**
         * Retrieve tasks from the web service.
         */
        void getTasks();
    }
}