package com.example.contactapp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.contactapp.data.Contact

@Composable
fun ContactDetailScreen(
    contact: Contact,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(android.R.drawable.ic_menu_revert), contentDescription = "Back")
            }
            Text(
                text = "Contact App",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2A2238),
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        AsyncImage(
            model = contact.photoUri ?: R.drawable.ic_launcher_background,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 20.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = contact.name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D324E),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp)
        )
        Text("Phone Numbers:", fontWeight = FontWeight.Bold, color = Color(0xFF4A3F5E))
        Text(
            text = contact.phoneNumber,
            color = Color(0xFF5C536B),
            fontSize = 15.sp,
            modifier = Modifier
                .padding(top = 6.dp, start = 16.dp)
                .clickable {
                    if (contact.phoneNumber.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:${contact.phoneNumber}".toUri())
                        context.startActivity(intent)
                    }
                }
        )
        contact.email?.let { email ->
            Text("Emails:", fontWeight = FontWeight.Bold, color = Color(0xFF4A3F5E), modifier = Modifier.padding(top = 16.dp))
            Text(text = email, color = Color(0xFF5C536B), fontSize = 15.sp, modifier = Modifier.padding(top = 6.dp, start = 16.dp))
        }

        contact.address?.let { address ->
            Text("Addresses:", fontWeight = FontWeight.Bold, color = Color(0xFF4A3F5E), modifier = Modifier.padding(top = 16.dp))
            Text(text = address, color = Color(0xFF5C536B), fontSize = 15.sp, modifier = Modifier.padding(top = 6.dp, start = 16.dp))
        }
    }
}
