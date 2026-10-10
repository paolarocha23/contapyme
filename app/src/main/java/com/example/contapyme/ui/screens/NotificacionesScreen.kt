package com.example.contapyme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.data.Alerta
import com.example.contapyme.data.SampleData
import com.example.contapyme.ui.components.EstadoChip
import com.example.contapyme.ui.components.FilterChipRow
import com.example.contapyme.ui.components.TopHeader
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber

@Composable
fun NotificacionesScreen(onBack: () -> Unit, onVerDetalle: (String) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        TopHeader(
            title = "Notificaciones",
            onBack = onBack,
            stats = listOf(
                "Inventario" to "9",
                "Por cobrar" to "5",
                "Por pagar" to "2"
            )
        )
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    FilterChipRow(listOf("Todas", "Inventario", "Cobrar", "Pagar"), selected = 0)
                }
            }
            val hoy = SampleData.alertas.filter { it.grupo == "Hoy" }
            val ayer = SampleData.alertas.filter { it.grupo == "Ayer" }

            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Hoy", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.weight(1f))
                        Text("${hoy.size} alertas", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            items(hoy.size) { index ->
                AlertaCard(hoy[index], onClick = { onVerDetalle(hoy[index].id) })
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ayer", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.weight(1f))
                        Text("${ayer.size} alertas", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            items(ayer.size) { index ->
                AlertaCard(ayer[index], onClick = { onVerDetalle(ayer[index].id) })
            }

            item { Spacer(Modifier.height(90.dp)) }
        }
    }
}

@Composable
private fun AlertaCard(alerta: Alerta, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColorFor(alerta.tipo)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconFor(alerta.tipo), contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(alerta.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text(alerta.descripcion, fontSize = 12.sp, color = TextSecondary)
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            EstadoChip(alerta.badge)
            Spacer(Modifier.width(10.dp))
            Text("${alerta.grupo} · ${alerta.hora}", fontSize = 11.sp, color = TextSecondary)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Filled.Visibility, contentDescription = null, tint = Navy, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(4.dp))
            Text("Ver detalle", color = Navy, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Navy, modifier = Modifier.size(16.dp))
        }
    }
}

private fun iconFor(tipo: String): ImageVector = when (tipo) {
    "stock_bajo" -> Icons.Filled.Inventory2
    "agotado" -> Icons.Filled.Close
    "por_cobrar_vencida", "por_cobrar_vencer" -> Icons.Filled.SouthWest
    else -> Icons.Filled.CardMembership
}

private fun iconColorFor(tipo: String): Color = when (tipo) {
    "stock_bajo" -> WarningAmber
    "agotado" -> ErrorRed
    "por_cobrar_vencida", "por_pagar_vencida" -> ErrorRed
    else -> WarningAmber
}
