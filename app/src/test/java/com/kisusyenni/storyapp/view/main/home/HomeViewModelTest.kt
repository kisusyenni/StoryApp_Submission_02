package com.kisusyenni.storyapp.view.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.local.entity.Story
import com.kisusyenni.storyapp.utils.CoroutinesTestRule
import com.kisusyenni.storyapp.utils.DataDummy
import com.kisusyenni.storyapp.utils.PagingTestDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(storyAppRepository)
    }

    @Test
    fun `When get stories should not null`() = runTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val dummyToken = DataDummy.generateDummyToken()

        val data = PagingTestDataSource.snapshot(dummyStories)

        val expectedStories: Flow<PagingData<Story>> = flow {
            emit(data)
        }

        Mockito.`when`(storyAppRepository.getStories(dummyToken)).thenReturn(expectedStories)

        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }

        homeViewModel.getStories(dummyToken).observeForever {
            val differ = AsyncPagingDataDiffer(
                diffCallback = HomeAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
            }
            advanceUntilIdle()

            Mockito.verify(storyAppRepository).getStories(dummyToken)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(differ.snapshot().size, DataDummy.generateDummyListStory().size)
        }
    }

}