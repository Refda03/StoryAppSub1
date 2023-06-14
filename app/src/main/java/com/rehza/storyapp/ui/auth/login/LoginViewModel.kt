package com.rehza.storyapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.rehza.storyapp.data.local.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email, password)
}