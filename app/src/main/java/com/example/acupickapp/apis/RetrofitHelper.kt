package com.example.acupickapp.apis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

   open val baseUrl = "https://jsonplaceholder.typicode.com/"
   open val baseUrl1 = "https://acupickgame.free.beeceptor.com/playerdetails/"



    fun getInstance(url: String): Retrofit {

        val gson =  GsonBuilder()
            .setLenient()
            .create();
        return Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            // we need to add converter factory to 
            // convert JSON object to Java object
            .build()
    }
}