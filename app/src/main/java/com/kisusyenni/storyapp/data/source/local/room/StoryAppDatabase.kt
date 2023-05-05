package com.kisusyenni.storyapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kisusyenni.storyapp.data.source.local.entity.RemoteKeys
import com.kisusyenni.storyapp.data.source.local.entity.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)

abstract class StoryAppDatabase : RoomDatabase() {
    abstract fun storyAppDao(): StoryAppDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: StoryAppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryAppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryAppDatabase::class.java, "story_app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}