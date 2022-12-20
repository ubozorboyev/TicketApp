package com.example.ticketapp.ui.main

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ticketapp.R
import com.example.ticketapp.data.NetworkModule
import com.example.ticketapp.databinding.FragmentFirstBinding
import com.example.ticketapp.dialog.QrScannerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.lockAndWaitNanos

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


        if (NetworkModule.BASE_URL_TEST.isEmpty())
            findNavController().navigate(R.id.SecondFragment)

        binding.buttonCheck.setOnClickListener {
            fetchResultFromEdit()
        }

        binding.inputResult.setOnKeyListener { v, keyCode, event ->

            when {

                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                    //perform an action here e.g. a send message button click
                    fetchResultFromEdit()
                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }


        }

        setUpObserve()
    }

    private fun setUpObserve() {

        viewModel.qrResult.observe(viewLifecycleOwner) { res ->
            res?.statustext?.let {
                binding.textviewResult.text = it
                binding.inputResult.setText("")
            }

            res?.status?.let {

                binding.textviewResult.setTextColor(

                    requireActivity().resources.getColor(

                        when (it) {
                            1 -> R.color.green_main
                            3 -> R.color.red_default
                            else -> R.color.yellow_main
                        }
                    )
                )

                binding.statusIcon.setImageResource(
                    when (it) {
                        1 -> R.drawable.ic_check_circle
                        3 -> R.drawable.ic_circle_x
                        else -> R.drawable.ic_error_outline
                    }
                )


                val uri: Uri =
                    Uri.parse("android.resource://" + context?.packageName.toString() + "/" + if (it == 1) R.raw.status_1 else R.raw.status_2_3)

                MediaPlayer.create(context, uri).start()

            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun fetchResultFromEdit() {
        hideKeyboard()
        binding.inputResult.text?.let {
            if (it.toString().isNotEmpty()) {
                binding.textviewResult.text = ""
                binding.statusIcon.setImageDrawable(null)
                viewModel.fetchDataFromUrl(it.toString())
            }
        }
    }

    private fun hideKeyboard() {
        val activity = requireActivity()
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun resultQrScanner(text: String) {
        hideKeyboard()
        viewModel.fetchDataFromUrl(text)
    }
}