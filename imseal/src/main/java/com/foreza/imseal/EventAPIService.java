package com.foreza.imseal;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EventAPIService {

    @POST("/session")
    Call<SessionModel.SessionResponse> initializeWithInfoForSessionID(@Body JSONObject session);

    @POST("/event")
    Call<EventModel.AdRequestEventModel> logEventForAdRequest(@Body EventModel.AdLoadEventModel event);

    @POST("/event/error")
    Call<EventModel.AdErrorEventModel> logEventForAdError(@Body EventModel.AdErrorEventModel event);

    @POST("/event/load")
    Call<EventModel.AdLoadEventModel> logEventForAdLoad(@Body EventModel.AdLoadEventModel event);

}
