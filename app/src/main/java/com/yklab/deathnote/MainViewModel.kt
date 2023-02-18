package com.yklab.deathnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yklab.deathnote.data.UserDatabase
import com.yklab.deathnote.model.NoteInfo
import com.yklab.deathnote.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    val readAllNote: LiveData<List<NoteInfo>>
    private val repository: NoteRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = NoteRepository(userDao)
        readAllNote = repository.readAllNote
    }

    fun addAllNote(notes: List<NoteInfo>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAllNote(notes)
        }
    }
    fun addNote(note: NoteInfo, newNoteId: (id: Long) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            newNoteId.invoke(repository.addNote(note))

        }
    }
    fun updateNote(note: NoteInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }
    fun deleteNote(note: NoteInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }
   // fun getLastAddedNote(): NoteInfo = repository.readLastAddedNote
}