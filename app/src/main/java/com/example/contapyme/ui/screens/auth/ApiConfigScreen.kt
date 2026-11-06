package com.example.contapyme.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dns
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.components.PrimaryButton
import com.example.contapyme.ui.components.SpacerH12
import com.example.contapyme.ui.components.SpacerH16
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiConfigScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val apiOrigin by viewModel.apiOrigin.collectAsState(initial = "http://10.0.2.2:5000")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var url by remember(apiOrigin) { mutableStateOf(apiOrigin) }
    
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Configuración de API",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.Dns,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Servidor de la API",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy
                            )
                            Text(
                                text = "Solo configura lo anterior a /api (la app agrega /api automáticamente)",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    
                    SpacerH12()
                    
                    Text(
                        text = "DIRECCIÓN DEL SERVIDOR (sin /api)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.6.sp,
                        color = TextSecondary
                    )
                    
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        placeholder = { Text("http://192.168.1.100:5000", color = TextLight) },
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
                        text = "Se usará: ${com.example.contapyme.data.local.SessionManager.buildApiBaseUrl(url.ifBlank { "http://..." })}",
                        fontSize = 11.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = "Ejemplos:\n• http://10.0.2.2:5000 (Emulador Android)\n• http://192.168.1.XXX:5000 (Dispositivo físico)\n• https://tu-dominio.com (Producción)",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                    
                    SpacerH16()
                    
                    PrimaryButton(
                        text = if (uiState.isLoading) "GUARDANDO..." else "GUARDAR URL",
                        onClick = {
                            if (url.isNotBlank()) {
                                viewModel.setApiOrigin(url)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading && url.isNotBlank()
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4E6)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Nota: Esta configuración se guarda localmente en la app. La API debe estar disponible en la ruta /api para funcionar correctamente.",
                    fontSize = 11.sp,
                    color = Color(0xFFB45309),
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
