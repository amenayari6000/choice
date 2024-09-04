package com.walid580.choice





import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private lateinit var signInButton: Button

    private val mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser

        if (user != null) {
            val intent = Intent(this@MainActivity,MainActivity2::class.java)
            startActivity(intent)
            finish()
        } else {
            // Find the sign-in button by its ID
            signInButton = findViewById(R.id.sign_in)

            // Set click listener for the sign-in button

            signInButton.setOnClickListener {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .build()

                signInLauncher.launch(signInIntent)
            }
        }
    }

    private val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build()
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

















        val scrollMain: ScrollView =findViewById(R.id.shrollmain)
        scrollMain.setOnTouchListener { v, event ->
            if (event.pointerCount > 1) {

                return@setOnTouchListener true
            }
            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                    v.scaleX = 1f
                    v.scaleY = 1f
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.historySize > 0) {

                        val deltaX = event.x - event.getHistoricalX(0)
                        val deltaY = event.y - event.getHistoricalY(0)

                        v.scaleX += deltaX / v.width
                        v.scaleY += deltaY / v.height
                    }
                }
            }
            false
        }

        val heartImageView: ImageView = findViewById(R.id.ic_Anim)

        val fadeOutAnimator = ObjectAnimator.ofFloat(heartImageView, "alpha", 1.0f, 0.0f)
        fadeOutAnimator.duration = 1000
        fadeOutAnimator.repeatMode = ObjectAnimator.REVERSE
        fadeOutAnimator.repeatCount = ObjectAnimator.INFINITE

        val fadeInAnimator = ObjectAnimator.ofFloat(heartImageView, "alpha", 0.0f, 1.0f)
        fadeInAnimator.duration = 1000
        fadeInAnimator.repeatMode = ObjectAnimator.REVERSE
        fadeInAnimator.repeatCount = ObjectAnimator.INFINITE

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(fadeOutAnimator, fadeInAnimator)
        animatorSet.start()



        val heartImageView1: ImageView = findViewById(R.id.ic_Anim1)

        val fadeOutAnimator1 = ObjectAnimator.ofFloat(heartImageView1, "alpha", 1.0f, 0.0f)
        fadeOutAnimator1.duration = 1000
        fadeOutAnimator1.repeatMode = ObjectAnimator.REVERSE
        fadeOutAnimator1.repeatCount = ObjectAnimator.INFINITE

        val fadeInAnimator1 = ObjectAnimator.ofFloat(heartImageView, "alpha", 0.0f, 1.0f)
        fadeInAnimator1.duration = 1000
        fadeInAnimator1.repeatMode = ObjectAnimator.REVERSE
        fadeInAnimator1.repeatCount = ObjectAnimator.INFINITE

        val animatorSet1 = AnimatorSet()
        animatorSet1.playSequentially(fadeOutAnimator1, fadeInAnimator1)
        animatorSet1.start()
















        mFirebaseAuth = FirebaseAuth.getInstance()

        signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            onSignInResult(result)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
      val  progress_sign_in:ProgressBar=findViewById(R.id.progress_sign_in)
        progress_sign_in.visibility=View.VISIBLE
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            progress_sign_in.visibility=View.INVISIBLE
            // Authentication successful, redirect to home screen
            val intent = Intent(this@MainActivity,MainActivity2::class.java)
            startActivity(intent)
            finish()
        } else {
            progress_sign_in.visibility=View.INVISIBLE
            // Handle authentication failure
            if (response != null) {
                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showToast("No network. Please connect to the internet.")
                } else if (response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showToast("Registration error. Please try again.")
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    public override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }


    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
