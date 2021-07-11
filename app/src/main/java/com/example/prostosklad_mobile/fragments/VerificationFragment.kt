package com.example.prostosklad_mobile.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import androidx.navigation.findNavController
import com.example.prostosklad_mobile.base.BaseFragment
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.databinding.FragmentVerificationBinding
import com.example.prostosklad_mobile.presenters.VerificationPresenter
import com.example.prostosklad_mobile.views.VerificationView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class VerificationFragment: BaseFragment(), VerificationView {

    private var binding: FragmentVerificationBinding by viewLifecycle()

    @Inject
    lateinit var presenterProvider: Provider<VerificationPresenter>

    private val verificationPresenter: VerificationPresenter by moxyPresenter { presenterProvider.get() }

    private lateinit var timer: CountDownTimer
    private var timerActive = false


    private var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            verificationPresenter.changeSpanState(SpanStates.Wait)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            context?.let { ds.color = it.getColor(R.color.magenta) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(layoutInflater)

        setInset(binding.root, top = true, bottom = true)
        initViews()

        return binding.root
    }

    fun initViews(){
        binding.apply {
            val textMaskListener = MaskedTextChangedListener(
                "[0000]",
                binding.etPhone,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                        verificationPresenter.checkCode(extractedValue)
                    }
                }
            )

            textMaskListener.autocomplete = false

            etPhone.addTextChangedListener(textMaskListener)

            val phoneStr = arguments?.getString(SignInFragment.phoneKey)
            text1.text = getString(R.string.verification_text1, phoneStr)

            toolbar.setNavigationOnClickListener {
                view?.findNavController()?.navigate(R.id.action_verificationFragment_to_signInFragment)
            }
        }
    }

    override fun setSpan(state: SpanStates){

        if (timerActive) timer.cancel()
        when (state){
            SpanStates.Start -> {
                startTimerWithString(R.string.verification_span_text1)
            }
            SpanStates.Repeat ->{
                setSpanString(getString(R.string.verification_span_text2))
            }
            SpanStates.Wait -> {
                startTimerWithString(R.string.verification_span_text4)
            }
            SpanStates.False -> {
                setSpanString(getString(R.string.verification_span_text5))
            }
            SpanStates.Complete -> {
                binding.tvVerification.text = getString(R.string.verification_span_text6)
            }
        }
    }

    fun startTimerWithString(strId: Int){

        timer = object: CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvVerification.text = getString(strId, seconds)
            }

            override fun onFinish() {
                timerActive = false
                verificationPresenter.changeSpanState(SpanStates.Repeat)
            }
        }

        timer.start()
        timerActive = true
    }

    fun setSpanString(str: String) {
        val endString = getString(R.string.verification_span_text3)
        val spannableString = SpannableString(str + " " + endString)

        spannableString.setSpan(clickableSpan, str.length + 1, str.length + endString.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvVerification.apply{
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    enum class SpanStates{
        Start, //Запуск таймера на 30 секунд и переход в Repeat
        Repeat, //Спан с переходом в Wait
        Wait, //После повторной отправки таймер как в Start
        False, //Если код неверный то спан аналогичный Repeat
        Complete // Заглушка с сообщением об удачном вводе
    }
}