package com.example.androidfirebase227.crudOperations

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidfirebase227.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CRUDHome : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var createButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var readButton: Button
    private lateinit var resultTextView: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crudhome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userIdEditText = findViewById(R.id.userIdEditText)
        emailEditText = findViewById(R.id.emailEditText)
        nameEditText = findViewById(R.id.nameEditText)
        createButton = findViewById(R.id.createButton)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)
        readButton = findViewById(R.id.readButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance("https://androidfirebase-cse227-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

        createButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()

            if(userId != null && name.isNotEmpty() && email.isNotEmpty()){
                val user = User(userId, name, email)
                database.child(userId.toString()).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
        updateButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()

            if (userId != null && (name.isNotEmpty() || email.isNotEmpty())) {
                val updates = mutableMapOf<String, Any>()
                if (name.isNotEmpty()) updates["name"] = name
                if (email.isNotEmpty()) updates["email"] = email

                database.child(userId.toString()).updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()

            if (userId != null) {
                database.child(userId.toString()).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show()
            }
        }

        readButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()

            if (userId != null) {
                database.child(userId.toString()).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                resultTextView.setText("ID: ${user.userId}\nName: ${user.name}\nEmail: ${user.email}")
                            }
                        } else {
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Failed to read user: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}