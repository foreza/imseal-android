package com.foreza.imseal.services;
import com.foreza.imseal.models.LocationModel;

import retrofit2.Call;
import retrofit2.http.GET;


public interface LocationAPIService {
        @GET("json/")
        Call<LocationModel> getAPIService();
}
