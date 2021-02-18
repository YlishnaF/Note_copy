package com.example.myapplication.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.extensions.MyLog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTIONS = "users"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNoteCollection().addSnapshotListener { snapshot, e ->
                    value = e?.let { NoteResult.Error(it) }
                        ?: snapshot?.let { snapshotDocuments ->
                            val notes =
                                snapshotDocuments.documents.map { it.toObject(Note::class.java) }
                            NoteResult.Success(notes)
                        }
                }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try{
                getUserNoteCollection().document(id).get()
                    .addOnSuccessListener { snapshot ->
                        value =
                            NoteResult.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener { value = NoteResult.Error(it) }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try{
                getUserNoteCollection().document(note.id)
                    .set(note).addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        OnFailureListener { exeption ->
                            Log.d(TAG, "Error saving note $note, message: ${exeption.message}")
                            value = NoteResult.Error(exeption)
                        }
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getCurrentUSer(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let{
                User(
                    it.displayName?: "",
                    it.email ?: ""
                )
            }
        }

    private fun getUserNoteCollection() = currentUser?.let {firebaseUser->
        db.collection(USERS_COLLECTIONS)
            .document(firebaseUser.uid)
            .collection(NOTES_COLLECTION)
    }?: throw NoAuthException()

}