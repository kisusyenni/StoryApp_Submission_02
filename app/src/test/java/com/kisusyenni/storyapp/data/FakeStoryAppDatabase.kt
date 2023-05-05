package com.kisusyenni.storyapp.data

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.kisusyenni.storyapp.data.source.local.room.RemoteKeysDao
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDao
import com.kisusyenni.storyapp.data.source.local.room.StoryAppDatabase
import org.mockito.Mockito

class FakeStoryAppDatabase: StoryAppDatabase() {
    override fun storyAppDao(): StoryAppDao = FakeStoryAppDao()

    override fun remoteKeysDao(): RemoteKeysDao = FakeRemoteKeysDao()

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java)
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java)
    }

    override fun clearAllTables() {}

}