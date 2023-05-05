package com.kisusyenni.storyapp.data.source.remote.network

import com.kisusyenni.storyapp.data.source.remote.response.FileUploadResponse
import com.kisusyenni.storyapp.data.source.remote.response.LoginResponse
import com.kisusyenni.storyapp.data.source.remote.response.RegisterResponse
import com.kisusyenni.storyapp.data.source.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/v1/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @GET("v1/stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ) : Response<StoriesResponse>

    @Multipart
    @POST("/v1/stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): Response<FileUploadResponse>
}

