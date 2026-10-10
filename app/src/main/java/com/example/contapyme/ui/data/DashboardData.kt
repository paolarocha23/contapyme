package com.example.contapyme.ui.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.WarningAmber

data class DashboardAlertItem(
    val alertaId: String,
    val nombre: String,
    val detalle: String,
    val cantidad: String? = null,
    val monto: String? = null,
    val estado: String,
    val estadoColor: Color
)

data class DashboardAlertGroup(
    val icon: ImageVector,
    val iconBg: Color,
    val title: String,
    val badgeCount: Int,
    val badgeColor: Color,
    val items: List<DashboardAlertItem>
)

val dashboardAlerts = listOf(
    DashboardAlertGroup(
        icon = Icons.Filled.Inventory2,
        iconBg = Color(0xFF1E3A8A),
        title = "Existencias bajas",
        badgeCount = 6,
        badgeColor = WarningAmber,
        items = listOf(
            DashboardAlertItem("N-0091", "SSD Kingston 480 GB", "Mínimo 8 pz", cantidad = "3 pz", estado = "Stock bajo", estadoColor = WarningAmber),
            DashboardAlertItem("N-0084", "Cable HDMI 2 m", "Mínimo 10 pz", cantidad = "4 pz", estado = "Stock bajo", estadoColor = WarningAmber)
        )
    ),
    DashboardAlertGroup(
        icon = Icons.Filled.Inventory2,
        iconBg = Color(0xFF1E3A8A),
        title = "Productos agotados",
        badgeCount = 3,
        badgeColor = ErrorRed,
        items = listOf(
            DashboardAlertItem("N-0090", "Mouse Logitech M185", "Última venta hace 2 días", cantidad = "0 pz", estado = "Agotado", estadoColor = ErrorRed),
            DashboardAlertItem("N-0093", "Cargador USB-C 65 W", "Última venta ayer", cantidad = "0 pz", estado = "Agotado", estadoColor = ErrorRed)
        )
    ),
    DashboardAlertGroup(
        icon = Icons.Filled.SouthWest,
        iconBg = Color(0xFF1E3A8A),
        title = "Cuentas por cobrar vencidas",
        badgeCount = 5,
        badgeColor = ErrorRed,
        items = listOf(
            DashboardAlertItem("N-0089", "Soluciones TI del Bajío", "Venta V-0231 · venció hace 5 días", monto = "$12,400", estado = "Vencida", estadoColor = ErrorRed),
            DashboardAlertItem("N-0092", "Oficinas Delta", "Venta V-0224 · venció hace 3 días", monto = "$8,900", estado = "Vencida", estadoColor = ErrorRed)
        )
    ),
    DashboardAlertGroup(
        icon = Icons.Filled.CardMembership,
        iconBg = Color(0xFF1E3A8A),
        title = "Pagos pendientes vencidos",
        badgeCount = 2,
        badgeColor = ErrorRed,
        items = listOf(
            DashboardAlertItem("N-0086", "Tecnomayoreo S.A.", "Compra C-0118 · venció hace 2 días", monto = "$18,600", estado = "Vencido", estadoColor = ErrorRed),
            DashboardAlertItem("N-0085", "Distribuidora Digital", "Compra C-0112 · venció hace 6 días", monto = "$7,350", estado = "Vencido", estadoColor = ErrorRed)
        )
    )
)
