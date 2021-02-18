package com.example.myapplication.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.data.model.Color
import com.example.myapplication.data.model.Note
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivityNoteBinding
import com.example.myapplication.extensions.MyLog
import com.example.myapplication.extensions.format
import com.example.myapplication.extensions.getColorInt
import com.example.myapplication.ui.common.BaseActivity
import com.example.myapplication.ui.note.NoteViewModel
import com.example.myapplication.ui.note.NoteViewState
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {

        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    override val ui: ActivityNoteBinding
            by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //not implemented
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //not implemented
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null) supportActionBar?.title = getString(R.string.new_note_title)
        initView()
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun initView() {
        note?.run {
            ui.toolbar.setBackgroundColor(resources.getColor(color.getColorInt(this@NoteActivity)))
            ui.titleEt.setText(title)
            ui.bodyEt.setText(note)

            supportActionBar?.title = lastChanged.format()
        }
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun triggerSaveNote() {
        Log.d(MyLog, "fun triggerSaveNote")
        if (ui.titleEt.text == null || ui.titleEt.text!!.length < 3) return

        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                title = ui.titleEt.text.toString(),
                note = ui.bodyEt.text.toString(),
                lastChanged = Date()
            ) ?: createNewNote()

            if (note != null) viewModel.saveChanges(note!!)
        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(
        viewModel.newId(),
        ui.titleEt.text.toString(),
        ui.bodyEt.text.toString()
    )

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }
}