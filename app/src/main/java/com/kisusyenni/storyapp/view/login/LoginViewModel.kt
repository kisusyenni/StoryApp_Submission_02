package com.kisusyenni.storyapp.view.login

import androidx.lifecycle.ViewModel
import com.kisusyenni.storyapp.data.source.StoryAppRepository

class LoginViewModel(private val storyAppRepository: StoryAppRepository): ViewModel() {

    fun login(email: String, password: String) = storyAppRepository.login(email = email, password = password)
}