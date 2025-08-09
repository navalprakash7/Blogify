package com.example.blogify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogify.adapters.YourArticleAdapter
import com.example.blogify.models.BlogItemModel
import com.example.blogify.databinding.ActivityYourArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class YourArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourArticleBinding
    private lateinit var databaseReference: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var blogAdapter: YourArticleAdapter
    private val EDIT_BLOG_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backSavedButton.setOnClickListener{
            finish()
        }

        val currentUserId = auth.currentUser?.uid
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        blogAdapter = YourArticleAdapter(this, emptyList(),object : YourArticleAdapter.OnItemClickListener{
            override fun onEditClick(blogItem: BlogItemModel) {
                val intent = Intent(this@YourArticleActivity, EditBlogActivity::class.java)
                intent.putExtra("blogItem",blogItem)
                startActivityForResult(intent,EDIT_BLOG_REQUEST_CODE)
            }

            override fun onReadMoreClick(blogItem: BlogItemModel) {
                val intent = Intent(this@YourArticleActivity, ReadMoreActivity::class.java)
                intent.putExtra("blogItem",blogItem)
                startActivity(intent)
            }

            override fun onDeleteClick(blogItem: BlogItemModel) {
                deleteBlogPost(blogItem)
            }
        })
        recyclerView.adapter = blogAdapter

        databaseReference = FirebaseDatabase.getInstance().getReference("blogs")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blogSaveList = ArrayList<BlogItemModel>()
                for (postSnapshot in snapshot.children){
                    val blogsSaved = postSnapshot.getValue(BlogItemModel::class.java)
                    if (blogsSaved != null && currentUserId == blogsSaved.userId){
                        blogSaveList.add(blogsSaved)
                    }
                }
                blogSaveList.reverse()
                blogAdapter.setData(blogSaveList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@YourArticleActivity, "Error in loading saved blogs", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteBlogPost(blogItem: BlogItemModel) {
        val postId = blogItem.postId
        val blogPostReference = databaseReference.child(postId)
        blogPostReference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Blog Removed", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Error in removing", Toast.LENGTH_SHORT).show()
        }
    }
}
