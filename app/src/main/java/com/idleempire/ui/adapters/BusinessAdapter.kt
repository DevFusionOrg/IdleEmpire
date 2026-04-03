package com.idleempire.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idleempire.data.model.Business
import com.idleempire.databinding.ItemBusinessBinding

class BusinessAdapter(
    private val onBuy: (Business) -> Unit,
    private val onCollect: (Business) -> Unit,
    private val onBuyManager: (Business) -> Unit,
    private val formatCoins: (Double) -> String
) : RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder>() {

    private var businesses: List<Business> = emptyList()
    private var playerCoins: Double = 0.0

    fun submitList(newList: List<Business>, coins: Double) {
        businesses = newList
        playerCoins = coins
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val binding = ItemBusinessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusinessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        holder.bind(businesses[position], playerCoins)
    }

    override fun getItemCount() = businesses.size

    inner class BusinessViewHolder(private val binding: ItemBusinessBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Business, coins: Double) {
            binding.tvEmoji.text = business.emoji
            binding.tvName.text = business.name
            binding.tvLevel.text = "Lv.${business.level}"

            val currentCost = business.currentCost
            val canAfford = coins >= currentCost

            // Update the cost and income text
            binding.tvCost.text = "🪙 Cost: ${formatCoins(currentCost)}"
            binding.tvIncome.text = "💵 Income: ${formatCoins(business.currentIncome)}/cycle"

            // Buy/Upgrade button
            binding.btnAction.text = if (!business.isOwned) "Buy" else "Upgrade"
            binding.btnAction.isEnabled = canAfford
            binding.btnAction.alpha = if (canAfford) 1.0f else 0.5f
            binding.btnAction.setOnClickListener { onBuy(business) }

            // Manager button
            val managerCost = currentCost * 10
            binding.btnManager.isEnabled = !business.hasManager && coins >= managerCost
            binding.btnManager.text = if (business.hasManager) "Manager ✅" else "Manager: ${formatCoins(managerCost)}"
            binding.btnManager.setOnClickListener { onBuyManager(business) }
            
            // Progress
            binding.progressCycle.progress = if (business.isOwned) 100 else 0
        }
    }
}
