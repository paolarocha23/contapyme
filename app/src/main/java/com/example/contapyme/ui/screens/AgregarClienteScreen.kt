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
import com.example.contapyme.ui.components.InfoBox
import com.example.contapyme.ui.components.NumberedSectionHeader
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SecondaryButton
import com.example.contapyme.ui.components.SectionCard
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.components.SectionTextField
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary

@Composable
fun AgregarClienteScreen(onBack: () -> Unit, onGuardar: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                        .padding(top = 48.dp, bottom = 24.dp)
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
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Agregar cliente", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(1, "Datos personales")
                    SectionCard {
                        SectionTextField("Nombre(s)", "", "Ej. María Fernanda", required = true)
                        SpacerH16()
                        SectionTextField("Apellido paterno", "", "Ej. López", required = true)
                        SpacerH16()
                        SectionTextField("Apellido materno", "", "Ej. Soto", required = true)
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(2, "Contacto")
                    SectionCard {
                        SectionTextField("Teléfono", "", "477 000 0000", required = true)
                        SpacerH16()
                        SectionTextField("Correo electrónico", "", "correo@ejemplo.com")
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberedSectionHeader(3, "Dirección")
                    SectionCard {
                        SectionTextField("Calle", "", "Nombre de la calle", required = true)
                        SpacerH16()
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                SectionTextField("Núm. exterior", "", "123", required = true)
                            }
                            Column(Modifier.weight(1f)) {
                                SectionTextField("C.P.", "", "37000", required = true)
                            }
                        }
                        SpacerH16()
                        SectionTextField("Colonia", "", "Nombre de la colonia", required = true)
                        SpacerH16()
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                SectionTextField("Ciudad", "", "León", required = true)
                            }
                            Column(Modifier.weight(1f)) {
                                SectionTextField("Estado", "", "Guanajuato", required = true)
                            }
                        }
                    }
                    InfoBox {
                        Text(
                            "El cliente se registrará con estatus Activo. Al modificar un cliente, el identificador no se puede cambiar.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(110.dp)) }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(text = "Cancelar", onClick = onBack, modifier = Modifier.weight(1f))
            PrimaryButton(text = "Guardar cliente", onClick = onGuardar, modifier = Modifier.weight(1.4f))
        }
    }
}
