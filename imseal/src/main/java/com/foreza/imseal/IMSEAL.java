package com.foreza.imseal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IMSEAL {

    private static final String logTag = "[IMSEAL]";

    String sessionId = null;
    boolean isInitialized = false;
    JSONObject localParams;
    IMSEALInterface listener;                                           // TODO: Plug in

    private String _currentEventId;
    private EndpointConfigs configs;


    public IMSEAL () {
    }

    public void initialize(String uuid, String plc){
        initializeLocalParamsBaseObject(uuid);
        updateLocalParamsWithPlacementID(plc);
        updateLocalParamsFromRemote();

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

    public void updateLocalParamsWithPlacementID(String plc){
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



    private void logLocalParams() {
        Log.d(logTag, localParams.toString());
    }


    private void initializeLocalParamsBaseObject(String deviceId){
        localParams = new JSONObject();

        try {
            localParams.put("device_id", deviceId);
            localParams.put("isActive", true);                  // TODO: Use
            localParams.put("cellular", false);                 // TODO: Use
            localParams.put("os", "Android");                   // TODO: Use
            localParams.put("date_created", new Date());
        } catch (JSONException e){
            // Handle
        }

    }


    // Helper function that will call the location API (referenced further below). Will be called by the initialize method in the background.
    private void updateLocalParamsFromRemote() {

        configs = new EndpointConfigs();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configs.getLocationAPIEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call call = apiService.getAPIService();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("API", "onResponse");
                if (response.body() != null) {
                    Log.d(logTag, "Response acquired: " + response.body());

                    String request_ip = ((LocationModel)response.body()).ip;
                    Log.d(logTag, "Response ip: " + request_ip);

                    try {
                        if (localParams == null) {
                            throw new Error("Local Params was null");   // TODO: Handle this workflow
                        }

                        localParams.put("request_ip", ((LocationModel)response.body()).ip);
                        localParams.put("continent",  ((LocationModel)response.body()).continent);
                        localParams.put("country",  ((LocationModel)response.body()).country);
                        localParams.put("city",  ((LocationModel)response.body()).city);
                        localParams.put("lat",  ((LocationModel)response.body()).latitude);
                        localParams.put("long",  ((LocationModel)response.body()).longitude);


                        logLocalParams();


                    } catch (JSONException e){
                        // Handle error
                    }


                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(logTag, "onFailure with: " + t.getLocalizedMessage());

                try {
                    if (localParams == null) {
                        throw new Error("Local Params was null");   // TODO: Handle this workflow
                    }
                    localParams.put("request_ip", "12.34.56.78");
                    localParams.put("continent", "N/A");
                    localParams.put("country", "N/A");
                    localParams.put("city", "N/A");
                    localParams.put("lat", "N/A");
                    localParams.put("long", "N/A");
                } catch (JSONException e){
                    // Handle error
                }
            }
        });

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
