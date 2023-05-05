package com.kisusyenni.storyapp.view.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.local.entity.Story

class HomeViewModel(
    private val storyAppRepository: StoryAppRepository
) : ViewModel() {

    fun getStories(token: String): LiveData<PagingData<Story>> =
        storyAppRepository.getStories(token).cachedIn(viewModelScope).asLiveData()

}