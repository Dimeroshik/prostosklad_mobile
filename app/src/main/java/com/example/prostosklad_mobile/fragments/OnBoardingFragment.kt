package com.example.prostosklad_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.prostosklad_mobile.base.BaseFragment
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.adapters.OnBoardingAdapter
import com.example.prostosklad_mobile.databinding.FragmentOnboardingBinding
import com.example.prostosklad_mobile.presenters.OnBoardingPresenter
import com.example.prostosklad_mobile.views.OnBoardingView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class OnBoardingFragment: BaseFragment(), OnBoardingView {

    private var binding: FragmentOnboardingBinding by viewLifecycle()

    @Inject
    lateinit var presenterProvider: Provider<OnBoardingPresenter>

    private val onBoardingPresenter: OnBoardingPresenter by moxyPresenter { presenterProvider.get() }

    private lateinit var adapter: OnBoardingAdapter

    companion object {
        var TAG = "OnBoardingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)

        setInset(binding.root, bottom = true)
        initViews()

        return binding.root
    }

    private fun initViews(){
        binding.apply {
            adapter = OnBoardingAdapter()
            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    if(position == 2) swapVisibility(View.GONE)
                    else swapVisibility(View.VISIBLE)
                }
            })

            buttonNext.setOnClickListener {
                val current = binding.viewPager.currentItem
                binding.viewPager.setCurrentItem(current+1, true)
            }

            buttonSkip.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_onBoardingFragment_to_signInFragment)
            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, _ ->
                tab.setCustomView(R.layout.item_onboarding_tablayout)
                tab.view.isClickable = false
            }.attach()
        }
    }

    override fun setAdapter(contentList: List<Triple<Int, Int, Int>>){
        val newList = mutableListOf<Triple<Int, String?, String?>>()
        for (i in contentList) newList.add(Triple(i.first, context?.getString(i.second),
            context?.getString(i.third))
        )
        adapter.content = newList
    }

    fun swapVisibility(state: Int){
        if (binding.buttonNext.visibility != state){
            binding.buttonNext.visibility = state
        }
    }
}