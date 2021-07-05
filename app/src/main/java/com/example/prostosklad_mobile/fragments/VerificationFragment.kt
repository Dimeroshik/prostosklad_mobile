package com.example.prostosklad_mobile.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.example.prostosklad_mobile.MainActivity
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.databinding.FragmentVerificationBinding
import com.example.prostosklad_mobile.dialogs.BotomSheetConfidentialFragment
import com.redmadrobot.inputmask.MaskedTextChangedListener
import java.util.*

class VerificationFragment: Fragment() {

    companion object {
        val TAG = "VerificationFragment"
    }



    private lateinit var binding: FragmentVerificationBinding

    private lateinit var timer: CountDownTimer
    private var timerActive = false

    var posTop = 0
    var posBottom = 0

    private var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            setSpan(SpanStates.Wait)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            context?.let { ds.color = it.getColor(R.color.magenta) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(layoutInflater)

        initInsets(container)
        initViews()

        return binding.root
    }

    fun initViews(){
        binding.apply {
            var textMaskListener = MaskedTextChangedListener(
                "[0000]",
                binding.etPhone,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, value: String) {
                        checkCode(value)
                    }
                }
            )

            textMaskListener?.autocomplete = false

            etPhone.addTextChangedListener(textMaskListener)

            text1.text = getString(R.string.verification_text1) + "+7 " + arguments?.getString("phone")

            toolbar.setNavigationOnClickListener {
                (activity as MainActivity).openAuthorization()
            }
        }

        setSpan(SpanStates.Start)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun initInsets(container: ViewGroup?){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            posTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            posBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                updateMargins(
                    top = posTop,
                    bottom = posBottom)
            }

            insets
        }

        val cb = @RequiresApi(Build.VERSION_CODES.R)
        object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(insets: WindowInsets, animations: MutableList<WindowInsetsAnimation>): WindowInsets {
                container?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    updateMargins(
                        bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    )
                }

                return insets
            }
        }
        container?.setWindowInsetsAnimationCallback(cb)
    }

    fun checkCode(code: String){
        //TODO тут должна быть проверка на правильность кода, пока оставлю заглушку с true/false с правильным кодом из макета
        if (code.length == 4){
            when(code){
              "2133" -> setSpan(SpanStates.Complete)
                else -> setSpan(SpanStates.False)
            }

        }
    }

    fun isSuccessful(code: String)= code == "2133"

    fun setSpan(state: SpanStates){

        if (timerActive) timer.cancel()
        when (state){
            SpanStates.Start -> {
                startTimerWithString(getString(R.string.verification_start))
            }
            SpanStates.Repeat ->{
                setSpanString(getString(R.string.verification_repeat))
            }
            SpanStates.Wait -> {
                startTimerWithString(getString(R.string.verification_wait))
            }
            SpanStates.False -> {
                setSpanString(getString(R.string.verification_false))
            }
            SpanStates.Complete -> {
                binding.tvVerification.text = getString(R.string.verification_complete)
            }
        }
    }

    fun startTimerWithString(str: String){

        timer = object: CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                var seconds = millisUntilFinished / 1000
                binding.tvVerification.text = str + " $seconds..."
            }

            override fun onFinish() {
                timerActive = false
                setSpan(SpanStates.Repeat)
            }
        }

        timer.start()
        timerActive = true
    }

    fun setSpanString(str: String) {
        var endString = getString(R.string.verification_send)
        var spannableString = SpannableString(str + " " + endString)

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