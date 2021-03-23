package com.rania.useralbum.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.rania.useralbum.model.Album
import com.rania.useralbum.repository.AlbumRepository
import com.rania.useralbum.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AlbumViewModel(val albumRepository: AlbumRepository) : ViewModel() {

    companion object {
        val TAG = AlbumViewModel::class.java.simpleName
    }

    fun albumList(userId: Int): LiveData<Resource<List<Album>>> {
        return albumRepository.getAlbumList(userId).map {
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
    fun insertAlbum(album: Album) = viewModelScope.launch(Dispatchers.IO) {
        albumRepository.insertAlbum(album)
    }

}

/**
 * Factory for [AlbumViewModel].
 */
class AlbumViewModelFactory(private val repository: AlbumRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}