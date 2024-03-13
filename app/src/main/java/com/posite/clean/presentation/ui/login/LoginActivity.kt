package com.posite.clean.presentation.ui.login

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var auth: FirebaseAuth

    override fun initView() {
        binding.vm = viewModel
        googleSignIn()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .requestIdToken(getString(R.string.google_client_id))
            .requestProfile()
            .build()

        googleClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)
        auth = FirebaseAuth.getInstance()
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
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val task: Task<GoogleSignInAccount> =
//                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                    handleSingIn(task)
//                }
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
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
                    account.photoUrl!!.toString()
                )
            } else {
                viewModel.onGoogleLoginSuccess(account.displayName.toString(), "")
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential((credential)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("firebase", task.result.user!!.displayName.toString())
                Log.d("firebase", task.result.user!!.photoUrl.toString())
                binding.userId.text = task.result.user!!.displayName
                Glide.with(this@LoginActivity).load(task.result.user!!.photoUrl)
                    .into(binding.profileImg)
                if (task.result.user!!.photoUrl != null) {
                    viewModel.onGoogleLoginSuccess(
                        task.result.user!!.displayName.toString(),
                        task.result.user!!.photoUrl!!.toString()
                    )
                } else {
                    viewModel.onGoogleLoginSuccess(task.result.user!!.displayName.toString(), "")
                }
            }
        }
    }

    companion object {
        private const val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
        private const val RC_SIGN_IN = 9001
    }
}