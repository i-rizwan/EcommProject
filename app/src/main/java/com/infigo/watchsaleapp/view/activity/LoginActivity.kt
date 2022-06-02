package com.infigo.watchsaleapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.infigo.watchsaleapp.databinding.ActivityLoginBinding
import com.infigo.watchsaleapp.utils.SharedPreferenceManager
import com.infigo.watchsaleapp.utils.Status
import com.infigo.watchsaleapp.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val storeViewModel: LoginViewModel by viewModels()
    lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var userEmail: String
    lateinit var emailInput: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferenceManager = SharedPreferenceManager(this)
        userEmail = sharedPreferenceManager.getUserEmail()

        if (userEmail != "") {

            binding.apply {
                loginForm.profileLL.visibility = View.VISIBLE
                loginForm.etemail.visibility = View.GONE
                loginForm.createnewac.visibility = View.GONE
                loginForm.emailText.text = userEmail
            }

        } else {
            binding.apply {
                loginForm.profileLL.visibility = View.GONE
                loginForm.etemail.visibility = View.VISIBLE
                loginForm.createnewac.visibility = View.VISIBLE
            }

        }
        binding.loginForm.createnewac.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginForm.btnlogin.setOnClickListener {

            emailInput = if (userEmail == "") {
                binding.loginForm.etemail.text.toString()
            } else {
                userEmail
            }
            val password = binding.loginForm.mypass.text.toString()
            doLogin(emailInput, password)
        }
    }

    private fun doLogin(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            binding.loginForm.etemail.error = "Please Enter Registered Email"
            binding.loginForm.etemail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            binding.loginForm.mypass.error = "Please Enter Valid PIN"
            binding.loginForm.mypass.requestFocus()
            return
        }
        binding.progressbar.visibility = View.VISIBLE


        storeViewModel.requestForLoginResponse(email, password)

        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            storeViewModel.loginResponse.observe(this@LoginActivity, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (userEmail == "") {
                            val data = it.data
                            if (data != null)
                                sharedPreferenceManager.addUserData(data)
                        }
                        binding.progressbar.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Status.LOADING -> {
                        if (userEmail == "") {
                            it.data?.let { it1 -> sharedPreferenceManager.addUserData(it1) }
                        }
                    }
                    Status.ERROR -> {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid User Details!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
    }
}