package com.infigo.watchsaleapp.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.infigo.watchsaleapp.databinding.ActivitySignUpBinding
import com.infigo.watchsaleapp.model.User
import com.infigo.watchsaleapp.utils.Status
import com.infigo.watchsaleapp.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signupForm.gotoLogin.setOnClickListener {
            finish()
        }


        binding.signupForm.buttonAcount.setOnClickListener {

            binding.apply {
                val name = signupForm.editName.text.toString()
                val email = signupForm.editEmail.text.toString()
                val mobile = signupForm.editMobile.text.toString()
                val pass = signupForm.editPass.text.toString()
                createNewAccount(name, email, mobile, pass)
            }


        }
    }

    private fun createNewAccount(name: String, email: String, mobile: String, pass: String) {
        if (TextUtils.isEmpty(name)) {
            binding.signupForm.editName.error = "Please Enter Name"
            binding.signupForm.editName.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            binding.signupForm.editEmail.error = "Please Enter Email"
            binding.signupForm.editEmail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.signupForm.editMobile.error = "Please Enter Mobile"
            binding.signupForm.editMobile.requestFocus()
            return
        }
        if (TextUtils.isEmpty(pass)) {
            binding.signupForm.editPass.error = "Please Enter PIN"
            binding.signupForm.editPass.requestFocus()
            return
        }

        val user = User("$mobile", name, email, pass, mobile)
        loginViewModel.requestForRegisterResponse(user)
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)

            loginViewModel.registerResponse.observe(this@SignUpActivity, Observer {

                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            val user = it
                        }
                    }

                    Status.ERROR -> {
                        it.data?.let {
                            val user = it
                        }
                    }
                    Status.LOADING -> {
                        it.data?.let {
                            val loading = it
                            showDialog(loading.email)
                        }
                    }
                }

            })

            showDialog("Registration Completed. Go to Login Page.")
        }


    }

    private fun showDialog(message: String) {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Watch Store App")
                setMessage(message)
                setPositiveButton("Login",
                    DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })
            }
            builder.create()
        }
        alertDialog?.show()
    }

}