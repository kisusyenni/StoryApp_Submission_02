package com.kisusyenni.storyapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.kisusyenni.storyapp.data.source.local.entity.Session
import com.kisusyenni.storyapp.databinding.ActivityWelcomeBinding
import com.kisusyenni.storyapp.view.login.LoginActivity
import com.kisusyenni.storyapp.view.main.MainActivity
import com.kisusyenni.storyapp.view.register.RegisterActivity
import com.kisusyenni.storyapp.viewmodel.DatastoreViewModel
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var datastoreViewModel: DatastoreViewModel
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        observeTheme()
        setBtnAction()
        startAnimation()
    }

    private fun startAnimation() {

        val image =
            ObjectAnimator.ofFloat(binding.welcomeImageView, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.welcomeLoginBtn, View.ALPHA, 1f).setDuration(500)
        val register =
            ObjectAnimator.ofFloat(binding.welcomeRegisterBtn, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.welcomeTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.welcomeDesc, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(image, title, desc, login, register)
            start()
        }
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        datastoreViewModel = ViewModelProvider(this, factory)[DatastoreViewModel::class.java]
        datastoreViewModel.fetchSession()
        datastoreViewModel.session.observe(this) { session ->
            this.session = session
            if (session.token != "") {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    private fun setBtnAction() {
        binding.welcomeLoginBtn.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        binding.welcomeRegisterBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun observeTheme() {
        datastoreViewModel.getTheme()
        datastoreViewModel.darkMode.observe(this@WelcomeActivity
        ) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}