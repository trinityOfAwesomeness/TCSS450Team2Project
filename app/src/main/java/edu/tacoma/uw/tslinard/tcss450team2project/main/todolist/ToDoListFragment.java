package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

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
import edu.tacoma.uw.tslinard.tcss450team2project.main.calendar.Events;

/**
 * Class to create and control the To Do List page.
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class ToDoListFragment extends Fragment {

    private List<Task> mTaskList;
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    /**
     * Called to do initial creation of CalendarFragment.
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
        // Launches AddEventFragment if create_event_item is selected
        if (id == R.id.add_item) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddTaskFragment())
                    .addToBackStack(null)
                    .commit();
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
        mView = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        getActivity().setTitle("To Do List");

        mRecyclerView = mView.findViewById(R.id.rv_to_do_list);

        updateToDoList();


//        mToDoListRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getContext(), "Clicked" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        return mView;
    }

    private void updateToDoList() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new TaskRecyclerAdapter(this, mTaskList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
//        Toast.makeText(getActivity(), ""+mTaskList.toString(), Toast.LENGTH_SHORT).show();
        updateToDoList();
    }

    public interface GetTasksListener {
        void getTasks();
    }
}