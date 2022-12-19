package com.example.ticketapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.data.modul.QrResponse
import com.example.ticketapp.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentVM @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _qrResult = MutableLiveData<QrResponse>()
    val qrResult: LiveData<QrResponse> get() = _qrResult


    fun fetchDataFromUrl(path: String) {

        viewModelScope.launch {

            _qrResult.value =
                apiService.getResultQrCode(path).body()

        }


    }


}