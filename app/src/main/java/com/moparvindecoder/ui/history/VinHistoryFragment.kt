package com.moparvindecoder.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moparvindecoder.databinding.FragmentVinHistoryBinding

class VinHistoryFragment : Fragment() {

    private var _binding: FragmentVinHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinHistoryViewModel by viewModels()

    private lateinit var adapter: VinHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVinHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VinHistoryAdapter { entity ->
            val action =
                VinHistoryFragmentDirections.actionVinHistoryFragmentToVinResultsFragment(entity.vin)
            findNavController().navigate(action)
        }
        binding.recyclerHistory.adapter = adapter

        viewModel.history.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyView.isVisible = list.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
