package com.example.patienttracker.presentation.patient_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun PatientListScreen(
    onFabClick: () -> Unit,
    onItemClick: (Int?) -> Unit,
    viewModel: PatientListViewModel = hiltViewModel()
) {

    val patientList by viewModel.patientList.collectAsState()

    Scaffold(
        topBar = { ListAppBar() },
        floatingActionButton = {
            ListFab(onFabClick = onFabClick)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(patientList) { patient ->
                PatientItem(
                    patient = patient,
                    onItemClick = { onItemClick(patient.patientId) },
                    onDeleteConfirm = { viewModel.deletePatient(patient) }
                )
            }
        }
        if (patientList.isEmpty() && !viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Patients Details\nby pressing add button.",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ListAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Patient Tracker",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
        }
    )
}

@Composable
fun ListFab(
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Patient Button"
        )
    }
}

@Preview
@Composable
fun PatientListScreenPrev() {
    ListAppBar()
}