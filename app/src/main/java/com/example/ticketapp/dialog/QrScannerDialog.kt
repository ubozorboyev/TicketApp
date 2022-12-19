package com.example.ticketapp.dialog

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.ticketapp.R
import com.example.ticketapp.databinding.DialogQrScannerBinding
import com.journeyapps.barcodescanner.CaptureManager


class QrScannerDialog(private val listener: CallBack) : DialogFragment() {

    private var _binding: DialogQrScannerBinding? = null

    private val binding get() = _binding!!

    private var isTorch = false

    private lateinit var capture: CaptureManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_FRAME,
            android.R.style.Theme_Black_NoTitleBar
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogQrScannerBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        capture = CaptureManager(requireActivity(), binding.barCodeView)
        capture.initializeFromIntent(requireActivity().intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()

        binding.barCodeView.decodeSingle {
            listener.resultQrScanner(it.text)
            dismiss()
        }
        binding.flightButton.setOnClickListener {

            isTorch = isTorch.not()

            if (isTorch) binding.barCodeView.setTorchOn()
            else binding.barCodeView.setTorchOff()

            binding.flightButton.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    if (isTorch) R.color.green_main
                    else R.color.divider_secondary
                )
            )
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }


    override fun onResume() {
        capture.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }


    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) == null) show(manager, TAG)
    }

    companion object Factory {
        private val TAG = this::class.simpleName
    }

    interface CallBack {
        fun resultQrScanner(text: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
