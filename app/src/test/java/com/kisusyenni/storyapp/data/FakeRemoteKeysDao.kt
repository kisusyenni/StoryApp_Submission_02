package com.kisusyenni.storyapp.data

import com.kisusyenni.storyapp.data.source.local.entity.RemoteKeys
import com.kisusyenni.storyapp.data.source.local.room.RemoteKeysDao

class FakeRemoteKeysDao : RemoteKeysDao {
    private val remoteKeys = mutableListOf<RemoteKeys>()

    override suspend fun insertAll(remoteKey: List<RemoteKeys>) {
        remoteKeys.addAll(remoteKeys)
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeys? {
        return remoteKeys.firstOrNull { it.id == id }
    }

    override suspend fun deleteRemoteKeys() {
        remoteKeys.clear()
    }
}