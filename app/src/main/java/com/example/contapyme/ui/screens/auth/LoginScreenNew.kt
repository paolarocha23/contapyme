package com.example.contapyme.ui.screens.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH20
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.theme.BlueDark
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreenNew(
    viewModel: AuthViewModel,
    onRequires2FA: () -> Unit,
    onAlreadyLoggedIn: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToApiConfig: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    
    // Paso 1 completado: el servidor envió el código 2FA
    LaunchedEffect(uiState.requires2FA, uiState.tempToken) {
        if (uiState.requires2FA && !uiState.tempToken.isNullOrBlank()) {
            onRequires2FA()
        }
    }
    
    // Sesión ya activa (restaurada o verificada)
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onAlreadyLoggedIn()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))
            LogoContaPyme()
            Spacer(Modifier.height(60.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "NOMBRE DE USUARIO O CORREO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = Navy
                    )
                    SpacerH8()
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Ingresa tu usuario o correo", color = TextLight) },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = TextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy,
                            unfocusedBorderColor = BorderSoft,
                            focusedTextColor = Navy,
                            unfocusedTextColor = Navy
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    SpacerH16()
                    Text(
                        text = "CONTRASEÑA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = Navy
                    )
                    SpacerH8()
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("••••••••", color = TextLight) },
                        leadingIcon = { Icon(Icons.Filled.Visibility, contentDescription = null, tint = TextSecondary) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy,
                            unfocusedBorderColor = BorderSoft,
                            focusedTextColor = Navy,
                            unfocusedTextColor = Navy
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    SpacerH12()
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy,
                            modifier = Modifier.clickable { onNavigateToForgotPassword() }
                        )
                    }
                    SpacerH20()
                    PrimaryButton(
                        text = if (uiState.isLoading) "VERIFICANDO..." else "INICIAR SESIÓN",
                        onClick = { 
                            if (!uiState.isLoading) {
                                viewModel.login(username.trim(), password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    )
                }
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
        
        IconButton(
            onClick = onNavigateToApiConfig,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Configurar API",
                tint = TextSecondary.copy(alpha = 0.6f)
            )
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
