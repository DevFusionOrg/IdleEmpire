package com.idleempire.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.idleempire.R
import com.idleempire.databinding.FragmentGameBinding
import com.idleempire.ui.activities.MainActivity
import com.idleempire.ui.viewmodel.GameViewModel

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        // Main tap button
        binding.btnTap.setOnClickListener {
            viewModel.onTap()
            val anim = AnimationUtils.loadAnimation(context, R.anim.tap_bounce)
            binding.btnTap.startAnimation(anim)
        }

        // Prestige button
        binding.btnPrestige.setOnClickListener {
            if (viewModel.canPrestige()) showPrestigeDialog()
        }

        // Observe game state
        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            binding.tvCoins.text = "💰 ${viewModel.formatCoins(state.coins)}"
            binding.tvCoinsPerSec.text = "+${viewModel.formatCoins(state.tapPower)}/tap"
            binding.tvPrestigeLevel.text = "⭐ Prestige ${state.prestigeLevel}"
            binding.tvMultiplier.text = "x${state.prestigeMultiplier.toInt()} multiplier"
            binding.btnPrestige.isEnabled = viewModel.canPrestige()
            binding.btnPrestige.alpha = if (viewModel.canPrestige()) 1.0f else 0.5f
        }
    }

    private fun showPrestigeDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("⭐ Prestige Reset")
            .setMessage("Reset everything and double your income multiplier permanently?")
            .setPositiveButton("Prestige!") { _, _ -> viewModel.prestige() }
            .setNegativeButton("Not Yet", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
