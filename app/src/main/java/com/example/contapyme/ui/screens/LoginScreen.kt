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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.AppTextField
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.components.SpacerH20
import com.example.contapyme.ui.theme.BlueDark
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextSecondary

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))
        LogoContaPyme()
        Spacer(Modifier.height(100.dp))

        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "NOMBRE DE USUARIO",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                color = Navy
            )
            SpacerH8()
            AppTextField(value = "", placeholder = "Ingresa tu usuario", icon = Icons.Filled.Person)
            SpacerH16()
            Text(
                text = "CONTRASEÑA",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                color = Navy
            )
            SpacerH8()
            AppTextField(value = "", placeholder = "••••••••", icon = Icons.Filled.Visibility, isPassword = true)
            SpacerH12()
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy
                )
            }
            SpacerH20()
            PrimaryButton(
                text = "INICIAR SESIÓN",
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 40.dp)) {
            Text(
                text = "¿Problemas para ingresar?",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            SpacerH8()
            Text(
                text = "Contacta al administrador",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )
        }
    }
}

@Composable
fun LogoContaPyme(compact: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(if (compact) 36.dp else 52.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(BlueDark, Color(0xFF3B82F6)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.TrendingUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(if (compact) 22.dp else 32.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = "Conta",
                    fontSize = if (compact) 20.sp else 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
                Text(
                    text = "Pyme",
                    fontSize = if (compact) 20.sp else 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
            }
        }
        SpacerH12()
        Text(
            text = "Administración y Contabilidad para PyMEs",
            fontSize = if (compact) 9.sp else 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Navy
        )
    }
}
