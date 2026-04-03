package com.idleempire.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idleempire.databinding.FragmentBusinessBinding
import com.idleempire.ui.activities.MainActivity
import com.idleempire.ui.adapters.BusinessAdapter
import com.idleempire.ui.viewmodel.GameViewModel

class BusinessFragment : Fragment() {

    private var _binding: FragmentBusinessBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: BusinessAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        adapter = BusinessAdapter(
            onBuy = { business ->
                viewModel.purchaseBusiness(business)
                (requireActivity() as MainActivity).trackUserAction()
            },
            onCollect = { business -> viewModel.collectBusiness(business) },
            onBuyManager = { business -> viewModel.buyManager(business) },
            formatCoins = { viewModel.formatCoins(it) }
        )

        binding.recyclerBusinesses.layoutManager = LinearLayoutManager(context)
        binding.recyclerBusinesses.adapter = adapter

        viewModel.businesses.observe(viewLifecycleOwner) { businesses ->
            val state = viewModel.gameState.value
            adapter.submitList(businesses, state?.coins ?: 0.0)
        }

        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            val businesses = viewModel.businesses.value ?: return@observe
            adapter.submitList(businesses, state.coins)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
