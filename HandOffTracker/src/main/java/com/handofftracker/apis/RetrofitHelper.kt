package com.handofftracker.apis

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

   val baseUrl = "https://acupickgame.free.beeceptor.com/playerdetails/"

    fun getInstance(): Retrofit {
        val gson =  GsonBuilder()
            .setLenient()
            .create();
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            // we need to add converter factory to 
            // convert JSON object to Java object
            .build()
    }
}