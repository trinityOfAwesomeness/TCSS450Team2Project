package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Edit Task page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class EditTaskFragment extends Fragment {

    private EditTaskListener mEditTaskListener;
    private Task mTask;
    private String mStatus;
    private EditText mTaskEditText;

    public EditTaskFragment(Task task) {
        mTask = task;
    }

    /**
     * Called to do initial creation of EditTaskFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditTaskListener = (EditTaskListener) getActivity();
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
        getActivity().setTitle("Edit Task");
        mTaskEditText = view.findViewById(R.id.et_task);

        Switch taskSwitch = view.findViewById(R.id.switch_task);
        Button saveTaskButton = view.findViewById(R.id.btn_save_task);

        mTaskEditText.setText(mTask.getTask());
        mStatus = mTask.getStatus();

        if (mTask.getStatus().equals("true")) {
            taskSwitch.setChecked(true);
        } else {
            taskSwitch.setChecked(false);
        }

        taskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                mTask.setTask(taskString);
                mTask.setStatus(mStatus);
                if (mEditTaskListener != null) {
                    mEditTaskListener.editTask(mTask);
                    // display a message when a task is completed
                    if (mStatus.equals("true")) {
                        Toast.makeText(getActivity(),
                                "Great job!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        return view;
    }

    /**
     * Interface for editing a task.
     */
    public interface EditTaskListener {
        /**
         * Editing a task through posting the task to the web service.
         *
         * @param task - task to be edited
         */
        void editTask(Task task);
    }
}