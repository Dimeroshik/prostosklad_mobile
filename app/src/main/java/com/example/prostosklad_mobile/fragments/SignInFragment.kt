package com.example.prostosklad_mobile.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.*
import androidx.fragment.app.Fragment
import com.example.prostosklad_mobile.MainActivity
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.databinding.FragmentSigninBinding
import com.example.prostosklad_mobile.dialogs.BotomSheetConfidentialFragment
import com.redmadrobot.inputmask.MaskedTextChangedListener


class SignInFragment: Fragment() {

    companion object {
        var TAG = "SignInFragment"
    }

    private lateinit var binding: FragmentSigninBinding

    private var posTop = 0
    private var posBottom = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(layoutInflater)

        initInsets(container)
        initViews()

        return binding.root
    }

    fun initViews(){

        var textMaskListener = MaskedTextChangedListener(
            "([000]) [000]-[00]-[00]",
            binding.etPhone,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, value: String) {
                    checkButton(value.length)
                }
            }
        )

        textMaskListener?.autocomplete = false

        binding.etPhone.addTextChangedListener(textMaskListener)

        binding.buttonNext.setOnClickListener {
            var phone = binding.etPhone.text.toString()
            (activity as MainActivity).openVerification(phone)
        }

        initSpan()
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

    fun checkButton(phoneLength: Int){
        if (phoneLength == 10 && !binding.buttonNext.isEnabled)
            binding.buttonNext.apply {
                isEnabled = true
                //setBackgroundResource(R.drawable.button_dark)
        }
        else if (phoneLength != 10 && binding.buttonNext.isEnabled)
            binding.buttonNext.apply {
                isEnabled = false
                //setBackgroundResource(R.drawable.button_dark_inactive)
        }
    }

    fun initSpan(){
        var string = getString(R.string.signin_text_confidential)

        var spannableString = SpannableString(string)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                var dialog = BotomSheetConfidentialFragment()
                dialog.show(childFragmentManager, "TAG")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                context?.let { ds.color = it.getColor(R.color.magenta) }
            }
        }

        spannableString.setSpan(clickableSpan, 29, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvConfidential.apply{
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }
}