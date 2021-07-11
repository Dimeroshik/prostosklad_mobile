package com.example.prostosklad_mobile.base

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.widget.Toast
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import moxy.MvpAppCompatFragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BaseFragment: MvpAppCompatFragment() {

    var posBottom = 0
    var posTop = 0

    override fun onDestroy() {
        super.onDestroy()
    }

//    fun setInset(view: View, top: Boolean = false, bottom: Boolean = false){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//
//            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
//            posTop = if (top) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top else 0
//            posBottom = if (bottom) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0
//
//            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                updateMargins(
//                    top = posTop,
//                    bottom = posBottom)
//            }
//
//            insets
//        }
//            val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
//                override fun onProgress(insets: WindowInsets, animations: MutableList<WindowInsetsAnimation>): WindowInsets {
//                    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                        updateMargins(
//                            top = posTop
//                            bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//                        )
//                    }
//                    return insets
//                }
//            }
//
//            view.setWindowInsetsAnimationCallback(cb)
//        } else {
//
//        }
//    }

    fun setInset(view: View, top: Boolean = false, bottom: Boolean = false){

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            posTop = if (top) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top else 0
            posBottom = if (bottom) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                updateMargins(
                    top = posTop,
                    bottom = posBottom)
            }

            insets
        }
    }

    fun setKeyboardInsetForButton(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(insets: WindowInsets, animations: MutableList<WindowInsetsAnimation>): WindowInsets {
                    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        updateMargins(
                            top = posTop,
                            bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - posBottom
                        )
                    }

                    return insets
                }
            }

            view.setWindowInsetsAnimationCallback(cb)
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    updateMargins(
                        bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - posBottom)
                }
                insets
            }
        }
    }

    fun <T> Fragment.viewLifecycle(): ReadWriteProperty<Fragment, T> =
        object: ReadWriteProperty<Fragment, T>, LifecycleObserver {

            private var binding: T? = null

            init {
                this@viewLifecycle
                    .lifecycle
                    .addObserver(this)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                binding = null
                Log.d("TEST", "binding destroyed")
                Toast.makeText(context, "binding destroyed", Toast.LENGTH_SHORT).show()
            }

            override fun getValue(
                thisRef: Fragment,
                property: KProperty<*>
            ): T {
                return this.binding!!
            }
            override fun setValue(
                thisRef: Fragment,
                property: KProperty<*>,
                value: T
            ) {
                this.binding = value
            }
        }
}