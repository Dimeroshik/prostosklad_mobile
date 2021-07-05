package com.example.prostosklad_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prostosklad_mobile.R
import kotlin.collections.Map.*
import com.example.prostosklad_mobile.databinding.ItemOnboardingBinding

class OnBoardingAdapter( val caller: OnBoardingCaller): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingVH>() {


    var content: List<Triple<Int, Int, Int>> = mutableListOf(
        Triple(R.drawable.onboarding_image1, R.string.onboarding_item_header1, R.string.onboarding_item_text1),
        Triple(R.drawable.onboarding_image2, R.string.onboarding_item_header2, R.string.onboarding_item_text2),
        Triple(R.drawable.onboarding_image3, R.string.onboarding_item_header3, R.string.onboarding_item_text3)
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemOnboardingBinding = ItemOnboardingBinding.inflate(layoutInflater, parent, false)
        return OnBoardingVH(itemOnboardingBinding)
    }

    override fun onBindViewHolder(holder: OnBoardingVH, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = content.size

    inner class OnBoardingVH(private val binding: ItemOnboardingBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int){
            var data = content[position]
            binding.itemImage.setImageResource(data.first)
            binding.itemHeader.text = caller.getStringResource(data.second)
            binding.itemText.text = caller.getStringResource(data.third)
        }

    }

    interface OnBoardingCaller{
        fun getStringResource(id: Int): String
    }
}

