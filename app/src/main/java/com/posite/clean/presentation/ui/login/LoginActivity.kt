package com.posite.clean.presentation.ui.login

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.posite.clean.R
import com.posite.clean.databinding.ActivityLoginBinding
import com.posite.clean.presentation.base.BaseActivity
import com.posite.clean.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity :
    BaseActivity<LoginViewModelImpl, ActivityLoginBinding>(R.layout.activity_login) {
    override val viewModel: LoginViewModelImpl by viewModels()

    override fun initView() {
        binding.vm = viewModel

        viewModel.checkAutoLogin()
    }

    override fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.kakaoEvent.collect {
                    if (it) {
                        viewModel.getKakaoToken(this@LoginActivity)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.kakaoInfo.collect {
                    binding.userId.text = it.nickname
                    Glide.with(this@LoginActivity).load(it.profile).into(binding.profileImg)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.goMain.collect {
                    if (it) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

}