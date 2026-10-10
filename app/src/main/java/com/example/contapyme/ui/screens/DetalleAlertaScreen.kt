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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.BottomFooterButton
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.StatusChip
import com.example.contapyme.ui.theme.BlueSoft
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber
import com.example.contapyme.ui.theme.WarningAmberBg

@Composable
fun DetalleAlertaScreen(onBack: () -> Unit, onVerVenta: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                    .padding(top = 48.dp, bottom = 18.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable(onClick = onBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Detalle de alerta", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Meta("ID ALERTA", "N-0087")
                    Meta("FECHA", "9 oct 2026")
                    Meta("REFERENCIA", "V-0241")
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NumberedSectionHeader(1, "Mensaje")
                        Spacer(Modifier.width(8.dp))
                        StatusChip("Por vencer", WarningAmberBg, WarningAmber)
                    }
                    SectionCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(WarningAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.SouthWest, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Cuenta por cobrar por vencer", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                                Text("Tipo: cuentas por vencer", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Jorge Ramírez Díaz tiene un saldo de \$3,250.00 que vence en 2 días.",
                            fontSize = 13.sp,
                            color = Navy,
                            lineHeight = 19.sp
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Cuenta relacionada")
                    SectionCard {
                        CuentaRow(Icons.Filled.Person, "CLIENTE", "Jorge Ramírez Díaz", Navy)
                        Spacer(Modifier.height(12.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(12.dp))
                        CuentaRow(Icons.Filled.ReceiptLong, "VENTA", "V-0241 · 7 oct 2026", Navy)
                        Spacer(Modifier.height(12.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(12.dp))
                        CuentaRow(Icons.Filled.CalendarToday, "VENCE", "11 oct 2026", Navy)
                        Spacer(Modifier.height(12.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(12.dp))
                        CuentaRow(Icons.Filled.Schedule, "SALDO PENDIENTE", "$3,250.00", WarningAmber)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlueSoft)
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Las alertas de cuentas se generan cuando vencen o están por vencer en los próximos 7 días.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.height(100.dp))
            }
        }

        Box(Modifier.align(Alignment.BottomCenter)) {
            BottomFooterButton(
                totalLabel = "Saldo",
                totalValue = "$3,250.00",
                buttonText = "Ver venta",
                onClick = onVerVenta
            )
        }
    }
}

@Composable
private fun Meta(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(3.dp))
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CuentaRow(icon: ImageVector, label: String, value: String, valueColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BlueSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp, color = TextSecondary)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}
