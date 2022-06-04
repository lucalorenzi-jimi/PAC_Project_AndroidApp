package com.example.myapplication;

import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationAPI {

    @POST("controller/reservation/{idApartment}")
    Call<ResponseBody> insertPrenotation(@Body Reservation reservation, @Path("idApartment") String apartmentId);

}
