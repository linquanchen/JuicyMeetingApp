package edu.cmu.juicymeeting.util;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class RESTfulAPI {
    private static String DNS = "http://ec2-52-91-106-6.compute-1.amazonaws.com/";

    public static String upcomingEventURL = DNS + "webapi/event/upcoming/";
    public static String creatEventURL = DNS + "webapi/event/create";
}
