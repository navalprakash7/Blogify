package com.example.blogify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogify.models.BlogItemModel
import com.example.blogify.adapters.BlogAdapter
import com.example.blogify.databinding.ActivitySavedArticlesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SavedArticlesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedArticlesBinding
    private lateinit var adapter: BlogAdapter
    private val savedList = mutableListOf<BlogItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backSavedButton.setOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Set to true to show save/unsave button and text
        adapter = BlogAdapter(savedList, showSaveButton = true)
        binding.recyclerView.adapter = adapter

        fetchSavedPosts()
    }

    private fun fetchSavedPosts() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val savedRef = FirebaseDatabase.getInstance().getReference("saved").child(userId)

        savedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                savedList.clear()
                for (child in snapshot.children) {
                    val model = child.getValue(BlogItemModel::class.java)
                    model?.let { savedList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
