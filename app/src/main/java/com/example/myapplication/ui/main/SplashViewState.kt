package com.example.myapplication.ui.main

import com.example.myapplication.ui.common.BaseViewState

class SplashViewState(isAuth : Boolean? = null, error: Throwable?=null) :
    BaseViewState<Boolean?>(isAuth, error)