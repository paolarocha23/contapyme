package com.example.contapyme.data.api

import android.content.Context
import com.example.contapyme.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private var retrofit: Retrofit? = null
    private var currentBaseUrl: String? = null

    @Synchronized
    fun getRetrofit(context: Context): Retrofit {
        val sessionManager = SessionManager(context.applicationContext)
        val baseUrl = runBlocking { sessionManager.apiBaseUrl.first() }
        
        if (retrofit == null || currentBaseUrl != baseUrl) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            currentBaseUrl = baseUrl
        }
        return retrofit!!
    }

    fun getAuthApi(context: Context): AuthApi {
        return getRetrofit(context).create(AuthApi::class.java)
    }

    @Synchronized
    fun reset() {
        retrofit = null
        currentBaseUrl = null
    }
}
