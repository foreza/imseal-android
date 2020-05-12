package com.foreza.imseal;

import android.util.Log;

import com.foreza.imseal.models.AdEventModel;
import com.foreza.imseal.models.LocationModel;
import com.foreza.imseal.models.SessionModel;
import com.foreza.imseal.services.EventAPIService;
import com.foreza.imseal.services.LocationAPIService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;


public class IMSEAL {

    private static final String logTag = "[IMSEAL]";

    int sessionId;
    boolean isInitialized = false;
    JSONObject localParams;

    private String _currentEventId;
    private Retrofit _retrofit;
    private EventAPIService _service;
    private EndpointConfigs configs;
    private IMSEALInterface _listener;


    public IMSEAL () {
    }

    public void initialize(IMSEALInterface listener, String uuid, String plc){

        _listener = listener;


        configs = new EndpointConfigs();
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


    public void util_sendAdEventCall(Call call){

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                Log.d(logTag, "Server responded with: " + response.code());

                // Bubble success up to handler. We are now allowed to send events
                if (_listener != null){
                    _listener.eventLogSuccess();
                }



            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Log.e(logTag, "Failed with: " + t.getLocalizedMessage());

                // Bubble success up to handler. We are now allowed to send events
                if (_listener != null){
                    _listener.eventLogFailure();
                }

            }
        });

    }

    public void recordAdRequest() {
        Log.d(logTag, "recordAdRequest");
        JSONObject event = new JSONObject();

        //        public IMSEALAdRequestEvent(int session_id) {
//            timestamp = new Date();
//            this.session_id = session_id;
        try {
            event.put("session_id", sessionId);
            event.put("timestamp", new Date());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(event.toString()));
            util_sendAdEventCall(_service.logEventForAdRequest(body));

        } catch (JSONException e){

        }

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
            localParams.put("placement_id", "N/A");
            localParams.put("request_ip", "Unknown IP");
            localParams.put("continent", "Unknown continent");
            localParams.put("country", "Unknown Country");
            localParams.put("region", "Unknown Region");
            localParams.put("city", "Unknown City");
            localParams.put("lat", "0.0");
            localParams.put("long", "0.0");
        } catch (JSONException e){
            // Handle
        }

    }


    private void retrieveSessionIDForParams(JSONObject sessionInfo){

        _retrofit = new Retrofit.Builder()
                .baseUrl(configs.getSEALAPIEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        _service = _retrofit.create(EventAPIService.class);
        
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(sessionInfo.toString()));

        Call call = _service.initializeWithInfoForSessionID(body);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(logTag, "Server responded with: " + response.code());


                try {
                    sessionId = Integer.parseInt(((SessionModel.SessionResponse)response.body()).id);

                    // Bubble success up to handler. We are now allowed to send events
                    if (_listener != null){
                        _listener.initSuccess(sessionId);
                    }

                    Log.d(logTag, "Server responded with session ID: " + sessionId);

                } catch (NumberFormatException e){
                    // Handle
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(logTag, "Failed with: " + t.getLocalizedMessage());

                // Bubble error up to the handler
                if (_listener != null){
                    _listener.initFail(t.getLocalizedMessage());
                }
            }
        });



    }

    // Helper function that will call the location API (referenced further below). Will be called by the initialize method in the background.
    private void updateLocalParamsFromRemote() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configs.getLocationAPIEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        LocationAPIService locationApiService = retrofit.create(LocationAPIService.class);
        Call call = locationApiService.getAPIService();

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
                        localParams.put("region", ((LocationModel)response.body()).region);
                        localParams.put("city",  ((LocationModel)response.body()).city);
                        localParams.put("lat",  ((LocationModel)response.body()).latitude);
                        localParams.put("long",  ((LocationModel)response.body()).longitude);

                        retrieveSessionIDForParams(localParams);
                        logLocalParams();


                    } catch (JSONException e){
                        // Handle error
                    }


                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(logTag, "onFailure with: " + t.getLocalizedMessage());

                if (localParams == null) {
                    throw new Error("Local Params was null");   // TODO: Handle this workflow
                }

                retrieveSessionIDForParams(localParams);
                logLocalParams();
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
