package com.yklab.deathnote.repository

import androidx.lifecycle.LiveData
import com.yklab.deathnote.data.UserDao
import com.yklab.deathnote.model.NoteInfo

class NoteRepository(private val userDao: UserDao) {

    val readAllNote: LiveData<List<NoteInfo>> = userDao.readAllNotes()
    //val readLastAddedNote: NoteInfo = userDao.readLastAddedNote()

    suspend fun addAllNote(notes: List<NoteInfo>){
        userDao.addAllNote(notes)
    }

    suspend fun addNote(note: NoteInfo): Long =
        userDao.addNote(note)

    suspend fun updateNote(note: NoteInfo){
        userDao.updateNote(note)
    }

    fun deleteNote(note: NoteInfo){
        userDao.deleteNote(note)
    }


}