package com.kisusyenni.storyapp.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kisusyenni.storyapp.data.SessionPreference
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.di.Injection
import com.kisusyenni.storyapp.view.addstory.AddStoryViewModel
import com.kisusyenni.storyapp.view.login.LoginViewModel
import com.kisusyenni.storyapp.view.main.home.HomeViewModel
import com.kisusyenni.storyapp.view.main.maps.MapsViewModel
import com.kisusyenni.storyapp.view.register.RegisterViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class ViewModelFactory(
    private val pref: SessionPreference,
    private val storyAppRepository: StoryAppRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyAppRepository) as T
            }
            modelClass.isAssignableFrom(DatastoreViewModel::class.java) -> {
                DatastoreViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                ViewModelFactory(
                    Injection.providePref(context.dataStore),
                    Injection.provideRepository(context)
                ).apply { instance = this }
            }
    }

}