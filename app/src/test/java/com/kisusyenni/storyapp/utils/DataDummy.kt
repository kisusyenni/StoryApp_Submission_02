package com.kisusyenni.storyapp.utils

import com.kisusyenni.storyapp.data.source.local.entity.Session
import com.kisusyenni.storyapp.data.source.local.entity.Story
import com.kisusyenni.storyapp.data.source.remote.response.*

object DataDummy {
    fun generateDummyStoriesResponse(): StoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<ListStoryItem>()

        for (i in 0 until 10) {
            val story = ListStoryItem(
                id = "story-lsSZjBDc_eoVDJqz",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1667835990790_b_VWZjl_.jpg",
                createdAt = "2022-11-07T15:46:30.794Z",
                name = "hallo",
                description = "Ini gambar",
                lon = -7.347061207239697,
                lat = -108.20655405814534
            )

            listStory.add(story)
        }

        return StoriesResponse(listStory = listStory, error = error, message = message)
    }

    fun generateDummyListStory(): List<Story> {
        val items = arrayListOf<Story>()

        for (i in 0 until 10) {
            val story = Story(
                id = "story-lsSZjBDc_eoVDJqz",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1667835990790_b_VWZjl_.jpg",
                createdAt = "2022-11-07T15:46:30.794Z",
                name = "hallo",
                description = "Ini gambar",
                lon = -7.347061207239697,
                lat = -108.20655405814534
            )
            items.add(story)
        }

        return items
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-NMUOtUP0oAnoL4xL",
            name = "hallo",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5NVU90VVAwb0Fub0w0eEwiLCJpYXQiOjE2Njc4Mzc0NDB9.DiObtYXuYFsmaFhn8jym-sDTd_wYEz824wmCg2OaijI"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyFileUploadResponse(): FileUploadResponse {
        return FileUploadResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummySession(): Session {
        return Session(
            userId = "user-NMUOtUP0oAnoL4xL",
            name = "hallo",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5NVU90VVAwb0Fub0w0eEwiLCJpYXQiOjE2Njc4Mzc0NDB9.DiObtYXuYFsmaFhn8jym-sDTd_wYEz824wmCg2OaijI",
            isLogin = true
        )
    }

    fun generateDummyEmptySession(): Session {
        return Session("", "", "", false)
    }

    fun generateDummyToken(): String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5NVU90VVAwb0Fub0w0eEwiLCJpYXQiOjE2Njc4Mzc0NDB9.DiObtYXuYFsmaFhn8jym-sDTd_wYEz824wmCg2OaijI"
    }

    fun generateDummyTheme(): Boolean {
        return false
    }

}