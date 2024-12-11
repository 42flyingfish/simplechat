package com.example.simplechat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplechat.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val emailEditText = binding.emailEditText.text
            val email = emailEditText.toString()

            val passwordEditText = binding.passwordEditText.text
            val password = passwordEditText.toString()

            Log.d("Whooo", "onCreate: $email $password")

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,
                        "Registration Successful",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,
                        "Registration Failed: " + task.exception?.message.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.loginButton.setOnClickListener {
            val emailEditText = binding.emailEditText.text
            val email = emailEditText.toString()

            val paswordEditText = binding.passwordEditText.text
            val password = paswordEditText.toString()

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(baseContext,
                        "Authentication failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}