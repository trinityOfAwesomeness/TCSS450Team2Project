package edu.tacoma.uw.tslinard.tcss450team2project.main.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Monthly Event class for calendar.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class MonthlyEvent {

    // Properties of Json object. Used to GET/POST events.
    public static final String EVENT_ID = "eventid";
    public static final String START_DATE = "startdate";
    public static final String START_TIME = "starttime";
    public static final String END_DATE = "enddate";
    public static final String END_TIME = "endtime";
    public static final String EVENT_NAME = "eventname";
    public static final String NOTE = "note";
    public static final String EMAIL = "email";

    private String mEventId;
    private String mStartDate;
    private String mStartTime;
    private String mEndDate;
    private String mEndTime;
    private String mEventName;
    private String mNote;

    public MonthlyEvent(String eventId, String startDate, String startTime, String endDate, String endTime, String eventName, String note){
        mEventId = eventId;
        mStartDate = startDate;
        mStartTime = startTime;
        mEndDate = endDate;
        mEndTime = endTime;
        mEventName = eventName;
        mNote = note;
    }

    public String getEventId() {
        return mEventId;
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


    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public void setEventName(String eventName) {
        mEventName = eventName;
    }

    public void setNote(String note) {
        mNote = note;
    }

    /**
     * Parse Json format and retrieve list of monthly events.
     *
     * @param monthlyEventJson - string in Json format
     * @return - list of monthly events retrieved from Json format
     * @throws JSONException - if input string is not in Json format
     */
    public static List<MonthlyEvent> parseMonthlyEventsJson(String monthlyEventJson) throws JSONException {
        List<MonthlyEvent> monthlyEventList = new ArrayList<>();
        if(monthlyEventJson != null){
            JSONArray arr = new JSONArray(monthlyEventJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                MonthlyEvent monthlyEvent = new MonthlyEvent(obj.getString(MonthlyEvent.EVENT_ID),
                        obj.getString(MonthlyEvent.START_DATE), obj.getString(MonthlyEvent.START_TIME),
                        obj.getString(MonthlyEvent.END_DATE), obj.getString(MonthlyEvent.END_TIME),
                        obj.getString(MonthlyEvent.EVENT_NAME), obj.getString(MonthlyEvent.NOTE));
                monthlyEventList.add(monthlyEvent);
            }
        }
        return monthlyEventList;
    }

    /**
     * Covert string representation of a date into Date object.
     *
     * @param eventDate - string representation of a date
     * @return - Date object
     */
    public static Date convertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
