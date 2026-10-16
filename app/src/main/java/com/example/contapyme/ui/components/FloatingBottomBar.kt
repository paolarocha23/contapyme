package com.example.contapyme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.contapyme.navigation.Routes
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.Navy

// Todos los botones del menú miden lo mismo
private val ItemSize = 48.dp
private val ItemShape = RoundedCornerShape(16.dp)

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
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(30.dp))
                .clip(RoundedCornerShape(30.dp))
                .background(Navy)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                icon = Icons.Filled.Home,
                contentDescription = "Inicio",
                selected = currentRoute == Routes.DASHBOARD,
                onClick = { onNavigate(Routes.DASHBOARD) },
                iconSize = 26.dp
            )
            BottomBarItem(
                icon = Icons.Filled.Inventory2,
                contentDescription = "Inventario",
                selected = currentRoute == Routes.INVENTARIO,
                onClick = { onNavigate(Routes.INVENTARIO) },
                iconSize = 24.dp
            )

            Box(
                modifier = Modifier
                    .size(ItemSize)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(BluePrimary)
                    .clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            BottomBarItem(
                icon = Icons.Filled.Groups,
                contentDescription = "Clientes",
                selected = currentRoute == Routes.CLIENTES ||
                        currentRoute == Routes.AGREGAR_CLIENTE ||
                        (currentRoute?.startsWith(Routes.DETALLE_CLIENTE) == true),
                onClick = { onNavigate(Routes.CLIENTES) },
                iconSize = 27.dp
            )
            BottomBarItem(
                icon = Icons.Filled.ReceiptLong,
                contentDescription = "Ventas",
                selected = currentRoute == Routes.VENTAS ||
                        (currentRoute?.startsWith(Routes.DETALLE_VENTA) == true),
                onClick = { onNavigate(Routes.VENTAS) },
                iconSize = 24.dp
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
    iconSize: Dp = 24.dp
) {
    Box(
        modifier = Modifier
            .size(ItemSize)
            .clip(ItemShape)
            .background(
                if (selected) Color.White.copy(alpha = 0.18f) else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(iconSize)
        )
    }
}