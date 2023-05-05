package com.kisusyenni.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kisusyenni.storyapp.data.source.local.entity.Story
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDao

class FakeStoryAppDao : StoryAppDao {
    private var stories = mutableListOf<Story>()

    override suspend fun insertStory(vararg story: Story) {
        stories.addAll(story)
    }

    override fun getStories(): PagingSource<Int, Story> {
        return FakePagingSource(stories)
    }

    override suspend fun deleteAll() {
        stories.clear()
    }

    class FakePagingSource(private val data: MutableList<Story>) :
        PagingSource<Int, Story>() {
        @Suppress("SameReturnValue")
        override fun getRefreshKey(state: PagingState<Int, Story>): Int = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
            return LoadResult.Page(data, 0, 1)
        }

    }
}