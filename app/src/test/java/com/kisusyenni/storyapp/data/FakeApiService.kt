package com.kisusyenni.storyapp.data

import com.kisusyenni.storyapp.data.source.remote.network.ApiService
import com.kisusyenni.storyapp.data.source.remote.response.FileUploadResponse
import com.kisusyenni.storyapp.data.source.remote.response.LoginResponse
import com.kisusyenni.storyapp.data.source.remote.response.RegisterResponse
import com.kisusyenni.storyapp.data.source.remote.response.StoriesResponse
import com.kisusyenni.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService: ApiService {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<RegisterResponse> {
        return Response.success(DataDummy.generateDummyRegisterResponse())
    }

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return Response.success(DataDummy.generateDummyLoginResponse())
    }

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): Response<StoriesResponse> {
        return Response.success(DataDummy.generateDummyStoriesResponse())
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Response<FileUploadResponse> {
        return Response.success(DataDummy.generateDummyFileUploadResponse())
    }
}