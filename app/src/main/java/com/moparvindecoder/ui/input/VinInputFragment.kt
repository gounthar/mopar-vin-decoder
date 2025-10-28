package com.moparvindecoder.ui.input

import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.moparvindecoder.R
import com.moparvindecoder.databinding.FragmentVinInputBinding
import timber.log.Timber
import java.util.Locale

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
                val clipboard = requireContext().getSystemService<ClipboardManager>()
                if (clipboard == null) {
                    showSnackbar(getString(R.string.error_clipboard_unavailable))
                    return@setEndIconOnClickListener
                }

                val clipData = clipboard.primaryClip
                if (clipData == null || clipData.itemCount == 0) {
                    showSnackbar(getString(R.string.error_clipboard_empty))
                    return@setEndIconOnClickListener
                }

                val pasted = clipData.getItemAt(0)
                    ?.coerceToText(requireContext())
                    ?.toString()
                    ?.trim()
                    ?.uppercase(Locale.US)
                    ?.filter { it.isLetterOrDigit() }

                when {
                    pasted.isNullOrBlank() -> {
                        showSnackbar(getString(R.string.error_clipboard_no_text))
                    }
                    pasted.length < 10 -> {
                        showSnackbar(getString(R.string.error_clipboard_too_short))
                    }
                    pasted.length in 11..12 -> {
                        // Reject 11-12 character VINs as invalid for vintage Mopar
                        binding.vinInputEdittext.setText(pasted)
                        binding.vinInputEdittext.setSelection(pasted.length)
                        binding.vinInputLayout.error = getString(R.string.error_vin_length_invalid, pasted.length)
                    }
                    pasted.length > 13 -> {
                        // Take first 13 characters for vintage Mopar VINs
                        val truncated = pasted.take(13)
                        binding.vinInputEdittext.setText(truncated)
                        binding.vinInputEdittext.setSelection(truncated.length)
                        showSnackbar(getString(R.string.info_paste_truncated))
                    }
                    else -> {
                        binding.vinInputEdittext.setText(pasted)
                        binding.vinInputEdittext.setSelection(pasted.length)
                    }
                }
            } catch (e: SecurityException) {
                Timber.e(e, "Security exception accessing clipboard")
                showSnackbar(getString(R.string.error_clipboard_permission))
            } catch (e: Exception) {
                Timber.e(e, "Failed to paste from clipboard")
                showSnackbar(getString(R.string.error_clipboard_failed))
            }
        }

        binding.decodeButton.setOnClickListener {
            val vin = binding.vinInputEdittext.text?.toString()?.trim().orEmpty()
            if (vin.isEmpty()) {
                binding.vinInputLayout.error = getString(R.string.error_vin_required)
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
