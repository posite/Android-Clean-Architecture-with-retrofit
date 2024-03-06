package com.posite.clean.presentation.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.databinding.ItemUserBinding
import dagger.hilt.android.qualifiers.ActivityContext

class UserListAdapter(
    @ActivityContext private val context: Context,
    private val goDetail: (Int) -> Unit,
) :
    ListAdapter<UserDto, UserListAdapter.ItemViewHolder>(differ) {
        
    inner class ItemViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserDto) {
            Glide.with(context).load(data.avatar).into(binding.profileImg)
            binding.emailTv.text = data.email
            binding.nameTv.text =
                StringBuilder().append(data.last_name).append(" ").append(data.first_name)
                    .toString()
            binding.root.setOnClickListener {
                goDetail(data.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<UserDto>() {
            override fun areItemsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
                return oldItem == newItem
            }
        }
    }
}