package com.example.prostosklad_mobile.views

import com.example.prostosklad_mobile.fragments.VerificationFragment
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface VerificationView: MvpView {

    @AddToEndSingle
    fun setSpan(state: VerificationFragment.SpanStates)
}