package com.foreza.imseal.services;
import com.foreza.imseal.models.AdEventModel;
import com.foreza.imseal.models.SessionModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventAPIService {

    @POST("/sessions")
    Call<SessionModel.SessionResponse> initializeWithInfoForSessionID(@Body RequestBody session);

    @POST("/events")
    Call<AdEventModel.IMSEALAdResponseEvent> logEventForAdRequest(@Body RequestBody event);

    @POST("/events/{event_id}")
    Call<String> logEventForAdLoad(@Path("event_id") int event_id, @Body RequestBody event);

    @POST("/events/{event_id}")
    Call<String> logEventForAdNoFill(@Path("event_id") int event_id, @Body RequestBody event);
}
