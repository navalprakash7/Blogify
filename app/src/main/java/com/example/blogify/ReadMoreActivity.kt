package com.example.blogify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blogify.models.BlogItemModel
import com.example.blogify.databinding.ActivityReadMoreBinding

class ReadMoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadMoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonRead.setOnClickListener{
            finish()
        }
        val blogItem = intent.getParcelableExtra<BlogItemModel>("blogItem")
        blogItem?.let {
            binding.headingRead.text = it.heading
            binding.userNameRead.text = it.userName
            binding.dateRead.text = it.date
            binding.postRead.text = it.post
        }
    }
}
