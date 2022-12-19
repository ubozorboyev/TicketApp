package com.example.ticketapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.ticketapp.databinding.FragmentFirstBinding
import com.example.ticketapp.dialog.QrScannerDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class MainFragment : Fragment(), QrScannerDialog.CallBack {

    private var _binding: FragmentFirstBinding? = null

    private val viewModel by viewModels<MainFragmentVM>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            QrScannerDialog(this).show(requireActivity().supportFragmentManager)
        }

        setUpObserve()
    }

    private fun setUpObserve() {

        viewModel.qrResult.observe(viewLifecycleOwner) { res ->
            res?.statustext?.let {
                binding.textviewResult.text = it

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun resultQrScanner(text: String) {
        viewModel.fetchDataFromUrl(text)
    }
}