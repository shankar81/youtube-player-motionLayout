package com.example.motionlayoutdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment

private const val TAG = "YoutubeFragment"

class YoutubeFragment : Fragment() {

    var progress = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_youtube, container, false)

        val ml = view.findViewById<MotionLayout>(R.id.headerLayout)
        val ml2 = view.findViewById<MotionLayout>(R.id.motionLayout)
        ml2.addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                ml.progress = p0?.progress ?: 0F
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                ml.progress = p0?.progress ?: 0F
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                ml.progress = p0?.progress ?: 0F
                progress = ml.progress
                if (ml2.progress > 0.8F) {
                    ml.isClickable = true
                    ml.setOnClickListener {
                        ml2.transitionToStart()
                    }
                    Log.d(TAG, "onTransitionCompleted: OF ")
                } else {
                    ml.setOnClickListener(null)
                    ml.isClickable = false
                    Log.d(TAG, "onTransitionCompleted: ELSE")
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                ml.progress = p0?.progress ?: 0F
            }
        })



        return view
    }
}