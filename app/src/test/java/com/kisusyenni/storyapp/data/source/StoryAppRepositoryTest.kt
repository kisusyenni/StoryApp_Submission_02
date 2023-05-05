package com.kisusyenni.storyapp.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.kisusyenni.storyapp.data.FakeApiService
import com.kisusyenni.storyapp.data.FakeStoryAppDao
import com.kisusyenni.storyapp.data.FakeStoryAppDatabase
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDao
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDatabase
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.network.ApiService
import com.kisusyenni.storyapp.utils.CoroutinesTestRule
import com.kisusyenni.storyapp.utils.DataDummy
import com.kisusyenni.storyapp.utils.PagingTestDataSource
import com.kisusyenni.storyapp.view.main.home.HomeAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryAppRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppDatabase: StoryAppDatabase

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var storyAppDao: StoryAppDao

    @Mock
    private lateinit var storyAppRepositoryMock: StoryAppRepository

    private lateinit var storyAppRepository: StoryAppRepository

    @Before
    fun setUp() {
        storyAppDatabase = FakeStoryAppDatabase()
        apiService = FakeApiService()
        storyAppDao = FakeStoryAppDao()
        storyAppRepository = StoryAppRepository(storyAppDatabase, apiService)
    }

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyFileUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyName = "hallo"
    private val dummyEmail = "hallo11@email.com"
    private val dummyPassword = "hallo123"

    @Test
    fun `When login successfully`() = runTest {
        val expectedResponse = dummyLoginResponse
        val actualResponse = storyAppRepository.login(email = dummyEmail, password = dummyPassword)

        actualResponse.observeForever {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue((actualResponse.value is ApiResponse.Success))
            Assert.assertEquals(
                expectedResponse.loginResult,
                (actualResponse.value as ApiResponse.Success).data
            )
        }
    }

    @Test
    fun `When register successfully`() = runTest {
        val expectedResponse = dummyRegisterResponse
        val actualResponse = storyAppRepository.register(dummyName, dummyEmail, dummyPassword)

        actualResponse.observeForever {
            Assert.assertNotNull(actualResponse)
            Assert.assertTrue((actualResponse.value is ApiResponse.Success))
            Assert.assertEquals(
                expectedResponse,
                (actualResponse.value as ApiResponse.Success).data
            )
        }
    }

    @Test
    fun `When get stories should not null with response`() = runTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val data = PagingTestDataSource.snapshot(dummyStories)

        val expectedResponse = flowOf(data)

        Mockito.`when`(storyAppRepositoryMock.getStories(dummyToken)).thenReturn(expectedResponse)

        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }

        storyAppRepositoryMock.getStories(dummyToken).collect { result ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = HomeAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(result)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                dummyStoriesResponse.listStory.size,
                differ.snapshot().size
            )
        }
    }

    @Test
    fun `When get stories by location should not null`() = runTest {
        val actualResponse = storyAppRepository.getStoriesByLocation(dummyToken)

        actualResponse.collect { res ->
            when (res) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(res.data)
                    Assert.assertEquals(
                        res.data!!.listStory.size,
                        dummyStoriesResponse.listStory.size
                    )
                }
                is ApiResponse.Error -> {
                    Assert.assertFalse(res.data!!.error)
                }

            }

        }

    }

    @Test
    fun `Upload story successfully`() = runTest {

        val expectedResponse = dummyFileUploadResponse
        val dummyImage: MultipartBody.Part = MultipartBody.Part.create("test".toRequestBody())
        val dummyDescription =
            "Lorem ipsum dolor sit amet".toRequestBody("text/plain".toMediaType())
        val dummyLon = "-107.0928393".toRequestBody("text/plain".toMediaType())
        val dummyLat = "127.928393".toRequestBody("text/plain".toMediaType())

        val actualResponse = storyAppRepository.uploadStory(
            token = dummyToken, image = dummyImage, description = dummyDescription, lon = dummyLon, lat = dummyLat
        )

        actualResponse.observeForever {
            Assert.assertNotNull(actualResponse)
            Assert.assertEquals(expectedResponse, (actualResponse.value as ApiResponse.Success).data)
        }
    }

}
