package com.example.blogify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blogify.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("users")

        if (uid != null) {
            database.child(uid).child("name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.getValue(String::class.java)
                        binding.profileName.text = name
                    }

                    override fun onCancelled(error: DatabaseError) {
                        binding.profileName.text = "Failed to load name"
                    }
                })
        }

        binding.addArticleActionButton.setOnClickListener {
            startActivity(Intent(this, AddArticleActivity::class.java))
        }
        binding.yourArticleButton.setOnClickListener {
            startActivity(Intent(this, YourArticleActivity::class.java))
        }
        binding.savedButton.setOnClickListener {
            startActivity(Intent(this, SavedArticlesActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
