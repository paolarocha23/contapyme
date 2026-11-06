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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.GradientEnd
import com.example.contapyme.ui.theme.GradientStart
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.NavyDeep
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.TextDark
import com.example.contapyme.ui.theme.TextSecondary

private val OverlayColor = Color(0xFF070E2C).copy(alpha = 0.75f)

/**
 * Modal inferior con los datos del usuario y la acción destacada de Cerrar Sesión.
 */
@Composable
fun PerfilUsuarioSheet(
    onDismiss: () -> Unit,
    onSolicitarCerrarSesion: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OverlayColor)
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
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Píldora superior de arrastre
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFCBD5E1))
            )

            Spacer(Modifier.height(16.dp))

            // Barra de título y botón cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "MI CUENTA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Navy
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Tarjeta principal del usuario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, BorderSoft, RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar grande con indicador de estado en línea
                    Box {
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(GradientStart, GradientEnd)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LM",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }

                        // Indicador de conexión verde
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Laura Morales",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Filled.VerifiedUser,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = "laura.morales@contapyme.com",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(Modifier.height(8.dp))

                        // Rol / Cargo
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDBEAFE))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "ADMINISTRADORA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = BluePrimary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Información rápida de contexto de sesión
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Business,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "ContaPyme • Matriz",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Sesión segura",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreen
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // BOTÓN ELEGANTE DE CERRAR SESIÓN
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFEF2F2))
                    .border(1.5.dp, Color(0xFFFECACA), RoundedCornerShape(18.dp))
                    .clickable(onClick = onSolicitarCerrarSesion)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Contenedor circular del icono con fondo rojo suave
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = ErrorRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ErrorRed
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Salir de tu cuenta en este dispositivo",
                            fontSize = 12.sp,
                            color = Color(0xFF991B1B).copy(alpha = 0.75f)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Diálogo de confirmación moderno y estilizado para evitar cierres de sesión accidentales.
 */
@Composable
fun CerrarSesionConfirmDialog(
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OverlayColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .fillMaxWidth()
                .shadow(28.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono Hero con doble anillo decorativo en rojo
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEE2E2).copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Título
            Text(
                text = "¿Cerrar sesión?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // Descripción
            Text(
                text = "¿Estás seguro de que deseas salir? Tendrás que volver a ingresar tus credenciales para acceder a tus ventas, clientes e inventario.",
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Cancelar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color(0xFF475569),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Botón Confirmar Salida
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = ErrorRed, ambientColor = ErrorRed)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                            )
                        )
                        .clickable(onClick = onConfirmar),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Sí, salir",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
