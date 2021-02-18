package com.example.myapplication.ui.common

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R
import com.example.myapplication.extensions.MyLog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity<T, VS : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, VS>
    abstract val layoutRes: Int
    abstract val ui: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        viewModel.getViewSate().observe(this, Observer<VS> { t->
            t?.apply {
                data?.let { renderData(it) }
                error?.let{ renderError(it)}
            }
        })
    }

    abstract fun renderData(data: T)

    protected open fun renderError(error: Throwable) {
        error.message?.let { showError(it) }
    }

    protected fun showError(error: String) {
        Snackbar.make(ui.root, error, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.ok_bth_title) { dismiss() }
            show()
        }
    }
}

