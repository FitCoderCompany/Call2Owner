package com.call2owner.utils.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView

object Animation {


    fun crossFade(toVisible: View, toInvisible: View) {
        val shortAnimationDuration = 20
        toVisible.alpha = 0f
        toVisible.visibility = View.VISIBLE
        toVisible.animate()
            .alpha(1f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(null)
        toInvisible.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    toInvisible.visibility = View.GONE
                }
            })
    }

    fun crossSide(toVisible: View, toInvisible: View) {
        val shortAnimationDuration = 20
        toVisible.translationX = 0f
        toVisible.visibility = View.VISIBLE
        toVisible.animate()
            .alpha(1f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(null)

        toInvisible.animate()
            .translationX(1000f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    toInvisible.visibility = View.GONE
                }
            })
    }

    fun fadeOut(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    fun View.fadeIn() {
        val animator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 1500
        animator.start()
    }

    fun fadeIn(view: View,timer:Long) {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        animator.interpolator = LinearInterpolator()
        animator.duration = timer
        animator.start()

//        view.animate()
//            .alpha(1f)
//            .setDuration(timer)
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {}
//            })
    }

    fun slideIn(view: View?) {
        val animator = ObjectAnimator.ofFloat(view, "translationX", -2000f, 0f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 400
        animator.start()
    }

    fun slideDown(view: View?) {
        val animator = ObjectAnimator.ofFloat(view, "translationY", -2000f, 0f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 400
        animator.start()
    }

    fun slideInAndVisible(view: View) {
        view.visibility = View.VISIBLE
        val animator = ObjectAnimator.ofFloat(view, "translationX", -2000f, 0f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 400
        animator.start()
    }

    fun slideUpVisible(view: View){
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(-40f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                }
            })
    }

    fun scaleUp(view: View){
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.INVISIBLE
                }
            })
    }




    fun slideDownInVisible(view: View){
        view.animate()
            .translationY(60f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.INVISIBLE
                }
            })
    }

    fun slideUpInVisible(view: View){
        view.animate()
            .translationY(-8000f)
            .alpha(0f)
            .setDuration(1000)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
//                    view.visibility = View.INVISIBLE
                }
            })
    }

    fun slideDownVisible(view: View){
//        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                }
            })
    }

    fun rotate(view: View,angle:Float){
        view.animate()
            .rotation(angle)
            .setDuration(200)
            .setInterpolator(LinearInterpolator())
    }


    fun slideOutAndInvisible(view: View) {
        view.animate()
            .translationX(2000f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.INVISIBLE
                }
            })
    }

    fun verticalShrink(view: RecyclerView, scrim: View) {
        view.pivotY = 0f
        view.animate()
            .scaleY(0f)
            .alpha(0f)
            .setDuration(200)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })

        scrim.animate()
            .alpha(0f)
            .setDuration(200)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    scrim.visibility = View.GONE
                }
            })

    }

    fun verticalExpand(view: RecyclerView, scrim: View) {
        view.visibility = View.VISIBLE
        view.pivotY = 0f
        view.animate()
            .scaleY(1f)
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.VISIBLE
                }
            })

        scrim.visibility = View.VISIBLE
        scrim.animate()
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(LinearInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    scrim.visibility = View.VISIBLE
                }
            })
    }
}