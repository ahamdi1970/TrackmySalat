package com.example.android.trackmysalatquality.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PresentationViewModelFactory(
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PresentationViewModel::class.java)) {
            return PresentationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
