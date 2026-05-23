package com.example.studentaccom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studentaccom.ui.theme.StudentAccomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val dao = db.accommodationDao()

        setContent {
            StudentAccomTheme {
                val databaseProperties by dao.getAll().collectAsState(initial = emptyList())
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    seedDatabase(dao, scope)
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var showSplash by remember { mutableStateOf(true) }
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var isProvider by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(2000)
                        showSplash = false
                    }

                    if (showSplash) {
                        SplashScreen()
                    } else if (!isLoggedIn) {
                        LoginScreen(onLoginSuccess = { selectedRole ->
                            isProvider = selectedRole
                            isLoggedIn = true
                        })
                    } else {
                        if (isProvider) {
                            ProviderDashboard(
                                properties = databaseProperties,
                                onAddProperty = { newAccom -> scope.launch { dao.insert(newAccom) } },
                                onLogout = { isLoggedIn = false }
                            )
                        } else {
                            MainNavigationContainer(
                                properties = databaseProperties,
                                onLogout = { isLoggedIn = false },
                                onBookingRequest = {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Booking Requested! Ref: BAC-${(1000..9999).random()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun seedDatabase(dao: AccommodationDao, scope: CoroutineScope) {
        scope.launch {
            val existing = dao.getAllSync()
            if (existing.isEmpty()) {
                val locations = listOf("G-West", "Tlokweng", "Phase 2", "Broadhurst", "Block 6", "Village", "Main Mall", "Phakalane", "Block 9", "Mmamashia")
                val types = listOf("Studio", "Single Room", "Cottage", "Apartment", "Bachelor")

                for (i in 1..50) {
                    val loc = locations.random()
                    dao.insert(
                        Accommodation(
                            id = 0,
                            title = "$loc Style Room #$i",
                            location = loc,
                            price = (1200..5500).random().toDouble(),
                            deposit = (500..1500).random().toDouble(),
                            type = types.random(),
                            amenities = "WiFi, Parking, Security",
                            providerPhone = "71000${100 + i}",
                            // STRICTLY HOUSES: Uses 'room,house,apartment' keywords and a lock per 'i'
                            imageUrl = "https://loremflickr.com/500/300/house,room,apartment/all?lock=$i"
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Home, null, modifier = Modifier.size(100.dp), tint = Color.White)
            Text("Student Accom Gabs", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationContainer(properties: List<Accommodation>, onLogout: () -> Unit, onBookingRequest: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Accommodations") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ExploreScreen(properties = properties, onBookingConfirmed = onBookingRequest)
        }
    }
}