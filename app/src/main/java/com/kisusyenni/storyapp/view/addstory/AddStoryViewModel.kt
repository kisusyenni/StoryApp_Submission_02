package com.kisusyenni.storyapp.view.addstory

import androidx.lifecycle.ViewModel
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyAppRepository: StoryAppRepository
) : ViewModel() {

    fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lon: RequestBody?,
        lat: RequestBody?
    ) = storyAppRepository.uploadStory(
        token = token,
        image = image,
        description = description,
        lon = lon,
        lat = lat
    )
}