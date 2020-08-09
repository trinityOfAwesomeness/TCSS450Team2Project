package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Task {

    // Properties of Json object. Used to GET/POST tasks.
    public static final String TO_DO_ID = "todoid";
    public static final String TASK = "task";
    public static final String STATUS = "taskstatus";
    public static final String EMAIL = "email";

    private String mToDoId;
    private String mTask;
    private String mStatus;

    public Task(String todoid, String task, String status) {
        mToDoId = todoid;
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

    public static List<Task> parseTasksJson(String eventsJson) throws JSONException {
        List<Task> taskList = new ArrayList<>();
        if(eventsJson != null){
            JSONArray arr = new JSONArray(eventsJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Task task = new Task(obj.getString(Task.TO_DO_ID),
                        obj.getString(Task.TASK), obj.getString("status"));
                taskList.add(task);
            }
        }
        return taskList;
    }

    public String toString(){
        return mToDoId + " " + mTask +" " +mStatus;
    }
}
