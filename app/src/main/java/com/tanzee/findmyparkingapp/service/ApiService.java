package com.tanzee.findmyparkingapp.service;

import com.tanzee.findmyparkingapp.dto.MapDirectionResponseDto;
import com.tanzee.findmyparkingapp.dto.SignupRequestDto;
import com.tanzee.findmyparkingapp.dto.SignupResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface ApiService{
    @GET
    Call<MapDirectionResponseDto> getDirection(@Url String url);

    @POST("signup.php")
    Call<SignupResponseDto> signUp(@Body SignupRequestDto dto);
}
