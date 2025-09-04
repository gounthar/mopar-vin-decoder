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
import com.moparvindecoder.data.local.AppDatabase
import com.moparvindecoder.data.repository.VinHistoryRepositoryImpl
import com.moparvindecoder.data.repository.VinRepositoryImpl
import com.moparvindecoder.domain.usecase.DecodeVinUseCase

class VinResultsFragment : Fragment() {

    private var _binding: FragmentVinResultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinResultsViewModel by viewModels {
        val appContext = requireContext().applicationContext
        val historyRepo = VinHistoryRepositoryImpl(AppDatabase.get(appContext).vinHistoryDao())
        val decodeVin = DecodeVinUseCase(VinRepositoryImpl())
        VinResultsViewModelFactory(decodeVin, historyRepo)
    }
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
        binding.tvError.isVisible = false
        binding.resultsContainer.isVisible = false

        viewModel.vinInfo.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = false
            when (result) {
                is Result.Success -> {
                    val info = result.data
                    binding.tvError.isVisible = false
                    binding.resultsContainer.isVisible = true
                    binding.tvVin.text = info.vin
                    binding.tvYear.text = info.year ?: "-"
                    binding.tvMake.text = info.make ?: "-"
                    binding.tvModel.text = info.model ?: "-"
                    binding.tvEngine.text = info.engine ?: "-"
                    binding.tvPlant.text = info.plant ?: "-"
                    binding.tvProductionSeq.text = info.productionSeq ?: "-"
                }
                is Result.Error -> {
                    binding.resultsContainer.isVisible = false
                    binding.tvError.isVisible = true
                    binding.tvError.text = "Error: ${result.exception.message ?: "Unknown"}"
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
