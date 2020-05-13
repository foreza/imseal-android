package com.foreza.imseal;

import android.util.Log;

import com.foreza.imseal.models.AdEventModel;
import com.foreza.imseal.models.LocationModel;
import com.foreza.imseal.models.SessionModel;
import com.foreza.imseal.services.EventAPIService;
import com.foreza.imseal.services.LocationAPIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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



public class IMSEAL {

    /* Public attributes */


    /* Constants */
    private static final String logTag = "[IMSEAL]";
    private static final int DEFAULT_CURRENT_EVENT_ID = -1;
    private static final String ERROR_REASON_AD_REQUEST_NOT_MADE =  "Did you try beginning an ad request with recordAdRequest()?";

    /* Private attributes */
    public boolean _isInitialized;
    private int _sessionId;
    private int _currentEventId;
    private JSONObject _localParams;
    private IMSEALInterface _listener;

    private Retrofit _retrofit;
    private EventAPIService _service;


    public IMSEAL () {}

    /* Public methods */

    public void initialize(IMSEALInterface listener, String uuid){
        _isInitialized = false;
        _listener = listener;
        _service = util_configureEventRetrofitService(EndpointConfigs._seal_api_url);
        _currentEventId = DEFAULT_CURRENT_EVENT_ID;
        _localParams = util_initializeLocalParamsBaseObject(uuid);

        // Trigger call to location API, then to SEAL API
        // Update the localParams object if successful
        util_fetchLocationParamsFromRemote();
    }


    public boolean isInitialized(){
        return _isInitialized;
    }


