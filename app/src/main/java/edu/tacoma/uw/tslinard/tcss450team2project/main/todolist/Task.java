package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Task class for to-do-list.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class Task {

    // Properties of Json object. Used to GET/POST tasks.
    public static final String TO_DO_ID = "todoid";
    public static final String TASK = "task";
    public static final String STATUS = "taskstatus";
    public static final String EMAIL = "email";

    private String mToDoId;
    private String mTask;
    private String mStatus;

    public Task(String toDoId, String task, String status) {
        mToDoId = toDoId;
        mTask = task;
        mStatus = status;
    }

    public String getToDoId() {
        return mToDoId;
    }

    public String getTask() {
        return mTask;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setTask(String task) {
        mTask = task;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    /**
     * Parse Json format and retrieve list of tasks.
     *
     * @param taskJson - string in Json format
     * @return - list of tasks retrieved from Json format
     * @throws JSONException - if input string is not in Json format
     */
    public static List<Task> parseTasksJson(String taskJson) throws JSONException {
        List<Task> taskList = new ArrayList<>();
        if(taskJson != null){
            JSONArray arr = new JSONArray(taskJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Task task = new Task(obj.getString(Task.TO_DO_ID),
                        obj.getString(Task.TASK), obj.getString("status"));
                taskList.add(task);
            }
        }
        return taskList;
    }
}
