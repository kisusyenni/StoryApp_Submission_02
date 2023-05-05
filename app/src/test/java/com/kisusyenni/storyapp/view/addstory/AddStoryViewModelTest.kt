package com.kisusyenni.storyapp.view.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.response.FileUploadResponse
import com.kisusyenni.storyapp.utils.CoroutinesTestRule
import com.kisusyenni.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository


    @Mock
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyAppRepository)
    }

    @Test
    fun `When upload story is successful`() = runTest {
        val dummyResponse = DataDummy.generateDummyFileUploadResponse()
        val dummyToken = DataDummy.generateDummyToken()
        val dummyImage: MultipartBody.Part = MultipartBody.Part.create("test".toRequestBody())
        val dummyDescription =
            "Lorem ipsum dolor sit amet".toRequestBody("text/plain".toMediaType())
        val dummyLon = "-107.0928393".toRequestBody("text/plain".toMediaType())
        val dummyLat = "127.928393".toRequestBody("text/plain".toMediaType())

        val expectedResponse = MutableLiveData<ApiResponse<FileUploadResponse?>>()
        expectedResponse.value = ApiResponse.Success(dummyResponse)

        Mockito.`when`(
            storyAppRepository.uploadStory(
                token = dummyToken,
                image = dummyImage,
                description = dummyDescription,
                lon = dummyLon,
                lat = dummyLat
            )
        )
            .thenReturn(expectedResponse)

        addStoryViewModel.uploadStory(
            token = dummyToken,
            image = dummyImage,
            description = dummyDescription,
            lon = dummyLon,
            lat = dummyLat
        ).observeForever { res ->
            when (res) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(res.data)
                    Assert.assertSame(res.data, dummyResponse)
                }
                is ApiResponse.Error -> {
                    Assert.assertFalse(res.data!!.error)
                }

            }

            Mockito.verify(storyAppRepository).uploadStory(
                token = dummyToken,
                image = dummyImage,
                description = dummyDescription,
                lon = dummyLon,
                lat = dummyLat
            )
        }
    }

}