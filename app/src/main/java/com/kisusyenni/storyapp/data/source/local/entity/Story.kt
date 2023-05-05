package com.kisusyenni.storyapp.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class Story(
    @PrimaryKey
    @NonNull
    val id: String,
    val photoUrl: String?,
    val createdAt: String?,
    val name: String?,
    val description: String?,
    val lon: Double?,
    val lat: Double?,
): Parcelable