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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
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
import com.example.contapyme.data.Producto
import com.example.contapyme.data.SampleData
import com.example.contapyme.ui.components.EstadoChip
import com.example.contapyme.ui.components.FilterChipRow
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.TopHeader
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber

@Composable
fun InventarioScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        TopHeader(
            title = "Inventario",
            onBack = onBack,
            stats = listOf(
                "Productos" to "9",
                "Stock bajo" to "3",
                "Agotados" to "2"
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
                        Text("Buscar producto o código...", color = TextSecondary, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(14.dp))
                    FilterChipRow(listOf("Todos", "Disponibles", "Bajo", "Agotados"), selected = 0)
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Productos", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.weight(1f))
                        Text("9 resultados", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
            items(SampleData.productos.size) { index ->
                ProductoCard(SampleData.productos[index], expanded = index == 0)
            }
            item {
                Column(Modifier.padding(16.dp)) {
                    InfoBox {
                        Text(
                            "Toca un producto para ver el detalle. Las entradas, salidas y ajustes se registran desde la versión de escritorio.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(Modifier.height(90.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductoCard(producto: Producto, expanded: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable {}
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Navy),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Inventory2, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy, maxLines = 1)
                Text(
                    "${producto.codigo} · ${producto.categoria}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$" + "%,.2f".format(producto.precio),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
                Text(
                    "${producto.existencia} pz",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        producto.existencia == 0 -> ErrorRed
                        producto.existencia < producto.minimo -> WarningAmber
                        else -> SuccessGreen
                    }
                )
            }
        }
        if (expanded) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MiniStat("EXISTENCIA", "${producto.existencia} pz", Navy)
                MiniStat("MÍNIMO", "${producto.minimo} pz", Navy)
                MiniStat("ESTADO", "Disponible", SuccessGreen)
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp, color = TextSecondary)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
