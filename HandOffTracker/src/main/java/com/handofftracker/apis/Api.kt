package com.handofftracker.apis

import com.handofftracker.models.ApiResponse
import com.handofftracker.models.RewardModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {


    @GET("{storeNumber}/{eventuserid}/") // Replace with your actual endpoint if different
    suspend fun getData(@Path("storeNumber") storeNumber: String,
                        @Path("eventuserid") eventuserid: String): Response<ApiResponse>
}