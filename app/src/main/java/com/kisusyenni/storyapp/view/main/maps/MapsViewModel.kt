package com.kisusyenni.storyapp.view.main.maps

import androidx.lifecycle.ViewModel
import com.kisusyenni.storyapp.data.source.StoryAppRepository

class MapsViewModel(
    private val storyAppRepository: StoryAppRepository
) : ViewModel() {

    suspend fun getStories(token: String) =
        storyAppRepository.getStoriesByLocation(token)
}