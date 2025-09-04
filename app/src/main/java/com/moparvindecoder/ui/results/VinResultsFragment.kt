package com.moparvindecoder.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moparvindecoder.databinding.FragmentVinResultsBinding
import com.moparvindecoder.utils.Result

class VinResultsFragment : Fragment() {

    private var _binding: FragmentVinResultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinResultsViewModel by viewModels()
    private val args: VinResultsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVinResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = true
        binding.resultText.text = ""

        viewModel.vinInfo.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = false
            when (result) {
                is Result.Success -> {
                    val info = result.data
                    val text = buildString {
                        appendLine("VIN: ${info.vin}")
                        appendLine("Year: ${info.year ?: "-"}")
                        appendLine("Make: ${info.make ?: "-"}")
                        appendLine("Model: ${info.model ?: "-"}")
                        appendLine("Plant: ${info.plant ?: "-"}")
                        appendLine("Engine: ${info.engine ?: "-"}")
                    }
                    binding.resultText.text = text.trim()
                }
                is Result.Error -> {
                    binding.resultText.text = "Error: ${result.exception.message ?: "Unknown"}"
                }
            }
        }

        viewModel.decode(args.vin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
