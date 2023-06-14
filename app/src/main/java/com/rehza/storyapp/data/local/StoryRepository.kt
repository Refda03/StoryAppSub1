package com.rehza.storyapp.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rehza.storyapp.data.api.ApiService
import com.rehza.storyapp.data.api.AddNewStoryResponse
import com.rehza.storyapp.data.api.ListStoryItem
import com.rehza.storyapp.data.api.LoginResponse
import com.rehza.storyapp.data.api.RegisterResponse
import com.rehza.storyapp.util.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {
    fun getAllStories(): LiveData<Resource<List<ListStoryItem>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllStories()
            emit(Resource.Success(response.listStory))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getAllStories: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun addNewStory(file: MultipartBody.Part, description: RequestBody): LiveData<Resource<AddNewStoryResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.addNewStory(file, description)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String): LiveData<Resource<RegisterResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "postSignUp: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }
}