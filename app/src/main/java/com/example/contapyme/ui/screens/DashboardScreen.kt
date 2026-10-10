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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.navigation.Routes
import com.example.contapyme.ui.theme.BarChartLight
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber
import com.example.contapyme.ui.data.DashboardAlertItem
import com.example.contapyme.ui.data.dashboardAlerts

@Composable
fun DashboardScreen(
    onNotificaciones: () -> Unit,
    onVerAlerta: (String) -> Unit,
    onVerVentas: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB)),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                    .padding(top = 48.dp, bottom = 24.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Navy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("LM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Viernes, 9 de octubre", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("Hola, Laura", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(onClick = onNotificaciones),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", tint = Navy, modifier = Modifier.size(22.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(ErrorRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("4", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                VentasDelDiaCard()
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Resumen del mes", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                Spacer(Modifier.weight(1f))
                Text("Octubre 2026", fontSize = 13.sp, color = TextSecondary)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Ver detalle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BluePrimary,
                    modifier = Modifier.clickable(onClick = onVerVentas)
                )
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard(
                        icon = Icons.Filled.ArrowUpward,
                        iconTint = BluePrimary,
                        iconBg = Color(0xFFDBEAFE),
                        badgeText = "+12,4%",
                        badgeColor = SuccessGreen,
                        label = "Ventas del mes",
                        value = "$186,540",
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        icon = Icons.Filled.ArrowUpward,
                        iconTint = Navy,
                        iconBg = Color(0xFFE2E8F0),
                        badgeText = "+3,1%",
                        badgeColor = TextSecondary,
                        label = "Gastos del mes",
                        value = "$64,200",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard(
                        icon = Icons.Filled.ArrowDownward,
                        iconTint = BluePrimary,
                        iconBg = Color(0xFFDBEAFE),
                        badgeText = "5 vencidas",
                        badgeColor = ErrorRed,
                        label = "Por cobrar",
                        value = "$52,300",
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        icon = Icons.Filled.CardMembership,
                        iconTint = Navy,
                        iconBg = Color(0xFFE2E8F0),
                        badgeText = "2 vencidos",
                        badgeColor = ErrorRed,
                        label = "Por pagar",
                        value = "$41,800",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item { VentasPorDiaCard() }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Alertas importantes", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                Spacer(Modifier.weight(1f))
                Text(
                    "Ver todas",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BluePrimary,
                    modifier = Modifier.clickable(onClick = onNotificaciones)
                )
            }
        }

        items(dashboardAlerts.size) { index ->
            val group = dashboardAlerts[index]
            AlertGroupCard(
                icon = group.icon,
                iconBg = group.iconBg,
                title = group.title,
                badgeCount = group.badgeCount,
                badgeColor = group.badgeColor,
                items = group.items,
                onItemClick = { onVerAlerta(it) }
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
        item { Spacer(Modifier.height(70.dp)) }
    }
}

@Composable
private fun VentasDelDiaCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.horizontalGradient(listOf(BluePrimary, GradientStart)))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("VENTAS DEL DÍA", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.8.sp)
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Filled.ArrowUpward, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                Spacer(Modifier.width(4.dp))
                Text("+7,4% vs ayer", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(2.dp))
            Text("13,420", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(".00", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(6.dp))
        Text("14 ventas · Ticket promedio $959", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
    }
}

@Composable
private fun KpiCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    badgeText: String,
    badgeColor: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.weight(1f))
            Text(badgeText, color = badgeColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(10.dp))
        Text(label, fontSize = 12.sp, color = TextSecondary)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Navy)
    }
}

@Composable
private fun VentasPorDiaCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("Ventas por día", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text("Últimos 7 días", fontSize = 12.sp, color = TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$76,900", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text("Total semana", fontSize = 11.sp, color = TextSecondary)
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Navy)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("$13.4k", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        val data = com.example.contapyme.data.SampleData.ventasDia
        val max = data.maxOf { it.second }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, (dia, monto) ->
                val isLast = index == data.lastIndex
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((70 * (monto / max)).dp.coerceAtLeast(10.dp))
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(if (isLast) Navy else BarChartLight)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(dia, fontSize = 10.sp, color = if (isLast) Navy else TextSecondary, fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}

@Composable
private fun AlertGroupCard(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    badgeCount: Int,
    badgeColor: Color,
    items: List<DashboardAlertItem>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Navy),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text("$badgeCount", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        items.forEachIndexed { index, item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.alertaId) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                if (index > 0) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFF1F5F9))
                    )
                    Spacer(Modifier.height(10.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(item.nombre, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                        Text(item.detalle, fontSize = 11.sp, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        item.cantidad?.let {
                            Text(it, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                        }
                        item.monto?.let {
                            Text(it, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                        }
                        Text(item.estado, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = item.estadoColor)
                    }
                }
            }
        }
    }
}
