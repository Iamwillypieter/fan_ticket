package com.example.fanticket.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.example.fanticket.databinding.ActivityRegisterBinding
import com.example.fanticket.login.LoginActivity // Ganti dengan package dan class LoginActivity kamu

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonReg.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmailReg.text.toString().trim()
            val password = binding.edtPasswordReg.text.toString().trim()
            val phoneNumber = binding.edtHpReg.text.toString().trim()

            if (validateInput(username, email, password, phoneNumber)) {
                registerUser(username, email, password, phoneNumber)
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String, phoneNumber: String): Boolean {
        if (username.isEmpty()) {
            binding.edtUsername.error = "Username is required"
            binding.edtUsername.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.edtEmailReg.error = "Email is required"
            binding.edtEmailReg.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailReg.error = "Please enter a valid email"
            binding.edtEmailReg.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.edtPasswordReg.error = "Password is required"
            binding.edtPasswordReg.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.edtPasswordReg.error = "Password must be at least 6 characters"
            binding.edtPasswordReg.requestFocus()
            return false
        }

        if (phoneNumber.isEmpty()) {
            binding.edtHpReg.error = "Phone number is required"
            binding.edtHpReg.requestFocus()
            return false
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            binding.edtHpReg.error = "Please enter a valid phone number"
            binding.edtHpReg.requestFocus()
            return false
        }

        return true
    }

    private fun registerUser(username: String, email: String, password: String, phoneNumber: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserInfo(it, username, phoneNumber)
                    }
                    Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()

                    // Pindah ke LoginActivity
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserInfo(user: FirebaseUser, username: String, phoneNumber: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(user.uid)

        val userInfo = mapOf(
            "username" to username,
            "email" to user.email,
            "phoneNumber" to phoneNumber
        )

        userRef.setValue(userInfo).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User information saved.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save user information: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}