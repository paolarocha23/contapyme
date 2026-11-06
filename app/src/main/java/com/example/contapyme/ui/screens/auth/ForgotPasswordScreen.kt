package com.example.contapyme.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigateToReset: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var correoOUsuario by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    var token by remember { mutableStateOf("") }
    
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            if (uiState.successLike() && !uiState.isError) {
                sent = true
            }
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Recuperar contraseña",
                        fontWeight = FontWeight.Bold,
                        color = Navy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Navy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF4F6FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SpacerH12()
            
            Icon(
                Icons.Filled.LockReset,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(64.dp)
            )
            
            Text(
                text = if (sent) "Revisa tu correo" else "¿Olvidaste tu contraseña?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Navy,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = if (sent) 
                    "Si el correo o usuario está registrado y activo, recibirás instrucciones para restablecer tu contraseña." 
                else 
                    "Ingresa tu correo electrónico o nombre de usuario y te enviaremos un enlace para restablecer tu contraseña.",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            if (!sent) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "CORREO O NOMBRE DE USUARIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            color = Navy
                        )
                        OutlinedTextField(
                            value = correoOUsuario,
                            onValueChange = { correoOUsuario = it },
                            placeholder = { Text("ejemplo@correo.com o usuario", color = TextLight) },
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = TextSecondary) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BorderSoft,
                                focusedTextColor = Navy,
                                unfocusedTextColor = Navy
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        SpacerH12()
                        
                        PrimaryButton(
                            text = if (uiState.isLoading) "ENVIANDO..." else "ENVIAR INSTRUCCIONES",
                            onClick = { 
                                if (!uiState.isLoading) {
                                    viewModel.forgotPassword(correoOUsuario.trim())
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading && correoOUsuario.isNotBlank()
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "TOKEN DE RECUPERACIÓN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            color = Navy
                        )
                        Text(
                            text = "Ingresa el token que recibiste (o copia el token de desarrollo si aplica)",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it },
                            placeholder = { Text("Pega el token aquí", color = TextLight) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = BorderSoft,
                                focusedTextColor = Navy,
                                unfocusedTextColor = Navy
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        PrimaryButton(
                            text = "CONTINUAR",
                            onClick = { 
                                if (token.isNotBlank()) {
                                    onNavigateToReset(token.trim())
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = token.isNotBlank()
                        )
                    }
                }
            }
        }
    }
}

private fun com.example.contapyme.ui.viewmodel.AuthUiState.successLike(): Boolean {
    return message?.contains("enviado", ignoreCase = true) == true || 
           message?.contains("instrucciones", ignoreCase = true) == true
}
