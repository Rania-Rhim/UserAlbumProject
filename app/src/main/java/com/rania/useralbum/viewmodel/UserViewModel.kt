package com.rania.useralbum.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.rania.useralbum.model.User
import com.rania.useralbum.repository.Resource
import com.rania.useralbum.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserViewModel(val userRepository: UserRepository) : ViewModel() {

    companion object {
        val TAG = UserViewModel::class.java.simpleName
    }

    val userList: LiveData<Resource<List<User>>> =
        userRepository.getUserList().map {
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

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.insertUser(user)
    }

}

/**
 * Factory for [UserViewModel].
 */
class UserViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}