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

package com.example.android.trackmysalatquality.salattracker

import android.app.Application
import androidx.lifecycle.*
import com.example.android.trackmysalatquality.database.SalatDatabaseDao
import com.example.android.trackmysalatquality.database.SalatData
import com.example.android.trackmysalatquality.formatSalates
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SalatTrackerViewModel(
        val database: SalatDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var salat = MutableLiveData<SalatData?>()
    private val salates = database.getAllSalates()

    /**
     * Converted salates to Spanned for displaying.
     */
    val salatString = Transformations.map(salates) { salates ->
        formatSalates(salates, application.resources)
    }

    /**
     * If salat has not been set, then the START button should be visible.
     */
    val startButtonVisible = Transformations.map(salat) {
        it == null
    }

    /**
     * If salat has been set, then the STOP button should be visible.
     */
    val stopButtonVisible = Transformations.map(salat) {
        it != null
    }

    /**
     * If there are any nights in the database, show the CLEAR button.
     */
    val clearButtonVisible = Transformations.map(salates) {
        it?.isNotEmpty()
    }

    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    /**
     * Variable that tells the Fragment to navigate to a specific [SalatChoiceFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToSalatChoice = MutableLiveData<SalatData>()

    /**
     * If this is non-null, immediately navigate to [SalatChoiceFragment] and call [doneNavigating]
     */
    val navigateToSalatChoice: LiveData<SalatData>
        get() = _navigateToSalatChoice

    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    /**
     * Call this immediately after navigating to [SalatChoiceFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigating() {
        _navigateToSalatChoice.value = null
    }

    init {
        initializeSalat()
    }

    private fun initializeSalat() {
        viewModelScope.launch {
            salat.value = getSalatFromDatabase()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */

    private suspend fun getSalatFromDatabase(): SalatData? {
        var salat = database.getSalat()
        if (salat?.startTimeMilli != salat?.endTimeMilli) {
            salat = null
        }
        return salat
    }

    private suspend fun insert(salat: SalatData) {
        database.insert(salat)
    }

    private suspend fun update(salat: SalatData) {
        database.update(salat)
    }

    suspend fun clear() {
        database.clear()
    }

    /**
     * Executes when the START button is clicked.
     */

    fun onStartTracking() {
        viewModelScope.launch {
            // Create a new salat, which captures the current time,
            // and insert it into the database.
            val newSalat = SalatData()
            insert(newSalat)
            salat.value = getSalatFromDatabase()
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */

    fun onStopTracking() {
        viewModelScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val oldSalat = salat.value ?: return@launch

            // Update the salat in the database to add the end time.
            oldSalat.endTimeMilli = System.currentTimeMillis()
            update(oldSalat)

            // Set state to navigate to the SalatChoiceFragment.
            _navigateToSalatChoice.value = oldSalat
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */

    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()
            // And clear salat since it's no longer in the database
            salat.value = null
            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
    }

}

