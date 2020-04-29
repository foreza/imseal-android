package com.foreza.imseal;
import retrofit2.Call;
import retrofit2.http.GET;


public interface LocationAPIService {
        @GET("json/")
        Call<LocationModel> getAPIService();
}
