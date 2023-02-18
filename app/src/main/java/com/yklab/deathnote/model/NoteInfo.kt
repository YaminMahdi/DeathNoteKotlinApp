package com.yklab.deathnote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "notes")
data class NoteInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val note: String="",
    val time: String=""
): Parcelable