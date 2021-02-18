package com.example.myapplication.ui.main

import com.example.myapplication.data.model.Note
import com.example.myapplication.ui.common.BaseViewState

class MainViewState (val notes: List<Note>?=null, error: Throwable?=null) :
    BaseViewState<List<Note>?>(notes, error)
