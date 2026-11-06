package com.example.contapyme.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            if (!uiState.isError && it.contains("actualizado", ignoreCase = true)) {
                currentPassword = ""
                newPassword = ""
                confirmPassword = ""
            }
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Cambiar contraseña",
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
                Icons.Filled.Key,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(64.dp)
            )
            
            Text(
                text = "Cambiar tu contraseña",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Navy,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Ingresa tu contraseña actual y la nueva contraseña",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
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
                        text = "CONTRASEÑA ACTUAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = Navy
                    )
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        placeholder = { Text("Ingresa tu contraseña actual", color = TextLight) },
                        leadingIcon = { Icon(Icons.Filled.Key, contentDescription = null, tint = TextSecondary) },
                        trailingIcon = {
                            IconButton(onClick = { currentVisible = !currentVisible }) {
                                Icon(
                                    if (currentVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (currentVisible) "Ocultar" else "Mostrar",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (currentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    
                    Text(
                        text = "NUEVA CONTRASEÑA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = Navy
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = { Text("Mínimo 6 caracteres", color = TextLight) },
                        leadingIcon = { Icon(Icons.Filled.Key, contentDescription = null, tint = TextSecondary) },
                        trailingIcon = {
                            IconButton(onClick = { newVisible = !newVisible }) {
                                Icon(
                                    if (newVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (newVisible) "Ocultar" else "Mostrar",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    
                    Text(
                        text = "CONFIRMAR NUEVA CONTRASEÑA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = Navy
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Repite la nueva contraseña", color = TextLight) },
                        leadingIcon = { Icon(Icons.Filled.Key, contentDescription = null, tint = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        text = if (uiState.isLoading) "ACTUALIZANDO..." else "ACTUALIZAR CONTRASEÑA",
                        onClick = { 
                            if (!uiState.isLoading) {
                                viewModel.changePassword(currentPassword, newPassword)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading && currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && newPassword == confirmPassword && newPassword.length >= 6
                    )
                }
            }
        }
    }
}
