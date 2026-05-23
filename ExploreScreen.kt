package com.example.studentaccom

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(properties: List<Accommodation>, onBookingConfirmed: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedHouse by remember { mutableStateOf<Accommodation?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Filter logic for the search bar
    val filteredList = properties.filter {
        it.location.contains(searchQuery, ignoreCase = true) ||
                it.title.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search bar for the "Dynamic Search" feature in your report
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by Location (e.g. G-West)") },
            placeholder = { Text("Where do you want to stay?") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No properties found. Try a different location.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredList) { house ->
                    Card(
                        onClick = {
                            selectedHouse = house
                            showSheet = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column {
                            // IMAGE SECTION: Updated to ensure 50+ unique photos
                            AsyncImage(
                                model = house.imageUrl.ifEmpty {
                                    // Fallback if URL is missing, using house ID for uniqueness
                                    "https://picsum.photos/id/${(house.id % 50) + 10}/500/300"
                                },
                                contentDescription = "Property Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )

                            ListItem(
                                headlineContent = { Text(house.title, fontWeight = FontWeight.Bold) },
                                supportingContent = {
                                    Text("${house.location} • P${house.price}")
                                },
                                trailingContent = {
                                    Text(house.type, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // DETAIL BOTTOM SHEET: Proven for Slide 4 of your report
    if (showSheet && selectedHouse != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Text(selectedHouse!!.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(selectedHouse!!.type, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text("📍 Location: ${selectedHouse!!.location}", style = MaterialTheme.typography.bodyLarge)
                Text("💰 Price: P${selectedHouse!!.price} / month", style = MaterialTheme.typography.bodyLarge)
                Text("✨ Amenities: ${selectedHouse!!.amenities}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // COMMUNICATION BUTTONS: Uses Android Intents
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${selectedHouse!!.providerPhone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Call")
                    }
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${selectedHouse!!.providerPhone}"))
                            intent.putExtra("sms_body", "Hi, I am interested in your property: ${selectedHouse!!.title}. Is it available?")
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Message")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // RESERVATION BUTTON: Triggers the Toast in MainActivity
                Button(
                    onClick = {
                        showSheet = false
                        onBookingConfirmed()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Reserve Now")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}