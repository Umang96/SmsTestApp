package com.umang96.smstestapp.api

import com.umang96.smstestapp.data.Request
import com.umang96.smstestapp.data.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SmsApi {

    @POST("getsms")
    fun getSms(@Body request: Request): Call<Response>

    @POST("putsms")
    fun putSms(@Body request: Request): Call<Response>

}