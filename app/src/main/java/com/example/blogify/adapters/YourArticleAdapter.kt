package com.example.blogify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogify.models.BlogItemModel
import com.example.blogify.databinding.YourArticleItemBinding


class YourArticleAdapter (
    private val context: Context,
    private var blogList: List<BlogItemModel>,
    private val itemClickListener: OnItemClickListener
):RecyclerView.Adapter<YourArticleAdapter.BlogViewHolder>(){

    interface OnItemClickListener{
        fun onEditClick(blogItem: BlogItemModel)
        fun onReadMoreClick(blogItem: BlogItemModel)
        fun onDeleteClick(blogItem: BlogItemModel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = YourArticleItemBinding.inflate(inflater,parent,false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blogItem = blogList[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    fun setData(blogSaveList: ArrayList<BlogItemModel>) {
        this.blogList = blogSaveList
        notifyDataSetChanged()
    }

    inner class BlogViewHolder(private val binding: YourArticleItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItem: BlogItemModel) {
            binding.heading.text = blogItem.heading
            binding.userName.text = blogItem.userName
            binding.date.text = blogItem.date
            binding.post.text = blogItem.post

            binding.readButton.setOnClickListener{
                itemClickListener.onReadMoreClick(blogItem)
            }
            binding.editButton.setOnClickListener{
                itemClickListener.onEditClick(blogItem)
            }
            binding.deleteButton.setOnClickListener{
                itemClickListener.onDeleteClick(blogItem)
            }
        }

    }
}