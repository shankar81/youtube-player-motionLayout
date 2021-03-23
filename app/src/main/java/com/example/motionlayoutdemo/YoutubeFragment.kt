package com.example.motionlayoutdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment

private const val TAG = "YoutubeFragment"

class YoutubeFragment : Fragment() {
    private var progress = 0F
    private lateinit var mainLayout: MotionLayout
    private lateinit var headerLayout: MotionLayout
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_youtube, container, false)

        mainLayout = view.findViewById(R.id.motionLayout)
        headerLayout = view.findViewById(R.id.headerLayout)

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainLayout.addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                headerLayout.progress = p0?.progress ?: 0F
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                headerLayout.progress = p0?.progress ?: 0F
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                headerLayout.progress = p0?.progress ?: 0F
                progress = headerLayout.progress
                isScrolling = false
                if (mainLayout.progress > 0.8F) {
                    headerLayout.isClickable = true
                    Log.d(TAG, "onTransitionCompleted: OF ")
                } else {
                    headerLayout.isClickable = false
                    Log.d(TAG, "onTransitionCompleted: ELSE")
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                headerLayout.progress = p0?.progress ?: 0F
            }
        })

        headerLayout.setOnTouchListener { v, event ->
            if (progress < 0.8F || isScrolling) {
                return@setOnTouchListener false
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    headerLayout.isClickable = true
                    isScrolling = false
                    Log.d(
                        TAG,
                        "onViewCreated: DOWN ${MotionEvent.ACTION_DOWN} ${event.y} ${event.yPrecision} ${event.rawY} "
                    )
                }
                MotionEvent.ACTION_MOVE -> {
                    headerLayout.isClickable = false
                    isScrolling = true
                    Log.d(
                        TAG,
                        "onViewCreated: SCROLL ${MotionEvent.ACTION_MOVE} ${event.y} ${event.yPrecision} ${event.rawY} "
                    )
                    return@setOnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    if (!isScrolling) {
                        mainLayout.transitionToStart()
                        headerLayout.isClickable = false
                    }
                    isScrolling = false
                    Log.d(
                        TAG,
                        "onViewCreated: UP ${MotionEvent.ACTION_UP} ${event.y} ${event.yPrecision} ${event.rawY}  "
                    )
                }
            }
            progress > 0.8F
        }
    }

}