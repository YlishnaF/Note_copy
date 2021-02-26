package com.example.myapplication.ui.main

import com.example.myapplication.data.Repository
import com.example.myapplication.data.model.NoAuthException
import com.example.myapplication.ui.common.BaseViewModel


class SplashViewModel(private val  repository : Repository) :
    BaseViewModel<Boolean?, SplashViewState>(){

    fun requestUser(){
        repository.getCurrentUser().observeForever {user->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuth = true)
            } ?: SplashViewState(error = NoAuthException())

        }
    }
}