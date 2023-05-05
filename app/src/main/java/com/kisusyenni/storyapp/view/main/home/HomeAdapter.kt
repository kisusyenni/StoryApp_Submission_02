package com.kisusyenni.storyapp.view.main.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kisusyenni.storyapp.R
import com.kisusyenni.storyapp.data.source.local.entity.Story
import com.kisusyenni.storyapp.databinding.ItemStoryBinding
import com.kisusyenni.storyapp.helper.formatDate
import com.kisusyenni.storyapp.view.detail.DetailActivity

class HomeAdapter :
    PagingDataAdapter<Story, HomeAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemStoryBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)

        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                tvItemName.text = story.name
                tvItemCreatedAt.text = story.createdAt?.let { formatDate(it) }
                tvItemDescription.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.preview)
                    .error(R.drawable.image_error)
                    .into(ivItemPhoto)

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "image"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemCreatedAt, "created_at"),
                            Pair(tvItemDescription, "description"),
                        )
                    val detailIntent = Intent(itemView.context, DetailActivity::class.java)
                    detailIntent.putExtra(DetailActivity.DETAIL_STORY, story)
                    itemView.context.startActivity(detailIntent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}