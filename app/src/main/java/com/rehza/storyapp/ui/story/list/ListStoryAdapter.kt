package com.rehza.storyapp.ui.story.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.rehza.storyapp.data.api.ListStoryItem
import com.rehza.storyapp.databinding.ItemStoryBinding
import com.bumptech.glide.Glide
import com.rehza.storyapp.R

class ListStoryAdapter(private val callback: (story: ListStoryItem) -> Unit) :
    RecyclerView.Adapter<ListStoryAdapter.ListStoryViewHolder>() {

    var list: List<ListStoryItem> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, callback)
    }

    override fun getItemCount(): Int = list.size

    class ListStoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem, callback: (story: ListStoryItem) -> Unit) {
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description

            val drawable = CircularProgressDrawable(binding.root.context)
            drawable.strokeWidth = 5f
            drawable.centerRadius = 30f
            drawable.start()

            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .placeholder(drawable)
                .into(binding.ivItemPhoto)

            val bundle = bundleOf(
                "storyName" to story.name,
                "storyDesc" to story.description,
                "storyId" to story.id,
                "storyPhotoUrl" to story.photoUrl
            )
            binding.cardView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_listStoryFragment_to_detailStoryFragment, bundle))
        }
    }
}
