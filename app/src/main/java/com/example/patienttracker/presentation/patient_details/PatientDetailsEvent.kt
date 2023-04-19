package com.example.patienttracker.presentation.patient_details

sealed class PatientDetailsEvent {
    data class EnteredName(val name: String): PatientDetailsEvent()
    data class EnteredAge(val age: String): PatientDetailsEvent()
    data class EnteredAssignedDoctor(val doctor: String): PatientDetailsEvent()
    data class EnteredPrescription(val prescription: String): PatientDetailsEvent()
    object SelectedMale: PatientDetailsEvent()
    object SelectedFemale: PatientDetailsEvent()
    object SaveButton: PatientDetailsEvent()
}
