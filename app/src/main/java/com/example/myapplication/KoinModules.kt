package com.example.myapplication

import com.example.myapplication.data.Repository
import com.example.myapplication.data.model.FireStoreProvider
import com.example.myapplication.data.model.RemoteDataProvider
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.main.SplashViewModel
import com.example.myapplication.ui.note.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.math.sin

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module{
    viewModel {SplashViewModel(get())}
}
val mainModule = module{
    viewModel {MainViewModel(get())}
}
val noteModule = module{
    viewModel {NoteViewModel(get())}
}