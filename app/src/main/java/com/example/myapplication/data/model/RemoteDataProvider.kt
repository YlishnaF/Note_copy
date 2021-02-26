package com.example.myapplication.data.model

import androidx.lifecycle.LiveData

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUSer(): LiveData<User?>
    fun deleteNote(noteId: String): LiveData<NoteResult>
}