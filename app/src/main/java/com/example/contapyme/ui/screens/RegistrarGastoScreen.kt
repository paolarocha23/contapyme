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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.AppTextField
import com.example.contapyme.ui.components.BottomFooterButton
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH4
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.components.InlineDot
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.TextSecondary

@Composable
fun RegistrarGastoScreen(onBack: () -> Unit, onGuardar: () -> Unit) {
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Registrar gasto", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
                        MetaItem("CÓDIGO DE GASTO", "G-0087")
                        MetaItem("ADMINISTRADOR", "Paola M.")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("ESTATUS", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    InlineDot(SuccessGreen)
                                    Spacer(Modifier.width(5.dp))
                                    Text("Activo", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(1, "Monto")
                    SectionCard {
                        Text("MONTO DEL GASTO *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        AppTextField(value = "", placeholder = "Ingresa el monto")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Detalle")
                    SectionCard {
                        Text("CONCEPTO *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        AppTextField(value = "Pago de internet empresarial", placeholder = "Concepto")
                        SpacerH16()
                        Text("CATEGORÍA *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        CategoryChips()
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(3, "Pago")
                    SectionCard {
                        Text("FECHA DEL GASTO *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH4()
                        AppTextField(value = "9 oct 2026", placeholder = "Fecha")
                        SpacerH16()
                        Text("FORMA DE PAGO *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp, color = TextSecondary)
                        SpacerH8()
                        SegmentedSelector(
                            listOf("Efectivo" to Icons.Filled.Payments, "Tarjeta" to Icons.Filled.Schedule),
                            selected = 0
                        )
                        SpacerH12()
                        InfoBox {
                            Text("Los gastos en efectivo registran una salida en caja.", fontSize = 12.sp, color = TextSecondary)
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
                            Text("Escribe una nota para este gasto...", color = TextSecondary.copy(alpha = 0.7f), fontSize = 13.sp)
                        }
                    }
                    InfoBox {
                        Text(
                            "Se registrará una salida de caja de \$3,450.00 por este gasto de la categoría Servicios.",
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
                totalLabel = "Total gasto",
                totalValue = "$3,450.00",
                buttonText = "Guardar gasto",
                onClick = onGuardar
            )
        }
    }
}

@Composable
private fun MetaItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        SpacerH4()
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategoryChips() {
    val categorias = listOf("Renta", "Servicios", "Nómina", "Publicidad", "Transporte", "Mantenimiento", "Otros")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.take(3).forEach { Chip(it, selected = it == "Servicios") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.drop(3).take(2).forEach { Chip(it, selected = false) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.drop(5).forEach { Chip(it, selected = false) }
        }
    }
}

@Composable
private fun Chip(text: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Navy else Color.White)
            .then(
                if (selected) Modifier else Modifier.background(Color(0xFFF1F5F9))
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(4.dp))
        }
        Text(
            text,
            color = if (selected) Color.White else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
