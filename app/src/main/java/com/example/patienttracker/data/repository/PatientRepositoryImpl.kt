package com.example.patienttracker.data.repository

import com.example.patienttracker.data.local.PatientDao
import com.example.patienttracker.domain.model.Patient
import com.example.patienttracker.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow

class PatientRepositoryImpl(
    private val dao: PatientDao
): PatientRepository {

    override suspend fun addOrUpdatePatient(patient: Patient) {
        dao.addOrUpdatePatient(patient)
    }

    override suspend fun deletePatient(patient: Patient) {
        dao.deletePatient(patient)
    }

    override suspend fun getPatientById(patientId: Int): Patient? {
        return dao.getPatientById(patientId)
    }

    override fun getAllPatients(): Flow<List<Patient>> {
        return dao.getAllPatients()
    }
}