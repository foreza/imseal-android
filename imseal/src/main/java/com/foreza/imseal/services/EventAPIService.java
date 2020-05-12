package com.foreza.imseal.services;
import com.foreza.imseal.models.AdEventModel;
import com.foreza.imseal.models.SessionModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EventAPIService {

    @Headers( "Content-Type: application/json; charset=utf-8")

    @POST("/sessions")
    Call<SessionModel.SessionResponse> initializeWithInfoForSessionID(@Body RequestBody session);

    @POST("/events")
    Call<AdEventModel.IMSEALAdResponseEvent> logEventForAdRequest(@Body RequestBody event);

    @POST("/events/{event_id}")
    Call<AdEventModel> logEventForAdLoad(@Body RequestBody event);

    @POST("/event/{event_id}")
    Call<AdEventModel> logEventForAdNoFill(@Body RequestBody event);
}
