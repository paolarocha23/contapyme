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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import com.example.contapyme.ui.components.BottomFooterButton
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BlueSoft
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber

@Composable
fun DetalleClienteScreen(onBack: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable(onClick = onBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Detalle del cliente", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    }
                    SpacerH16()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("MF", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("María Fernanda López Soto", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Cliente desde 14 mar 2026", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                        }
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
                        Kpi("VENTAS", "12")
                        Kpi("COBRADO", "$48,200")
                        Kpi("SALDO", "$10,496")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(1, "Estatus")
                    SectionCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Cliente activo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                                Text("Puede seleccionarse en nuevas ventas", fontSize = 11.sp, color = TextSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .width(52.dp)
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(SuccessGreen),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 3.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                        SpacerH12()
                        InfoBox(background = Color(0xFFF1F5F9)) {
                            Text(
                                "Si lo desactivas no podrá elegirse en nuevas ventas, pero conserva su historial.",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Contacto")
                    SectionCard {
                        ContactRow(Icons.Filled.Phone, "TELÉFONO", "477 598 2210")
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        ContactRow(Icons.Filled.Email, "CORREO", "mafer.lopez@correo.mx")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(3, "Dirección")
                    SectionCard {
                        ContactRow(Icons.Filled.Place, "CALLE Y NÚMERO", "Blvd. Adolfo López Mateos 1520")
                        Spacer(Modifier.height(10.dp))
                        ContactRow(Icons.Filled.Place, "COLONIA · C.P.", "Jardines del Moral · 37160")
                        Spacer(Modifier.height(10.dp))
                        ContactRow(Icons.Filled.Place, "CIUDAD · ESTADO", "León · Guanajuato")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(4, "Historial de operaciones")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE8EEFC))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Navy)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ventas", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cobros", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                    SpacerH12()
                    SectionCard {
                        SampleData.historialVentasCliente.forEachIndexed { index, (codigo, meta, pair) ->
                            val (monto, estado) = pair
                            if (index > 0) {
                                Spacer(Modifier.height(8.dp))
                                Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                                Spacer(Modifier.height(8.dp))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(codigo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                                    Text(meta, fontSize = 11.sp, color = TextSecondary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "$" + "%,.2f".format(monto),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Navy
                                    )
                                    Text(
                                        estado,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (estado == "Pagada") SuccessGreen else WarningAmber
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        SampleData.historialCobrosCliente.forEachIndexed { index, cobro ->
                            if (index > 0) {
                                Spacer(Modifier.height(8.dp))
                                Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                                Spacer(Modifier.height(8.dp))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text("Cobro · ${cobro.referencia}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy)
                                    Text("${cobro.fecha} · ${cobro.forma}", fontSize = 11.sp, color = TextSecondary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "$" + "%,.2f".format(cobro.monto),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Navy
                                    )
                                    Text(
                                        cobro.estado,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (cobro.estado == "Liquidado") SuccessGreen else BluePrimary
                                    )
                                }
                            }
                        }
                    }
                    SpacerH12()
                    SectionCard {
                        TotalRow("Total vendido", "$58,696.00")
                        SpacerH12()
                        TotalRow("Total cobrado", "$48,200.00")
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSoft))
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Saldo pendiente", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                            Spacer(Modifier.weight(1f))
                            Text("$10,496.00", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }

        Box(Modifier.align(Alignment.BottomCenter)) {
            BottomFooterButton(
                totalLabel = "Saldo",
                totalValue = "$10,496.00",
                buttonText = "Ver venta",
                onClick = {}
            )
        }
    }
}

@Composable
private fun Kpi(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
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
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Navy)
        }
    }
}

@Composable
private fun TotalRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Spacer(Modifier.weight(1f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
    }
}
