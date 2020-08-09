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

public class AddTaskFragment extends Fragment {
    private AddTaskListener mAddTaskListener;
    private String mStatus;
    private EditText mTaskEditText;
    private Switch mTaskSwitch;
    private Button mSaveTaskButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatus = "false";
        mAddTaskListener = (AddTaskListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        getActivity().setTitle("Add Task");
        mTaskEditText = view.findViewById(R.id.et_task);
        mTaskSwitch = view.findViewById(R.id.switch_task);
        mSaveTaskButton = view.findViewById(R.id.btn_save_task);

        mTaskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mStatus = "true";
//                    Toast.makeText(getActivity(),
//                            "Switch is on", Toast.LENGTH_LONG).show();
                } else {
                    mStatus = "false";
//                    Toast.makeText(getActivity(),
//                            "Switch is off", Toast.LENGTH_LONG).show();
                }
            }
        });

        mSaveTaskButton.setOnClickListener(new View.OnClickListener() {
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


    public interface AddTaskListener {
        void addTask(Task task);
    }
}