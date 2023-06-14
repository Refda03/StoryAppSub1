package com.rehza.storyapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rehza.storyapp.util.SettingPreferences
import androidx.lifecycle.*

class SplashViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()
    class Factory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(pref) as T
        }
    }
}