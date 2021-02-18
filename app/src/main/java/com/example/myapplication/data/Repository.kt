package com.example.myapplication.data

import android.util.Log
import com.example.myapplication.data.model.FireStoreProvider
import com.example.myapplication.data.model.Note
import com.example.myapplication.data.model.RemoteDataProvider
import com.example.myapplication.extensions.MyLog

object Repository {
    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

    fun getCurrentUser() = remoteDataProvider.getCurrentUSer()
}