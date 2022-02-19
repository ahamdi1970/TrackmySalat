package com.example.android.trackmysalatquality.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysalatquality.R
import com.example.android.trackmysalatquality.salatchoice.SalatChoiceFragmentArgs
import kotlinx.android.synthetic.main.fragment_presentation.*

class PresentationFragment : Fragment(R.layout.fragment_presentation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_goto_reference.setOnClickListener{

           val action = PresentationFragmentDirections.actionFragmentPresentationToSalatTrackerFragment2()
            findNavController().navigate(action)

        }

        button_goto_track_salat.setOnClickListener {

            val action = PresentationFragmentDirections.actionFragmentPresentationToAssistanceFragment()
            findNavController().navigate(action)
        }
    }

}