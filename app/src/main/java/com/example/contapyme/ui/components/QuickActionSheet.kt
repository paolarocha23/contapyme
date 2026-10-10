package com.example.contapyme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.Navy

@Composable
fun QuickActionSheet(
    onDismiss: () -> Unit,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .clickable(enabled = false) {}
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ACCIÓN RÁPIDA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Navy)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Navy, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            QuickActionCard(
                dark = true,
                icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(26.dp)) },
                title = "Registrar venta",
                subtitle = "Cliente, productos y forma de pago",
                onClick = onRegistrarVenta
            )
            Spacer(Modifier.height(12.dp))
            QuickActionCard(
                dark = false,
                icon = { Icon(Icons.Filled.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp)) },
                title = "Registrar gasto",
                subtitle = "Concepto, categoría y monto",
                onClick = onRegistrarGasto
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun QuickActionCard(
    dark: Boolean,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val bg = if (dark) Navy else Color.White
    val titleColor = if (dark) Color.White else Navy
    val subtitleColor = if (dark) Color.White.copy(alpha = 0.75f) else Color(0xFF64748B)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (dark) Color.White else BluePrimary),
            contentAlignment = Alignment.Center
        ) { icon() }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(text = title, color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = subtitleColor, fontSize = 12.sp)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (dark) Color.White.copy(alpha = 0.7f) else BluePrimary
        )
    }
}
