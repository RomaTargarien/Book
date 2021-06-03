package com.example.photogallery

import android.app.Application
import androidx.lifecycle.*

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchItem = MutableLiveData<String>()
    val searchTerm: String
        get() = mutableSearchItem.value ?: ""

    init {
        mutableSearchItem.value = QuerryPreferences.getSoredQuerry(app)
        galleryItemLiveData =
                Transformations.switchMap(mutableSearchItem) { searchTerm ->
                    if (searchTerm.isBlank()) {
                        flickrFetchr.fetchPhotos()
                    } else {
                        flickrFetchr.searchPhotos(searchTerm)
                    }
                }
    }
    fun fetchFotos(query: String = ""){
        QuerryPreferences.setStoredQuerry(app,query)
        mutableSearchItem.value = query
    }
}