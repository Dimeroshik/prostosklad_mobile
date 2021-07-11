package com.example.prostosklad_mobile.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import androidx.core.view.*
import androidx.navigation.findNavController
import com.example.prostosklad_mobile.base.BaseFragment
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.databinding.FragmentSigninBinding
import com.example.prostosklad_mobile.dialogs.BotomSheetConfidentialFragment
import com.redmadrobot.inputmask.MaskedTextChangedListener


class SignInFragment: BaseFragment() {

    companion object {
        var TAG = "SignInFragment"
        var phoneKey = "phone"
    }

    private var binding: FragmentSigninBinding by viewLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(layoutInflater)

        setInset(binding.root, top = true, bottom = true)
        setKeyboardInsetForButton(binding.buttonNext)
        binding.buttonNext.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(bottom = 0)
        }
        initViews()

        return binding.root
    }

    fun initViews(){

        val textMaskListener = MaskedTextChangedListener(
            "([000]) [000]-[00]-[00]",
            binding.etPhone,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                    checkButton(extractedValue.length)
                }
            }
        )

        textMaskListener?.autocomplete = false

        binding.etPhone.addTextChangedListener(textMaskListener)

        binding.buttonNext.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val action = SignInFragmentDirections.actionSignInFragmentToVerificationFragment()
            action.phone = phone
            view?.findNavController()?.navigate(action)
        }

        initSpan()
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
        val string = getString(R.string.signin_span_text)

        val spannableString = SpannableString(string)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val dialog = BotomSheetConfidentialFragment()
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