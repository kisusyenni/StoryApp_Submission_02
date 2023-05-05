package com.kisusyenni.storyapp.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.JsonParser
import com.kisusyenni.storyapp.data.source.local.entity.Story
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDatabase
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.StoryAppRemoteMediator
import com.kisusyenni.storyapp.data.source.remote.network.ApiService
import com.kisusyenni.storyapp.data.source.remote.response.FileUploadResponse
import com.kisusyenni.storyapp.data.source.remote.response.LoginResponse
import com.kisusyenni.storyapp.data.source.remote.response.RegisterResponse
import com.kisusyenni.storyapp.data.source.remote.response.StoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAppRepository(
    private val storyAppDatabase: StoryAppDatabase,
    private val apiService: ApiService
) {
    fun login(email: String, password: String): LiveData<ApiResponse<LoginResponse?>> =
        liveData {
            emit(ApiResponse.Loading())
            try {
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(response.body()))
                } else {
                    Log.e(TAG, "OnFailure: ${response.body()?.message}")

                    val jsonObj =
                        JsonParser().parse(response.errorBody()!!.charStream().readText()).asJsonObject
                    val message = jsonObj.get("message").asString

                    emit(ApiResponse.Error(message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "OnException: ${e.message.toString()} ")
                emit(ApiResponse.Error(e.message.toString()))
            }
        }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<RegisterResponse?>> =
        liveData {
            emit(ApiResponse.Loading())
            try {
                val response = apiService.register(name, email, password)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(response.body()))
                } else {
                    Log.e(TAG, "OnFailure: ${response.body()?.message}")

                    val jsonObj =
                        JsonParser().parse(response.errorBody()!!.charStream().readText()).asJsonObject
                    val message = jsonObj.get("message").asString

                    emit(ApiResponse.Error(message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "OnException: ${e.message.toString()} ")
                emit(ApiResponse.Error(e.message.toString()))
            }
        }

    fun getStories(token: String): Flow<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryAppRemoteMediator(
                database = storyAppDatabase,
                apiService = apiService,
                token = token
            ),
            pagingSourceFactory = {
                storyAppDatabase.storyAppDao().getStories()
            }
        ).flow
    }

    suspend fun getStoriesByLocation(token: String): Flow<ApiResponse<StoriesResponse?>> =  flow {
        emit(ApiResponse.Loading())
        try {
            val response = apiService.getStories(token = "Bearer $token", location = 1 )
            if (response.isSuccessful) {
                emit(ApiResponse.Success(response.body()))
            } else {
                Log.e(TAG, "OnFailure: ${response.errorBody()}")

                val jsonObj =
                    JsonParser().parse(response.errorBody()!!.charStream().readText()).asJsonObject
                val message = jsonObj.get("message").asString

                emit(ApiResponse.Error(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "OnException: ${e.message.toString()} ")
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lon: RequestBody?,
        lat: RequestBody?
    ): LiveData<ApiResponse<FileUploadResponse?>> = liveData {
        emit(ApiResponse.Loading())
        try {
            val response = apiService.uploadStory(
                token = "Bearer $token",
                file = image,
                description = description,
                lon = lon,
                lat = lat
            )
            if (response.isSuccessful) {
                emit(ApiResponse.Success(response.body()))
            } else {
                Log.e(TAG, "OnFailure: ${response.errorBody()}")

                val jsonObj =
                    JsonParser().parse(response.errorBody()!!.charStream().readText()).asJsonObject
                val message = jsonObj.get("message").asString

                emit(ApiResponse.Error(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "OnException: ${e.message.toString()} ")
            emit(ApiResponse.Error(e.message.toString()))
        }

    }

    companion object {
        private const val TAG = "StoryAppRepository"
    }
}