package com.builditcreative.newsapp.api

import com.builditcreative.newsapp.models.ArticleResponse
import com.builditcreative.newsapp.models.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface ApiService {
    @GET("/v2/sources")
    fun getSources(@QueryMap query: Map<String?, String?>?): Call<SourcesResponse?>?

    @GET("/v2/top-headlines")
    fun getTopHeadlines(@QueryMap query: Map<String?, String?>?): Call<ArticleResponse?>?

    @GET("/v2/everything")
    fun getEverything(@QueryMap query: Map<String, String>?): Call<ArticleResponse?>?
}