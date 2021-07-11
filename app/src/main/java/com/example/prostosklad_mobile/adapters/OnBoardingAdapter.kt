package com.example.prostosklad_mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prostosklad_mobile.databinding.ItemOnboardingBinding

class OnBoardingAdapter: RecyclerView.Adapter<OnBoardingAdapter.OnBoardingVH>() {

    var content: MutableList<Triple<Int, String?, String?>> = mutableListOf()
        set(value){
            field = value
            notifyItemRangeChanged(0, content.size)
        }


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
            val data = content[position]
            binding.itemImage.setImageResource(data.first)
            binding.itemHeader.text = data.second
            binding.itemText.text = data.third
        }

    }
}

