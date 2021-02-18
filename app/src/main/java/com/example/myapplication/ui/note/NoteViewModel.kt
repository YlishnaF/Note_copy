package com.example.myapplication.ui.note

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Repository
import com.example.myapplication.data.model.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.extensions.MyLog
import com.example.myapplication.ui.common.BaseViewModel
import java.util.*

class NoteViewModel(val repository: Repository = Repository) :
    BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return

                when (t) {
                    is NoteResult.Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                    is NoteResult.Error ->
                        viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }

    fun newId(): String {
        Log.d(MyLog, "newID created")
        return UUID.randomUUID().toString()
    }
}
