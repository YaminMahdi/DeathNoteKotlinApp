package com.yklab.deathnote.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yklab.deathnote.model.NoteInfo


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllNote(noteList: List<NoteInfo>)

    @Update
    suspend fun updateNote(note: NoteInfo)

    @Query("SELECT * FROM notes")
    fun readAllNotes(): LiveData<List<NoteInfo>>

//    @Query("SELECT * FROM notes ORDER BY ID DESC LIMIT 1")
//    fun readLastAddedNote() : NoteInfo

    @Delete
    fun deleteNote(note: NoteInfo)

}