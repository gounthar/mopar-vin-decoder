package com.moparvindecoder.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moparvindecoder.databinding.FragmentVinInputBinding
import android.content.ClipboardManager
import android.content.Context

class VinInputFragment : Fragment() {

    private var _binding: FragmentVinInputBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinInputViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVinInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vinInputEdittext.doAfterTextChanged {
            viewModel.onVinChanged(it?.toString().orEmpty())
            binding.vinInputLayout.error = null
        }

        binding.vinInputLayout.setEndIconOnClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val pasted = clipboard.primaryClip?.getItemAt(0)
                ?.coerceToText(requireContext())
                ?.toString()
                ?.uppercase()
                ?.take(13)

            if (!pasted.isNullOrBlank()) {
                binding.vinInputEdittext.setText(pasted)
                binding.vinInputEdittext.setSelection(pasted.length)
            }
        }

        binding.decodeButton.setOnClickListener {
            val vin = binding.vinInputEdittext.text?.toString()?.trim().orEmpty()
            if (vin.isEmpty()) {
                binding.vinInputLayout.error = "VIN is required"
                return@setOnClickListener
            }
            val action = VinInputFragmentDirections.actionVinInputFragmentToVinResultsFragment(vin)
            findNavController().navigate(action)
        }

        binding.scanButton.setOnClickListener {
            val action = VinInputFragmentDirections.actionVinInputFragmentToVinScannerFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
