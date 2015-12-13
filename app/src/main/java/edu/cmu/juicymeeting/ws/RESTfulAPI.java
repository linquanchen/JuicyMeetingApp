package edu.cmu.juicymeeting.ws;

/**
 * Created by chenlinquan on 12/7/15.
 */
public class RESTfulAPI {
    public static String DNS = "http://ec2-52-91-106-6.compute-1.amazonaws.com/";

    public static String upcomingEventURL = DNS + "webapi/event/upcoming/";
    public static String exploreEventURL = DNS + "webapi/event/explore";
    public static String creatEventURL = DNS + "webapi/event/create";
    public static String joinEventURL = DNS + "webapi/event/join";
    public static String disjoinEventURL = DNS + "webapi/event/disjoin";

    public static String signUp = DNS + "webapi/user/register";
    public static String login = DNS + "webapi/user/login";
}
