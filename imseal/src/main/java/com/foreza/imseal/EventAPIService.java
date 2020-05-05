package com.foreza.imseal;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EventAPIService {

    @Headers( "Content-Type: application/json; charset=utf-8")

    @POST("/sessions")
    Call<SessionModel.SessionResponse> initializeWithInfoForSessionID(@Body RequestBody session);

    @POST("/event")
    Call<EventModel.AdRequestEventModel> logEventForAdRequest(@Body EventModel.AdLoadEventModel event);

    @POST("/event/error")
    Call<EventModel.AdErrorEventModel> logEventForAdError(@Body EventModel.AdErrorEventModel event);

    @POST("/event/load")
    Call<EventModel.AdLoadEventModel> logEventForAdLoad(@Body EventModel.AdLoadEventModel event);

}
