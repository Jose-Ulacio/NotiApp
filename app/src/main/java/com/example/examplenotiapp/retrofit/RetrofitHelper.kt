package com.example.examplenotiapp.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
     val BASE_URL = "https://newsdata.io/"

     fun getRetrofit(): NewsApiClient {
         val client = OkHttpClient.Builder()
             .addInterceptor { chain ->
                 val originalRequest = chain.request()
                 val compressedRequest = originalRequest.newBuilder()
                     .header("Accept-Encoding", "gzip") // Aceptar respuestas comprimidas
                     .build()
                 chain.proceed(compressedRequest)
             }
             .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiClient::class.java)
    }

    private val cliente = OkHttpClient.Builder().apply {
        addInterceptor(InterceptorClient())
    }.build()


}

class InterceptorClient(): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .build()
        return chain.proceed(request)
    }

}