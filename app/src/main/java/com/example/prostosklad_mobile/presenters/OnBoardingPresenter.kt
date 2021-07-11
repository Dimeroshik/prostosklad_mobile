package com.example.prostosklad_mobile.presenters

import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.views.OnBoardingView
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject


@InjectViewState
class OnBoardingPresenter @Inject constructor(): MvpPresenter<OnBoardingView>() {

    private var content: MutableList<Triple<Int, Int, Int>> = mutableListOf(
        Triple(
            R.drawable.img1_onboarding, R.string.onboarding_item_header1, R.string.onboarding_item_text1),
        Triple(
            R.drawable.img2_onboarding, R.string.onboarding_item_header2, R.string.onboarding_item_header2),
        Triple(
            R.drawable.img3_onboarding, R.string.onboarding_item_header3, R.string.onboarding_item_header3)
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setAdapter()
    }

    fun setAdapter(){
        viewState.setAdapter(content)
    }}