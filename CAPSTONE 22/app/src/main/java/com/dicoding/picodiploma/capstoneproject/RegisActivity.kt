package com.dicoding.picodiploma.capstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityRegisBinding
import com.google.firebase.auth.FirebaseAuth

class RegisActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Register Activity"
    }

    private lateinit var binding: ActivityRegisBinding
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()


            CustomDialog.showLoading(this)
            if (checkValidation(name,email, pass)){
                registerToServer(email, pass)
            }
        }

        myEditText = findViewById(R.id.passwordEditText)



    }

    private fun registerToServer(email: String, pass: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                CustomDialog.hideLoading()
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkValidation(name: String, email: String, pass: String): Boolean {
        if (email.isEmpty()){
            binding.emailEditText.error = "Please field your email"
            binding.emailEditText.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEditText.error = "Please use valida email"
            binding.emailEditText.requestFocus()
        }else if (pass.isEmpty()){
            binding.passwordEditText.error = "Please field your password"
            binding.passwordEditText.requestFocus()
        }else{
            binding.passwordEditText.error = null
            return true
        }
        CustomDialog.hideLoading()
        return false
    }



}