package com.example.prostosklad_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.prostosklad_mobile.MainActivity
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.adapters.OnBoardingAdapter
import com.example.prostosklad_mobile.databinding.FragmentOnboardingBinding

class OnBoardingFragment: Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    companion object {
        var TAG = "OnBoardingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root){
                view, insets ->
            view.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        initViews()

        return binding.root
    }

    fun initViews(){
        binding.apply {
            viewPager.adapter = OnBoardingAdapter(onBoardingCaller)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    if(position == 2) swapVisibility(View.GONE)
                    else swapVisibility(View.VISIBLE)
                }
            })

            buttonNext.setOnClickListener {
                var current = binding.viewPager.currentItem
                binding.viewPager.setCurrentItem(current+1, true)
            }

            buttonSkip.setOnClickListener {
                (activity as MainActivity).openAuthorization()
            }

            circleIndicator.setViewPager(binding.viewPager)
        }
    }

    var onBoardingCaller = object : OnBoardingAdapter.OnBoardingCaller {
        override fun getStringResource(id: Int): String = context?.getString(id) ?: ""
    }

    fun swapVisibility(state: Int){
        if (binding.buttonNext.visibility != state){
            binding.buttonNext.visibility = state
        }
    }
}