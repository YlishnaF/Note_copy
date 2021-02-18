package com.example.myapplication.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T, VS : BaseViewState<T>>: ViewModel() {
    open val viewStateLiveData = MutableLiveData<VS>()
    open fun getViewSate(): LiveData<VS> = viewStateLiveData
}