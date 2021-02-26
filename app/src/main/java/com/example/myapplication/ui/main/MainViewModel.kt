package com.example.myapplication.ui.main

import android.util.Log
import androidx.lifecycle.Observer

import com.example.myapplication.data.Repository

import com.example.myapplication.data.model.Note
import com.example.myapplication.data.model.NoteResult
import com.example.myapplication.extensions.MyLog
import com.example.myapplication.ui.common.BaseViewModel

class MainViewModel(val repository: Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return

            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}