package com.rehza.storyapp.ui.story.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rehza.storyapp.data.local.StoryRepository
import com.rehza.storyapp.data.api.ListStoryItem
import com.rehza.storyapp.util.Resource

class ListStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getAllStories(): LiveData<Resource<List<ListStoryItem>>> {
        return repository.getAllStories()
    }
}