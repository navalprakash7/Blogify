package com.example.blogify.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.blogify.models.BlogItemModel
import com.example.blogify.R
import com.example.blogify.ReadMoreActivity
import com.example.blogify.databinding.BlogItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BlogAdapter(
    private var items: MutableList<BlogItemModel>,
    private val showSaveButton: Boolean
) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding = BlogItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: BlogItemModel) {
            val context = binding.root.context

            binding.heading.text = model.heading
            binding.userName.text = model.userName
            binding.date.text = model.date
            binding.post.text = model.post

            binding.readButton.setOnClickListener {
                val intent = Intent(context, ReadMoreActivity::class.java)
                intent.putExtra("blogItem", model)
                context.startActivity(intent)
            }

            if (showSaveButton) {
                binding.saveButton.visibility = View.VISIBLE
                binding.saveUnsaveText.visibility = View.VISIBLE

                val auth = FirebaseAuth.getInstance()
                val savedRef = FirebaseDatabase.getInstance().getReference("saved")
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val postId = model.postId
                    val savePath = savedRef.child(userId).child(postId)

                    // Set initial icon and text
                    savePath.get().addOnSuccessListener {
                        if (it.exists()) {
                            binding.saveButton.setImageResource(R.drawable.saved)
                            binding.saveUnsaveText.text = "UNSAVE"
                        } else {
                            binding.saveButton.setImageResource(R.drawable.unsaved)
                            binding.saveUnsaveText.text = "SAVE"
                        }
                    }

                    // Toggle on click
                    binding.saveButton.setOnClickListener {
                        savePath.get().addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                savePath.removeValue()
                                binding.saveButton.setImageResource(R.drawable.unsaved)
                                binding.saveUnsaveText.text = "SAVE"
                                Toast.makeText(context, "Removed from Saved", Toast.LENGTH_SHORT).show()
                            } else {
                                savePath.setValue(model)
                                binding.saveButton.setImageResource(R.drawable.saved)
                                binding.saveUnsaveText.text = "UNSAVE"
                                Toast.makeText(context, "Added to Saved", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                binding.saveButton.visibility = View.GONE
                binding.saveUnsaveText.visibility = View.GONE
            }
        }
    }
}
