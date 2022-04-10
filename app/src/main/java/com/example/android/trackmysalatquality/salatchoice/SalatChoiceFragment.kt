/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysalatquality.salatchoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysalatquality.R
import com.example.android.trackmysalatquality.database.SalatDatabase
import com.example.android.trackmysalatquality.databinding.FragmentSalatChoiceBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SalatChoiceFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSalatChoiceBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_salat_choice, container, false)

        val application = requireActivity().application // was val application = requireNotNull(this.activity).application

        val arguments = SalatChoiceFragmentArgs.fromBundle(requireArguments())

        val dataSource = SalatDatabase.getInstance(application).salatDatabaseDao

        val viewModelFactory = SalatChoiceViewModelFactory(arguments.salatKey, dataSource)

        val salatChoiceViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(SalatChoiceViewModel::class.java)

        binding.salatChoiceViewModel = salatChoiceViewModel

        salatChoiceViewModel.navigateToSalatTracker.observe(this, Observer {
            if (it == true) { //Observed state is true.
                this.findNavController().navigate(
                        SalatChoiceFragmentDirections.actionSalatChoiceFragmentToSalatTrackerFragment())
                salatChoiceViewModel.doneNavigating()
            }

        })

        return binding.root
    }
}
