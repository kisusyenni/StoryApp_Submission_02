package com.kisusyenni.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kisusyenni.storyapp.data.SessionPreference
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDatabase
import com.kisusyenni.storyapp.data.source.remote.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryAppDatabase.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(database, apiService)
    }

    fun providePref(dataStore: DataStore<Preferences>): SessionPreference {
        return SessionPreference.getInstance(dataStore)
    }
}