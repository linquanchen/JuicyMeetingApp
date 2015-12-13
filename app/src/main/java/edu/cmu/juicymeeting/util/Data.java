package edu.cmu.juicymeeting.util;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.juicymeeting.database.model.Event;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class Data {
    public static String userEmail = "zxq@cmu.edu";
    public static String userName = "zhexin";
    //public static String upComingEvents = null;
    public static Event[] upcomingEvents = null;
    //public static String exploreEvents = null;
    public static Event[] exploreEvents = null;
    public static Map<Integer, Boolean> isJoinMap = new HashMap<Integer, Boolean>();
    public static double lat = 37.4;
    public static double log = -122.3;
    public static long distance = 50000;

    public static final String UPCOMING_EVENTS = "upcomingEvents";
    public static final String EXPLORE_EVENTS = "exploreEvents";

    public static boolean signUpStatus = true;
    public static boolean loginStatus = true;

}
