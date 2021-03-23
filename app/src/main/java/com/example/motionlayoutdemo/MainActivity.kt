package com.example.motionlayoutdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout

private const val TAG = "MotionLayoutListener"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ml = findViewById<MotionLayout>(R.id.headerLayout)
        findViewById<MotionLayout>(R.id.motionLayout).addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                ml.progress = p0?.progress ?: 0F
                Log.d(TAG, "onTransitionStarted: ${p0?.progress} $p1 $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                ml.progress = p0?.progress ?: 0F
                Log.d(TAG, "onTransitionChange: ${p0?.progress} $p1 $p2 $p3")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                ml.progress = p0?.progress ?: 0F
                Log.d(TAG, "onTransitionCompleted: ${p0?.progress} $p1")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                ml.progress = p0?.progress ?: 0F
                Log.d(TAG, "onTransitionTrigger: ${p0?.progress} $p1 $p2 $p3")
            }

        })
        Log.d(TAG, "onCreate: Progress ${findViewById<MotionLayout>(R.id.motionLayout).progress}")
    }
}