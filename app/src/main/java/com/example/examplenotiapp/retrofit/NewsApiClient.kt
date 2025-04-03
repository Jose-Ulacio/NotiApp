package com.example.examplenotiapp.retrofit

import com.example.examplenotiapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiClient {
    @GET("/api/1/news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("language") language: String
    ): NewsResponse

    @GET("version")
    suspend fun prueba()
}