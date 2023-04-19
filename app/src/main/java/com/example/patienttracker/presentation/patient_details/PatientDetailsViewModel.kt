package com.example.patienttracker.presentation.patient_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patienttracker.domain.model.Patient
import com.example.patienttracker.domain.repository.PatientRepository
import com.example.patienttracker.util.Constants.PATIENT_DETAILS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDetailsViewModel @Inject constructor(
    private val repository: PatientRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(PatientDetailsUiState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPatientId: Int? = null

    init {
        fetchPatientDetails()
    }

    fun onAction(event: PatientDetailsEvent) {
        when(event) {
            is PatientDetailsEvent.EnteredName -> {
                state = state.copy(name = event.name)
            }
            is PatientDetailsEvent.EnteredAge -> {
                state = state.copy(age = event.age)
            }
            is PatientDetailsEvent.EnteredAssignedDoctor -> {
                state = state.copy(doctorAssigned = event.doctor)
            }
            is PatientDetailsEvent.EnteredPrescription -> {
                state = state.copy(prescription = event.prescription)
            }
            PatientDetailsEvent.SelectedFemale -> {
                state = state.copy(gender = 2)
            }
            PatientDetailsEvent.SelectedMale -> {
                state = state.copy(gender = 1)
            }
            PatientDetailsEvent.SaveButton -> {
                viewModelScope.launch {
                    try {
                        savePatient()
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(
                                message = e.message ?: "Couldn't save patient's details."
                            )
                        )
                    }
                }
            }
        }
    }

    private fun savePatient() {
        val age = state.age.toIntOrNull()
        when {
            state.name.isEmpty() -> throw TextFieldException("Please enter name.")
            state.age.isEmpty() -> throw TextFieldException("Please enter age.")
            state.gender == 0 -> throw TextFieldException("Please select gender")
            state.doctorAssigned.isEmpty() -> throw TextFieldException("Please enter doctor assigned.")
            state.prescription.isEmpty() -> throw TextFieldException("Please enter prescription.")
            age == null || age < 0 || age > 100 -> throw TextFieldException("Please enter valid age.")
        }
        val trimmedName = state.name.trim()
        val trimmedDoctorName = state.doctorAssigned.trim()
        viewModelScope.launch {
            repository.addOrUpdatePatient(
                patient = Patient(
                    name = trimmedName,
                    age = state.age,
                    gender = state.gender,
                    doctorAssigned = trimmedDoctorName,
                    prescription = state.prescription,
                    patientId = currentPatientId
                )
            )
        }
    }

    private fun fetchPatientDetails() {
        savedStateHandle.get<Int>(key = PATIENT_DETAILS_ARGUMENT_KEY)?.let { patientId ->
            if (patientId != -1) {
                viewModelScope.launch {
                    repository.getPatientById(patientId)?.apply {
                        state = state.copy(
                            name = name,
                            age = age,
                            gender = gender,
                            doctorAssigned = doctorAssigned,
                            prescription = prescription
                        )
                        currentPatientId = patientId
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String): UiEvent()
        object SaveNote : UiEvent()
    }

}

class TextFieldException(message: String?): Exception(message)

