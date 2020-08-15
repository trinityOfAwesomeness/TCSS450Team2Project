package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Add Task page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class AddTaskFragment extends Fragment {
    private AddTaskListener mAddTaskListener;
    private String mStatus;
    private EditText mTaskEditText;

    /**
     * Called to do initial creation of AddTaskFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatus = "false";
        mAddTaskListener = (AddTaskListener) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_add_task.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        getActivity().setTitle("Add Task");
        mTaskEditText = view.findViewById(R.id.et_task);

        Switch taskSwitch = view.findViewById(R.id.switch_task);
        Button saveTaskButton = view.findViewById(R.id.btn_save_task);

        taskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mStatus = "true";
                } else {
                    mStatus = "false";
                }
            }
        });

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskString = mTaskEditText.getText().toString();
                Task task = new Task(null, taskString, mStatus);
                if (mAddTaskListener != null) {
                    mAddTaskListener.addTask(task);
                }
            }
        });
        return view;
    }

    /**
     * Interface for adding a task.
     */
    public interface AddTaskListener {
        /**
         * Add a task through posting the task to the web service.
         *
         * @param task - task to be added
         */
        void addTask(Task task);
    }
}