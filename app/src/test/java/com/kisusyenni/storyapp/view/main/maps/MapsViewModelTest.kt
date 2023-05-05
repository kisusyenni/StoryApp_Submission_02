package com.kisusyenni.storyapp.view.main.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.response.StoriesResponse
import com.kisusyenni.storyapp.utils.CoroutinesTestRule
import com.kisusyenni.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository

    @Mock
    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyAppRepository)
    }

    @Test
    fun `When get stories by location should not null`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesResponse()
        val dummyToken = DataDummy.generateDummyToken()

        val expectedStories: Flow<ApiResponse<StoriesResponse?>> =
            flowOf(ApiResponse.Success(dummyStories))

        Mockito.`when`(storyAppRepository.getStoriesByLocation(dummyToken))
            .thenReturn(expectedStories)

        mapsViewModel.getStories(dummyToken).collect { res ->
            when (res) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    Assert.assertTrue(true)
                    Assert.assertNotNull(res.data)
                    Assert.assertSame(res.data, dummyStories)
                }
                is ApiResponse.Error -> {
                    Assert.assertFalse(res.data!!.error)
                }
                else -> {}
            }

            Mockito.verify(storyAppRepository).getStoriesByLocation(dummyToken)
        }
    }
}