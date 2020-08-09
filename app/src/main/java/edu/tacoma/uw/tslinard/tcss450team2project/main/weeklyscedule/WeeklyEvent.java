package edu.tacoma.uw.tslinard.tcss450team2project.main.weeklyscedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeeklyEvent {

    // Properties of Json object. Used to GET/POST events.
    public static final String EVENT_ID = "eventid";
    public static final String DAY_OF_WEEK = "dayofweek";
    public static final String START_TIME = "starttime";
    public static final String END_TIME = "endtime";
    public static final String EVENT_NAME = "eventname";
    public static final String COLOR = "color";
    public static final String NOTE = "note";
    public static final String EMAIL = "email";

    private String mEventId;
    private String mDayofWeek;
    private String mStartTime;
    private String mEndTime;
    private String mEventName;
    private String mColor;
    private String mNote;

    public WeeklyEvent(String eventId, String dayOfWeek, String startTime, String endTime,
                       String eventName, String color, String note) {
        mEventId = eventId;
        mDayofWeek = dayOfWeek;
        mStartTime = startTime;
        mEndTime = endTime;
        mEventName = eventName;
        mColor = color;
        mNote = note;
    }

    public String getEventId() {
        return mEventId;
    }

    public String getDayOfWeek() {
        return mDayofWeek;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getColor() {
        return mColor;
    }

    public String getNote() {
        return mNote;
    }

    /**
     * Parse Json format and retrieve list of events.
     *
     * @param weeklyEventsJson - string in Json format
     * @return - list of events retrieved from Json format
     * @throws JSONException - if input string is not in Json format
     */
    public static List<WeeklyEvent> parseWeeklyEventJson(String weeklyEventsJson) throws JSONException {
        List<WeeklyEvent> weeklyEventList = new ArrayList<>();
        if (weeklyEventsJson != null) {
            JSONArray arr = new JSONArray(weeklyEventsJson);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                WeeklyEvent weeklyEvent = new WeeklyEvent(obj.getString(WeeklyEvent.EVENT_ID),
                        obj.getString(WeeklyEvent.DAY_OF_WEEK), obj.getString(WeeklyEvent.START_TIME),
                        obj.getString(WeeklyEvent.END_TIME), obj.getString(WeeklyEvent.EVENT_NAME),
                        obj.getString(WeeklyEvent.COLOR), obj.getString(WeeklyEvent.NOTE));
                weeklyEventList.add(weeklyEvent);
            }
        }
        return weeklyEventList;
    }

    public String toString() {
        return mEventId + " " + mDayofWeek + " " + mStartTime + " " + mEventName + " " + mColor;
    }
}
