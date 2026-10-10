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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.data.Cliente
import com.example.contapyme.data.SampleData
import com.example.contapyme.ui.components.EstadoChip
import com.example.contapyme.ui.components.RowFilterChips
import com.example.contapyme.ui.components.FilterChipRow
import com.example.contapyme.ui.components.TopHeader
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary

@Composable
fun ClientesScreen(
    onBack: () -> Unit,
    onAgregar: () -> Unit,
    onVerDetalle: (String) -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        TopHeader(title = "Clientes", onBack = onBack)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(BluePrimary)
                            .clickable(onClick = onAgregar)
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir cliente", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Buscar cliente por nombre...", color = TextSecondary, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(14.dp))
                    FilterChipRow(listOf("Todos", "Activos", "Inactivos"), selected = 0)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(listOf("Con adeudo", "Sin adeudo", "Ciudad", "Estado")[0], outlined = true)
                        FilterChip("Sin adeudo", outlined = false)
                        FilterChip("Ciudad", outlined = false)
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Clientes", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.weight(1f))
                        Text("5 resultados", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
            items(SampleData.clientes.size) { index ->
                val cliente = SampleData.clientes[index]
                ClienteCard(cliente = cliente, onClick = { onVerDetalle(cliente.id) })
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
}

@Composable
private fun FilterChip(text: String, outlined: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (outlined) Color.White else Navy)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text,
            color = if (outlined) Navy else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ClienteCard(cliente: Cliente, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (cliente.activo) Navy else Color(0xFF94A3B8)),
                contentAlignment = Alignment.Center
            ) {
                Text(cliente.iniciales, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(cliente.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text("${cliente.id} · ${cliente.ciudad}", fontSize = 12.sp, color = TextSecondary)
            }
            EstadoChip(if (cliente.activo) "Activo" else "Inactivo")
        }
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Phone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(cliente.telefono, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.weight(1f))
            if (cliente.adeudo > 0) {
                Text(
                    "Debe $${"%,.2f".format(cliente.adeudo)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ErrorRed
                )
            } else {
                Text("Sin adeudo", fontSize = 12.sp, color = TextSecondary)
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Email, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(cliente.correo, fontSize = 12.sp, color = TextSecondary)
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .then(Modifier)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Visibility, contentDescription = null, tint = Navy, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text("Ver detalle", color = Navy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Navy, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}
