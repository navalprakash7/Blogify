package com.example.blogify

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogify.models.BlogItemModel
import com.example.blogify.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAddButton.setOnClickListener {
            finish()
        }

        binding.postButton.setOnClickListener {
            val heading = binding.headingInput.text.toString().trim()
            val post = binding.postInput.text.toString().trim()
            val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "UnknownUser"

            if (heading.isNotEmpty() && post.isNotEmpty()) {
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                userRef.get().addOnSuccessListener { snapshot ->
                    val userName = snapshot.child("name").value?.toString() ?: "Unknown User"

                    val ref = FirebaseDatabase.getInstance().getReference("blogs")
                    val postId = ref.push().key!!

                    val model = BlogItemModel(
                        heading = heading,
                        post = post,
                        userName = userName,
                        date = date,
                        postId = postId,
                        userId = userId
                    )

                    ref.child(postId).setValue(model).addOnSuccessListener {
                        Toast.makeText(this, "Post uploaded", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch user name", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
