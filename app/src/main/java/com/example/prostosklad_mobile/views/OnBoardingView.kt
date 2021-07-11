package com.example.prostosklad_mobile.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface OnBoardingView: MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setAdapter(list: List<Triple<Int, Int, Int>>)
}