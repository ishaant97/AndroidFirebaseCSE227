package com.example.androidfirebase227.advancedCRUD

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidfirebase227.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReadDataFromFirebase : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var readFirebaseButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_read_data_from_firebase)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userIdEditText = findViewById(R.id.idEditText)
        readFirebaseButton = findViewById(R.id.readFirebaseButton)
        resultTextView = findViewById(R.id.resultTextView)

        database = FirebaseDatabase.getInstance("https://androidfirebase-cse227-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("students")


        readFirebaseButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()
            if (userId != null) {
                database.child(userId.toString()).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val student = snapshot.getValue(Student::class.java)
                            if (student != null) {
                                resultTextView.setText("ID: ${student.userId}\nName: ${student.name}\nClass: ${student.className}\nRoll Number: ${student.rollNumber}\nSection: ${student.section}\nAddress: ${student.address}\nPhone Number: ${student.phoneNumber}\nUniversity: ${student.university}")
                                }
                        } else {
                            resultTextView.setText("User not found")
                        }
                    }
                    .addOnFailureListener {
                        resultTextView.setText("Failed to read data: ${it.message}")
                    }
                }
            else {
                Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}