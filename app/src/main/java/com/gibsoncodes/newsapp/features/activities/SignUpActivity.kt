package com.gibsoncodes.newsapp.features.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.gibsoncodes.newsapp.R
import com.gibsoncodes.newsapp.common.AppPreferences
import com.gibsoncodes.newsapp.common.CropCircleTransformation
import com.gibsoncodes.newsapp.common.ShowSnackbar
import com.gibsoncodes.newsapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val callBackInteger = 100
    private val appPreferences: AppPreferences by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(
            this@SignUpActivity,
            getSignInOptions()
        )
        Picasso.with(this@SignUpActivity)
            .load(R.drawable.splash_bg_one)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoOne)
        Picasso.with(this@SignUpActivity)
            .load(R.drawable.splash_bg_two)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoTwo)
        Picasso.with(this@SignUpActivity)
            .load(R.drawable.splash_bg_three)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoThree)

        Picasso.with(this@SignUpActivity)
            .load(R.drawable.splash_bg_four)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoFive)

        Picasso.with(this@SignUpActivity)
            .load(R.drawable.get_started_bg)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoFive)

        Picasso.with(this@SignUpActivity)
            .load(R.drawable.splash_bg_five)
            .fit()
            .transform(CropCircleTransformation())
            .centerCrop()
            .into(binding.logoFour)


     /* CoroutineScope(Dispatchers.Main).launch {

            kotlinx.coroutines.delay(4500L)
            binding.getStartedText.animate().alpha(0f).start()
            binding.signupBg.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.signupBg.visibility = View.GONE
                    binding.signUpLayout.animate().alpha(1f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                binding.signUpLayout.visibility = View.VISIBLE
                            }
                        })
                }
            })

        }*/
        binding.signInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        googleSignInClient.signInIntent.also { it ->
            startActivityForResult(it, callBackInteger)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == callBackInteger) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInTask(task)
        }
    }

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private fun handleSignInTask(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken)
        } catch (e: ApiException) {
            Timber.e("Failed to sign in: ${e.message}")
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Timber.d("Sign in  successful")
                    Timber.d("sign in successful ${user?.displayName} ${user?.email}")
                    ShowSnackbar(binding.signupcord,"sign in successful ${user?.displayName} ${user?.email}")
                    appPreferences.saveLoggedInStatus(AppPreferences.loggedInKey, true)
                    user?.displayName.let { name ->
                        if (name != null) {
                                appPreferences.savedLoggedInName(AppPreferences.loggedInName, name)
                            }
                        }
                    user?.email.let { email ->
                        {
                            when {
                                email != null  -> appPreferences.savedLoggedInEmail(
                                    AppPreferences.loggedInEmail,
                                    email
                                )

                            }
                        }
                    }

                    updateUi(user)
                }else{
                    ShowSnackbar(binding.signupcord,"failed to sign in try again later")
                    Timber.e("Sign in failed")
                    updateUi(null)
                }
            }

    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            binding.signInBtn.visibility = View.GONE
            val sourcesList= appPreferences.retrieveSourcesListStatus(AppPreferences.isSourcesListSaved)
            if (sourcesList){
                startActivity(Intent(this@SignUpActivity,MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SignUpActivity, NewsSources::class.java))
                finish()
            }

        }
    }

    override fun onStart() {
        super.onStart()
      //  auth.signOut()
       val currentUser = auth.currentUser
        updateUi(currentUser)
    }
}

