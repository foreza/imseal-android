package com.foreza.imseal;
import retrofit2.Call;
import retrofit2.http.GET;


public interface APIService {
        @GET("json/")
        Call<LocationModel> getAPIService();
}
