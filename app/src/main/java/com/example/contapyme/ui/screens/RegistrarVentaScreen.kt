package com.example.contapyme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.data.VentaItem
import com.example.contapyme.ui.components.AppTextField
import com.example.contapyme.ui.components.BottomFooterButton
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH4
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.components.StatusChip
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber
import com.example.contapyme.ui.theme.WarningAmberBg
import com.example.contapyme.ui.theme.SuccessGreenBg
import com.example.contapyme.ui.theme.SuccessGreen
import androidx.compose.ui.graphics.Brush
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart

@Composable
fun RegistrarVentaScreen(
    onBack: () -> Unit,
    onGuardar: () -> Unit
) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                        .padding(top = 48.dp, bottom = 20.dp)
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
                            Icon(
                                androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Registrar venta", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    SpacerH16()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetaInfo("CÓDIGO DE VENTA", "V-0245")
                        MetaInfo("FECHA", "9 oct 2026")
                        MetaInfo("VENDEDOR", "Laura M.")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(1, "Cliente (opcional)")
                    SectionCard {
                        AppTextField(value = "", placeholder = "Buscar cliente por nombre...", icon = Icons.Filled.Search)
                        SpacerH12()
                        InfoBox {
                            Text(
                                "Obligatorio solo si la venta es a crédito. En ventas de contado puedes continuar sin cliente.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Productos", trailing = "3 agregados")
                    SectionCard {
                        val items = listOf(
                            VentaItem("Laptop Lenovo IdeaPad 3 15\"", "LEN-IP3", 1, 9499.0, 8),
                            VentaItem("Monitor Samsung 24\" Full HD", "SAM-M24", 2, 2899.0, 12),
                            VentaItem("Audífonos Sony WH-CH520", "SNY-S20", 1, 1199.0, 3, stockBajo = true)
                        )
                        items.forEachIndexed { index, item ->
                            if (index > 0) {
                                Spacer(Modifier.height(10.dp))
                                Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                                Spacer(Modifier.height(10.dp))
                            }
                            ProductoRow(item)
                        }
                        SpacerH12()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.5.dp, BluePrimary, RoundedCornerShape(10.dp))
                                .background(Color(0xFFEFF4FF))
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.AddCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Agregar producto o servicio", color = BluePrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(3, "Pago")
                    SectionCard {
                        Text("FORMA DE PAGO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        SegmentedSelector(listOf("Efectivo" to Icons.Filled.Payments, "Tarjeta" to Icons.Filled.Schedule), selected = 0)
                        SpacerH16()
                        Text("CONDICIONES DE PAGO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        SegmentedSelector(listOf("Contado" to Icons.Filled.Schedule, "Crédito" to Icons.Filled.Schedule), selected = 1)
                        SpacerH16()
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text("VENCE *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                                SpacerH4()
                                AppTextField(value = "30 oct 2026", placeholder = "Fecha")
                            }
                            Column(Modifier.weight(1f)) {
                                Text("MONTO PAGADO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                                SpacerH4()
                                AppTextField(value = "$ 6,000.00", placeholder = "$ 0.00")
                            }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(4, "Observaciones", trailing = "Opcional")
                    SectionCard {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Text("Escribe una nota para esta venta...", color = TextSecondary.copy(alpha = 0.7f), fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NumberedSectionHeader(5, "Resumen")
                        Spacer(Modifier.width(8.dp))
                        StatusChip("Pago parcial", WarningAmberBg, WarningAmber)
                    }
                    SectionCard {
                        ResumenRow("Subtotal", "$16,496.00")
                        SpacerH12()
                        ResumenRow("Monto pagado", "$6,000.00")
                        SpacerH12()
                        ResumenRow("Saldo pendiente", "$10,496.00", bold = true)
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Total", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
                            Spacer(Modifier.weight(1f))
                            Text("$16,496.00", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                        }
                    }
                    InfoBox {
                        Text(
                            "Se generará una cuenta por cobrar de \$10,496.00 para el cliente de la venta y se descontará el inventario.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }

        Box(Modifier.align(Alignment.BottomCenter)) {
            BottomFooterButton(
                totalLabel = "Saldo",
                totalValue = "$10,496.00",
                buttonText = "Guardar venta",
                onClick = onGuardar
            )
        }
    }
}

@Composable
private fun MetaInfo(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        SpacerH4()
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProductoRow(item: VentaItem) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(item.producto, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                SpacerH4()
                Text(
                    "${item.codigo} · Disp. ${item.disponibilidad} pz",
                    fontSize = 11.sp,
                    color = if (item.stockBajo) WarningAmber else TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$" + "%,.2f".format(item.precioUnitario * item.cantidad),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
            }
        }
        SpacerH8()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.RemoveCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(26.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BluePrimary)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text("${item.cantidad}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Icon(Icons.Filled.AddCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                "× $" + "%,.2f".format(item.precioUnitario),
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(Modifier.weight(1f))
            Icon(Icons.Filled.Delete, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun SegmentedSelector(options: List<Pair<String, androidx.compose.ui.graphics.vector.ImageVector>>, selected: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F5F9))
            .padding(4.dp)
    ) {
        options.forEachIndexed { index, (label, icon) ->
            val isSelected = index == selected
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Navy else Color.Transparent)
                    .padding(vertical = 9.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = if (isSelected) Color.White else TextSecondary, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    label,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun ResumenRow(label: String, value: String, bold: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            fontSize = 13.sp,
            color = if (bold) Navy else TextSecondary,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(Modifier.weight(1f))
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold,
            color = if (bold && value.contains("10,496")) Navy else Navy
        )
    }
}
