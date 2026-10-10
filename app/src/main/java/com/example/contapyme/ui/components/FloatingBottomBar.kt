package com.example.contapyme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.navigation.Routes
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.Navy

@Composable
fun FloatingBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(Navy)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                icon = Icons.Filled.Home,
                label = "Inicio",
                selected = currentRoute == Routes.DASHBOARD,
                onClick = { onNavigate(Routes.DASHBOARD) }
            )
            BottomBarItem(
                icon = Icons.Filled.Inventory2,
                label = "Inventario",
                selected = currentRoute == Routes.INVENTARIO,
                onClick = { onNavigate(Routes.INVENTARIO) }
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(BluePrimary)
                    .clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Acción rápida",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            BottomBarItem(
                icon = Icons.Filled.Groups,
                label = "Clientes",
                selected = currentRoute == Routes.CLIENTES || currentRoute == Routes.AGREGAR_CLIENTE || (currentRoute?.startsWith(Routes.DETALLE_CLIENTE) == true),
                onClick = { onNavigate(Routes.CLIENTES) }
            )
            BottomBarItem(
                icon = Icons.Filled.ReceiptLong,
                label = "Ventas",
                selected = currentRoute == Routes.VENTAS || (currentRoute?.startsWith(Routes.DETALLE_VENTA) == true),
                onClick = { onNavigate(Routes.VENTAS) }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selected) Color.White.copy(alpha = 0.18f) else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) Color.White else Color.White.copy(alpha = 0.55f),
                modifier = Modifier.size(23.dp)
            )
        }
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.55f),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
