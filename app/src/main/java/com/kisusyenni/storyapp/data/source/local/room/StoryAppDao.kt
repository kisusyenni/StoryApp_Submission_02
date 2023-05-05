package com.kisusyenni.storyapp.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kisusyenni.storyapp.data.source.local.entity.Story

@Dao
interface StoryAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg story: Story)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}