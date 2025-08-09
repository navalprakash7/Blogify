package com.example.blogify

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogify.models.BlogItemModel
import com.example.blogify.databinding.ActivityEditBlogBinding
import com.google.firebase.database.FirebaseDatabase

class EditBlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAddButton.setOnClickListener{
            finish()
        }
        val blogItemModel = intent.getParcelableExtra<BlogItemModel>("blogItem")

        binding.headingInput.setText(blogItemModel?.heading)
        binding.postInput.setText(blogItemModel?.post)
        binding.saveButton.setOnClickListener {
            val updatedTitle = binding.headingInput.text.toString().trim()
            val updatedDescription = binding.postInput.text.toString().trim()
            if (updatedTitle.isEmpty() || updatedDescription.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }else{
                blogItemModel?.heading = updatedTitle
                blogItemModel?.post = updatedDescription
                if (blogItemModel != null){
                    updateDataInFirebase(blogItemModel)
                }
            }
        }

    }

    private fun updateDataInFirebase(blogItemModel: BlogItemModel) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("blogs")
        val postId = blogItemModel.postId
        databaseReference.child(postId).setValue(blogItemModel).addOnSuccessListener {
            Toast.makeText(this, "Blog Updated Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Error in Updating", Toast.LENGTH_SHORT).show()
        }
    }
}
