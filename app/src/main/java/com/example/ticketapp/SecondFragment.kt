package com.example.ticketapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ticketapp.data.NetworkModule
import com.example.ticketapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireActivity().getSharedPreferences("app_code", Context.MODE_PRIVATE)

        binding.tvCurrentUrl.text = NetworkModule.BASE_URL_TEST


        binding.buttonSecond.setOnClickListener {
            val newUrl = binding.inputUrl.text?.toString()

            if (newUrl?.startsWith("http") == true) {
                NetworkModule.BASE_URL_TEST = newUrl

                preferences.edit().putString("BASE_URL", newUrl).apply()

                findNavController().popBackStack()
            } else
                Toast.makeText(
                    requireContext(),
                    "Url is must start https or http",
                    Toast.LENGTH_SHORT
                ).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}