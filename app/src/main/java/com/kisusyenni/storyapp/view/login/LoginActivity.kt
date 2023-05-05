package com.kisusyenni.storyapp.view.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kisusyenni.storyapp.R
import com.kisusyenni.storyapp.data.source.local.entity.Session
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.databinding.ActivityLoginBinding
import com.kisusyenni.storyapp.view.main.MainActivity
import com.kisusyenni.storyapp.viewmodel.DatastoreViewModel
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var datastoreViewModel: DatastoreViewModel
    private lateinit var session: Session

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        datastoreViewModel = ViewModelProvider(this, factory)[DatastoreViewModel::class.java]
        datastoreViewModel.fetchSession()
        datastoreViewModel.session.observe(this) { session ->
            this.session = session
            if (session.token != "") {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.pbLogin.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.visibility = if (loading) View.GONE else View.VISIBLE
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email = email, password = password).observe(this@LoginActivity) {
            when (it) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> {
                    showLoading(false)
                    val result = it.data?.loginResult
                    val session =
                        Session(name = result!!.name, userId = result.userId, token = result.token, isLogin = true)
                    datastoreViewModel.saveSession(session)

                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.login_success),
                        Toast.LENGTH_LONG
                    ).show()

                    Intent(this@LoginActivity, MainActivity::class.java).also { intent ->
                        startActivity(intent)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_LONG).show()
                }
                else ->
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.error),
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewModel()
        showLoading(false)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, R.string.login_error, Toast.LENGTH_LONG).show()
            } else {
                loginUser(email, password)
            }

        }
    }
}