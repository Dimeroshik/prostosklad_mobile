package com.example.prostosklad_mobile

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import com.example.prostosklad_mobile.databinding.ActivityMainBinding
import com.example.prostosklad_mobile.fragments.OnBoardingFragment
import com.example.prostosklad_mobile.fragments.SignInFragment
import com.example.prostosklad_mobile.fragments.VerificationFragment


class MainActivity : AppCompatActivity() {

    private lateinit var activity: ActivityMainBinding

    private val onBoardingFragment: OnBoardingFragment by lazy{ OnBoardingFragment() }
    private val signInFragment: SignInFragment by lazy { SignInFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        setContentView(activity.root)
    }

    override fun onResume() {
        addFragment()
        super.onResume()
    }

    fun addFragment(){
        var transaction = supportFragmentManager.beginTransaction()

        transaction.add(activity.containterMain.id, onBoardingFragment, OnBoardingFragment.TAG)

        transaction.commit()
    }

    fun openAuthorization(){
        var transaction = supportFragmentManager.beginTransaction()

        transaction.replace(activity.containterMain.id, signInFragment, OnBoardingFragment.TAG)

        transaction.commit()
    }

    fun openVerification(phone: String){
        var arguments = Bundle()
        arguments.putString("phone", phone)

        var fragment = VerificationFragment()
        fragment.arguments = arguments

        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(activity.containterMain.id, fragment, VerificationFragment.TAG)

        transaction.commit()
    }


}