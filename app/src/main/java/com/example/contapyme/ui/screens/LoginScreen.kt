package com.example.contapyme.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.R

// Colores del login (puedes cambiarlos por los de tu theme)
private val AzulMarca = Color(0xFF142A95)
private val AzulClaroBoton = Color(0xFF2A44C9)
private val AzulProfundo = Color(0xFF0B1C66)
private val TextoOscuro = Color(0xFF0F1B4D)
private val TextoGris = Color(0xFF6B7280)
private val FondoCampo = Color(0xFFF5F7FD)
private val BordeCampo = Color(0xFFE2E7F6)
private val FondoIcono = Color(0xFFE6EBFC)
private val Placeholder = Color(0xFFA3A9BC)

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit = {}
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var verContrasena by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFE6ECFF), Color(0xFFF2F5FF)))
            )
    ) {
        // Parte de arriba: logo azul
        Box(
            modifier = Modifier
                .weight(0.33f)
                .fillMaxWidth()
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            LogoContaPyme()
        }

        // Hoja blanca redondeada
        Column(
            modifier = Modifier
                .weight(0.67f)
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp),
                    ambientColor = AzulMarca,
                    spotColor = AzulMarca
                )
                .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
                .background(Color.White)
                .imePadding()
        ) {

            // Formulario
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp)
                    .padding(top = 24.dp)
            ) {
                EtiquetaCampo("NOMBRE DE USUARIO")
                CampoLogin(
                    value = usuario,
                    onValueChange = { usuario = it },
                    placeholder = "Ingresa tu usuario",
                    icono = Icons.Filled.Person
                )

                Spacer(Modifier.height(20.dp))

                EtiquetaCampo("CONTRASEÑA")
                CampoLogin(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    placeholder = "Ingresa tu contrasena",
                    icono = Icons.Filled.Lock,
                    esContrasena = true,
                    contrasenaVisible = verContrasena,
                    onToggleVisible = { verContrasena = !verContrasena }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulMarca,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onForgotPassword() }
                )

                Spacer(Modifier.height(24.dp))

                BotonIniciarSesion(onClick = onLogin)
            }

            // Ayuda al pie (sobre fondo blanco)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(top = 12.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Problemas para ingresar?",
                    fontSize = 12.sp,
                    color = TextoGris
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Contacta al administrador",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoOscuro
                )
            }
        }
    }
}

@Composable
private fun EtiquetaCampo(texto: String) {
    Text(
        text = texto,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.8.sp,
        color = AzulMarca,
        modifier = Modifier.padding(bottom = 9.dp)
    )
}

@Composable
private fun CampoLogin(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icono: ImageVector,
    esContrasena: Boolean = false,
    contrasenaVisible: Boolean = false,
    onToggleVisible: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(FondoCampo)
            .border(1.dp, BordeCampo, RoundedCornerShape(18.dp))
            .padding(start = 10.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cuadrito del ícono
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(FondoIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = AzulMarca, modifier = Modifier.size(19.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = TextoOscuro,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(AzulMarca),
            visualTransformation = if (esContrasena && !contrasenaVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esContrasena) KeyboardType.Password else KeyboardType.Text
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = Placeholder, fontSize = 14.5.sp)
                    }
                    innerTextField()
                }
            }
        )

        if (esContrasena) {
            Icon(
                imageVector = if (contrasenaVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = "Mostrar u ocultar contraseña",
                tint = Color(0xFF9AA1B8),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onToggleVisible() }
            )
        }
    }
}

@Composable
private fun BotonIniciarSesion(onClick: () -> Unit) {
    val forma = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(16.dp, forma, ambientColor = AzulMarca, spotColor = AzulMarca)
            .clip(forma)
            .background(
                Brush.linearGradient(listOf(AzulClaroBoton, AzulMarca, AzulProfundo))
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "INICIAR SESIÓN",
            color = Color.White,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.4.sp
        )
        Spacer(Modifier.width(12.dp))
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun LogoContaPyme(compact: Boolean = false) {
    Image(
        painter = painterResource(id = R.drawable.logoazul),
        contentDescription = "ContaPyme",
        modifier = Modifier.width(if (compact) 120.dp else 230.dp),
        contentScale = ContentScale.FillWidth
    )
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onLogin = {})
}