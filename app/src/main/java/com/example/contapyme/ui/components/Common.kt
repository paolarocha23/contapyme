package com.example.contapyme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contapyme.ui.theme.BluePrimary
import com.example.contapyme.ui.theme.BorderSoft
import com.example.contapyme.ui.theme.ErrorRed
import com.example.contapyme.ui.theme.ErrorRedBg
import com.example.contapyme.ui.theme.Navy
import com.example.contapyme.ui.theme.SuccessGreen
import com.example.contapyme.ui.theme.SuccessGreenBg
import com.example.contapyme.ui.theme.TextLight
import com.example.contapyme.ui.theme.TextSecondary
import com.example.contapyme.ui.theme.WarningAmber
import com.example.contapyme.ui.theme.WarningAmberBg

@Composable
fun StatusChip(text: String, container: Color, content: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(container)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = content, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun EstadoChip(estado: String) {
    val (container, content) = when {
        estado == "Activo" || estado == "Pagada" || estado == "Liquidado" || estado == "Disponible" ->
            SuccessGreenBg to SuccessGreen
        estado == "Inactivo" || estado == "Cancelada" || estado == "Agotado" ->
            Color(0xFFE2E8F0) to TextSecondary
        estado == "Pendiente" || estado == "Vencida" ->
            ErrorRedBg to ErrorRed
        else ->
            WarningAmberBg to WarningAmber
    }
    StatusChip(estado, container, content)
}

@Composable
fun SectionLabel(text: String, required: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = TextSecondary
        )
        if (required) {
            Text(text = " *", color = ErrorRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        placeholder = { Text(placeholder, color = TextLight) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null, tint = TextSecondary) } },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Navy,
            disabledBorderColor = BorderSoft,
            disabledContainerColor = Color(0xFFF8FAFC),
            disabledPlaceholderColor = TextLight,
            disabledLeadingIconColor = TextSecondary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SectionTextField(label: String, value: String, placeholder: String, required: Boolean = false) {
    Column {
        SectionLabel(label, required)
        Spacer(Modifier.height(6.dp))
        AppTextField(value = value, placeholder = placeholder)
    }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) Navy else Navy.copy(alpha = 0.5f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.5.dp, Navy, RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Navy, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun AddProductButton(text: String = "Agregar producto o servicio") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, BluePrimary, RoundedCornerShape(12.dp))
            .background(Color(0xFFEFF4FF))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.AddCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = text, color = BluePrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun InfoBox(background: Color = Color(0xFFE8EEFC), text: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Box(Modifier.weight(1f)) { text() }
    }
}

@Composable
fun FilterChipRow(chips: List<String>, selected: Int = 0) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        chips.forEachIndexed { index, chip ->
            val isSelected = index == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Navy else Color.White)
                    .then(
                        if (isSelected) Modifier else Modifier.border(1.dp, BorderSoft, RoundedCornerShape(10.dp))
                    )
                    .clickable {}
                    .padding(horizontal = 18.dp, vertical = 9.dp)
            ) {
                Text(
                    text = chip,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun RowFilterChips(vararg chips: String, selected: Int = 0) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEachIndexed { index, chip ->
            val isSelected = index == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Navy else Color.White)
                    .then(
                        if (isSelected) Modifier else Modifier.border(1.dp, BorderSoft, RoundedCornerShape(10.dp))
                    )
                    .clickable {}
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Text(
                    text = chip,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BottomFooterButton(
    totalLabel: String,
    totalValue: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = totalLabel.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.8.sp, color = TextSecondary)
            Text(text = totalValue, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
        }
        PrimaryButton(text = buttonText, onClick = onClick)
    }
}

@Composable
fun InlineDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun SpacerH8() = Spacer(Modifier.height(8.dp))
@Composable
fun SpacerH12() = Spacer(Modifier.height(12.dp))
@Composable
fun SpacerH16() = Spacer(Modifier.height(16.dp))
@Composable
fun SpacerH4() = Spacer(Modifier.height(4.dp))
@Composable
fun SpacerH20() = Spacer(Modifier.height(20.dp))
