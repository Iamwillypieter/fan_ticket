package com.example.fanticket.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.fanticket.R
import com.example.fanticket.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonReg.setOnClickListener() {
            val email = binding.edtEmailReg.text.toString()
            val password = binding.edtPasswordReg.text.toString()
            if (checkAllField()){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        auth.signOut()
                        Toast.makeText(this, "Account was created, let's go!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.e("error: ", it.exception.toString())
                    }
                }
            }
        }
    }

    private fun checkAllField() : Boolean {
        val email = binding.edtEmailReg.text.toString()
        if (binding.edtEmailReg.text.toString() == ""){
            binding.tvEmailReg.error = "This is Required Field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tvEmailReg.error = "Check Email format"
            return false
        }
        if (binding.edtPasswordReg.text.toString() == ""){
            binding.tvPasswordReg.error = "This is Required Field"
            return false
        }
        if (binding.edtPasswordReg.length() <= 8){
            binding.tvPasswordReg.error = "Password at least 8 character"
            return false
        }
        return true
    }
}