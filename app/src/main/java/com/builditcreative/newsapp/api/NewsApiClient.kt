package com.builditcreative.newsapp.api

import com.builditcreative.newsapp.models.ArticleResponse
import com.builditcreative.newsapp.models.SourcesResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.net.HttpURLConnection

class NewsApiClient(val mApiKey: String) {
    var query: MutableMap<String, String>
    val mAPIService: ApiService

    init {
        mAPIService = ApiClient.getAPIService
        query = HashMap()
        query["apiKey"] = mApiKey
    }

    //Callbacks
    interface SourcesCallback {
        fun onSuccess(response: SourcesResponse?)
        fun onFailure(throwable: Throwable?)
    }

    interface ArticlesResponseCallback {
        fun onSuccess(response: ArticleResponse?)
        fun onFailure(throwable: Throwable?)
    }

    private fun errMsg(str: String): Throwable {
        var throwable: Throwable? = null
        try {
            val obj = JSONObject(str)
            throwable = Throwable(obj.getString("message"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (throwable == null) {
            throwable = Throwable("An error occured")
        }
        return throwable
    }

    private fun createQuery(): MutableMap<String, String> {
        query = HashMap()
        query["apiKey"] = mApiKey
        return query
    }

    fun getEverything(
        everythingRequest: EverythingRequest,
        callback: ArticlesResponseCallback
    ) {
        query = createQuery()
        query["q"] = everythingRequest.q
        query["sources"] = everythingRequest.sources
        query["domains"] = everythingRequest.domains
        query["from"] = everythingRequest.from
        query["to"] = everythingRequest.to
        query["language"] = everythingRequest.language
        query["sortBy"] = everythingRequest.sortBy
        query["pageSize"] = everythingRequest.pageSize
        query["page"] = everythingRequest.page
        query.values.removeAll(setOf<Any?>(null))
        query.values.removeAll(setOf("null"))
        mAPIService.getEverything(query)
            ?.enqueue(object : Callback<ArticleResponse?> {
                override fun onResponse(
                    call: Call<ArticleResponse?>?,
                    response: retrofit2.Response<ArticleResponse?>
                ) {
                    if (response.code() === HttpURLConnection.HTTP_OK) {
                        callback.onSuccess(response.body())
                    } else {
                        try {
                            callback.onFailure(response.errorBody()?.let { errMsg(it.string()) })
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ArticleResponse?>?, throwable: Throwable?) {
                    callback.onFailure(throwable)
                }
            })
    }
}