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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
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
import com.example.contapyme.data.Venta
import com.example.contapyme.data.SampleData
import com.example.contapyme.ui.components.EstadoChip
import com.example.contapyme.ui.components.FilterChipRow
import com.example.contapyme.ui.components.TopHeader
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary

@Composable
fun VentasScreen(
    onBack: () -> Unit,
    onVerDetalle: (String) -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        TopHeader(
            title = "Ventas",
            onBack = onBack,
            stats = listOf(
                "Ventas" to "62",
                "Total mes" to "$186,540",
                "Por cobrar" to "$52,300"
            )
        )
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Buscar cliente", color = TextSecondary, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(14.dp))
                    FilterChipRow(listOf("Todas", "Pagadas", "Parcial", "Pendiente"), selected = 0)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownChip("Fecha")
                        DropdownChip("Vendedor")
                        DropdownChip("Producto")
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ventas", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.weight(1f))
                        Text("5 resultados", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
            items(SampleData.ventas.size) { index ->
                val venta = SampleData.ventas[index]
                VentaCard(venta = venta, onClick = { onVerDetalle(venta.codigo) })
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
}

@Composable
private fun DropdownChip(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = TextSecondary, fontSize = 12.sp)
        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun VentaCard(venta: Venta, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Navy),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ReceiptLong, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "${venta.codigo} · ${venta.cliente}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
                Text(
                    "${venta.fecha} · ${venta.vendedor} · ${venta.numProductos} producto${if (venta.numProductos != 1) "s" else ""}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            EstadoChip(venta.estado)
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$" + "%,.2f".format(venta.total),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
                if (venta.saldoPendiente > 0) {
                    Text(
                        "Saldo $${"%,.2f".format(venta.saldoPendiente)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorRed
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Visibility, contentDescription = null, tint = Navy, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(5.dp))
            Text("Ver detalle", color = Navy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Navy, modifier = Modifier.size(18.dp))
        }
    }
}
