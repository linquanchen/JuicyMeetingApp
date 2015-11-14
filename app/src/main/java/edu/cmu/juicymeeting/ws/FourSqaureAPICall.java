package edu.cmu.juicymeeting.ws;

/**
 *
 * https://developer.foursquare.com/docs/explore#req=venues/explore%3Fll%3D40.7,-74
 * Created by qiuzhexin on 11/14/15.
 */
public interface FourSqaureAPICall {
    public String getPlacesNearby(double lat, double lon);
}
