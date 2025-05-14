package com.example.acupickapp.apis

import com.example.acupickapp.Model.Quote
import com.handofftracker.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/posts") // Replace with your actual endpoint if different
    suspend fun getQuotes(): Response<List<Quote>>

    @GET("/1883/smar602") // Replace with your actual endpoint if different
    suspend fun getData(): Response<ApiResponse>
}