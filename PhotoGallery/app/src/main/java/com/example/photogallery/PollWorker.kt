package com.example.photogallery

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.content.edit
import androidx.work.Worker
import androidx.work.WorkerParameters



private const val TAG = "PollWorker"

class PollWorker (val context: Context,workerParams: WorkerParameters): Worker(context,workerParams){
    override fun doWork(): Result {
        val query = QuerryPreferences.getSoredQuerry(context)
        val lastResultId = QuerryPreferences.getLastResultId(context)
        val items: List<GalleryItem> = if (query.isEmpty()){
            FlickrFetchr().fetchPhotosRequest().execute().body()?.photos?.galleryItems
        } else {
            FlickrFetchr().searchPhotosReqeust(query).execute().body()?.photos?.galleryItems
        } ?: emptyList()
        if (items.isEmpty()){
            return Result.success()
        }
        val resultId = items.first().id
        if (resultId == lastResultId){
            Log.i(TAG,"OLD")
        } else {
            Log.i(TAG,"NEW")
            QuerryPreferences.setLastResultId(context,resultId)
        }
        return Result.success()
    }


}