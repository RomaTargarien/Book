package com.example.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.api.FlickrApi
import com.example.photogallery.api.FlickrResponse
import com.example.photogallery.api.PhotoInterceptor
import com.example.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Query

private const val TAG = "FlickrFletch"


class FlickrFetchr {

    private val flickrApi: FlickrApi

    init {
        val client = OkHttpClient.Builder()
                .addInterceptor(PhotoInterceptor())
                .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
                .client(client)
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }
    fun fetchPhotosRequest(): Call<FlickrResponse>{
        return flickrApi.fetchPhotos()
    }
    fun searchPhotosReqeust(query: String): Call<FlickrResponse>{
        return flickrApi.searchPhtos(query)
    }
    fun fetchPhotos(): LiveData<List<GalleryItem>>{
        return fetchPhotoMetaData(fetchPhotosRequest())
    }
    fun searchPhotos(query: String): LiveData<List<GalleryItem>>{
        return fetchPhotoMetaData(searchPhotosReqeust(query))
    }
    fun fetchPhotoMetaData(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>>{
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        flickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG,"Failed to fetch fotos",t)
            }

            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG,"Responce recieved ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                Log.d(TAG,flickrResponse?.photos.toString())
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })
        return responseLiveData
    }
    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchByUrls(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG,"Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}