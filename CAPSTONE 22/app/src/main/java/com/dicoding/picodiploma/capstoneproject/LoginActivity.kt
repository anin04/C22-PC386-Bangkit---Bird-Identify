package com.dicoding.picodiploma.capstoneproject
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.dicoding.picodiploma.capstoneproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var myEditText: MyEditText

    companion object{
        private const val TAG = "Login Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebaseAuth()
        myEditText = findViewById(R.id.passwordEditText)




        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            if (checkValidation(email,pass)){
                loginToServer(email,pass)
            }

        }
        binding.regisButton.setOnClickListener {
            val intent = Intent(this, RegisActivity::class.java)
            startActivity(intent)
        }
        binding.btnForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if (email.isEmpty()){
            emailEditText.error = "Please field your email"
            emailEditText.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.error = "Please use valid email"
            emailEditText.requestFocus()
        }else if (pass.isEmpty()){
            passwordEditText.error = "Please field your password"
            passwordEditText.requestFocus()
        }else{
            return true
        }
        CustomDialog.hideLoading()
        return false
    }


    private fun loginToServer(email: String, pass: String) {
        val credential = EmailAuthProvider.getCredential(email, pass)
        fireBaseAuth(credential)
    }
    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }
    private fun fireBaseAuth(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                CustomDialog.hideLoading()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

}