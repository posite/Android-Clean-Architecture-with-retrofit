package com.posite.clean.presentation.ui.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.posite.clean.R
import com.posite.clean.databinding.ActivityMainBinding
import com.posite.clean.presentation.base.BaseActivity
import com.posite.clean.presentation.ui.detail.UserInfoActivity
import com.posite.clean.presentation.ui.main.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainViewModelImpl, ActivityMainBinding>(R.layout.activity_main) {
    override val viewModel: MainViewModelImpl by viewModels()
    private val adapter by lazy {
        UserListAdapter(this) {
            val intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("id", it.toLong())
            startActivity(intent)
        }
    }
    private var page = 1L
    override fun initView() {
        viewModel.getUserList(page)
        binding.userList.adapter = adapter
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userList.collect {
                    adapter.submitList(it.user)
                    page++
                }
            }
        }
    }

}