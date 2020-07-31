package edu.tacoma.uw.tslinard.tcss450team2project.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// calendar event class
public class Events {

    private String mStartDate;
    private String mStartTime;
    private String mEndDate;
    private String mEndTime;
    private String mEventName;
    private String mNote;

    public static final String START_DATE = "startdate";
    public static final String START_TIME = "starttime";
    public static final String END_DATE = "enddate";
    public static final String END_TIME = "endtime";
    public static final String EVENT_NAME = "eventname";
    public static final String NOTE = "note";
    public static final String EMAIL = "email";

    public Events(String startDate, String startTime, String endDate, String endTime, String eventName, String note){
        mStartDate = startDate;
        mStartTime = startTime;
        mEndDate = endDate;
        mEndTime = endTime;
        mEventName = eventName;
        mNote = note;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getNote() {
        return mNote;
    }

    public static List<Events> parseEventsJson(String eventsJson) throws JSONException {
        List<Events> eventsList = new ArrayList<>();
        if(eventsJson != null){

            JSONArray arr = new JSONArray(eventsJson);

            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Events event = new Events(obj.getString(Events.START_DATE), obj.getString(Events.START_TIME),
                        obj.getString(Events.END_DATE), obj.getString(Events.END_TIME),
                        obj.getString(Events.EVENT_NAME), obj.getString(Events.NOTE));
                eventsList.add(event);
            }
        }
        return eventsList;
    }

    public String toString(){
        return mStartDate;
    }
}
