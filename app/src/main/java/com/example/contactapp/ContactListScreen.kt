package com.example.contactapp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactapp.data.Contact

@Composable
fun ContactListScreen(
    viewModel: ContactViewModel = viewModel(),
    onContactClick: (Contact) -> Unit
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.loadContacts()
            }
        }
    )
    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(contacts){ contact ->
            ContactItem(contact){onContactClick(contact)}
        }
    }

}