package com.rehza.storyapp.ui.story.create

import androidx.lifecycle.ViewModel
import com.rehza.storyapp.data.local.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun addNewStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.addNewStory(file, description)
}