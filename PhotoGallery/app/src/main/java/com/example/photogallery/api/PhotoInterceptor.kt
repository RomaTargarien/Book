package com.example.photogallery.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.http.Url

private const val API_KEY = "8a3cd18b34e1d15aeedba0798afd4cee"
class PhotoInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val origianlRequest: Request = chain.request()
        val newUrl: HttpUrl = origianlRequest.url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("format","json")
                .addQueryParameter("nojsoncallback","1")
                .addQueryParameter("extras","url_s")
                .addQueryParameter("safesearch","1")
                .build()
        val newRequest: Request = origianlRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)

    }

}