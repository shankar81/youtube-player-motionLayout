package com.example.motionlayoutdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
    private var rect: Rect? = null

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

        // Remove Touch Listener from headerLayout
        // Instead add it to whole parent and check if user touched inside the header
        // If user touched inside header and moved then start moving else do nothing
        // Also check the onClick from parent only by checking the rect
        headerLayout.setOnTouchListener { v, event ->
            // If already full screen
            // Then don't allow touch event
            if (progress < 0.8F) {
                return@setOnTouchListener false
            }

            Log.d(TAG, "EVENT MOTION: ${event.action} ${event.actionMasked}")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    headerLayout.isClickable = true
                    isScrolling = false
                    rect = Rect(v.left, v.top, v.right, v.bottom)
                }
                MotionEvent.ACTION_MOVE -> {
                    val noNullRect = rect
                    // As user moves the finger change the progress of MotionLayout
                    headerLayout.isClickable = false
                    isScrolling = true
                    progress = (event.rawY / resources.displayMetrics.heightPixels) - 0.1F
                    mainLayout.progress = progress

                    if (noNullRect != null && !noNullRect.contains(
                            v.left + event.x.toInt(),
                            v.top + event.y.toInt()
                        )
                    ) {
                        // User moved outside bounds
                        if (progress < 0.5F) {
                            mainLayout.transitionToStart()
                        } else {
                            mainLayout.transitionToEnd()
                        }
                    }
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    Log.d(TAG, "onViewCreated: $progress $isScrolling")
                    if (!isScrolling) {
                        mainLayout.transitionToStart()
                    } else if (progress < 0.5F) {
                        mainLayout.transitionToStart()
                    } else {
                        mainLayout.transitionToEnd()
                    }
                    isScrolling = false

                }

            }
            progress > 0.8F
        }
    }

    private fun getStatusBarHeight(resources: Resources): Int {
        var height = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = resources.getDimensionPixelOffset(resourceId)
        }

        Log.d(TAG, "getStatusBarHeight: $height")
        return height
    }

    private fun getActionBarHeight(context: Context): Int {
        val tv = TypedValue()
        context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
        val height = context.resources.getDimensionPixelOffset(tv.resourceId)
        Log.d(TAG, "getActionBarHeight: $height")
        return height
    }

}