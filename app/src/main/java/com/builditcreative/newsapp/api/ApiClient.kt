package com.builditcreative.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private var mRetrofit: Retrofit? = null
    private val retrofit: Retrofit?
        private get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
    val getAPIService: ApiService
        get() = retrofit!!.create(ApiService::class.java)
}

