package com.example.semester_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


// The activity to handle user registration
class RegisterActivity:Activity(){
    lateinit var registerBut: Button
    lateinit var clearBut: Button
    lateinit var passwordView: EditText
    lateinit var emailView: EditText
    private var validator = Validators()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        clearBut = findViewById(R.id.clearBtn)
        registerBut = findViewById(R.id.registerBtn)
        passwordView = findViewById(R.id.password)
        emailView = findViewById(R.id.email)

        registerBut.setOnClickListener{ registerNewUser() }

        clearBut.setOnClickListener{
            passwordView.setText("")
            emailView.setText("")
        }
        mAuth = FirebaseAuth.getInstance()
    }

    private fun registerNewUser() {

        val email: String = emailView.text.toString()
        val password: String = passwordView.text.toString()

        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...", Toast.LENGTH_SHORT).show()
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(applicationContext, "Please enter a valid password!", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val tmpIntent = Intent()
                    setResult(RESULT_OK, tmpIntent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }
}