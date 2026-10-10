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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
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
import com.example.contapyme.data.SampleData
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.StatusChip
import com.example.contapyme.ui.theme.BlueSoft
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber
import com.example.contapyme.ui.theme.WarningAmberBg

@Composable
fun DetalleVentaScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
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
                Text("Detalle de venta", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
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
                Meta("CÓDIGO DE VENTA", "V-0245")
                Meta("FECHA", "9 oct 2026")
                Meta("VENDEDOR", "Laura M.")
            }
        }

        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(1, "Cliente")
                    SectionCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BlueSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = BluePrimary)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("CLIENTE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp, color = TextSecondary)
                                Text("María Fernanda López Soto", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                            }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Productos", trailing = "3 productos")
                    SectionCard {
                        SampleData.ventaDetalle.productos.forEachIndexed { index, item ->
                            if (index > 0) {
                                Spacer(Modifier.height(10.dp))
                                Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                                Spacer(Modifier.height(10.dp))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(item.producto, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                                    Text(
                                        "${item.codigo} · ${item.cantidad} × $${"%,.2f".format(item.precioUnitario)}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    "$" + "%,.2f".format(item.precioUnitario * item.cantidad),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(3, "Pago")
                    SectionCard {
                        InfoLine(Icons.Filled.CreditCard, "FORMA DE PAGO", "Efectivo")
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        InfoLine(Icons.Filled.Schedule, "CONDICIONES", "Crédito")
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        InfoLine(Icons.Filled.CalendarToday, "VENCE", "30 oct 2026")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NumberedSectionHeader(4, "Resumen")
                        Spacer(Modifier.width(8.dp))
                        StatusChip("Pago parcial", WarningAmberBg, WarningAmber)
                    }
                    SectionCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Subtotal", fontSize = 13.sp, color = TextSecondary)
                            Spacer(Modifier.weight(1f))
                            Text("$16,496.00", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                        }
                        SpacerH12()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Monto pagado", fontSize = 13.sp, color = TextSecondary)
                            Spacer(Modifier.weight(1f))
                            Text("$6,000.00", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                        }
                        SpacerH12()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Saldo pendiente", fontSize = 13.sp, color = TextSecondary)
                            Spacer(Modifier.weight(1f))
                            Text("$10,496.00", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Navy)
                        }
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
                            Spacer(Modifier.weight(1f))
                            Text("$16,496.00", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
                        }
                    }
                    InfoBox {
                        Text(
                            "Esta venta generó una cuenta por cobrar de \$10,496.00 y descontó el inventario.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun Meta(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.4.sp)
        Spacer(Modifier.height(3.dp))
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InfoLine(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BlueSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp, color = TextSecondary)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
        }
    }
}
