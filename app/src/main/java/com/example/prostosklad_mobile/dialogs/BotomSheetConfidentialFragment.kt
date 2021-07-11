package com.example.prostosklad_mobile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prostosklad_mobile.R
import com.example.prostosklad_mobile.databinding.FragmentBotomsheetConfidentialBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotomSheetConfidentialFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBotomsheetConfidentialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBotomsheetConfidentialBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.UserBottomSheetDialogTheme)
    }
}