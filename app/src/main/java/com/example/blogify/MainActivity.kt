package com.example.blogify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.SearchView
import com.example.blogify.models.BlogItemModel
import com.example.blogify.adapters.BlogAdapter
import com.example.blogify.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BlogAdapter
    private val blogList = mutableListOf<BlogItemModel>()
    private val filteredList = mutableListOf<BlogItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.savedImage.setOnClickListener {
            startActivity(Intent(this, SavedArticlesActivity::class.java))
        }
        binding.addArticleActionButton.setOnClickListener {
            startActivity(Intent(this, AddArticleActivity::class.java))
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BlogAdapter(filteredList, showSaveButton = true)
        binding.recyclerView.adapter = adapter

        fetchBlogPosts()
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        fetchBlogPosts()
    }

    private fun fetchBlogPosts() {
        val dbRef = FirebaseDatabase.getInstance().getReference("blogs")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                blogList.clear()
                for (child in snapshot.children) {
                    val blog = child.getValue(BlogItemModel::class.java)
                    blog?.let { blogList.add(it) }
                }
                blogList.reverse()
                filteredList.clear()
                filteredList.addAll(blogList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        val searchText = query?.trim()?.lowercase() ?: ""
        filteredList.clear()
        if (searchText.isEmpty()) {
            filteredList.addAll(blogList)
        } else {
            filteredList.addAll(
                blogList.filter { it.userName?.lowercase()?.contains(searchText) == true }
            )
        }
        adapter.notifyDataSetChanged()
    }
}
