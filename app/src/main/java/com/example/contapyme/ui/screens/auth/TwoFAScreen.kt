package com.example.contapyme.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.components.SpacerH20
import com.example.contapyme.ui.components.SpacerH8
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFAScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var code by remember { mutableStateOf("") }
    
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onVerified()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Verificación en dos pasos",
                        fontWeight = FontWeight.Bold,
                        color = Navy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.reset2FAFlow()
                        onBack()
                    }) {
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
                Icons.Filled.Security,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(64.dp)
            )
            
            Text(
                text = "Código de verificación",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Navy,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Hemos enviado un código de 6 dígitos a tu correo electrónico${uiState.emailMasked?.let { "\n$it" } ?: ""}",
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                        Text(
                            text = "INGRESA EL CÓDIGO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            color = Navy
                        )
                    }
                    
                    OutlinedTextField(
                        value = code,
                        onValueChange = { if (it.length <= 6) code = it.filter { c -> c.isDigit() } },
                        placeholder = { Text("000000", color = TextLight) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BorderSoft,
                            focusedTextColor = Navy,
                            unfocusedTextColor = Navy,
                            cursorColor = BluePrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            letterSpacing = 8.sp,
                            color = Navy
                        )
                    )
                    
                    SpacerH12()
                    
                    PrimaryButton(
                        text = if (uiState.isLoading) "VERIFICANDO..." else "VERIFICAR",
                        onClick = { 
                            if (!uiState.isLoading && code.length == 6) {
                                viewModel.verify2FA(code)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading && code.length == 6
                    )
                    
                    TextButton(
                        onClick = { if (!uiState.isLoading) viewModel.resend2FA() },
                        enabled = !uiState.isLoading
                    ) {
                        Text(
                            text = "Reenviar código",
                            color = if (uiState.isLoading) TextSecondary else BluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
