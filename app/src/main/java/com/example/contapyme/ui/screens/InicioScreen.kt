package com.example.contapyme.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contapyme.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val AzulClaro = Color(0xFF1A34AE)
private val AzulMarca = Color(0xFF142A95)
private val AzulProfundo = Color(0xFF0A1A5E)

@Composable
fun InicioScreen(onTimeout: () -> Unit = {}) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.92f) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(900)) }
        launch { scale.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
        launch { progress.animateTo(1f, tween(2200, easing = LinearEasing)) }
        delay(2400)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AzulClaro, AzulMarca, AzulProfundo)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    this.alpha = alpha.value
                    scaleX = scale.value
                    scaleY = scale.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoblanco),
                contentDescription = "ContaPyme",
                modifier = Modifier.width(230.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.height(34.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun InicioScreenPreview() {
    InicioScreen()
}