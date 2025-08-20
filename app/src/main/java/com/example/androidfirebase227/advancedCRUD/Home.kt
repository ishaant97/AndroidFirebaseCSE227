package com.example.androidfirebase227.advancedCRUD

import android.content.Intent
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

class Home : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var classEditText: EditText
    private lateinit var rollNumberEditText: EditText
    private lateinit var sectionEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var universityEditText: EditText
    private lateinit var postFirebaseButton: Button
    private lateinit var readFirebaseButton: Button
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userIdEditText = findViewById(R.id.userIdEditText)
        nameEditText = findViewById(R.id.nameEditText)
        classEditText = findViewById(R.id.classEditText)
        rollNumberEditText = findViewById(R.id.rollNumberEditText)
        sectionEditText = findViewById(R.id.sectionEditText)
        addressEditText = findViewById(R.id.addressEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        universityEditText = findViewById(R.id.universityEditText)
        postFirebaseButton = findViewById(R.id.postFirebaseButton)
        readFirebaseButton = findViewById(R.id.readFirebaseButton)

        database = FirebaseDatabase.getInstance("https://androidfirebase-cse227-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("students")

        postFirebaseButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()
            val name = nameEditText.text.toString()
            val className = classEditText.text.toString()
            val rollNumber = rollNumberEditText.text.toString().toIntOrNull()
            val section = sectionEditText.text.toString()
            val address = addressEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString().toLongOrNull()
            val university = universityEditText.text.toString()
            if (userId != null && name.isNotEmpty() && className.isNotEmpty() && rollNumber != null && section.isNotEmpty() && address.isNotEmpty() && phoneNumber != null && university.isNotEmpty()) {
                val student = Student(userId, name, className, rollNumber, section, address, phoneNumber, university)
                database.child(userId.toString()).setValue(student)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data posted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to post data: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        readFirebaseButton.setOnClickListener {
            startActivity(Intent(this, ReadDataFromFirebase::class.java))
        }
    }
}