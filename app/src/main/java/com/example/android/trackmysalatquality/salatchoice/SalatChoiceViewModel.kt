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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysalatquality.database.SalatDatabaseDao
import kotlinx.coroutines.launch

class SalatChoiceViewModel(
        private val salatKey: Long = 0L,
        val database: SalatDatabaseDao) : ViewModel() {

    private val _navigateToSalatTracker = MutableLiveData<Boolean?>()

    val navigateToSalatTracker: LiveData<Boolean?>
        get() = _navigateToSalatTracker

    fun doneNavigating() {
        _navigateToSalatTracker.value = null
    }

    fun onSetSalatChoice(choice: Int) {
        viewModelScope.launch {
            val salat = database.get(salatKey) ?: return@launch
            salat.salatChoice = choice
            database.update(salat)

            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigateToSalatTracker.value = true
        }
    }

}
