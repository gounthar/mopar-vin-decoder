package com.moparvindecoder.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.moparvindecoder.databinding.FragmentVinHistoryBinding

class VinHistoryFragment : Fragment() {

    private var _binding: FragmentVinHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinHistoryViewModel by viewModels()

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

        // Placeholder observer to show simple history text list
        viewModel.history.observe(viewLifecycleOwner) { list ->
            binding.historyText.text = if (list.isEmpty()) {
                "No history yet"
            } else {
                list.joinToString(separator = "\n")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
