package com.rania.useralbum.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.rania.useralbum.model.Photo
import com.rania.useralbum.repository.PhotoRepository
import com.rania.useralbum.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PhotoViewModel(val photoRepository: PhotoRepository) : ViewModel() {

    companion object {
        val TAG = PhotoViewModel::class.java.simpleName
    }

    fun photoList(albumId: Int): LiveData<Resource<List<Photo>>> {
        return photoRepository.getPhotoList(albumId).map {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "LOADING status")
                    Resource.loading(null)
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "SUCCESS status")
                    Resource.success(it.data)
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "ERROR status")
                    Resource.error(it.message!!, null)
                }
            }
        }.asLiveData(viewModelScope.coroutineContext)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertPhoto(photo: Photo) = viewModelScope.launch(Dispatchers.IO) {
        photoRepository.insertPhoto(photo)
    }

}

/**
 * Factory for [PhotoViewModel].
 */
class PhotoViewModelFactory(private val repository: PhotoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}