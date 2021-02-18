package com.example.myapplication.ui.note

import com.example.myapplication.data.model.Note
import com.example.myapplication.ui.common.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)