package com.foreza.imseal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class IMSEAL {


    private static final String logTag = "IMSEAL";



    String sessionId = null;
    boolean isInitialized = false;

    JSONObject localParams;
    IMSEALInterface listener;                                           // TODO: Plug in


    private String _currentEventId;
    private String _currentPrimaryAPIRemote;

    private String _currentLocationAPIRemote;


    public IMSEAL () {
    }

    public void initialize(String uuid){

        // TODO: Make API call to remote to fetch IP.

        try {
            localParams = new JSONObject();
            localParams.put("device_id", "123456789");
            localParams.put("isActive", true);
            localParams.put("cellular", false);
            localParams.put("os", "Android");
            localParams.put("request_ip", "123.123.123.123");
            localParams.put("continent", "");
            localParams.put("country", "xxx");
            localParams.put("city", "xxx");
            localParams.put("lat", "xxx");
            localParams.put("long", "xxx");
            localParams.put("date_created", new Date());

        } catch (JSONException e){
            // Handle error
        }


    }

    public boolean isInitialized(){
        Log.i(logTag, "isInitialized");
        return isInitialized;
    }


    // GETTERs TODO: Add more getters



    public String getPlacementID() {

        try {
            return (localParams.get("placement_id")).toString();        // TODO: Handle this better
        } catch (JSONException e) {
            return "00000000";                                          // TODO: Handle this better
        }

    }



    // Setters (TODO: Add additional settings for the local params)

    public void setPlacementID(String plc){
        try {
            localParams.put("placement_id", plc);
        } catch (JSONException e) {
            // Handle error
        }
    }




    public void recordAdRequest() {
        // TODO
        Log.d(logTag, "recordAdRequest");
        // TODO
    }

    public void recordAdLoaded() {
        // TODO
        Log.d(logTag, "recordAdLoaded");
    }

    public void recordAdError(String errString) {
        // TODO
        Log.d(logTag, "recordAdError");
    }



    // Helper function that will call the location API (referenced further below). Will be called by the initialize method in the background.
    private void retrieveLocationForSession() {
        // TODO
    };

    // Helper function that will specifically ask for a new created event as JSON using a HTTP POST to the specified path. It should expect a session ID from the remote and save it. If this isn't provided, throw an error up to the listener.
    private boolean logAdRequestToRemote() {
        return false;
    }

    // Helper function that will log the error to the remote. Would be expected for the "no ad fill" use case. No event ID, no log.
    private boolean logAdErrorToRemote(Error e, String currentEventId) {
        return false;
    }

    // Helper function that will log a successful ad load to the remote. Would be expected if an ad was returned. No event id, no log.
    private boolean logAdLoadToRemote(String currentEventId) {
        return false;
    }
















}
