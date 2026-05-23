package com.example.studentaccom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BookingsScreen(bookedList: List<Accommodation>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("My Bookings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (bookedList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No reservations.") }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(bookedList) { house ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Home, null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(house.title, fontWeight = FontWeight.Bold)
                                Text(house.location)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
        Text("Student Name", style = MaterialTheme.typography.headlineSmall)
        Text("ID: 24001234", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Edit Profile") }
        OutlinedButton(onClick = onLogout, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) { Text("Log Out") }
    }
}