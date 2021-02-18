package com.example.myapplication.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R
import com.example.myapplication.data.model.Note
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.common.BaseActivity

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {
    override val viewModel: MainViewModel
            by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override val ui: ActivityMainBinding
            by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val layoutRes: Int = R.layout.activity_main
    private lateinit var adapter: MainAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })
        mainRecycler.adapter = adapter

        fab.setOnClickListener { openNoteScreen(null) }
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }

    private fun openNoteScreen(note: Note? = null) {
        startActivity(NoteActivity.getStartIntent(this, note?.id))
    }

    companion object{
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}