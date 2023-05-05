package com.kisusyenni.storyapp.view.register

import androidx.lifecycle.ViewModel
import com.kisusyenni.storyapp.data.source.StoryAppRepository

class RegisterViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        storyAppRepository.register(name = name, email = email, password = password)

}