    public void recordAdRequest() {
        Log.d(logTag, "recordAdRequest");

        JSONObject event = new JSONObject();

        try {
            event.put("session_id", _sessionId);
            event.put("timestamp", new Date());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(event.toString()));

            Call newAdRequestCall = _service.logEventForAdRequest(body);

            newAdRequestCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    Log.d(logTag, "Server responded with: " + response.code());

                    try {
                        _currentEventId = Integer.parseInt(((AdEventModel.IMSEALAdResponseEvent)response.body()).event_id);

                        // Bubble success up to handler. We are now allowed to send events
                        if (_listener != null){
                            _listener.startEventLogSuccess();
                        }

                        Log.d(logTag, "Server responded with new event ID: " + _currentEventId);

                    } catch (NumberFormatException e){
                        // Handle
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    Log.e(logTag, "Failed with: " + t.getLocalizedMessage());
                    _currentEventId = DEFAULT_CURRENT_EVENT_ID;

                    // Bubble success up to handler. We are now allowed to send events
                    if (_listener != null){
                        _listener.startEventLogFail();
                    }

                }
            });


        } catch (JSONException e){
            // TODO: Handle
        }

    }


    public void recordAdLoaded() {

        if (!util_checkForExistingEventID()) {
            if (_listener != null){
                _listener.eventLogFailure();
            }
            return;
        }

        JSONObject event = new JSONObject();

        try {
            event.put("type", 1);
            event.put("event_id", _currentEventId);
            event.put("timestamp", new Date());

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(event.toString()));
            Call<String> eventCall = _service.logEventForAdLoad(_currentEventId, body);

            util_sendAdEventCall(eventCall);
            Log.d(logTag, "recordAdLoaded");

        } catch (JSONException e){
            // TODO: Handle
        }


    }


    public void recordAdNoFill(String reason_string) {

        if (!util_checkForExistingEventID()) {
            if (_listener != null){
                _listener.eventLogFailure();
            }
            return;
        }


        JSONObject event = new JSONObject();

        try {
            event.put("type", 2);
            event.put("event_id", _currentEventId);
            event.put("timestamp", new Date());
            event.put("reason_string", reason_string);

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(event.toString()));
            Call<String> eventCall = _service.logEventForAdNoFill(_currentEventId, body);
            util_sendAdEventCall(eventCall);
            Log.d(logTag, "recordAdNoFill");

        } catch (JSONException e){
            // TODO: Handle
        }


        // TODO
    }




    /* Private class methods */


    private boolean util_checkForExistingEventID(){
        if (_currentEventId == -1 || _currentEventId == 0) {
            Log.e(logTag, ERROR_REASON_AD_REQUEST_NOT_MADE);
            return false;
        }
        return true;
    }


    private void util_sendAdEventCall(Call<String> call){


        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(logTag, "util_sendAdEventCall - Server responded with: " + response.code());

                // Bubble success up to handler. We are now allowed to send events
                if (_listener != null){
                    _listener.eventLogSuccess();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Log.e(logTag, "util_sendAdEventCall - Failed with: " + t.getLocalizedMessage());

                // Bubble success up to handler. We are now allowed to send events
                if (_listener != null){
                    _listener.eventLogFailure();
                }

            }
        });

    }


    private EventAPIService util_configureEventRetrofitService(String endpoint){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        _retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return _retrofit.create(EventAPIService.class);
    }


    private JSONObject util_initializeLocalParamsBaseObject(String deviceId){

        JSONObject params = new JSONObject();

        try {
            params.put("device_id", deviceId);
            params.put("isActive", true);                  // TODO: Use
            params.put("cellular", false);                 // TODO: Use
            params.put("os", "Android");                   // TODO: Use
            params.put("plc", "380000");                   // TODO: Use
            params.put("placement_id", "N/A");
            params.put("request_ip", "Unknown IP");
            params.put("continent", "Unknown continent");
            params.put("country", "Unknown Country");
            params.put("region", "Unknown Region");
            params.put("city", "Unknown City");
            params.put("lat", "0.0");
            params.put("long", "0.0");
        } catch (JSONException e){
            // Handle
        }

        return params;
    }


    private void util_retrieveSessionIDForParams(JSONObject sessionInfo){

        if (_service == null){
            _listener.initFail("Service was not initialized!");
            return;
        }


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(sessionInfo.toString()));

        Call call = _service.initializeWithInfoForSessionID(body);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(logTag, "Server responded with: " + response.code());

                if (response.code() != 200){
                    if (_listener != null){
                        _listener.initFail("Response code was not 200 - check!");
                    }
                    return;
                }

                try {
                    _sessionId = Integer.parseInt(((SessionModel.SessionResponse)response.body()).id);

                    // Bubble success up to handler. We are now allowed to send events
                    if (_listener != null){
                        _listener.initSuccess(_sessionId);
                    }

                    _isInitialized = true;
                    Log.d(logTag, "Server responded with session ID: " + _sessionId);


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

                _isInitialized = false;

            }
        });



    }


    // Helper function that will call the location API (referenced further below). Will be called by the initialize method in the background.
    private void util_fetchLocationParamsFromRemote() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndpointConfigs._location_api_url)
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
                        if (_localParams == null) {
                            throw new Error("Local Params was null");   // TODO: Handle this workflow
                        }

                        LocationModel l = (LocationModel)response.body();

                        _localParams.put("request_ip", l.ip);
                        _localParams.put("continent",  l.continent);
                        _localParams.put("country",  l.country);
                        _localParams.put("region", l.region);
                        _localParams.put("city",  l.city);
                        _localParams.put("lat",  l.latitude);
                        _localParams.put("long",  l.longitude);

                        util_retrieveSessionIDForParams(_localParams);

                        helper_logLocalParams();


                    } catch (JSONException e){
                        // Handle error
                    }


                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(logTag, "onFailure with: " + t.getLocalizedMessage());

                if (_localParams == null) {
                    throw new Error("Local Params was null");   // TODO: Handle this workflow
                }

                util_retrieveSessionIDForParams(_localParams);
                helper_logLocalParams();
            }
        });

    };


    private void helper_logLocalParams() {
        Log.d(logTag, _localParams.toString());
    }
}
