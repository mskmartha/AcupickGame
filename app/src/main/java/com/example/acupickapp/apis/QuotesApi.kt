package com.example.acupickapp.apis

import com.example.acupickapp.Model.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {
    @GET("/posts") // Replace with your actual endpoint if different
    suspend fun getQuotes(): Response<List<Quote>>
}