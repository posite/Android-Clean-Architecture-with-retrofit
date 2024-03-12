package com.posite.clean.presentation.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleClient: GoogleSignInClient

    override fun initView() {
        binding.vm = viewModel
        googleSignIn()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .requestProfile()
            .build()

        googleClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)
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
                viewModel.userInfo.collect {
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.naverEvent.collect {
                    if (it) {
                        viewModel.getNaverToken(this@LoginActivity)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.googleEvent.collect {
                    if (it) {
                        val signInIntent = googleClient.signInIntent
                        resultLauncher.launch(signInIntent)
                    }
                }
            }
        }

    }

    private fun googleSignIn() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSingIn(task)
                }
            }
    }

    private fun handleSingIn(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("google", account.displayName.toString())
            Log.d("google", account.photoUrl.toString())
            binding.userId.text = account.displayName
            Glide.with(this@LoginActivity).load(account.photoUrl).into(binding.profileImg)
            if (account.photoUrl != null) {
                viewModel.onGoogleLoginSuccess(
                    account.displayName.toString(),
                    account.photoUrl!!.path!!
                )
            } else {
                viewModel.onGoogleLoginSuccess(account.displayName.toString(), "")
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

}