
package com.hotelgmaopro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HotelGmaoApp()
        }
    }
}

@Composable
fun HotelGmaoApp() {
    var french by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf(0) }

    val blue = Color(0xFF1565C0)
    val bg = Color(0xFFF3F6FA)

    MaterialTheme {
        Scaffold(
            containerColor = bg,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("HOTEL GMAO PRO", fontWeight = FontWeight.Bold)
                            Text(
                                if (french) "Maintenance hôtelière"
                                else "إدارة صيانة الفندق",
                                fontSize = 12.sp
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { french = !french }) {
                            Text(if (french) "AR" else "FR")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = page == 0,
                        onClick = { page = 0 },
                        icon = { Icon(Icons.Default.Dashboard, null) },
                        label = { Text(if (french) "Accueil" else "الرئيسية") }
                    )
                    NavigationBarItem(
                        selected = page == 1,
                        onClick = { page = 1 },
                        icon = { Icon(Icons.Default.Build, null) },
                        label = { Text(if (french) "Interventions" else "التدخلات") }
                    )
                    NavigationBarItem(
                        selected = page == 2,
                        onClick = { page = 2 },
                        icon = { Icon(Icons.Default.Settings, null) },
                        label = { Text(if (french) "Réglages" else "الإعدادات") }
                    )
                }
            }
        ) { padding ->
            if (page == 0) {
                DashboardContent(french, blue, Modifier.padding(padding))
            } else {
                SimplePage(
                    title = when (page) {
                        1 -> if (french) "Interventions" else "التدخلات"
                        else -> if (french) "Réglages" else "الإعدادات"
                    },
                    french = french,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun DashboardContent(french: Boolean, blue: Color, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                if (french) "Tableau de bord" else "لوحة التحكم",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (french) "Vue générale de la maintenance"
                else "نظرة عامة على الصيانة",
                color = Color.Gray
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard(
                    if (french) "En cours" else "قيد التنفيذ",
                    "08", Color(0xFFFF9800), Modifier.weight(1f)
                )
                KpiCard(
                    if (french) "Terminées" else "منجزة",
                    "24", Color(0xFF2E7D32), Modifier.weight(1f)
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard(
                    if (french) "En attente" else "انتظار",
                    "05", Color(0xFF7B1FA2), Modifier.weight(1f)
                )
                KpiCard(
                    if (french) "Urgentes" else "عاجلة",
                    "02", Color(0xFFD32F2F), Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                if (french) "Actions rapides" else "إجراءات سريعة",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton(
                    if (french) "Nouvelle panne" else "عطل جديد",
                    Icons.Default.Add,
                    blue,
                    Modifier.weight(1f)
                )
                QuickButton(
                    if (french) "Équipement" else "معدات",
                    Icons.Default.Build,
                    blue,
                    Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                if (french) "Interventions récentes"
                else "آخر التدخلات",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            InterventionRow("Climatisation", "Chambre 204", "En cours")
            InterventionRow("Électricité", "Restaurant", "Urgente")
            InterventionRow("Plomberie", "Chambre 105", "Terminée")
        }
    }
}

@Composable
fun KpiCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Text(value, color = color, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuickButton(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector,
                blue: Color, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = blue)
    ) {
        Icon(icon, null)
        Spacer(Modifier.width(6.dp))
        Text(title, fontSize = 12.sp)
    }
}

@Composable
fun InterventionRow(title: String, location: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Build, null, tint = Color(0xFF1565C0))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(location, color = Color.Gray, fontSize = 13.sp)
            }
            Text(status, color = Color(0xFF1565C0), fontSize = 12.sp)
        }
    }
}

@Composable
fun SimplePage(title: String, french: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
