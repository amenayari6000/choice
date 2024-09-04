package com.walid580.choice

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI

class MainActivity2 : AppCompatActivity() {

    private lateinit var zoomInAnimation: ScaleAnimation
    private lateinit var zoomOutAnimation: ScaleAnimation
    private lateinit var textViewResponse: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // Initialize your UI components and setup animations and click listeners here.
        initializeUI()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeUI() {
        val scrollViewHome = findViewById<ScrollView>(R.id.schrollHome)

        scrollViewHome.setOnTouchListener { v, event ->
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

        // Initialize zoom in animation
        zoomInAnimation = ScaleAnimation(
            1.0f, 1.225f, // fromXScale, toXScale
            1.0f, 1.225f, // fromYScale, toYScale
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotXType, pivotXValue
            Animation.RELATIVE_TO_SELF, 0.5f // pivotYType, pivotYValue
        ).apply {
            duration = 300
        }

        // Initialize zoom out animation
        zoomOutAnimation = ScaleAnimation(
            1.225f, 1.0f, // fromXScale, toXScale
            1.225f, 1.0f, // fromYScale, toYScale
            Animation.RELATIVE_TO_SELF, 0.5f, // pivotXType, pivotXValue
            Animation.RELATIVE_TO_SELF, 0.5f // pivotYType, pivotYValue
        ).apply {
            duration = 300
        }

        textViewResponse = findViewById(R.id.textResponse)

        val responseMap = mapOf(
            1 to getString(R.string.response_1),
            2 to getString(R.string.response_2),
            3 to getString(R.string.response_3),
            4 to getString(R.string.response_4),
            5 to getString(R.string.response_5),
            6 to getString(R.string.response_6),
            7 to getString(R.string.response_7),
            8 to getString(R.string.response_8)
        )

        val textViewIds = listOf(
            R.id.textViewChoice1, R.id.textViewChoice2, R.id.textViewChoice3, R.id.textViewChoice4,
            R.id.textViewChoice5, R.id.textViewChoice6, R.id.textViewChoice7, R.id.textViewChoice8
        )

        textViewIds.forEach { textViewId ->
            val textView = findViewById<TextView>(textViewId)

            textView.setOnClickListener {
                textView.text.toString()
                val responseText = responseMap[textViewIds.indexOf(textViewId) + 1] ?: ""

                textViewResponse.text = responseText

                // Apply zoom in animation
                textView.startAnimation(zoomInAnimation)

                // Delay zoom out animation
                textView.postDelayed({
                    // Apply zoom out animation
                    textView.startAnimation(zoomOutAnimation)
                }, 200) // Adjust the delay as needed
            }
        }

        val logoutBtn: Button = findViewById(R.id.idBtnLogout)
        logoutBtn.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this@MainActivity2)
                .addOnCompleteListener {
                    Toast.makeText(this@MainActivity2, "User Signed Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity2, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }



}
