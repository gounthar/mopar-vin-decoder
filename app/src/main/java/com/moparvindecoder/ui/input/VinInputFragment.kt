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
import com.google.android.material.snackbar.Snackbar

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
            try {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                if (clipboard == null) {
                    showError("Clipboard service not available")
                    return@setEndIconOnClickListener
                }

                val clipData = clipboard.primaryClip
                if (clipData == null || clipData.itemCount == 0) {
                    showError("Clipboard is empty")
                    return@setEndIconOnClickListener
                }

                val pasted = clipData.getItemAt(0)
                    ?.coerceToText(requireContext())
                    ?.toString()
                    ?.trim()
                    ?.uppercase()
                    ?.filter { it.isLetterOrDigit() }

                when {
                    pasted.isNullOrBlank() -> {
                        showError("No valid text in clipboard")
                    }
                    pasted.length < 10 -> {
                        showError("Clipboard text too short for a valid VIN")
                    }
                    pasted.length > 13 -> {
                        // Take first 13 characters for vintage Mopar VINs
                        val truncated = pasted.take(13)
                        binding.vinInputEdittext.setText(truncated)
                        binding.vinInputEdittext.setSelection(truncated.length)
                        showInfo("Pasted and truncated to 13 characters")
                    }
                    else -> {
                        binding.vinInputEdittext.setText(pasted)
                        binding.vinInputEdittext.setSelection(pasted.length)
                    }
                }
            } catch (e: SecurityException) {
                showError("Permission denied to access clipboard")
            } catch (e: Exception) {
                showError("Failed to paste from clipboard")
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

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showInfo(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
