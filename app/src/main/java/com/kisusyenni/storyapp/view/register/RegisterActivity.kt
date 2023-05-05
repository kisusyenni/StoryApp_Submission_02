package com.kisusyenni.storyapp.view.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kisusyenni.storyapp.R
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.databinding.ActivityRegisterBinding
import com.kisusyenni.storyapp.view.login.LoginActivity
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun showLoading(loading: Boolean) {
        binding.pbRegister.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !loading
    }

    private fun registerUser(name: String, email: String, password: String) {
        registerViewModel.register(name = name, email = email, password = password).observe(this) {
            when (it) {
                is ApiResponse.Loading -> showLoading(true)
                is ApiResponse.Success -> {
                    showLoading(false)
                    Intent(this, LoginActivity::class.java).also { intent ->
                        startActivity(intent)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_LONG).show()
                }
                else ->
                    Toast.makeText(
                        this@RegisterActivity,
                        resources.getString(R.string.error),
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewModel()
        showLoading(false)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@RegisterActivity, R.string.register_error, Toast.LENGTH_LONG)
                    .show()
            } else {
                registerUser(name, email, password)
            }

        }
    }

}