package com.example.myapplication.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R
import com.example.myapplication.data.model.NoAuthException
import com.example.myapplication.data.model.Note
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.common.BaseActivity
import com.example.myapplication.ui.common.BaseViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
private const val RC_SING_IN = 42
private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val viewModel: SplashViewModel
            by lazy { ViewModelProviders.of(this).get(SplashViewModel::class.java) }

    override val ui: ActivitySplashBinding
            by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override val layoutRes: Int = R.layout.activity_splash

    override fun renderData(data: Boolean?) {
        data?.takeIf { bool->true}?.let {
            startMainActivity()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { showError(it) }
        }
    }

    private fun startLoginActivity(){
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
                )
                .build(),
            RC_SING_IN
        )
    }

    private fun startMainActivity(){
        startActivity(MainActivity.getStartIntent(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SING_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper())
            .postDelayed({
                viewModel.requestUser()},
                START_DELAY)
    }
}