package com.moparvindecoder.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.moparvindecoder.databinding.FragmentVinScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VinScannerFragment : Fragment() {

    private var _binding: FragmentVinScannerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VinScannerViewModel by viewModels()

    private var cameraExecutor: ExecutorService? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            viewModel.setStatus("Starting camera…")
            startCamera()
        } else {
            viewModel.setStatus("Camera permission denied")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVinScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.scanStatus.text = status
        }

        ensurePermissionAndStart()
    }

    private fun ensurePermissionAndStart() {
        val ctx = requireContext()
        when {
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.setStatus("Starting camera…")
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                viewModel.setStatus("Camera permission required to scan VIN")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        val context = requireContext()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            cameraProvider = provider

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                provider.unbindAll()
                provider.bindToLifecycle(viewLifecycleOwner, selector, preview)
                viewModel.setStatus("Point camera at VIN barcode")
            } catch (e: Exception) {
                viewModel.setStatus("Camera start failed: ${e.localizedMessage}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider?.unbindAll()
        cameraExecutor?.shutdown()
        _binding = null
    }
}
