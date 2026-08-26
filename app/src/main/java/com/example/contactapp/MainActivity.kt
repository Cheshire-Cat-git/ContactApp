package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactapp.data.Contact


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var selectedContact by remember { mutableStateOf<Contact?>(null) }

                NavHost(navController = navController, startDestination = "contact_list") {
                    composable("contact_list") {
                        ContactListScreen(
                            onContactClick = { contact ->
                                selectedContact = contact
                                navController.navigate("contact_detail")
                            }
                        )
                    }
                    composable("contact_detail") {
                        selectedContact?.let { contact ->
                            ContactDetailScreen(
                                contact = contact,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
