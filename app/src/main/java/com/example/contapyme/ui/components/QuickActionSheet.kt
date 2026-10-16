package com.example.contapyme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
private val AzulMarca = Color(0xFF142A95)
private val AzulClaro = Color(0xFF2A44C9)
private val AzulProfundo = Color(0xFF0B1C66)
private val FondoOverlay = Color(0xFF091652)
private val TextoGris = Color(0xFF6B7280)


@Composable
fun QuickActionSheet(
    onDismiss: () -> Unit,
    onRegistrarVenta: () -> Unit,
    onRegistrarGasto: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOverlay.copy(alpha = 0.80f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "ACCIÓN RÁPIDA",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.2.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 4.dp, bottom = 2.dp)
            )

            QuickActionCard(
                destacada = true,
                icono = Icons.Filled.ShoppingCart,
                titulo = "Registrar venta",
                subtitulo = "Cliente, productos y forma de pago",
                onClick = onRegistrarVenta
            )

            QuickActionCard(
                destacada = false,
                icono = Icons.Filled.CreditCard,
                titulo = "Registrar gasto",
                subtitulo = "Concepto, categoría y monto",
                onClick = onRegistrarGasto
            )

            Spacer(Modifier.height(8.dp))

            // Botón X para salir
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar",
                    tint = AzulProfundo,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    destacada: Boolean,
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(24.dp)
    val fondo = if (destacada)
        Modifier.background(Brush.linearGradient(listOf(AzulClaro, AzulMarca, AzulProfundo)))
    else
        Modifier.background(Color.White)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(18.dp, forma, ambientColor = Color.Black, spotColor = Color.Black)
            .clip(forma)
            .then(fondo)
            .then(
                if (destacada) Modifier.border(1.dp, Color.White.copy(alpha = 0.18f), forma)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cuadro del ícono
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (destacada) Color.White else AzulMarca),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (destacada) AzulMarca else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (destacada) Color.White else Color(0xFF0F1B4D)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitulo,
                fontSize = 11.5.sp,
                lineHeight = 15.sp,
                color = if (destacada) Color.White.copy(alpha = 0.78f) else TextoGris
            )
        }

        Spacer(Modifier.width(10.dp))

        // Circulito con flecha
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (destacada) Color.White.copy(alpha = 0.18f) else Color(0xFFEEF1FC)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (destacada) Color.White else AzulMarca,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}