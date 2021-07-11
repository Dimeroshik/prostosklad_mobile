package com.example.prostosklad_mobile.presenters

import com.example.prostosklad_mobile.fragments.VerificationFragment.SpanStates
import com.example.prostosklad_mobile.views.VerificationView
import moxy.MvpPresenter
import javax.inject.Inject

class VerificationPresenter @Inject constructor(): MvpPresenter<VerificationView>() {

    override fun onFirstViewAttach() {
        changeSpanState(SpanStates.Start)
        super.onFirstViewAttach()
    }

    fun changeSpanState(state: SpanStates){
        if (state == SpanStates.Wait || state == SpanStates.Start) {
            //TODO отправка sms
        }

        viewState.setSpan(state)
    }

    fun checkCode(code: String){
        //TODO тут должна быть проверка на правильность кода, пока оставлю заглушку с true/false с правильным кодом из макета
        if (code.length == 4){
            when(code){
                "2133" -> changeSpanState(SpanStates.Complete)
                else -> changeSpanState(SpanStates.False)
            }
        }
    }
}