package com.posite.clean.presentation.ui.single

import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.posite.clean.R
import com.posite.clean.databinding.ActivityUserInfoBinding
import com.posite.clean.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoActivity :
    BaseActivity<UserInfoViewModelImpl, ActivityUserInfoBinding>(R.layout.activity_user_info) {
    override val viewModel: UserInfoViewModelImpl by viewModels()

    override fun initView() {
        binding.vm = viewModel
        val id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            viewModel.getSingeUserInfo(id)
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData.collect {
                    Glide.with(this@UserInfoActivity).load(it.avatar).into(binding.profileImg)
                }
            }
        }
    }
}